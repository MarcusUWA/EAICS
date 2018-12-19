/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eaics.CAN;

import eaics.MiscCAN.CANHandler;
import eaics.MiscCAN.CANMessage;
import eaics.Settings.BMSSettings;
import eaics.Settings.EAICS_Settings;
import java.io.IOException;
import java.util.Arrays;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ChargerGBT 
{
    private ScheduledExecutorService handshakeExecutor;
    boolean isHandshakeExecutorOn;
    private ScheduledExecutorService identificationExecutor;
    private ScheduledExecutorService exeSendReadyToCharge;
    
    private ScheduledExecutorService chargeExecutor;
    boolean isChargeExecutorOn;
    private ScheduledExecutorService stopExecutor;
    
    private EVMS_v3 evms;
    private BMSSettings settings;
    private CANHandler handler;
    
    private Boolean chargeMode = false;
    
    int currentState = 0;
    private boolean isStoppedExecutorOn;
    
    private int maxChargeVoltage;
    private int maxChargeCurrent;
    private int packCapacity;
    private int stateOfCharge;
    private int fullVoltage;
    private int bmsMaxVoltage;
    private int evmsMaxTemp;
    
    public ChargerGBT(CANFilter filter)
    {
        isHandshakeExecutorOn = false;
        isChargeExecutorOn = false;
        isStoppedExecutorOn = false;
        this.evms = (EVMS_v3)filter.getEVMS_v3();
        this.settings = EAICS_Settings.getInstance().getBmsSettings();
        this.handler = filter.getCANHandler(0);
        this.maxChargeVoltage = settings.getSetting(19);
        this.maxChargeCurrent = settings.getSetting(20);
        this.packCapacity = settings.getSetting(0);
        this.stateOfCharge = settings.getSetting(1);
        this.fullVoltage = settings.getSetting(2);
        this.bmsMaxVoltage = settings.getSetting(14);
        this.evmsMaxTemp = settings.getSetting(5);
    }
    
    public void setChargeMode(Boolean mode)
    {
        chargeMode = mode;
    }
    
    public void handleCharger(CANMessage message) 
    {
        final int CANID = message.getFrameID();
        int[] data = message.getByteData();
        
        switch(CANID) 
        {
            case 0x1826F456: // CHM, charger protocol number (sent every 250ms)
                // Initially waiting for charger to initiate the handshake
                if(currentState == 0) 
                {
                    System.out.println("Received Charger Handshake Message (CHM), send BMS Handshake Messages (BHM). Increment to State 2");
                    startSendHandshake();
                    currentState = 1;
                }
                else 
                {
                    System.out.println("Incorrect state=|"+currentState+"|, for CHM ID=1826F456");
                }
                break;
                
            case 0x1801F456: // CRM
                switch (currentState) 
                {
                    case 1:
                        if(data[0]==0x00) // Charger is waiting for BMS's ID
                        {
                            System.out.println("Received Charger Identification Message (CRM), increment to state 2");
                            stopSendHandshake();
                            sendTransportCommManagement();
                            currentState = 2;
                        }
                        break;
                    case 3: case 4: //Do not need the accept
                        if(data[0] == 0xAA) // Charger has accepted BMS's ID
                        {
                            System.out.println("Charger has received BRM, increment to state 5");
                            System.out.println("Completed handshake phase, continue to charging parameter setting stage");
                            sendPreParameterSettings();
                            currentState = 5;
                        }
                        break;
                    
                    default:
                        System.out.println("Incorrect state=|"+currentState+"| CRM ID=1801F456");
                        break;
                }
                break;
        
            case 0x1CECF456: // BCP
                switch (currentState) 
                {
                    case 2:
                        if(data[0]==0x11) 
                        {
                            if(data[1] == 0x07) 
                            {
                                System.out.println("(Charger) Acknowledging Pre-ID received from BMS");
                                System.out.println("Continue to state 3, Data: "+ Int2String(data));
                                sendIdentificationParams();
                                currentState = 3;
                            }
                        }
                        break;
                    case 3:
                        if(data[0] == 0x13) 
                        {
                            System.out.println("(Charger) ID settings acknowledged");
                            System.out.println("Continue to state 4, Data: "+ Int2String(data));
                            currentState = 4;
                        }
                        break;
                    case 5:
                        if(data[0]==0x11) 
                        {
                            if(data[1] == 0x02) 
                            {
                                System.out.println("(Charger) Acknowledgment of Pre-Parameter settings");
                                System.out.println("Continue to state 6, Data: "+ Int2String(data));
                                sendParameterSettings();
                                currentState = 6;
                            }
                        }
                        break;
                    case 6:
                        if(data[0]==0x13) 
                        {
                            if(data[1] == 0x0D) // Don't think we need this second check???? Remove later if no problem 
                            {
                                System.out.println("(Charger) Acknowledgment of Parameter settings");
                                System.out.println("Continue to state 7, Data: "+ Int2String(data));
                                currentState = 7;
                            }
                        }
                        break;
                    case 11:
                        if(data[0]==0x11) 
                        {
                            if(data[1] == 0x02) 
                            {
                                System.out.println("(Charger) Acknowledgment OK to receive charge update");
                                System.out.println("Continue to state 12, Data: "+ Int2String(data));
                                currentState = 12;
                            }
                        }
                        break;
                    case 12:
                        if(data[0] == 0x11)
                        {
                            if(data[1] == 0x02)
                            {
                                sendBatteryChargingState(); //This MAY be so slow, this is sending after 3 CEC's were sent
                            }
                        }
                        break;
                    default:
                        System.out.println("0x1CECF456 Incorrect state: "+ currentState);
                        break;
                }
                break;
                
            case 0x1807F456:
                if(currentState == 7) 
                {
                    System.out.println("(Charger) Sending Time Sync Data");
                    System.out.println("Continue to state 8, Data: "+ Int2String(data));
                    currentState = 8;
                }
                else if(currentState == 4 || currentState == 5)
                {
                    //ignore because just date and time from charger
                }
                else 
                {
                    System.out.println("0x1807F456 Incorrect state: "+currentState);
                }
                break;
                
            case 0x1808F456:
                if(currentState == 8) 
                {
                    System.out.println("(Charger) Sending Charger stats");
                    System.out.println("Continue to state 9 READY to charge, Data: "+ Int2String(data));
                    startSendReadyToCharge();
                    currentState = 9;
                }
                else 
                {
                    System.out.println("0x1808F456 Incorrect state: "+currentState);
                }
                break;
                
            case 0x100AF456:
                if(currentState == 9) 
                {
                    if(data[0] == 0xAA) 
                    {
                        stopSendReadyToCharge();
                        startSendChargingPacket();
                        System.out.println("(Charger) Charger Ready to charge");
                        System.out.println("Continue to state 10, Data: "+ Int2String(data));
                        currentState = 10;
                    }
                    else if(data[0] == 0x00)
                    {
                        System.out.println("Charger is not ready for charging");
                    }
                }
                else if(currentState == 10) //This state is not used, remove later
                {
                    //System.out.println("(Charger) Charger OK, updating details");
                    System.out.println("Continue to state 11");
                    currentState = 11;
                }
                break;
                
            case 0x1812F456:
                if(currentState == 12) 
                {
                    System.out.println("Happy Packets");
                    System.out.println("(Charger) Charger OK, updating details");
                    System.out.println("Data: "+ Int2String(data));
                }
                break;
                
            case 0x101AF456:
                System.out.println("(Charger) Charger request stop charging");
                System.out.println("Data: "+ Int2String(data));
                stopCharging();
                currentState = 20;
                break;
                    
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
    
    public void startSendHandshake() {
        isHandshakeExecutorOn = true;
        
        Runnable Handshake = new Runnable()  {            
	    @Override
	    public void run()  {   
                int[] data = {
                    maxChargeVoltage&0xFF,  //lower byte of charge voltage
                    (maxChargeVoltage>>8)&0xFF,  //upper byte
                    0xFF, 0xFF, 0xFF, 0xFF, 0xFF, 0xFF};  //filler data
                try 
                {
                    handler.writeMessage(0x182756F4, data);
                    System.out.println("0x182756F4: "+Int2String(data));
                } 
                catch (IOException ex) 
                {
                    ex.printStackTrace();
                    Logger.getLogger(ChargerGBT.class.getName()).log(Level.SEVERE, null, ex);
                }
	    }
	};

	this.handshakeExecutor = Executors.newScheduledThreadPool(1);
	this.handshakeExecutor.scheduleAtFixedRate(Handshake, 0, 250, TimeUnit.MILLISECONDS);   // Run every second
    }
    
    public void stopSendHandshake() {
        if(this.isHandshakeExecutorOn){
            this.handshakeExecutor.shutdown();
        }
        this.isHandshakeExecutorOn = false;
    }
    
    public void sendTransportCommManagement(){
        //hardcoded message
        int[] data = {0x10,0x29,0x00,0x07,0xFF, 0x00, 0x02,0x00};
        try {
            handler.writeMessage(0x1CEC56F4, data);
            System.out.println("0x1CEC56F4: "+Int2String(data));
        }
        catch (IOException ex)  {
            ex.printStackTrace();
            Logger.getLogger(ChargerGBT.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void sendIdentificationParams() {        
        //First message 
        //b[0] = byte no. = 01, b[1,2,3] = message protocol 
        //b[4] = battery type LiPo=7, b[5-6]=Capacity (Ah), b[7] = Pack Voltage 0.1V (LSB)
        int[] chg1 = {
            0x01, //byte no. = 01
            0x01, //b[1,2,3] = message protocol
            0x01, //b[1,2,3] = message protocol
            0x00, //b[1,2,3] = message protocol
            0x07, //b[4] = battery type LiPo=7
            packCapacity&0xFF, //Capacity (Ah) Low Byte
            (packCapacity>>8), //Capacity (Ah) High Byte Ah
            (fullVoltage*10)&0xFF //Rated Voltage of Battery Low Byte 0.1V
        }; 
        try 
        {
            handler.writeMessage(0x1CEB56F4, chg1);
            System.out.println("0x1CEB56F4: "+Int2String(chg1));
        } 
        catch (IOException ex) 
        {
            ex.printStackTrace();
            Logger.getLogger(ChargerGBT.class.getName()).log(Level.SEVERE, null, ex);
        }

        //Second message
        //b[0] = byte no. = 02, b[1] = Pack Voltage 0.1V (MSB)
        //b[2-5] = Batt Manuf. Name (ASCII), b[6-7] = Batt Ser (LSB)
        int[] chg2 = {
            0x02,  //byte no. = 02
            (fullVoltage*10)>>8, //Max Voltage High Byte
            0x45, //E - Ascii
            0x4C, //L - Ascii
            0x41, //A - Ascii
            0x45, //E - Ascii
            0x00, //Batt serial
            0x00 //Batt serial
        };
        try 
        {
            handler.writeMessage(0x1CEB56F4, chg2);
            System.out.println("0x1CEB56F4: "+Int2String(chg2));
        } 
        catch (IOException ex) 
        {
            ex.printStackTrace();
            Logger.getLogger(ChargerGBT.class.getName()).log(Level.SEVERE, null, ex);
        }

        //Third message
        //b[0] = byte no. = 03, b[1-2] = Batt SER (MSB)
        //b[3] = Prod Yr (1985-2235), b[4] = Prod Month (1-12), b[5] = Prod Day(1-31)
        //b[6-7] = No. Times charged (LSB)
        int[] chg3 = {
            0x03, //packet no. = 03
            0x00, //Batt serial
            0x00, //Batt Serial
            18, // Prod Yr (1985-2235)
            12, //Prod Month (1-12)
            12, //Prod Day(1-31)
            0x00, //no. times charged
            0x00 //no times charged
        };
        try 
        {
            handler.writeMessage(0x1CEB56F4, chg3);
            System.out.println("0x1CEB56F4: "+Int2String(chg3));
        } 
        catch (IOException ex) 
        {
            ex.printStackTrace();
            Logger.getLogger(ChargerGBT.class.getName()).log(Level.SEVERE, null, ex);
        }

        //Fourth message
        //b[0] = byte no. = 04, b[1] = No. Times charged (MSB)
        //b[2] = owner (0=leased) , b[3] = Res, b[4-5] = VIN
        //b[6-7] = No. Times charged (LSB)
        int[] chg4 = {
            0x04, //Packet no. = 4
            0x00, //Times charged
            0x00, //Owner (0=leased)
            0x00, // Res
            0x01, //VIN
            0x00, //VIN
            0x00, //Times charged
            0x00  //Times charged
        };
        try 
        {
            handler.writeMessage(0x1CEB56F4, chg4);
            System.out.println("0x1CEB56F4: "+Int2String(chg4));
        } 
        catch (IOException ex) 
        {
            ex.printStackTrace();
            Logger.getLogger(ChargerGBT.class.getName()).log(Level.SEVERE, null, ex);
        }

        //Fifth-Seventh Message are all reserved..
        int[] chg = {
            0x05, 
            0x00, 
            0x00, 
            0x00, 
            0x00, 
            0x00, 
            0x00, 
            0x00
        };
        
        try 
        {
            handler.writeMessage(0x1CEB56F4, chg);
            System.out.println("0x1CEB56F4: "+Int2String(chg));
        } 
        catch (IOException ex) 
        {
            ex.printStackTrace();
            Logger.getLogger(ChargerGBT.class.getName()).log(Level.SEVERE, null, ex);
        }

        chg[0] = 0x06;
        
        try 
        {
            handler.writeMessage(0x1CEB56F4, chg);
            System.out.println("0x1CEB56F4: "+Int2String(chg));
        } 
        catch (IOException ex) 
        {
            ex.printStackTrace();
            Logger.getLogger(ChargerGBT.class.getName()).log(Level.SEVERE, null, ex);
        }

        chg[0] = 0x07;
        try 
        {
            handler.writeMessage(0x1CEB56F4, chg);
            System.out.println("0x1CEB56F4: "+Int2String(chg));
        } 
        catch (IOException ex) 
        {
            ex.printStackTrace();
            Logger.getLogger(ChargerGBT.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void sendPreParameterSettings()
    {
        int[] chg = {
            0x10, 
            0x0D, 
            0x00, 
            0x02, 
            0xFF, 
            0x00, 
            0x06, 
            0x00
        };
        try 
        {
            handler.writeMessage(0x1CEC56F4, chg);
            System.out.println("0x1CEC56F4: "+Int2String(chg));
        } catch (IOException ex) 
        {
            ex.printStackTrace();
            Logger.getLogger(ChargerGBT.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void sendParameterSettings() 
    {
        //First message
        //b[0] = byte no. = 01, b[1-2] = Max Voltage of single cell (0.01V)
        //b[3-4] = Max charge current 40000 - 100*Curr(A) , b[5-6] = Battery energy 0.1kWh
        //b[7] = Charging Voltage (0.1V)
        int[] chg1 = {
            0x01, //Packet No. = 1
            (bmsMaxVoltage/10)&0xFF, 
            (bmsMaxVoltage/10)>>8, 
            (40000-maxChargeCurrent*100)&0xFF, 
            (40000-(maxChargeCurrent*100))>>8, 
            (packCapacity*maxChargeVoltage/100)&0xFF, 
            (packCapacity*maxChargeVoltage/100)>>8, 
            (maxChargeVoltage*10)&0xFF
        };
        
        try 
        {
            handler.writeMessage(0x1CEB56F4, chg1);
            System.out.println("0x1CEB56F4: "+Int2String(chg1));
        } 
        catch (IOException ex) 
        {
            ex.printStackTrace();
            Logger.getLogger(ChargerGBT.class.getName()).log(Level.SEVERE, null, ex);
        }

        //Second message
        //b[0] = byte no. = 02, b[1] = Max Charge Voltage
        //b[2-3] = Max Temp 200-1*Temp, b[4-5] = SoC (0.1%), 
        //b[6-7] = No. Times charged (LSB)
        //int[] chg2 = {0x02, (settings.getSetting(19)*10)>>8, 200-settings.getSetting(5), (int)(evms.getAmpHours()*1000/settings.getSetting(0)), (int)((evms.getAmpHours()*1000/settings.getSetting(0)))>>8, (int)evms.getVoltage(), (int)(settings.getSetting(0)*settings.getSetting(19)*10)>>8, settings.getSetting(19), 0xFF};
        
        // Marcus, we need to splite the SOC and Battery voltage accross two bytes
        int[] chg2 = {  
                        0x02,//packet number =  2
                        (maxChargeVoltage*10) >> 8,//maxAllowableTotalCharge,
                        (200-evmsMaxTemp)&0xFF,
                        ((int)evms.getAmpHours()*1000/packCapacity)&0xFF,
                        (int)(evms.getAmpHours()*1000/packCapacity)>>8,
                        ((int)evms.getVoltage()*10)&0xFF,
                        (int)(evms.getVoltage()*10)>>8,
                        0xFF
                      };
        
        try 
        {
            handler.writeMessage(0x1CEB56F4, chg2);
            System.out.println("0x1CEB56F4: "+Int2String(chg2));
        } 
        catch (IOException ex) 
        {
            ex.printStackTrace();
            Logger.getLogger(ChargerGBT.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void startSendReadyToCharge() 
    {
        //if ready to charge, set first byte to AA, if not ready to 00, if invalid state FF
        int[] chg1 = {0xAA,0xFF,0xFF,0xFF,0xFF,0xFF,0xFF,0xFF};
        
        Runnable Identification = new Runnable() 
	{            
	    @Override
	    public void run() 
	    {
                try 
                {
                    handler.writeMessage(0x100956F4, chg1);
                    System.out.println("0x100956F4: "+Int2String(chg1));
                } 
                catch (IOException ex) 
                {
                    ex.printStackTrace();
                    Logger.getLogger(ChargerGBT.class.getName()).log(Level.SEVERE, null, ex);
                }
	    }
	};

        this.exeSendReadyToCharge = Executors.newScheduledThreadPool(1);
        this.exeSendReadyToCharge.scheduleAtFixedRate(Identification, 0, 250, TimeUnit.MILLISECONDS);
    }
    
    public void stopSendReadyToCharge()
    {
        this.exeSendReadyToCharge.shutdown();
    }
    
    public void startSendChargingPacket() 
    {
        //b[0-1] = Max Charge Voltage b[2-3] = Max Charge Current, b[4] = Mode 01 = Const. Voltage, 02 Const. Current
        //b[5-7] = Unused
        int[] chg1 = {
            (maxChargeVoltage*10)&0xFF,
            (maxChargeVoltage*10)>>8,
            ((400-maxChargeCurrent)*10)&0xFF,
            ((400-maxChargeCurrent)*10)>>8,
            0x01, //constant voltage mode
            0xFF,0xFF,0xFF
        };
        
        int[] chg2 = {0x10, 0x09, 0x00, 0x02, 0xFF, 0x00, 0x11, 0x00};  //Leave this hardcoded (FINE)
        
        //TODO: - Find out what is oging on with this Packet!!!!
        int[] chg3 = {0x05, 0x5F, 0x0A, 0x32, 0x14, 0x00, 0xD0, 0xFF};  //This is hardcodded fix later (FIX THIS!
        
        Runnable packet1 = new Runnable() 
	{
            int count = 0;  // for 250ms
            int count2 = 0;
            
	    @Override
	    public void run() 
	    {
                try 
                {
                    
                    handler.writeMessage(0x181056F4, chg1);  //10ms
                    System.out.println("0x181056F4: "+Int2String(chg1));
                    
                    if(++count == 25)
                    {
                        handler.writeMessage(0x1cec56f4, chg2);  //250ms
                        System.out.println("0x1CEC56F4: "+Int2String(chg2));
                        count = 0;
                    }
                    
                    if(++count2 == 1000)
                    {
                        handler.writeMessage(0x181356F4, chg3);
                        System.out.println("0x181356F4: "+Int2String(chg3));
                        count2 = 0;
                    }
                } 
                catch (IOException ex) 
                {
                    ex.printStackTrace();
                    Logger.getLogger(ChargerGBT.class.getName()).log(Level.SEVERE, null, ex);
                }
	    }
	};

        this.isChargeExecutorOn = true;
	this.chargeExecutor = Executors.newScheduledThreadPool(1);
	this.chargeExecutor.scheduleAtFixedRate(packet1, 0, 10, TimeUnit.MILLISECONDS);
    }
     
    public void sendChargingPreUpdate() 
    {
        //hardcoded
        int[] chg = {0x10, 0x09, 0x00, 0x02, 0xFF, 0x00, 0x11, 0x00};
        
        try 
        {
            handler.writeMessage(0x1CEC56F4, chg);
            System.out.println("0x1CEC56F4: "+Int2String(chg));
            
        }
        catch (IOException ex) 
        {
            ex.printStackTrace();
            Logger.getLogger(ChargerGBT.class.getName()).log(Level.SEVERE, null, ex);
        }
     }
     
    public void sendBatteryChargingState() {
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
        
        int[] chg1 = {
            0x01, 
            (maxChargeVoltage*10)&0xFF,
            (maxChargeVoltage*10)>>8, 
            40000-(maxChargeCurrent*100)&0xFF,
            (40000-(maxChargeCurrent*100))>>8, 
            (maxVoltage/10)&0xFF,  //max cell votlage lower byte
            ((maxVoltage/10)>>8)<<4 &0xF0 + 0x01, //max cell voltage upper half--byte and posisiton (at 1)
            (int)evms.getAmpHours()*100/packCapacity
        };
        /*
        System.out.println("---------Ah:" + (int)evms.getAmpHours());
        System.out.println("---------Ah without typoecast:" + evms.getAmpHours());
        System.out.println("---------Capacity:"+ packCapacity);
        */
        int[] chg2 = {
            0x02, 
            ((int)(packCapacity-evms.getAmpHours())*60/maxChargeCurrent)&0xFF, 
            (int)((packCapacity-evms.getAmpHours())*60/maxChargeCurrent)>>8, 
            0xFF, 0xFF, 0xFF, 0xFF, 0xFF};
        try {
            handler.writeMessage(0x1CEB56F4, chg1);
            System.out.println("0x1CEB56F4: "+Int2String(chg1));
            handler.writeMessage(0x1CEB56F4, chg2);
            System.out.println("0x1CEB56F4: "+Int2String(chg2));
        } catch (IOException ex) {
            ex.printStackTrace();
            Logger.getLogger(ChargerGBT.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void stopCharging() //not needed for prototype
    {        
        if(this.isChargeExecutorOn)
        {
            this.chargeExecutor.shutdown();
        }
        this.isChargeExecutorOn = false;
        /*
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
                try 
                {
                    handler.writeMessage(0x101956F4, chg);
                } 
                catch (IOException ex) 
                {
                    ex.printStackTrace();
                    Logger.getLogger(ChargerGBT.class.getName()).log(Level.SEVERE, null, ex);
                }
	    }
	};

        this.isStoppedExecutorOn = true;
	this.chargeExecutor = Executors.newScheduledThreadPool(1);
	this.chargeExecutor.scheduleAtFixedRate(Identification, 0, 10, TimeUnit.MILLISECONDS);
        */
    }
}
