/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eaics.CAN;

import java.io.IOException;
import java.util.Date;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 *
 * @author troyg
 */
public class ChargerGBT 
{
    private ScheduledExecutorService handshakeExecutor;
    boolean isHandshakeExecutorOn;
    private ScheduledExecutorService identificationExecutor;
    
    public ChargerGBT()
    {
        isHandshakeExecutorOn = false;
    }
    
    public void startSendHandshake()
    {
        isHandshakeExecutorOn = true;
        
        Runnable Handshake = new Runnable() 
	{            
	    @Override
	    public void run() 
	    {
                String msg = "182756F4#";
                msg = addToMsg(msg, 350);
                
                try
                {
                    final Process sendHandshake = Runtime.getRuntime().exec("/home/pi/bin/CANsend can0 " + msg);
                    //final Process loadCell2 = Runtime.getRuntime().exec("/home/pi/bin/CANsend can1 " + msg);
                }
                catch(IOException e)
                {
                    e.printStackTrace();
                }
	    }
	};

	this.handshakeExecutor = Executors.newScheduledThreadPool(1);
	this.handshakeExecutor.scheduleAtFixedRate(Handshake, 0, 1, TimeUnit.SECONDS);   // Run every second
    }
    
    public void stopSendHandshake()
    {
        if(this.isHandshakeExecutorOn)
        {
            this.handshakeExecutor.shutdown();
        }
        this.isHandshakeExecutorOn = false;
    }
    
    public void startSendTransportCommManagement()
    {        
        Runnable Identification = new Runnable() 
	{            
	    @Override
	    public void run() 
	    {
                String msg = "";
                
                try
                {
                    msg = "1CEC56F4#10290007FF000200";
                    Runtime.getRuntime().exec("/home/pi/bin/CANsend can0 " + msg);
                }
                catch(IOException e)
                {
                    e.printStackTrace();
                }
	    }
	};

	this.identificationExecutor = Executors.newScheduledThreadPool(1);
	this.identificationExecutor.scheduleAtFixedRate(Identification, 0, 1, TimeUnit.SECONDS);   // Run every second
    }
    
    public void sendIdentificationParams()
    {        
        try
        {
            String msg = "";
            
            msg = "1CEB56F4#01010100071027AC";
            Runtime.getRuntime().exec("/home/pi/bin/CANsend can0 " + msg);

            msg = "1CEB56F4#020F414243443412";
            Runtime.getRuntime().exec("/home/pi/bin/CANsend can0 " + msg);

            msg = "1CEB56F4#0300001F01010300";
            Runtime.getRuntime().exec("/home/pi/bin/CANsend can0 " + msg);

            msg = "1CEB56F4#040001FF30313233";
            Runtime.getRuntime().exec("/home/pi/bin/CANsend can0 " + msg);

            msg = "1CEB56F4#0534353637383941";
            Runtime.getRuntime().exec("/home/pi/bin/CANsend can0 " + msg);

            msg = "1CEB56F4#0642434445464701";
            Runtime.getRuntime().exec("/home/pi/bin/CANsend can0 " + msg);

            msg = "1CEB56F4#07010107E0FFFFFF";
            Runtime.getRuntime().exec("/home/pi/bin/CANsend can0 " + msg);
        }
        catch(IOException e)
        {
            e.printStackTrace();
        }
    }
    
    public void stopSendingTransportCommManagement()
    {
        this.identificationExecutor.shutdown();
    }
    
    public void sendPreParameterSettings()
    {
        try
        {
            String msg = "1CEC56F4#100D0002FF000600";
            Runtime.getRuntime().exec("/home/pi/bin/CANsend can0 " + msg);
        }
        catch(IOException e)
        {
            e.printStackTrace();
        }
    }
    
    public void sendParameterSettings()
    {
        try
        {
            String msg = "";
            
            msg = "1CEB56F4#0160098C0F0A0090";
            Runtime.getRuntime().exec("/home/pi/bin/CANsend can0 " + msg);
            
            msg = "1CEB56F4#020196038409C4FF";
            Runtime.getRuntime().exec("/home/pi/bin/CANsend can0 " + msg);
        }
        catch(IOException e)
        {
            e.printStackTrace();
        }
    }
    
    public void sendReadyToCharge()
    {
        try
        {
            String msg = "";
            
            msg = "100956F4#AACCCCCCCCCCCCCC";
            Runtime.getRuntime().exec("/home/pi/bin/CANsend can0 " + msg);
        }
        catch(IOException e)
        {
            e.printStackTrace();
        }
    }
    
    private String addToMsg(String msg, int setting) 
    {
        String temp = "";
        
        temp += Integer.toHexString(setting);
       
        int len = temp.length();
	if(len == 1 || len == 3 || len == 5 || len == 7)
	{
	    msg += "0";
	}
	msg += temp;
        
        return msg;
    }
}
