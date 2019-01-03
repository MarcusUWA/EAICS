/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eaics.CAN;

import eaics.MiscCAN.CANMessage;
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
    
    private CANFilter filter;
    
    private ScheduledExecutorService displayExecutor;
    
    boolean amSending = false;
    
    public MGLDisplay() {
        filter = CANFilter.getInstance();
        runDisplay();
    }
    
    private void send4s() throws IOException {
        
         //sent every 4 secs, Fuel Pressure?
        filter.getCANHandler(0).writeMessageSFF(0x201, new int[]{0,0,0,0,0,0,0,0});
        
    }
    
    private void send500ms() throws IOException {
        //sent every 500ms the next few seconds
        //temperature messages for 12 TC sensors in degrees C, 2 bytes per reading, LSB first
        filter.getCANHandler(0).writeMessageSFF(0x202, new int[]{1,0,2,0,3,0,4,0});
        filter.getCANHandler(0).writeMessageSFF(0x203, new int[]{5,0,6,0,7,0,8,0});
        filter.getCANHandler(0).writeMessageSFF(0x204, new int[]{9,0,10,0,11,0,12,0});
        
        int [] byte5 = new int[]{
            20, //Oil Temp LSB
            0, //Oil Temp MSB
            40, //Oil Pressure LSB
            0, //Oil Pressure MSB
            60, //Aux1 LSB
            0, //Aux1 MSB
            70, //Aux2 LSB
            0  //Aux2 MSB
        };
        
        filter.getCANHandler(0).writeMessageSFF(0x205, byte5);
        
        int [] byte6 = new int[]{
            20, //Fuel Pressure LSB
            0, //Fuel Pressure MSB
            250, //Coolant LSB
            0, //Coolant MSB
            0, //Fuel Level1 LSB
            0xF0, //Fuel Level1 MSB
            0, //Fuel Level2 LSB
            0  //Fuel Level2 MSB
        };
        filter.getCANHandler(0).writeMessageSFF(0x206, byte6);

        int [] byte7 = new int[]{
            20, //Temperature
            0, //Temperature
            100, //Voltage 
            0, //Voltage
            0,0,0,0 //unused
        };
        
        filter.getCANHandler(0).writeMessageSFF(0x207, byte7);
    }
    
    private void send200ms() throws IOException {
        
        //sent every 200ms
        
        int [] msg = new int[]{
            0xB8, //RPM1 LSB
            0x0B,  //RPM1 LSB
            40, //RPM2 LSB
            0,  //RPM2 LSB
            60, //MAP
            0,  //MAP
            70, //Current
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
    
}