/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eaics.MiscCAN;

import de.entropia.can.CanSocket;
import de.entropia.can.CanSocket.CanFrame;
import de.entropia.can.CanSocket.CanId;
import de.entropia.can.CanSocket.CanInterface;
import de.entropia.can.CanSocket.Mode;
import eaics.CAN.CANFilter;
import java.io.IOException;

/**
 *
 * @author troyg
 */
public class CANHandler 
{
    //name for the interface, can change to can0 or can1 for either mode, needs to match the setting in the /boot/config.txt
    private String canSocketString;
    
    private CanSocket socket;
    private CanInterface canIf;
    private Thread threadReadCAN1;
    
    //Boolean to set reading mode to off/on, doesnt turn off thread
    private boolean amReading;
    //Boolean to indicate whether the thread has started or not
    private boolean startedReading;
    //Boolean to indicate whether the port is binded or not
    private boolean portBinded;
    
    private CANMessage canMessage;
    
    public CANHandler(String canSocketString)
    {
        this.canSocketString = canSocketString;
        
        this.amReading = true;
        this.startedReading = false;
        this.portBinded = false;
        try
        {
            socket = new CanSocket(Mode.RAW);
        
            //searches for the interface matching this name
            canIf = new CanInterface(socket, this.canSocketString);
        }
        catch(IOException e)
        {
            e.printStackTrace();
        }
        
        canMessage = new CANMessage();
    }
    
    public void openPort() throws IOException 
    {
        //Binding the socket
        socket.bind(canIf);
        
        portBinded = true;
    }

    private void closePort() throws IOException 
    {
        if(portBinded) 
        {
            socket.close();
            portBinded = false;
        }
        else 
        {
            throw new IllegalStateException("Port not opened yet!");
        }
    }
    
    public void startReading() throws IOException 
    {
        if(!portBinded) 
        {
            throw new IllegalStateException("Port not open");
        }
        else 
        {
            amReading = true;

            //Thread for reading...
            if(!startedReading) 
            {
                threadReadCAN1 = new Thread(new Runnable(){
                    public void run()
                    {
                        try 
                        {
                            CANFilter filter = CANFilter.getInstance();
                            while(amReading&&portBinded) 
                            {
                                //to receive all that is needed is the below line
                                CanFrame currentFrame = socket.recv();
                                
                                int canID = currentFrame.getCanId().getCanId_EFF();
                                
                                int data[] = byte2int(currentFrame.getData());
                                
                                canMessage.newMessage(canSocketString, canID, data);
                                filter.run(canMessage);
                            }
                        } 
                        catch(IOException e) 
                        {
                            e.printStackTrace();
                        }
                    }
                });
                threadReadCAN1.start();
            }
        }
    }
    
    /**
     * Converts a HEX string to a byte array ready for sending
     * Note this requires that the hex string is an even length, needs padding
     * @param s
     * @return 
     */
    private static byte[] hexStringToByteArray(String s) 
    {    
        int len = s.length();
        byte[] data = new byte[len / 2];
        for (int i = 0; i < len; i += 2) 
        {
            data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4)
                             + Character.digit(s.charAt(i+1), 16));
        }
        return data;
    }
    
    public static byte[] int2byte(int[] src) {
        byte[] dst = new byte[src.length];
    
        for (int i=0; i<src.length; i++) {
            dst[i] = (byte) (src[i]&0xFF); //masking top bits, only taking lowest 8 bits
        }
        return dst;
    }
    
    public static int[] byte2int(byte[] src) {
        
        int[] dst = new int[src.length];
        
        for (int i=0; i<src.length; i++) {
            dst[i] = (src[i]&0xFF);
        }
        return dst;
    }
    
    
    public void writeMessage(int id, int[] data) throws IOException 
    {
        if(!portBinded) 
        {
            throw new IllegalStateException("Port not open");
        }
        else 
        {
            //Grabbing data from UI to send
            CanId frameID = new CanId(id);

            //Setting to Extended Frame Format (CAN2.0b)
            frameID.setEFFSFF();

            //Covert int data array to bytes
            byte[] frameData;

            /* If in String format...
            frameData = hexStringToByteArray(data);
            */
            
            frameData = int2byte(data);
            
            //Packaging the frame
            CanFrame frame = new CanFrame(canIf, frameID, frameData);

            //Sending the frame
            socket.send(frame);
        }
    }
    
    
    private void stopReading() 
    {        
        amReading = false;
    }
}