/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eaics.CAN.Charger.GBT;

import eaics.CAN.Battery.BMS.BMS12v3;
import eaics.CAN.CANFilter;
import eaics.CAN.Battery.EVMS;
import eaics.CAN.MiscCAN.CANHandler;
import eaics.CAN.MiscCAN.CANMessage;
import eaics.Settings.SettingsEVMS;
import eaics.Settings.SettingsEAICS;
import eaics.Settings.TYPEVehicle;
import java.io.IOException;
import java.util.Arrays;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ChargerGBT  {
    private State state0;
    private State state1;
    private State state2;
    private State state3;
    private State state4;
    private State state5;
    private State state6;
    private State state7;
    private State state8;
    private State state9;
    private State state10;
    private State state11;
    private State state12;
    private State waitingToStopState;
    
    private State state;
    
    private ScheduledExecutorService handshakeExecutor;
    private boolean isHandshakeExecutorOn;
    
    private ScheduledExecutorService exeSendReadyToCharge;
    private boolean isExeSendReadyToCharge;
    
    private ScheduledExecutorService chargeExecutor;
    private boolean isChargeExecutorOn;
    
    private EVMS evms;
    private SettingsEVMS settings;
    private CANHandler handler;
    
    private int maxChargeVoltage;
    private int maxChargeCurrent;
    private int packCapacity;
    private int stateOfCharge;
    private int fullVoltage;
    private int bmsMaxVoltage;
    private int evmsMaxTemp;
    
    private float observedCurrent; // variables needed for FXMLChargingController
    private float observedVoltage;
    private float maxChargerCurrent;
    private float minChargerCurrent;
    private float maxChargerVoltage;
    private float minChargerVoltage;
    private int timeOnCharge;
    
    public void setMaxChargerCurrent(float maxChargerCurrent) // Set all variables for FXMLChargingController
    {
        this.maxChargerCurrent = maxChargerCurrent;
    }
    public void setMaxChargerVoltage(float maxChargerVoltage) 
    {
        this.maxChargerVoltage = maxChargerVoltage;
    }
    public void setMinChargerCurrent(float maxChargerCurrent)
    {
        this.maxChargerCurrent = maxChargerCurrent;
    }
    public void setMinChargerVoltage(float maxChargerVoltage) 
    {
        this.maxChargerVoltage = maxChargerVoltage;
    }
    public void setObservedCurrent(float observedCurrent) 
    {
        this.observedCurrent = observedCurrent;
    }
    public void setObservedVoltage(float observedVoltage) 
    {
        this.observedVoltage = observedVoltage;
    }
    public void setTimeOnCharge(int timeOnCharge) 
    {
        this.timeOnCharge = timeOnCharge;
    }
    

    public float getObservedCurrent()  // Set all variables for FXMLChargingController
    {
        return observedCurrent;
    }
    public float getObservedVoltage() // Set all variables for FXMLChargingController
    {
        return observedVoltage;
    }
    public float getMaxChargerCurrent() 
    {
        return maxChargerCurrent;
    }
    public float getMaxChargerVoltage() 
    {
        return maxChargerVoltage;
    }
    public float getMinChargerCurrent() 
    {
        return maxChargerCurrent;
    }
    public float getMinChargerVoltage() 
    {
        return maxChargerVoltage;
    }
    public int getTimeOnCharge() 
    {
        return timeOnCharge;
    }
    
    public ChargerGBT(CANFilter filter)
    {
        state0 = new State0(this);
        state1 = new State1(this);
        state2 = new State2(this);
        state3 = new State3(this);
        state4 = new State4(this);
        state5 = new State5(this);
        state6 = new State6(this);
        state7 = new State7(this);
        state8 = new State8(this);
        state9 = new State9(this);
        state10 = new State10(this);
        state11 = new State11(this);
        state12 = new State12(this);
        waitingToStopState = new WaitingToStopState(this);
        
        this.state = state0;
        
        isHandshakeExecutorOn = false;
        isChargeExecutorOn = false;
        
        this.evms = filter.getEVMS();
        this.settings = SettingsEAICS.getInstance().getEVMSSettings();
        if(SettingsEAICS.getInstance().getGeneralSettings().getVeh()!=TYPEVehicle.WAVEFLYER) {
            this.handler = filter.getCANHandler(1); // Must change manually, UPDATE AS A = 0 B = 1
        }
        else {
            this.handler = filter.getCANHandler(0); // Must change manually, UPDATE AS A = 0 B = 1
        }
        
        this.maxChargeVoltage = settings.getSetting(19);
        this.maxChargeCurrent = settings.getSetting(20);
        this.packCapacity = settings.getSetting(0);
        this.stateOfCharge = settings.getSetting(1);
        this.fullVoltage = settings.getSetting(2);
        this.bmsMaxVoltage = settings.getSetting(14);
        this.evmsMaxTemp = settings.getSetting(5);
    }
    
    public void handleCharger(CANMessage message) 
    {        
        state.action(message);
    }
    
    public static String Int2String(int[] data) 
    {
        return Arrays.toString(data);
    }
    
    protected void startSendHandshake() 
    {
        isHandshakeExecutorOn = true;
        
        Runnable Handshake = new Runnable()  
        {            
	    @Override
	    public void run()  
            {   
                int[] data = {
                    maxChargeVoltage&0xFF,              //lower byte of charge voltage
                    (maxChargeVoltage>>8)&0xFF,         //upper byte
                    0xFF, 0xFF, 0xFF, 0xFF, 0xFF, 0xFF  //filler data
                };  
                
                try 
                {
                    handler.writeMessage(0x182756F4, data);
                } 
                catch (IOException ex) 
                {
                    ex.printStackTrace();
                }
	    }
	};

	this.handshakeExecutor = Executors.newScheduledThreadPool(1);
	this.handshakeExecutor.scheduleAtFixedRate(Handshake, 0, 250, TimeUnit.MILLISECONDS);   // Run every second
    }
    
    public void stopSendHandshake() 
    {
        if(this.isHandshakeExecutorOn)
        {
            this.handshakeExecutor.shutdown();
        }
        this.isHandshakeExecutorOn = false;
    }
    
    public void sendTransportCommManagement()
    {
        //hardcoded message
        int[] data = {0x10,0x29,0x00,0x07,0xFF, 0x00, 0x02,0x00};
        try 
        {
            handler.writeMessage(0x1CEC56F4, data);
            //System.out.println("0x1CEC56F4: "+Int2String(data));
        }
        catch (IOException ex)  
        {
            ex.printStackTrace();
        }
    }
    
    public void sendIdentificationParams() 
    {        
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
            //System.out.println("0x1CEB56F4: "+Int2String(chg1));
        } 
        catch (IOException ex) 
        {
            ex.printStackTrace();
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
            //System.out.println("0x1CEB56F4: "+Int2String(chg2));
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
            //System.out.println("0x1CEB56F4: "+Int2String(chg3));
        } 
        catch (IOException ex) 
        {
            ex.printStackTrace();
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
            //System.out.println("0x1CEB56F4: "+Int2String(chg4));
        } 
        catch (IOException ex) 
        {
            ex.printStackTrace();
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
            //System.out.println("0x1CEB56F4: "+Int2String(chg));
        } 
        catch (IOException ex) 
        {
            ex.printStackTrace();
        }

        chg[0] = 0x06;
        
        try 
        {
            handler.writeMessage(0x1CEB56F4, chg);
            //System.out.println("0x1CEB56F4: "+Int2String(chg));
        } 
        catch (IOException ex) 
        {
            ex.printStackTrace();
        }

        chg[0] = 0x07;
        
        try 
        {
            handler.writeMessage(0x1CEB56F4, chg);
            //System.out.println("0x1CEB56F4: "+Int2String(chg));
        } 
        catch (IOException ex) 
        {
            ex.printStackTrace();
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
            //System.out.println("0x1CEC56F4: "+Int2String(chg));
        } 
        catch (IOException ex) 
        {
            ex.printStackTrace();
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
            ((400-maxChargeCurrent)*10)&0xFF, 
            ((400-maxChargeCurrent)*10)>>8, 
            (packCapacity*maxChargeVoltage/100)&0xFF, 
            (packCapacity*maxChargeVoltage/100)>>8, 
            (maxChargeVoltage*10)&0xFF
        };
        
        try 
        {
            handler.writeMessage(0x1CEB56F4, chg1);
            //System.out.println("0x1CEB56F4: "+Int2String(chg1));
        } 
        catch (IOException ex) 
        {
            ex.printStackTrace();
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
            (int)(evms.getAmpHours()*1000/packCapacity)>>8,  //0x03  These two need to be non-zero to make work
            ((int)evms.getVoltage()*10)&0xFF,                //0x84
            (int)(evms.getVoltage()*10)>>8,
            0xFF
        };
        
        try 
        {
            handler.writeMessage(0x1CEB56F4, chg2);
            //System.out.println("0x1CEB56F4: "+Int2String(chg2));
        } 
        catch (IOException ex) 
        {
            ex.printStackTrace();
        }
    }
    
    public void startSendReadyToCharge() 
    {
        this.isExeSendReadyToCharge = true;
        
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
                    //System.out.println("0x100956F4: "+Int2String(chg1));
                } 
                catch (IOException ex) 
                {
                    ex.printStackTrace();
                }
	    }
	};

        this.exeSendReadyToCharge = Executors.newScheduledThreadPool(1);
        this.exeSendReadyToCharge.scheduleAtFixedRate(Identification, 0, 250, TimeUnit.MILLISECONDS);
    }
    
    public void stopSendReadyToCharge()
    {
        if(this.isExeSendReadyToCharge)
        {
            this.exeSendReadyToCharge.shutdown();
        }
        this.isExeSendReadyToCharge = false;
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
        
        //int[] chg3 = {0x05, 0x5F, 0x0A, 0x32, 0x14, 0x00, 0xD0, 0xFF};  //This is hardcodded fix later (FIX THIS!
	
	//----------------------------------------------------------------------------------------------------------------------
	
	int bmsMaxTemp = -50; // Find the maximum temperature of each cell, each BMS is connected to 2 battery modules.
        int loopi = 0;
        int loopj = 0;
        for(int ii = 0; ii < CANFilter.NUM_OF_BMS; ii++)
        {
            for(int jj = 0; jj < 2; jj++)
            {
                int tmp = CANFilter.getInstance().getBMS()[ii].getTemp(jj);
                if(tmp > bmsMaxTemp) 
                {
                    bmsMaxTemp = tmp;
                    loopi = ii;
                    loopj = jj;
                }
            }
        }
        int bmsMaxTempLocation = (loopi-1)*2+loopj-1+1; // Find the location of the Max cell temperature
        if (bmsMaxTempLocation > 127) // Ensure it does not exceed 127, the maximum value the charger can take
        {
            bmsMaxTempLocation = 127;
        }
        int bmsMinTemp = 200; // Find the minimum temperature of each cell, each BMS is connected to 2 battery modules.
        for(int ii = 0; ii < CANFilter.NUM_OF_BMS; ii++)
        {
            for(int jj = 0; jj < 2; jj++)
            {
                int tmp = CANFilter.getInstance().getBMS()[ii].getTemp(jj);
                if(tmp < bmsMinTemp) 
                {
                    bmsMinTemp = tmp;
                    loopi = ii;
                    loopj = jj;
                }
            }
        }
        int bmsMinTempLocation = (loopi-1)*2+loopj-1+1; // Find the location of the Min cell temperature
        if (bmsMinTempLocation > 127) // Ensure it does not exceed 127, the maximum value the charger can take
        {
            bmsMinTempLocation = 127;
        }
        int  chg3Byte6 = 0b00000000;
        // 0b00 = normal, 0b01 = Wrong value, 0b10 = untrusted value
        // LSB b[1,2] = is a single cell overvoltage? 
        // b[3,4] = Is SoC right?
        // b[5,6] = Is charing overcurrent?
        // MSB b[7,8] = is temperature too high?
        // ------------------ USED FOR 1813 (chg3) --------------------------------
        //b[0-1] = Max Charge Voltage b[2-3] = Max Charge Current, b[4] = Mode 01 = Const. Voltage, 02 Const. Current
        //b[5-7] = Unused
        int  chg3Byte7 = 0b11010000;
        // 0b00 = normal, 0b01 = Wrong value, 0b10 = untrusted value
        // LSB b[1,2] =Is the battery insulation test normal?
        // b[3,4] = Is the battery connector status normal?
        // b[5,6] = Is charging allowed? THIS IS DIFFERENT 0b00 = don't allow charging, 0b01 = allow charging (01)
        // MSB b[7,8] = undefined (11)
        // ------------------ USED FOR 1813 (chg3) --------------------------------

	// ID: 181356F4 (BCP)
        int[] chg3 = {0x05, // Location of highest single battery power range 1 to 256
                      0x05, // Location of the cell with the highest power, NOTE THIS IS HARD CODED
                      (200-bmsMaxTemp)&0xFF, // Maximum cell temperature, range -50 to 200C
                      bmsMaxTempLocation+1, // Maximum cell temperature location, bit = location+1, range 1 to 127
                      (200-bmsMaxTemp)&0xFF, // Minimum cell temperature, range -50 to 200C
                      bmsMinTempLocation+1, // Minimum cell temperature location, bit = location+1,range 1 to 127
                      chg3Byte6, // Refer to declaration comments HARDCODED
                      chg3Byte7, // Refer to declaration comments HARDCODED
                      0xFF}; // This byte is undefined
        
	//----------------------------------------------------------------------------------------------------------------------
	
        this.isChargeExecutorOn = true;
        
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
                    //System.out.println("0x181056F4: "+Int2String(chg1));
                    
                    if(++count == 25)
                    {
                        handler.writeMessage(0x1cec56f4, chg2);  //250ms
                        //System.out.println("0x1CEC56F4: "+Int2String(chg2));
                        count = 0;
                    }
                    
                    if(++count2 == 1000)
                    {
                        handler.writeMessage(0x181356F4, chg3);
                        //System.out.println("0x181356F4: "+Int2String(chg3));
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
            //System.out.println("0x1CEC56F4: "+Int2String(chg));
            
        }
        catch (IOException ex) 
        {
            ex.printStackTrace();
            Logger.getLogger(ChargerGBT.class.getName()).log(Level.SEVERE, null, ex);
        }
     }
     
    public void sendBatteryChargingState() 
    {
         //First message
        //b[1] = data no., b[1-2] = charging voltage
        //b[3-4] =  Charge Current, b[5] = highest cell
        //b[5-7] = Unused
        
        int maxVoltage = -1;
	int maxCellNumber = -1;
	int maxBmsNumber = -1;
        BMS12v3[] bms = CANFilter.getInstance().getBMS();
	
	for(int ii = 0; ii < bms.length; ii++)
	{
	    for(int jj = 0; jj < BMS12v3.NUMBER_OF_CELLS; jj++)
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
            0xFF, 0xFF, 0xFF, 0xFF, 0xFF
        };
        
        try 
        {
            handler.writeMessage(0x1CEB56F4, chg1);
            //System.out.println("0x1CEB56F4: "+Int2String(chg1));
            handler.writeMessage(0x1CEB56F4, chg2);
            //System.out.println("0x1CEB56F4: "+Int2String(chg2));
        } 
        catch (IOException ex) 
        {
            ex.printStackTrace();
        }
    }
    
    public void stopCharging() //not needed for prototype
    {        
        if(this.isChargeExecutorOn)
        {
            this.chargeExecutor.shutdown();
        }
        this.isChargeExecutorOn = false;
        
        if(this.isHandshakeExecutorOn)
        {
            this.handshakeExecutor.shutdown();
        }
        this.isHandshakeExecutorOn = false;
        
        if(this.isExeSendReadyToCharge)
        {
            this.chargeExecutor.shutdown();
        }
        this.isExeSendReadyToCharge = false;
        
        if(state == state1)
        {
            this.state = waitingToStopState;            
        }
        else
        {
            this.state = state12;
        }
    }
    
    public void setState(State state)
    {
        this.state = state;
    }
    
    public State getState0()
    {
        return state0;
    }

    public State getState1() 
    {
        return state1;
    }

    public State getState2() 
    {
        return state2;
    }

    public State getState3() 
    {
        return state3;
    }

    public State getState4() 
    {
        return state4;
    }

    public State getState5() 
    {
        return state5;
    }

    public State getState6() 
    {
        return state6;
    }

    public State getState7() 
    {
        return state7;
    }

    public State getState8() 
    {
        return state8;
    }

    public State getState9() 
    {
        return state9;
    }

    public State getState10() 
    {
        return state10;
    }

    public State getState11() 
    {
        return state11;
    }

    public State getState12() 
    {
        return state12;
    }
    
    public State getWaitingToStopState()
    {
        return waitingToStopState;
    }
}
