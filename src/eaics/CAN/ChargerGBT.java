/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eaics.CAN;

import eaics.MiscCAN.CANHandler;
import eaics.Settings.BMSSettings;
import eaics.Settings.EAICS_Settings;
import java.io.IOException;
import java.util.Arrays;
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
    
    private ScheduledExecutorService chargeExecutor;
    boolean isChargeExecutorOn;
    private ScheduledExecutorService stopExecutor;
    
    private EVMS_v3 evms;
    private BMSSettings settings;
    private CANHandler handler;
    
    private Boolean chargeMode = false;
    
    int currentState = 0;
    private boolean isStoppedExecutorOn;
    
    public ChargerGBT(CANFilter filter)
    {
        isHandshakeExecutorOn = false;
        isChargeExecutorOn = false;
        isStoppedExecutorOn = false;
        this.evms = (EVMS_v3)filter.getEVMS_v3();
        this.settings = EAICS_Settings.getInstance().getBmsSettings();
        this.handler = filter.getCANHandler(0);
    }
    
    public void setChargeMode(Boolean mode){
        chargeMode = mode;
    }
    
    public void handleCharger(int CANID, int[] data) {
        switch(CANID) {
            case 0x1826F456:
                System.out.println("-->Handshake in");
                //if in initial start mode
                if(currentState == 0) {
                    System.out.println("Continue to state 1, Data: "+ Int2String(data));
                    startSendHandshake();
                    currentState = 1;
                }
                else {
                    System.out.println("Incorrect state "+currentState);
                }
                break;
                
            case 0x1801F456:
                switch (currentState) {
                    case 1:
                        if(data[0]==0x00) {
                            System.out.println("(Charger) Asking for Pre-Identification from BMS");
                            System.out.println("Continue to state 2, Data: "+ Int2String(data));
                            stopSendHandshake();
                            startSendTransportCommManagement();
                            currentState = 2;
                        }
                        break;
                    case 4:
                        if(data[0] == 0xAA) {
                            System.out.println("(Charger) ID Complete State");
                            System.out.println("Continue to state 5, Data: "+ Int2String(data));
                            sendPreParameterSettings();
                            currentState = 5;
                        }
                        break;
                    
                    default:
                        System.out.println("Incorrect state "+currentState);
                        break;
                }
        
            case 0x1CECF456:
                switch (currentState) {
                    case 2:
                        if(data[0]==0x11) {
                            if(data[1] == 0x07) {
                                System.out.println("(Charger) Acknowledging Pre-ID received from BMS");
                                System.out.println("Continue to state 3, Data: "+ Int2String(data));
                                stopSendingTransportCommManagement();
                                sendIdentificationParams();
                                currentState = 3;
                            }
                        }
                        break;
                    case 3:
                        if(data[0] == 0x13) {
                            System.out.println("(Charger) ID settings acknowledged");
                            System.out.println("Continue to state 4, Data: "+ Int2String(data));
                            currentState = 4;
                        }
                        break;
                    case 5:
                        if(data[0]==0x11) {
                            if(data[1] == 0x02) {
                                System.out.println("(Charger) Acknowledgment of Pre-Parameter settings");
                                System.out.println("Continue to state 6, Data: "+ Int2String(data));
                                sendParameterSettings();
                                currentState = 6;
                            }
                        }
                        break;
                    case 6:
                        if(data[0]==0x13) {
                            if(data[1] == 0x02) {
                                System.out.println("(Charger) Acknowledgment of Parameter settings");
                                System.out.println("Continue to state 7, Data: "+ Int2String(data));
                                currentState = 7;
                            }
                        }
                        break;
                    case 11:
                        if(data[0]==0x11) {
                            if(data[1] == 0x02) {
                                System.out.println("(Charger) Acknowledgment OK to receive charge update");
                                System.out.println("Continue to state 12, Data: "+ Int2String(data));
                                sendChargingPreUpdate();
                                currentState = 12;
                            }
                        }
                        break;
                    case 12:
                        if(data[0]==0x13) {
                            if(data[1] == 0x09) {
                                System.out.println("(Charger) Acknowledgment OK");
                                System.out.println("Continue to state 13, Data: "+ Int2String(data));
                                sendChargingPreUpdate();
                                currentState = 13;
                            }
                        }
                        break;
                    default:
                        System.out.println("Incorrect state: "+currentState);
                        break;
                }
                
            case 0x1807F456:
                if(currentState == 7) {
                    System.out.println("(Charger) Sending Time Sync Data");
                    System.out.println("Continue to state 8, Data: "+ Int2String(data));
                    currentState = 8;
                }
                else {
                    System.out.println("Incorrect state: "+currentState);
                }
                break;
                
            case 0x1808F456:
                if(currentState == 8) {
                    System.out.println("(Charger) Sending Charger stats");
                    System.out.println("Continue to state 9 READY to charge, Data: "+ Int2String(data));
                    sendReadyToCharge();
                    currentState = 9;
                }
                else {
                    System.out.println("Incorrect state: "+currentState);
                }
                break;
                
            case 0x100AF456:
                if(currentState == 9) {
                    if(data[1] == 0xAA) {
                        System.out.println("(Charger) Charger Ready to charge");
                        System.out.println("Continue to state 10, Data: "+ Int2String(data));
                        startSendChargingPacket();
                        currentState = 10;
                    }
                }
                else if(currentState == 10) {
                    System.out.println("(Charger) Charger OK, updating details");
                    System.out.println("Data: "+ Int2String(data));
                    currentState = 11;
                }
                break;
                
            case 0x1812F456:
                if(currentState == 13) {
                    System.out.println("(Charger) Charger OK, updating details");
                    System.out.println("Data: "+ Int2String(data));
                    currentState = 10;
                }
                
            case 0x101AF456:
                System.out.println("(Charger) Charger request stop charging");
                System.out.println("Data: "+ Int2String(data));
                stopCharging();
                currentState = 20;
                    
            case 0x81FFF456:
                System.out.println("Charger Timeout");
                break;
                
             case 0x181DF456:
                System.out.println("Charger IDLE");
                break;
                
            default:
                System.out.println("Invalid CAN ID" + CANID);
                break;         
        }
    }
    
    private String Int2String(int[] data) {
        return Arrays.toString(data);
    }
    
    public void startSendHandshake()
    {
        isHandshakeExecutorOn = true;
        
        Runnable Handshake = new Runnable() 
	{            
	    @Override
	    public void run() 
	    {
                int[] data = {settings.getSetting(19)};
                try {
                    handler.writeMessage(0x182756F4, data);
                } catch (IOException ex) {
                    Logger.getLogger(ChargerGBT.class.getName()).log(Level.SEVERE, null, ex);
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
                int[] data = {0x10,0x29,0x00,0x07,0xFF, 0x00, 0x02,0x00};
                try {
                    handler.writeMessage(0x1CEC56F4, data);
                } catch (IOException ex) {
                    Logger.getLogger(ChargerGBT.class.getName()).log(Level.SEVERE, null, ex);
                }
	    }
	};

	this.identificationExecutor = Executors.newScheduledThreadPool(1);
	this.identificationExecutor.scheduleAtFixedRate(Identification, 0, 1, TimeUnit.SECONDS);   // Run every second
    }
    
    public void sendIdentificationParams() {        
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
        } 
        catch (IOException ex) {
            Logger.getLogger(ChargerGBT.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void stopSendingTransportCommManagement()
    {
        this.identificationExecutor.shutdown();
    }
    
    public void sendPreParameterSettings()
    {
        int[] chg = {0x10, 0x0D, 0x00, 0x02, 0xFF, 0x00, 0x06, 0x00};
        try {
            handler.writeMessage(0x1CEC56F4, chg);
        } catch (IOException ex) {
            Logger.getLogger(ChargerGBT.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void sendParameterSettings() {
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
    
    public void sendReadyToCharge() {
        //if ready to charge, set first byte to AA, if not ready to 00, if invalid state FF
        int[] chg1 = {0xAA,0,0,0,0,0,0,0};
        try {
            handler.writeMessage(0x100956F4, chg1);
        } catch (IOException ex) {
            Logger.getLogger(ChargerGBT.class.getName()).log(Level.SEVERE, null, ex);
        }
                
    }
    
    public void startSendChargingPacket() {
        //b[0-1] = Max Charge Voltage b[2-3] = Max Charge Current, b[4] = Mode 01 = Const. Voltage, 02 Const. Current
        //b[5-7] = Unused
        int[] chg = {settings.getSetting(19)*10,(settings.getSetting(19)*10)>>8,40000-(settings.getSetting(20)*100),(40000-(settings.getSetting(20)*100))>>8,0x01,0xFF,0xFF,0xFF};
        Runnable Identification = new Runnable() 
	{            
	    @Override
	    public void run() 
	    {
                try {
            handler.writeMessage(0x100956F4, chg);
        } catch (IOException ex) {
            Logger.getLogger(ChargerGBT.class.getName()).log(Level.SEVERE, null, ex);
        }
	    }
	};

        this.isChargeExecutorOn = true;
	this.chargeExecutor = Executors.newScheduledThreadPool(1);
	this.chargeExecutor.scheduleAtFixedRate(Identification, 0, 10, TimeUnit.MILLISECONDS);
    }
     
    public void sendChargingPreUpdate() {
        int[] chg = {0x10, 0x09, 0x00, 0x02, 0xFF, 0x00, 0x11, 0x00};
        try {
            handler.writeMessage(0x1CEC56F4, chg);
        } catch (IOException ex) {
            Logger.getLogger(ChargerGBT.class.getName()).log(Level.SEVERE, null, ex);
        }
     }
     
    public void sendChargingUpdate() {
         //First message
        //b[1] = data no., b[1-2] = charging voltage
        //b[3-4] =  Charge Current, b[5] = highest cell
        //b[5-7] = Unused
        
        int maxVoltage = -1;
	int maxCellNumber = -1;
	int maxBmsNumber = -1;
        BMS[] bms = CANFilter.getInstance().getBMS();
	
	for(int ii = 0; ii < bms.length; ii++)
	{
	    for(int jj = 0; jj < BMS.NUMBER_OF_CELLS; jj++)
	    {
		int tempVoltage = bms[ii].getVoltage(jj);
		if(tempVoltage > maxVoltage)
	        {
	    	    maxVoltage = tempVoltage;
		    maxCellNumber = jj;
		    maxBmsNumber = ii;
		}
	    }
	}
        
        int[] chg1 = {0x01, 
            settings.getSetting(19)*10,
            (settings.getSetting(19)*10)>>8, 
            40000-(settings.getSetting(20)*100),
            (40000-(settings.getSetting(20)*100))>>8, 
            maxVoltage/10, 
            ((maxVoltage/10)>>8)<<4&0xF0+0x01, 
            (int)evms.getAmpHours()*100/settings.getSetting(0)};
        
        int[] chg2 = {0x02, 
            (int)(settings.getSetting(0)-evms.getAmpHours())*60/settings.getSetting(20), 
            (int)(settings.getSetting(0)-evms.getAmpHours())*60/settings.getSetting(20)>>8, 
            0xFF, 0xFF, 0xFF, 0xFF, 0xFF};
        try {
            handler.writeMessage(0x1CEB56F4, chg1);
            handler.writeMessage(0x1CEB56F4, chg2);
        } catch (IOException ex) {
            Logger.getLogger(ChargerGBT.class.getName()).log(Level.SEVERE, null, ex);
        }
     }
    
    public void stopCharging() {
        if(this.isHandshakeExecutorOn)
        {
            this.handshakeExecutor.shutdown();
        }
        this.isHandshakeExecutorOn = false;
         
        int[] chg = {0x40,0,0,0xF0,0xCC,0xCC,0xCC,0xCC};
        
         Runnable Identification = new Runnable() 
	{            
	    @Override
	    public void run() 
	    {
                try {
                    handler.writeMessage(0x101956F4, chg);
                } catch (IOException ex) {
                    Logger.getLogger(ChargerGBT.class.getName()).log(Level.SEVERE, null, ex);
                }
	    }
	};

        this.isStoppedExecutorOn = true;
	this.chargeExecutor = Executors.newScheduledThreadPool(1);
	this.chargeExecutor.scheduleAtFixedRate(Identification, 0, 10, TimeUnit.MILLISECONDS);
        
    }
}
