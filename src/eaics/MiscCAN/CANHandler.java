/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eaics.MiscCAN;

import de.entropia.can.CanSocket;
import de.entropia.can.CanSocket.CanInterface;
import de.entropia.can.CanSocket.Mode;
import java.io.IOException;

/**
 *
 * @author troyg
 */
public class CANHandler 
{
    //name for the interface, can change to can0 or can1 for either mode, needs to match the setting in the /boot/config.txt
    private static final String CAN_INTERFACE = "can1";
    
    private final CanSocket socket;
    private final CanInterface canIf;
    
    private Thread threadReadCAN1;
    
    //Boolean to set reading mode to off/on, doesnt turn off thread
    private boolean amReading;
    //Boolean to indicate whether the thread has started or not
    private boolean startedReading;
    //Boolean to indicate whether the port is binded or not
    private boolean portBinded;
    
    public CANHandler()
    {
        this.amReading = true;
        this.startedReading = false;
        this.portBinded = false;
        try
        {
            socket = new CanSocket(Mode.RAW);
        
            //searches for the interface matching this name
        
            canIf = new CanInterface(socket, CAN_INTERFACE);
        }
        catch(IOException e)
        {
            e.printStackTrace();
        }
        
        private void openPort(ActionEvent event) throws IOException {
        if(DEBUG) {
            System.out.println("Opening CAN Port!");
            System.out.println(canIf.toString());
        }
        
        //Binding the socket
        socket.bind(canIf);
        
        portBinded = true;
        
        //Binding all?
        //socket.bind(CanSocket.CAN_ALL_INTERFACES);
    }
    
    @FXML
    private void closePort(ActionEvent event) throws IOException {
        
        if(portBinded) {
            if(DEBUG) {
                System.out.println("Closing CAN Port!");
            }
            socket.close();
            portBinded = false;
        }
        else {
            System.out.println("Port not opened yet!");
        }
    }
    
    @FXML
    private void startReading(ActionEvent event) throws IOException {
        
        if(!portBinded) {
            System.out.println("Port not open");
        }
        
        else {
        
            if(DEBUG) {
                System.out.println("Start CANRead thread!");
                System.out.println(canIf.toString());
            }

            amReading = true;

            //Thread for reading...
            if(!startedReading) {
                threadReadCAN1 = new Thread(new Runnable(){
                    public void run(){
                        try {
                            while(amReading&&portBinded) {
                                //to receive all that is needed is the below line
                                System.out.println("CANID: " + socket.recv().getCanId().getCanId_EFF() + ", Data: " + socket.recv().getData().toString());
                            }
                        } catch (IOException ex) {
                            Logger.getLogger(FXMLDocumentController.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                });
                threadReadCAN1.start();
            }
        }
        
    }
    
    
}