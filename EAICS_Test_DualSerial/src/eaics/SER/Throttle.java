/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eaics.SER;

import eaics.CAN.CANFilter;
import java.io.IOException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class Throttle 
{
    private int throttleSetting;
    private ScheduledExecutorService executor;
    private boolean isSendingThrottleCommands;
    
    private boolean isUsingManualThrottle;
    
    public Throttle() 
    {
        throttleSetting = 0;
        startSendThrottleCommands();
        this.isSendingThrottleCommands = false;
        this.isUsingManualThrottle = true;
    }

    public void setIsUsingManualThrottle(boolean isUsingManualThrottle) 
    {
        this.isUsingManualThrottle = isUsingManualThrottle;
    }

    public boolean isSendingThrottleCommands() 
    {
        return isSendingThrottleCommands;
    }

    public void setIsSendingThrottleCommands(boolean isSendingThrottleCommands) 
    {
        this.isSendingThrottleCommands = isSendingThrottleCommands;
    }

    public void setMsg(String msg) {
        if(this.isUsingManualThrottle) {
            String[] msgArray = msg.split(",");

            if(msgArray.length == 8 || msgArray.length == 10)  {
                try  {
                    this.throttleSetting =  Integer.parseInt(msgArray[3]);
                }
                catch (NumberFormatException e)  {
                    this.throttleSetting = 0;
                }
            }
        }
    }
    
    public int getThrottleSetting() 
    {
        return throttleSetting;
    }
    
    public void setThrottleSetting(int throttleSetting)
    {
        this.throttleSetting = throttleSetting;   
    }
    
    public void startSendThrottleCommands()
    {
        Runnable ThrottleCommandSender = new Runnable() 
	{
            CANFilter filter = CANFilter.getInstance();
            
	    @Override
	    public void run() 
	    {
                //DEC: "346095616" = HEX: "14A10000"
                try
                {
                    int upperByte = throttleSetting >> 8;
                    int lowerByte = throttleSetting & 0xFF;
                    if(isSendingThrottleCommands)
                    {
                        //filter.getCANHandler(1).writeMessage(346095616, new int[]{lowerByte, upperByte});
                        filter.getCANHandler(0).writeMessage(0x14A10000, new int[]{lowerByte, upperByte});
                    }
                }
                catch(IOException e)
                {
                    System.out.println("Can bus is possibly not in correct state, check terminating resistors or use another bus");
                    //e.printStackTrace();
                }
            }
        };

	ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);
	executor.scheduleAtFixedRate(ThrottleCommandSender, 0, 200, TimeUnit.MILLISECONDS);
    }
    
    public void stopSendingThrottleCommands()
    {
        this.executor.shutdown();
    }
    
    @Override
    public String toString() 
    {
        return "" + throttleSetting;
    }
}
