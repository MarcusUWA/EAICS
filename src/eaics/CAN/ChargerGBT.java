/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eaics.CAN;

import eaics.MiscCAN.CANHandler;
import eaics.Settings.BMSSettings;
import java.io.IOException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author troyg
 */
public class ChargerGBT 
{
    private ScheduledExecutorService handshakeExecutor;
    boolean isHandshakeExecutorOn;
    private ScheduledExecutorService identificationExecutor;
    
    private EVMS_v3 evms;
    private BMSSettings settings;
    private CANHandler handler;
    
    Boolean JavaCAN = false;
    
    public ChargerGBT(EVMS_v3 evms)
    {
        isHandshakeExecutorOn = false;
        this.evms = evms;
    }
    
    public void startSendHandshake()
    {
        isHandshakeExecutorOn = true;
        
        Runnable Handshake = new Runnable() 
	{            
	    @Override
	    public void run() 
	    {
                if(JavaCAN) {
                    int[] data = {settings.getSetting(19)};
                    try {
                        handler.writeMessage(0x182756F4, data);
                    } catch (IOException ex) {
                        Logger.getLogger(ChargerGBT.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
                else {
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
                if(JavaCAN) {
                    int[] data = {0x10,0x29,0x00,0x07,0xFF, 0x00, 0x02,0x00};
                    try {
                        handler.writeMessage(0x1CEC56F4, data);
                    } catch (IOException ex) {
                        Logger.getLogger(ChargerGBT.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
                else {
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
	    }
	};

	this.identificationExecutor = Executors.newScheduledThreadPool(1);
	this.identificationExecutor.scheduleAtFixedRate(Identification, 0, 1, TimeUnit.SECONDS);   // Run every second
    }
    
    public void sendIdentificationParams()
    {        
        try
        {
            if(JavaCAN) {
                //First message 
                //b[0] = byte no. = 01, b[1,2,3] = message protocol 
                //b[4] = battery type LiPo=7, b[5-6]=Capacity (Ah), b[7] = Pack Voltage 0.1V (LSB)
                int[] chg1 = {0x01,0x01,0x01,0x00,0x07, settings.getSetting(0)*10, (settings.getSetting(0)*10)>>8, settings.getSetting(3)};
                try {
                    handler.writeMessage(0x1CEB56F4, chg1);
                } catch (IOException ex) {
                    Logger.getLogger(ChargerGBT.class.getName()).log(Level.SEVERE, null, ex);
                }
                
                //Second message
                //b[0] = byte no. = 02, b[1] = Pack Voltage 0.1V (MSB)
                //b[2-5] = Batt Manuf. Name (ASCII), b[6-7] = Batt Ser (LSB)
                int[] chg2 = {0x02, settings.getSetting(3)>>8,0x45, 0x4C, 0x41, 0x45, 0x00, 0x00};
                try {
                    handler.writeMessage(0x1CEB56F4, chg2);
                } catch (IOException ex) {
                    Logger.getLogger(ChargerGBT.class.getName()).log(Level.SEVERE, null, ex);
                }
                
                //Third message
                //b[0] = byte no. = 03, b[1-2] = Batt SER (MSB)
                //b[3] = Prod Yr (1985-2235), b[4] = Prod Month (1-12), b[5] = Prod Day(1-31)
                //b[6-7] = No. Times charged (LSB)
                int[] chg3 = {0x03, 0x00, 0x00, 18, 12, 12, 0x00, 0x00};
                try {
                    handler.writeMessage(0x1CEB56F4, chg3);
                } catch (IOException ex) {
                    Logger.getLogger(ChargerGBT.class.getName()).log(Level.SEVERE, null, ex);
                }
                
                //Fourth message
                //b[0] = byte no. = 04, b[1] = No. Times charged (MSB)
                //b[2] = owner (0=leased) , b[3] = Res, b[4-7] = VIN
                //b[6-7] = No. Times charged (LSB)
                int[] chg4 = {0x04, 0x00, 0x00, 0x00, 0x01, 0x00, 0x00, 0x00};
                try {
                    handler.writeMessage(0x1CEB56F4, chg4);
                } catch (IOException ex) {
                    Logger.getLogger(ChargerGBT.class.getName()).log(Level.SEVERE, null, ex);
                }
                
                //Fifth-Seventh Message are all reserved..
                int[] chg = {0x05, 0x00, 0x00, 0x00, 0x01, 0x00, 0x00, 0x00};
                try {
                    handler.writeMessage(0x1CEB56F4, chg);
                } catch (IOException ex) {
                    Logger.getLogger(ChargerGBT.class.getName()).log(Level.SEVERE, null, ex);
                }
                
                chg[0] = 0x06;
                try {
                    handler.writeMessage(0x1CEB56F4, chg);
                } catch (IOException ex) {
                    Logger.getLogger(ChargerGBT.class.getName()).log(Level.SEVERE, null, ex);
                }
                
                chg[0] = 0x07;
                try {
                    handler.writeMessage(0x1CEB56F4, chg1);
                } catch (IOException ex) {
                    Logger.getLogger(ChargerGBT.class.getName()).log(Level.SEVERE, null, ex);
                }
                
                    
            }
            else {
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
        if(JavaCAN) {
                int[] chg = {0x10, 0x0D, 0x00, 0x02, 0xFF, 0x00, 0x06, 0x00};
                try {
                    handler.writeMessage(0x1CEC56F4, chg);
                } catch (IOException ex) {
                    Logger.getLogger(ChargerGBT.class.getName()).log(Level.SEVERE, null, ex);
                }
        }
        else {
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
    }
    
    public void sendParameterSettings()
    {
        if(JavaCAN) {
                //First message
                //b[0] = byte no. = 01, b[1-2] = Max Voltage of single cell (0.01V)
                //b[3-4] = Max charge current 40000 - 100*Curr(A) , b[5-6] = Battery energy 0.1kWh
                //b[7] = Charging Voltage (0.1V)
                int[] chg1 = {0x01, settings.getSetting(14)*100, (settings.getSetting(14)*100)>>8, 40000-(settings.getSetting(20)*100), (40000-(settings.getSetting(20)*100))>>8, settings.getSetting(0)*settings.getSetting(19)*10, (settings.getSetting(0)*settings.getSetting(19)*10)>>8, settings.getSetting(19)*10};
                try {
                    handler.writeMessage(0x1CEB56F4, chg1);
                } catch (IOException ex) {
                    Logger.getLogger(ChargerGBT.class.getName()).log(Level.SEVERE, null, ex);
                }
                
                //Second message
                //b[0] = byte no. = 02, b[1] = Max Charge Voltage
                //b[2-3] = Max Temp 200-1*Temp, b[4-5] = SoC (0.1%), 
                //b[6-7] = No. Times charged (LSB)
                int[] chg2 = {0x02, (settings.getSetting(19)*10)>>8, 200-settings.getSetting(5), (int)(evms.getAmpHours()*1000/settings.getSetting(0)), (int)((evms.getAmpHours()*1000/settings.getSetting(0)))>>8, (int)evms.getVoltage(), (int)(settings.getSetting(0)*settings.getSetting(19)*10)>>8, settings.getSetting(19), 0xFF};
                try {
                    handler.writeMessage(0x1CEB56F4, chg2);
                } catch (IOException ex) {
                    Logger.getLogger(ChargerGBT.class.getName()).log(Level.SEVERE, null, ex);
                }
        }
        else {
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
    }
    
    public void sendReadyToCharge()
    {
        if(JavaCAN) {
                //if ready to charge, set first byte to AA, if not ready to 00, if invalid state FF
                int[] chg1 = {0xAA,0,0,0,0,0,0,0};
                try {
                    handler.writeMessage(0x100956F4, chg1);
                } catch (IOException ex) {
                    Logger.getLogger(ChargerGBT.class.getName()).log(Level.SEVERE, null, ex);
                }
                
        }
        else {
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
