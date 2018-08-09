/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eaics;

import java.io.IOException;

/**
 *
 * @author Troy
 */
public class BMSSettings 
{
    private ConfigData packCapacity;
    private ConfigData socWarning;
    private ConfigData fullVoltage;
    private ConfigData warnCurrent;
    private ConfigData tripCurrent;
    private ConfigData evmsTempWarning;
    private ConfigData minAuxVoltage;
    private ConfigData minIsolation;
    private ConfigData tachoPPR;
    private ConfigData fuelGaugeFull;
    private ConfigData fuelGaugeEmpty;
    private ConfigData tempGaugeHot;
    private ConfigData tempGaugeCold;
    private ConfigData bmsMinVoltage;
    private ConfigData bmsMaxVoltage;
    private ConfigData balanceVoltage;
    private ConfigData bmsHysteresis;
    private ConfigData bmsMinTemp;
    private ConfigData bmsMaxTemp;
    private ConfigData maxChargeVoltage;
    private ConfigData maxChargeCurrent;
    private ConfigData altChargeVoltage;
    private ConfigData altChargeCurrent;
    private ConfigData sleepDelay;
    private ConfigData mpiFunction;
    private ConfigData mpo1Function;
    private ConfigData mpo2Function;
    private ConfigData parallelStrings;
    private ConfigData enablePrecharge;
    private ConfigData stationaryMode;
    
    public BMSSettings()
    {
        this.packCapacity = new ConfigData("Ah x5", 1, 250, 20);
        this.socWarning = new ConfigData("%", 0, 99, 20);
        this.fullVoltage = new ConfigData("V x2", 5, 251, 80);
        this.warnCurrent = new ConfigData("A x10", 1, 121, 121);
        this.tripCurrent = new ConfigData("A x10", 1, 121, 121);
        this.evmsTempWarning = new ConfigData("degrees C", 0, 151, 151);    //Over temp (degC)
        this.minAuxVoltage = new ConfigData("V", 8, 15, 10);
        this.minIsolation = new ConfigData("%", 0, 99, 50);
	
        this.tachoPPR = new ConfigData("", 1, 6, 2);
        this.fuelGaugeFull = new ConfigData("%", 0, 100, 80);
        this.fuelGaugeEmpty = new ConfigData("%", 0, 100, 20);
        this.tempGaugeHot = new ConfigData("%", 0, 100, 80);
        this.tempGaugeCold = new ConfigData("%", 0, 100, 20);
        this.bmsMinVoltage = new ConfigData("1.50 + 0.01nV", 0, 250, 100);
        this.bmsMaxVoltage = new ConfigData("2.00 + 0.01nV", 0, 250, 180);
        this.balanceVoltage = new ConfigData("2.00 + 0.01nV", 0, 252, 251);
	
        this.bmsHysteresis = new ConfigData("x0.01V", 0, 50, 20);
        this.bmsMinTemp = new ConfigData("n-40 degrees C", 0, 141, 0);
        this.bmsMaxTemp = new ConfigData("n-40 degrees C", 0, 141, 141);
        this.maxChargeVoltage = new ConfigData("V", 0, 255, 100);
        this.maxChargeCurrent = new ConfigData("A", 0, 255, 10);
        this.altChargeVoltage = new ConfigData("V", 0, 255, 100);
        this.altChargeCurrent = new ConfigData("A", 0, 255, 20);
        this.sleepDelay = new ConfigData("minutes", 1, 6, 5);
        this.mpiFunction = new ConfigData("", 0, 3, 0);
        this.mpo1Function = new ConfigData("", 0, 6, 0);
        this.mpo2Function = new ConfigData("", 0, 6, 0);
        this.parallelStrings = new ConfigData("", 1, 20, 1);
        this.enablePrecharge = new ConfigData("", 0, 1, 1);
        this.stationaryMode = new ConfigData("", 0, 1, 0);
    }
    
    public void setSetting(int index, int setting)
    {        
        switch(index)
        {
            case 0:
                this.packCapacity.setSetting(setting);
                break;
            case 1:
                this.socWarning.setSetting(setting);
                break;
            case 2:
                this.fullVoltage.setSetting(setting);
                break;
            case 3:
                this.warnCurrent.setSetting(setting);
                break;
            case 4:
                this.tripCurrent.setSetting(setting);
                break;
            case 5:
                this.evmsTempWarning.setSetting(setting);
                break;
            case 6:
                this.minAuxVoltage.setSetting(setting);
                break;
            case 7:
                this.minIsolation.setSetting(setting);
                break;
            case 8:
                this.tachoPPR.setSetting(setting);
                break;
            case 9:
                this.fuelGaugeFull.setSetting(setting);
                break;
            case 10:
                this.fuelGaugeEmpty.setSetting(setting);
                break;
            case 11:
                this.tempGaugeHot.setSetting(setting);
                break;
            case 12:
                this.tempGaugeCold.setSetting(setting);
                break;
            case 13:
                this.bmsMinVoltage.setSetting(setting);
                break;
            case 14:
                this.bmsMaxVoltage.setSetting(setting);
                break;
            case 15:
                this.balanceVoltage.setSetting(setting);
                break;
            case 16:
                this.bmsHysteresis.setSetting(setting);
                break;
            case 17:
                this.bmsMinTemp.setSetting(setting);
                break;
            case 18:
                this.bmsMaxTemp.setSetting(setting);
                break;
            case 19:
                this.maxChargeVoltage.setSetting(setting);
                break;
            case 20:
                this.maxChargeCurrent.setSetting(setting);
                break;
            case 21:
                this.altChargeVoltage.setSetting(setting);
                break;
            case 22:
                this.altChargeCurrent.setSetting(setting);
                break;
            case 23:
                this.sleepDelay.setSetting(setting);
                break;
            case 24:
                this.mpiFunction.setSetting(setting);
                break;
            case 25:
                this.mpo1Function.setSetting(setting);
                break;
            case 26:
                this.mpo2Function.setSetting(setting);
                break;
            case 27:
                this.parallelStrings.setSetting(setting);
                break;
            case 28:
                this.enablePrecharge.setSetting(setting);
                break;
            case 29:
                this.stationaryMode.setSetting(setting);
                break;
            case 30:
                //reserved
                break;
            case 31:
                //reserved
                break;
        }
    }
    
    public String getUnit(int index)
    {
        String unit = "";
        
        switch(index)
        {
            case 0:
                unit = this.packCapacity.getUnit();
                break;
            case 1:
                unit = this.socWarning.getUnit();
                break;
            case 2:
                unit = this.fullVoltage.getUnit();
                break;
            case 3:
                unit = this.warnCurrent.getUnit();
                break;
            case 4:
                unit = this.tripCurrent.getUnit();
                break;
            case 5:
                unit = this.evmsTempWarning.getUnit();
                break;
            case 6:
                unit = this.minAuxVoltage.getUnit();
                break;
            case 7:
                unit = this.minIsolation.getUnit();
                break;
            case 8:
                unit = this.tachoPPR.getUnit();
                break;
            case 9:
                unit = this.fuelGaugeFull.getUnit();
                break;
            case 10:
                unit = this.fuelGaugeEmpty.getUnit();
                break;
            case 11:
                unit = this.tempGaugeHot.getUnit();
                break;
            case 12:
                unit = this.tempGaugeCold.getUnit();
                break;
            case 13:
                unit = this.bmsMinVoltage.getUnit();
                break;
            case 14:
                unit = this.bmsMaxVoltage.getUnit();
                break;
            case 15:
                unit = this.balanceVoltage.getUnit();
                break;
            case 16:
                unit = this.bmsHysteresis.getUnit();
                break;
            case 17:
                unit = this.bmsMinTemp.getUnit();
                break;
            case 18:
                unit = this.bmsMaxTemp.getUnit();
                break;
            case 19:
                unit = this.maxChargeVoltage.getUnit();
                break;
            case 20:
                unit = this.maxChargeCurrent.getUnit();
                break;
            case 21:
                unit = this.altChargeVoltage.getUnit();
                break;
            case 22:
                unit = this.altChargeCurrent.getUnit();
                break;
            case 23:
                unit = this.sleepDelay.getUnit();
                break;
            case 24:
                unit = this.mpiFunction.getUnit();
                break;
            case 25:
                unit = this.mpo1Function.getUnit();
                break;
            case 26:
                unit = this.mpo2Function.getUnit();
                break;
            case 27:
                unit = this.parallelStrings.getUnit();
                break;
            case 28:
                unit = this.enablePrecharge.getUnit();
                break;
            case 29:
                unit = this.stationaryMode.getUnit();
                break;
            case 30:
                //reserved
                break;
            case 31:
                //reserved
                break;
        }
        
        return unit;
    }
    
    public int getSetting(int index)
    {
        int setting = 0;
        
        switch(index)
        {
            case 0:
                setting = this.packCapacity.getSetting();
                break;
            case 1:
                setting = this.socWarning.getSetting();
                break;
            case 2:
                setting = this.fullVoltage.getSetting();
                break;
            case 3:
                setting = this.warnCurrent.getSetting();
                break;
            case 4:
                setting = this.tripCurrent.getSetting();
                break;
            case 5:
                setting = this.evmsTempWarning.getSetting();
                break;
            case 6:
                setting = this.minAuxVoltage.getSetting();
                break;
            case 7:
                setting = this.minIsolation.getSetting();
                break;
            case 8:
                setting = this.tachoPPR.getSetting();
                break;
            case 9:
                setting = this.fuelGaugeFull.getSetting();
                break;
            case 10:
                setting = this.fuelGaugeEmpty.getSetting();
                break;
            case 11:
                setting = this.tempGaugeHot.getSetting();
                break;
            case 12:
                setting = this.tempGaugeCold.getSetting();
                break;
            case 13:
                setting = this.bmsMinVoltage.getSetting();
                break;
            case 14:
                setting = this.bmsMaxVoltage.getSetting();
                break;
            case 15:
                setting = this.balanceVoltage.getSetting();
                break;
            case 16:
                setting = this.bmsHysteresis.getSetting();
                break;
            case 17:
                setting = this.bmsMinTemp.getSetting();
                break;
            case 18:
                setting = this.bmsMaxTemp.getSetting();
                break;
            case 19:
                setting = this.maxChargeVoltage.getSetting();
                break;
            case 20:
                setting = this.maxChargeCurrent.getSetting();
                break;
            case 21:
                setting = this.altChargeVoltage.getSetting();
                break;
            case 22:
                setting = this.altChargeCurrent.getSetting();
                break;
            case 23:
                setting = this.sleepDelay.getSetting();
                break;
            case 24:
                setting = this.mpiFunction.getSetting();
                break;
            case 25:
                setting = this.mpo1Function.getSetting();
                break;
            case 26:
                setting = this.mpo2Function.getSetting();
                break;
            case 27:
                setting = this.parallelStrings.getSetting();
                break;
            case 28:
                setting = this.enablePrecharge.getSetting();
                break;
            case 29:
                setting = this.stationaryMode.getSetting();
                break;
            case 30:
                //reserved
                break;
            case 31:
                //reserved
                break;
        }
        
        return setting;
    }
    
    public void update()
    {
        String msg1 = "00000020#";
        
        msg1 = addToMsg(msg1, this.packCapacity.getSetting());
        msg1 = addToMsg(msg1, this.socWarning.getSetting());
        msg1 = addToMsg(msg1, this.fullVoltage.getSetting());
        msg1 = addToMsg(msg1, this.warnCurrent.getSetting());
        msg1 = addToMsg(msg1, this.tripCurrent.getSetting());
        msg1 = addToMsg(msg1, this.evmsTempWarning.getSetting());
        msg1 = addToMsg(msg1, this.minAuxVoltage.getSetting());
        msg1 = addToMsg(msg1, this.minIsolation.getSetting());
        
        String msg2 = "00000021#";
        
        msg2 = addToMsg(msg2, this.tachoPPR.getSetting());
        msg2 = addToMsg(msg2, this.fuelGaugeFull.getSetting());
        msg2 = addToMsg(msg2, this.fuelGaugeEmpty.getSetting());
        msg2 = addToMsg(msg2, this.tempGaugeHot.getSetting());
        msg2 = addToMsg(msg2, this.tempGaugeCold.getSetting());
        msg2 = addToMsg(msg2, this.bmsMinVoltage.getSetting());
        msg2 = addToMsg(msg2, this.bmsMaxVoltage.getSetting());
        msg2 = addToMsg(msg2, this.balanceVoltage.getSetting());
        
        String msg3 = "00000022#";
        
        msg3 = addToMsg(msg3, this.bmsHysteresis.getSetting());
        msg3 = addToMsg(msg3, this.bmsMinTemp.getSetting());
        msg3 = addToMsg(msg3, this.bmsMaxTemp.getSetting());
        msg3 = addToMsg(msg3, this.maxChargeVoltage.getSetting());
        msg3 = addToMsg(msg3, this.maxChargeCurrent.getSetting());
        msg3 = addToMsg(msg3, this.altChargeVoltage.getSetting());
        msg3 = addToMsg(msg3, this.altChargeCurrent.getSetting());
        msg3 = addToMsg(msg3, this.sleepDelay.getSetting());
        
        String msg4 = "00000023#";
        
        msg4 = addToMsg(msg4, this.mpiFunction.getSetting());
        msg4 = addToMsg(msg4, this.mpo1Function.getSetting());
        msg4 = addToMsg(msg4, this.mpo2Function.getSetting());
        msg4 = addToMsg(msg4, this.parallelStrings.getSetting());
        msg4 = addToMsg(msg4, this.enablePrecharge.getSetting());
        msg4 = addToMsg(msg4, this.stationaryMode.getSetting());
        //reserved
        //reserved        
        
        System.out.println("Message>>"+msg1+"<<");
        System.out.println("Message>>"+msg2+"<<");
        System.out.println("Message>>"+msg3+"<<");
        System.out.println("Message>>"+msg4+"<<");
        
        try
        {
            final Process runMsg1 = Runtime.getRuntime().exec("/home/pi/bin/CANsend can0 " + msg1);
            final Process runMsg2 = Runtime.getRuntime().exec("/home/pi/bin/CANsend can0 " + msg2);
            final Process runMsg3 = Runtime.getRuntime().exec("/home/pi/bin/CANsend can0 " + msg3);
            final Process runMsg4 = Runtime.getRuntime().exec("/home/pi/bin/CANsend can0 " + msg4);
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
	if(temp.length() == 1)
	{
	    msg += "0";
	}
	msg += temp;
        
        return msg;
    }
}
