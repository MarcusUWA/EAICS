/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eaics.CAN.Battery;

import eaics.CAN.CANFilter;
import eaics.Settings.SettingsEAICS;
import eaics.Settings.TYPEVehicle;
import java.io.IOException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 *
 * @author Markcuz
 */
public class CANThrottle {
    CANFilter filter;
    int refreshRate = 100; //in ms
    
    int DAQNum;
    int DAQInput;
    
    int escAddress = 0x14A10000;
    
    int throttleSetting = 0;
    
    private ScheduledExecutorService displayExecutor;
    
    private boolean isSendingThrottleCommands;

    public CANThrottle(CANFilter filter, int daqNum, int inputNum, int escAddr) {
	this.filter = filter;
        
        this.escAddress = escAddr;
        
        this.DAQNum = daqNum;
        this.DAQInput = inputNum;
       // sendMessages();
    }
    
    public void setThrottle(int value){
        throttleSetting = value;
    }
    
    private void sendMessages() {
        displayExecutor = null;
        Runnable Id = new Runnable() { 

            int count = 0;

            @Override
            public void run() {
                try {
                    int upperByte = throttleSetting >> 8;
                    int lowerByte = throttleSetting & 0xFF;
                    if(isSendingThrottleCommands) {
                        if(SettingsEAICS.getInstance().getGeneralSettings().getVeh()!=TYPEVehicle.WAVEFLYER) {
                            filter.getCANHandler(1).writeMessage(escAddress, new int[]{lowerByte, upperByte});
                        }
                        else {
                            filter.getCANHandler(0).writeMessage(escAddress, new int[]{lowerByte, upperByte});
                        }
                    }
                } catch (IOException ex) {
                }
            }
        };
        displayExecutor = Executors.newScheduledThreadPool(1);
        displayExecutor.scheduleAtFixedRate(Id, 0, 50, TimeUnit.MILLISECONDS);
    }
}
