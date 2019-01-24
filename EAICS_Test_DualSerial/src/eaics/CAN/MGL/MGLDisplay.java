/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eaics.CAN.MGL;

import eaics.CAN.CANFilter;
import eaics.CAN.MiscCAN.CANMessage;
import java.io.IOException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Markcuz
 */
public class MGLDisplay {
    
    private ScheduledExecutorService displayExecutor;
    
    boolean amSending = false;

    private CANFilter filter;
    
    short bankAngle; //resolution: 0.1 deg
    short pitchAngle; //resolution: 0.1 deg
    short yawAngle; //resolution: 0.1 deg
    int gndSpeed; //resolution: knots (mph originally)
    
    public MGLDisplay(CANFilter filter) {

        bankAngle = 0; //resolution: 0.1 deg
        pitchAngle = 0; //resolution: 0.1 deg
        yawAngle = 0; //resolution: 0.1 deg
        gndSpeed = 0; //resolution: knots (mph originally)
        
        this.filter = filter;
        runDisplay();
    }
    
    private void send4s() throws IOException {
        int[] send = new int[]{
            0, //Fuel Flow 1 Pulse Count
            0,
            0,//Fuel Flow 1 pulse ratio
            0,
            0,//Fuel Flow 2 Pulse Count
            0,
            0, //Fuel Flow 2 pulse ratio
            0
        };
         //sent every 4 secs, Fuel Pressure?
        filter.getCANHandler(0).writeMessageSFF(0x201, send);
        
    }
    
    private void send500ms() throws IOException {
        //sent every 500ms the next few seconds
        //temperature messages for 12 TC sensors in degrees C, 2 bytes per reading, LSB first
        filter.getCANHandler(0).writeMessageSFF(0x202, new int[]{100,0,0,0,0,0,0,0});
        filter.getCANHandler(0).writeMessageSFF(0x203, new int[]{0,0,0,0,0,0,0,0});
        filter.getCANHandler(0).writeMessageSFF(0x204, new int[]{0,0,0,0,0,0,0,0});
        
        int [] byte5 = new int[]{
            (int)filter.getEVMS().getVoltage()%256, //Oil Temp LSB - Now Using Voltage
            (int)filter.getEVMS().getVoltage()/256, //Oil Temp MSB
            (int)(filter.getCurrentSensor().getCurrent()*1000)%256, //Oil Pressure LSB
            (int)(filter.getCurrentSensor().getCurrent()*1000)/256, //Oil Pressure MSB
            0, //Aux1 LSB
            0, //Aux1 MSB
            0, //Aux2 LSB
            0  //Aux2 MSB
        };
        
        filter.getCANHandler(0).writeMessageSFF(0x205, byte5);
        
        int [] byte6 = new int[]{
            0, //Fuel Pressure LSB
            0, //Fuel Pressure MSB
            0, //Coolant LSB
            0, //Coolant MSB
            0, //Fuel Level1 LSB
            0, //Fuel Level1 MSB
            0, //Fuel Level2 LSB
            0  //Fuel Level2 MSB
        };
        filter.getCANHandler(0).writeMessageSFF(0x206, byte6);

        int [] byte7 = new int[]{
            0, //Temperature
            0, //Temperature
            0, //Voltage 
            0, //Voltage
            0,0,0,0 //unused
        };
        
        filter.getCANHandler(0).writeMessageSFF(0x207, byte7);
    }
    
    private void send200ms() throws IOException {
        
        //sent every 200ms
        
        int [] msg = new int[]{
            filter.getESC()[0].getRpm()%256, //RPM1 LSB
            filter.getESC()[0].getRpm()/256,  //RPM1 LSB
            0, //RPM2 LSB
            0,  //RPM2 LSB
            0, //MAP
            0,  //MAP
            0, //Current
            0   //Current
        };
        
        filter.getCANHandler(0).writeMessageSFF(0x208, msg);
    }
    
    public void runDisplay() {

        displayExecutor = null;
        Runnable Id = new Runnable() { 

            int count = 0;

            @Override
            public void run() {
                
                if(amSending) {
                    //every 200ms
                    if(count%2==1) {
                        try {
                            send200ms();
                        } catch (IOException ex) {
                            Logger.getLogger(MGLDisplay.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                    //every 500ms
                    if(count%5==1) {
                        try {
                            send500ms();
                        } catch (IOException ex) {
                            Logger.getLogger(MGLDisplay.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                    //every 4s
                    if((count%40)==1){
                        try {
                            send4s();
                        } catch (IOException ex) {
                            Logger.getLogger(MGLDisplay.class.getName()).log(Level.SEVERE, null, ex);
                        }
                        count = 0;
                    }
                    count++;
                }
            }
        };
        displayExecutor = Executors.newScheduledThreadPool(1);
        displayExecutor.scheduleAtFixedRate(Id, 0, 100, TimeUnit.MILLISECONDS);
    }
    
    public void stopDisplay() {
          amSending = false;
    }
    
    public void startDisplay() {
          amSending = true;
    }
    
    public void processMessage(CANMessage message) {
        switch (message.getFrameID()) {
            case 0x11:
                break;
            case 0x12:
                bankAngle = (short) ((message.getByte(0)+message.getByte(1)*256));
                bankAngle = (short) (bankAngle/10);
                
                pitchAngle = (short) ((message.getByte(2)+message.getByte(3)*256));
                pitchAngle = (short) (pitchAngle/10);
                
                yawAngle = (short) ((message.getByte(4)+message.getByte(5)*256));
                yawAngle = (short) (yawAngle/10);
                
                gndSpeed = message.getByte(6)+message.getByte(7)*256;
                gndSpeed = (int) (gndSpeed*0.868976);
                
                break;
            default:
                break;
        }
        
    }
        public short getBankAngle() {
        return bankAngle;
    }

    public short getPitchAngle() {
        return pitchAngle;
    }

    public short getYawAngle() {
        return yawAngle;
    }

    public int getGndSpeed() {
        return gndSpeed;
    }
    
}
