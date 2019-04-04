/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package THROTTLE;

import eaics.CAN.CANFilter;
import eaics.Settings.SettingsEAICS;
import eaics.Settings.TYPEVehicle;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 *
 * @author Markcuz
 */
public abstract class Throttle {
    public int throttleSetting;
    private ScheduledExecutorService executor;
    private boolean isSendingThrottleCommands;
    
    int busNum;
    int escAddress;
    
    public boolean isUsingManualThrottle;
    CANFilter filter;
    
    public Throttle(CANFilter filter) {
        this.filter = filter;
        this.escAddress = 0x14A10000;
        throttleSetting = 0;
        startSendThrottleCommands();
        this.isSendingThrottleCommands = false;
        this.isUsingManualThrottle = true;
    }
    
    public void setIsUsingManualThrottle(boolean isUsingManualThrottle) {
        this.isUsingManualThrottle = isUsingManualThrottle;
    }

    public boolean isSendingThrottleCommands() {
        return isSendingThrottleCommands;
    }

    public void setIsSendingThrottleCommands(boolean isSendingThrottleCommands) {
        this.isSendingThrottleCommands = isSendingThrottleCommands;
    }
    
    public int getThrottleSetting() {
        return throttleSetting;
    }
    
    public void setThrottleSetting(int throttleSetting) {
        this.throttleSetting = throttleSetting;   
    }
    
    public void startSendThrottleCommands() {
        Runnable ThrottleCommandSender = new Runnable()  {
            
	    @Override
	    public void run()  {
                //DEC: "346095616" = HEX: "14A10000"
                int upperByte = throttleSetting >> 8; //e.printStackTrace();
                int lowerByte = throttleSetting & 0xFF;
                if(isSendingThrottleCommands) {
                    //System.out.println("Outputting throttle on CAN"+ SettingsEAICS.getInstance().getCanSettings().getThrottleCAN()+" : "+throttleSetting);

                    if(SettingsEAICS.getInstance().getGeneralSettings().getVeh()!=TYPEVehicle.WAVEFLYER) {
                        filter.getCANHandler(busNum).writeMessage(escAddress, new int[]{lowerByte, upperByte});
                    }
                    else {
                        filter.getCANHandler(SettingsEAICS.getInstance().getCanSettings().getThrottleCAN()).writeMessage(escAddress, new int[]{lowerByte, upperByte});
                    }
                }
            }
        };

	ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);
	executor.scheduleAtFixedRate(ThrottleCommandSender, 0, 200, TimeUnit.MILLISECONDS);
}
    
    public void stopSendingThrottleCommands() {
        this.executor.shutdown();
    }
}
