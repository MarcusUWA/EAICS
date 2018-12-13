/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eaics.SER;

import eaics.CAN.CANFilter;
import java.io.IOException;
import java.util.Date;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class Throttle 
{
    private int throttleSetting;
    private ScheduledExecutorService executor;
    
    public Throttle() 
    {
        throttleSetting = 0;
        startSendThrottleCommands();
    }

    public void setMsg(String msg) 
    {
        String[] msgArray = msg.split(",");

        if(msgArray.length > 3) {
            try {
                this.throttleSetting =  Integer.parseInt(msgArray[3]);
            }
            catch (NumberFormatException e) {
                this.throttleSetting = 0;
            }
        }
    }
    
    public int getThrottle() {
        return throttleSetting;
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
                    filter.getCANHandler(1).writeMessage(346095616, new int[]{lowerByte, upperByte});
                    System.out.println("Sending Throttle CAN: " + throttleSetting);
                }
                catch(IOException e)
                {
                    System.out.println("Can bus is possibily not in correct state, check terminating resistors or use another bus");
                    e.printStackTrace();
                }
            }
        };

	ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);
	executor.scheduleAtFixedRate(ThrottleCommandSender, 0, 100, TimeUnit.MILLISECONDS);   // Run every second        
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
