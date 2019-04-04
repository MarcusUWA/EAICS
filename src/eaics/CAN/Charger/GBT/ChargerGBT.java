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
import java.util.Arrays;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class ChargerGBT  {
    
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
    
    int state = 0;
    boolean isCharging = false;
    String error = "";
    
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
        return minChargerCurrent;
    }
    public float getMinChargerVoltage() 
    {
        return minChargerVoltage;
    }
    public int getTimeOnCharge() 
    {
        return timeOnCharge;
    }

    public int getState() {
        return state;
    }

    public String getError() {
        return error;
    }

    public void setIsCharging(boolean isCharging) {
        this.isCharging = isCharging;
    }

    public boolean isIsCharging() {
        return isCharging;
    }
    
    public ChargerGBT(CANFilter filter) {
        isHandshakeExecutorOn = false;
        isChargeExecutorOn = false;
        
        this.evms = filter.getEVMS();
        this.settings = SettingsEAICS.getInstance().getEVMSSettings();
        if(SettingsEAICS.getInstance().getGeneralSettings().getVeh()!=TYPEVehicle.WAVEFLYER) {
            this.handler = filter.getCANHandler(SettingsEAICS.getInstance().getCanSettings().getChargerCAN()); // Must change manually, UPDATE AS A = 0 B = 1
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
    
    public void handleCharger(CANMessage message)  {        
        final int CANID = message.getFrameID();
        int[] data = message.getByteData();
        
        if(isCharging) {
        
            switch(state) {  
                //Pre-Handshake state
                case 0:
                    if(CANID == 0x1826F456) {
                        System.out.println("Received Charger Handshake Message (CHM), send BMS Handshake Messages (BHM). Increment to State 1");
                        error = "Starting Handshake";
                        startSendHandshake();
                        setState(1);
                    }
                    else if(CANID == 0x101AF456) {
                        //Ignore - Charge just blasts these while stopping charge
                        error = "Stopping Charge - "+message.toString();
                    }
                    else if(CANID == 0x81FFF456) {
                        error = "Charger Timeout - "+message.toString();
                        //System.out.println("Charger Timeout");
                    }
                    else if(CANID == 0x1801F456) {
                         error = "Error Messages - "+message.toString();
                        //Ignore - error messages
                    }
                    else if(CANID == 0x81FF456) {
                        error = "Charger Error Messages - "+message.toString();
                        //Ignore - charger error messages
                    }
                    else {
                        error = "Incorrect State 0 - "+message.toString();
                        //System.out.println("Incorrect state=|"+0+"|, for CHM ID=" + Integer.toHexString(CANID));
                    }
                    break;
                //Handshake
                case 1:
                    if(CANID == 0x1801F456) {
                        if(data[0]==0x00) {// Charger is waiting for BMS's ID
                            error = "Completed Handshake, Starting Transport Comms";
                            stopSendHandshake();
                            sendTransportCommManagement();
                            setState(2);
                        }
                    }
                    else if(CANID == 0x1826F456) {
                        error = "Unknown Packet on State 1 - " + message.toString();
                        //I don't know what this packet is, ask Alex.            
                    }
                    else if(CANID == 0x182756F4) {
                        error = "Handshaking - " + message.toString();
                    }
                    else {
                        error = "Incorrect State 1 - "+message.toString();
                        System.out.println("Incorrect state=|"+1+"|, for CHM ID=" + Integer.toHexString(CANID));
                        //throw new IllegalStateException("Incorrect state=|"+1+"|, for CHM ID=" + CANID);
                    }
                    break;
                //Transport
                case 2:
                    if(CANID == 0x1CECF456) {
                        if(data[0]==0x11) {// Charger is waiting for BMS's ID
                            if(data[1] == 0x07)  {
                                System.out.println("(Charger) Acknowledging Pre-ID received from BMS");
                                error = "Completed Transport Comms, Starting Pre-ID";
                                sendIdentificationParams();
                                setState(3);
                            }
                        }
                    }
                    else {
                        error = "Incorrect State 2 - "+message.toString();
                        System.out.println("Incorrect state=|"+2+"|, for CHM ID=" + Integer.toHexString(CANID));
                        //throw new IllegalStateException("Incorrect state=|"+2+"|, for CHM ID=" + CANID);
                    }
                    break;
                //ID
                case 3:
                    if(CANID == 0x1CECF456) {
                        if(data[0] == 0x13)  {
                            System.out.println("(Charger) ID settings acknowledged, sending BRM");
                            error = "Completed ID Check, sending BRM";
                            setState(4);
                        }
                    }
                    else {
                        error = "Incorrect State 3 - "+message.toString();
                        System.out.println("Incorrect state=|"+3+"|, for CHM ID=" + Integer.toHexString(CANID));
                        //throw new IllegalStateException("Incorrect state=|"+3+"|, for CHM ID=" + CANID);
                    }
                    break;    
                case 4:
                    if(CANID == 0x1801F456) {
                        if(data[0] == 0xAA) { // Charger has accepted BMS's ID
                            error = "Completed BRM, start sending Pre-Parameters";
                            System.out.println("Charger has received BRM, increment to state 5");
                            System.out.println("Completed handshake phase, continue to charging parameter setting stage");
                            sendPreParameterSettings();
                            setState(5);
                        }
                    }
                    else if(CANID == 0x1807F456) {
                        error = "Charger Date/Time - "+message.toString();
                        //ignore because just date and time from charger
                    }
                    else {
                        error = "Incorrect State 4 - "+message.toString();
                        System.out.println("Incorrect state=|"+4+"|, for CHM ID=" + Integer.toHexString(CANID));
                        //throw new IllegalStateException("Incorrect state=|"+4+"|, for CHM ID=" + CANID);
                    }
                    break;
                case 5:
                    if(CANID == 0x1CECF456) {
                        if(data[0]==0x11)  {
                            if(data[1] == 0x02) {
                                error = "Completed Pre-parameters, start sending Parameters";
                                System.out.println("(Charger) Acknowledgment of Pre-Parameter settings");
                                System.out.println("Continue to state 6");
                                sendParameterSettings();
                                setState(6);
                            }
                        }
                    }
                    else if(CANID == 0x1807F456) {
                        error = "Charger Date/Time - "+message.toString();
                        //ignore because just date and time from charger
                    }
                    else {
                        error = "Incorrect State 5 - "+message.toString();
                        System.out.println("Incorrect state=|"+5+"|, for CHM ID=" + Integer.toHexString(CANID));
                        //throw new IllegalStateException("Incorrect state=|"+5+"|, for CHM ID=" + CANID);
                    }
                    break;
                case 6:
                    if(CANID == 0x1CECF456) {
                        if(data[0]==0x13)  {
                            if(data[1] == 0x0D) {// Don't think we need this second check???? Remove later if no problem 
                                error = "ACK parameters";
                                System.out.println("(Charger) Acknowledgment of Parameter settings");
                                System.out.println("Continue to state 7");
                                setState(7);
                            }
                        }
                    }
                    else {
                        error = "Incorrect State 6 - "+message.toString();
                        System.out.println("Incorrect state=|"+6+"|, for CHM ID=" + Integer.toHexString(CANID));
                        //throw new IllegalStateException("Incorrect state=|"+6+"|, for CHM ID=" + CANID);
                    }
                    break;
                case 7:
                    if(CANID == 0x1807F456) {
                        error = "Time Syncing...";
                        System.out.println("(Charger) Sending Time Sync Data");
                        System.out.println("Continue to state 8");
                        setState(8);
                    }
                    else {
                        error = "Incorrect State 7 - "+message.toString();
                        System.out.println("Incorrect state=|"+7+"|, for CHM ID=" + Integer.toHexString(CANID));
                        //throw new IllegalStateException("Incorrect state=|"+7+"|, for CHM ID=" + CANID);
                    }
                    break;  
                case 8:
                    if(CANID == 0x1808F456) {
                        error = "Charger Stats";
                        System.out.println("(Charger) Sending Charger stats");
                        System.out.println("Continue to state 9 READY to charge");
                        startSendReadyToCharge();
                        setState(9);

                        System.out.println("Stats" +message.toString());
                        
                        // Concatenate hex values, such that the second byte of data is first, then divide by 10 and ensure that it is type casted to a float as data is originally an int
                        maxChargerVoltage = ((float) ((data[1]*256+data[0]%256)/10.0)); 
                       
                        minChargerVoltage = ((float) ((data[3]*256+data[2]%256)/10.0)); 
                        
                        // to calculate current it is 400 - data/10
                        maxChargerCurrent = ((float) (400.0 - (float) ((data[5]*256+data[4]%256)/10.0)));
                        
                        minChargerCurrent = ((float) (400.0 - (float) ((data[7]*256+data[6]%256)/10.0)));
                    }
                    else {
                        error = "Incorrect State 8 - "+message.toString();
                        System.out.println("Incorrect state=|"+8+"|, for CHM ID=" + Integer.toHexString(CANID));
                        //throw new IllegalStateException("Incorrect state=|"+8+"|, for CHM ID=" + CANID);
                    }
                    break;
                case 9:
                    if(CANID == 0x100AF456) {
                        if(data[0] == 0xAA) {
                            error = "Ready to Charge";
                            stopSendReadyToCharge();
                            startSendChargingPacket();
                            System.out.println("(Charger) Charger Ready to charge");
                            System.out.println("Continue to state 10");
                            setState(10);
                        }
                        else if(data[0] == 0x00) {
                            error = "Charger not Ready";
                            System.out.println("Charger is not ready for charging");
                        }
                    }
                    else {
                        error = "Incorrect State 9 - "+message.toString();
                        System.out.println("Incorrect state=|"+9+"|, for CHM ID=" + Integer.toHexString(CANID));
                        //throw new IllegalStateException("Incorrect state=|"+9+"|, for CHM ID=" + CANID);
                    }
                    break;
                case 10:
                    if(CANID == 0x1CECF456) {
                        if(data[0]==0x11) {
                            if(data[1] == 0x02) {
                                error = "Charing Updates....";
                                System.out.println("(Charger) Acknowledgment OK to receive charge update");
                                System.out.println("Continue to state 11");
                                setState(11);
                            }
                        }
                    }
                    else {
                        error = "Incorrect State 10 - "+message.toString();
                        System.out.println("Incorrect state=|"+10+"|, for CHM ID=" + Integer.toHexString(CANID));
                        //throw new IllegalStateException("Incorrect state=|"+10+"|, for CHM ID=" + CANID);
                    }
                    break;
                case 11:
                    if(CANID == 0x1CECF456) {
                        if(data[0] == 0x11) {
                            if(data[1] == 0x02) {
                                sendBatteryChargingState(); //This MAY be so slow, this is sending after 3 CEC's were sent
                            }
                        }
                    }
                    else if(CANID == 0x1812F456) {// The charger is sending back observed voltage/current
                        System.out.println("(Charger) Charger OK it is sending observed current/voltage");
                        // Concatenate hex values, such that the second byte of data is first, then divide by 10 and ensure that it is type casted to a float as data is originally an int
                        observedVoltage = ((float) ((data[1]*256+data[0]%256)/10.0));
                        // to calculate current it is 400 - data/10
                        observedCurrent = ((float) (400.0 - (float) ((data[3]*256+data[2]%256)/10.0)));
                        // convert each bit to a minute. NOTE: the range is 0 to 600 minutes
                        timeOnCharge = ((data[5]*256+data[4]%256));
                    }
                    else if(CANID == 0x100AF456) {
                        //Don't know what this packet is, Alex fix
                    }
                    else if(CANID == 0x101956F4) {
                        System.out.println("(Charger) Charger request stop charging");
                        stopCharging();
                        setState(12);
                    }
                    else if(CANID == 0x101AF456) {
                        setState(12);
                    }
                    else {
                        error = "Incorrect State 11 - "+message.toString();
                        System.out.println("Incorrect state=|"+11+"|, for CHM ID=" + Integer.toHexString(CANID));
                        //throw new IllegalStateException("Incorrect state=|"+11+"|, for CHM ID=" + CANID);
                    }
                    break;  
                case 12:
                    if(CANID == 0x101AF456) {
                        System.out.println("Stop charging accpeted - go to state 0");
                        setState(0);
                    }
                    break;
                case 13:
                    if(CANID == 0x1801F456)  {
                        System.out.println("Stop charging accpeted - go to state 0");
                        setState(0);
                    }

                default:
                    break;
            }
        }
        else {
            error = "Not in charging mode - "+message.toString();
            stopCharging();
            System.out.println("Not in charging mode...");
        }
    }
    
    public static String Int2String(int[] data) {
        return Arrays.toString(data);
    }
    
    private void startSendHandshake()  {
        isHandshakeExecutorOn = true;
        
        Runnable Handshake = new Runnable()  {            
	    @Override
	    public void run()  {   
                int[] data = {
                    maxChargeVoltage&0xFF,              //lower byte of charge voltage
                    (maxChargeVoltage>>8)&0xFF,         //upper byte
                    0xFF, 0xFF, 0xFF, 0xFF, 0xFF, 0xFF  //filler data
                };   
                handler.writeMessage(0x182756F4, data);
	    }
	};

	this.handshakeExecutor = Executors.newScheduledThreadPool(1);
	this.handshakeExecutor.scheduleAtFixedRate(Handshake, 0, 250, TimeUnit.MILLISECONDS);   // Run every second
    }
    
    private void stopSendHandshake() {
        if(this.isHandshakeExecutorOn) {
            this.handshakeExecutor.shutdown();
        }
        this.isHandshakeExecutorOn = false;
    }
    
    public void sendTransportCommManagement() {
        //hardcoded message
        int[] data = {0x10,0x29,0x00,0x07,0xFF, 0x00, 0x02,0x00};
        handler.writeMessage(0x1CEC56F4, data);
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
        
        handler.writeMessage(0x1CEB56F4, chg1);
        //System.out.println("0x1CEB56F4: "+Int2String(chg1));

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
        
        handler.writeMessage(0x1CEB56F4, chg2);
        //System.out.println("0x1CEB56F4: "+Int2String(chg2));

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
        
        handler.writeMessage(0x1CEB56F4, chg3);
        //System.out.println("0x1CEB56F4: "+Int2String(chg3));

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
        
        handler.writeMessage(0x1CEB56F4, chg4);
        //System.out.println("0x1CEB56F4: "+Int2String(chg4));

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
        
        handler.writeMessage(0x1CEB56F4, chg);
        //System.out.println("0x1CEB56F4: "+Int2String(chg));

        chg[0] = 0x06;
        
        handler.writeMessage(0x1CEB56F4, chg);
        //System.out.println("0x1CEB56F4: "+Int2String(chg));

        chg[0] = 0x07;
        
        handler.writeMessage(0x1CEB56F4, chg);
        //System.out.println("0x1CEB56F4: "+Int2String(chg));
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
        
        handler.writeMessage(0x1CEC56F4, chg);
        //System.out.println("0x1CEC56F4: "+Int2String(chg));
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
        
        handler.writeMessage(0x1CEB56F4, chg1);
        //System.out.println("0x1CEB56F4: "+Int2String(chg1));

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
        
        handler.writeMessage(0x1CEB56F4, chg2);
        //System.out.println("0x1CEB56F4: "+Int2String(chg2));
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
                handler.writeMessage(0x100956F4, chg1);
                //System.out.println("0x100956F4: "+Int2String(chg1));
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
	};

	this.chargeExecutor = Executors.newScheduledThreadPool(1);
	this.chargeExecutor.scheduleAtFixedRate(packet1, 0, 10, TimeUnit.MILLISECONDS);
    }
     
    public void sendChargingPreUpdate() 
    {
        //hardcoded
        int[] chg = {0x10, 0x09, 0x00, 0x02, 0xFF, 0x00, 0x11, 0x00};
        
        handler.writeMessage(0x1CEC56F4, chg);
        //System.out.println("0x1CEC56F4: "+Int2String(chg));
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
        
        handler.writeMessage(0x1CEB56F4, chg1);
        //System.out.println("0x1CEB56F4: "+Int2String(chg1));
        handler.writeMessage(0x1CEB56F4, chg2);
        //System.out.println("0x1CEB56F4: "+Int2String(chg2));
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
        
        if(state == 1) {
            this.state = 13;            
        }
        else {
            this.state = 12;
        }
    }
    
    public void setState(int state) {
        this.state = state;
    }
}
