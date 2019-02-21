/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eaics.CAN.Battery;

import eaics.CAN.CANFilter;
import eaics.CAN.MGL.MGLDisplay;
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
public class CANPrecharger {
    
    CANFilter filter;
    int[] error;
    int[] status;
    
    boolean setState = false;
    
    private ScheduledExecutorService displayExecutor;

    public CANPrecharger(CANFilter filter) {
	this.filter = filter;
        this.error = new int[64];
        this.status = new int[64];
        
       // sendMessages();
    }
    
    public void setAll(CANMessage message){
        int prechargeNum = message.getByte(0);
	error[prechargeNum] = message.getByte(2);
        status[prechargeNum] = message.getByte(1);
        
        System.out.println("Precharger: "+prechargeNum+" Status: "+status[prechargeNum]+" Error: "+error[prechargeNum]);
    }
    
    public void setState(int num, boolean set) throws IOException {
        setState = set;
    }
    
    private void sendMessages() {
        displayExecutor = null;
        Runnable Id = new Runnable() { 

            int count = 0;

            @Override
            public void run() {
                if(setState) {
                    try {
                        filter.getCANHandler(0).writeMessage(0x30, new int[]{0, 1, 0, 0, 0, 0, 0, 0 });
                    } catch (IOException ex) {
                        Logger.getLogger(CANPrecharger.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
                else {
                    try {
                        filter.getCANHandler(0).writeMessage(0x30, new int[]{0, 0, 0, 0, 0, 0, 0, 0});
                    } catch (IOException ex) {
                        Logger.getLogger(CANPrecharger.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }

            }
        };
        displayExecutor = Executors.newScheduledThreadPool(1);
        displayExecutor.scheduleAtFixedRate(Id, 0, 50, TimeUnit.MILLISECONDS);
    }
    
    public int getError(int num) {
        return error[num];
    }
    
    public int getStatus(int num) {
        return status[num];
    }
}
