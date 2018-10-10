/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eaics;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Writer;

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
        //ConfigData(unit, min, max, initial, displayUnit, multiplier)
        this.packCapacity = new ConfigData("Ah x5", 1, 250, 20, "Ah", 5);
        this.socWarning = new ConfigData("%", 0, 99, 20, "%", 1);
        this.fullVoltage = new ConfigData("V x2", 5, 251, 80, "V", 2);
        this.warnCurrent = new ConfigData("A x10", 1, 121, 121, "A", 10);
        this.tripCurrent = new ConfigData("A x10", 1, 121, 121, "A", 10);
        this.evmsTempWarning = new ConfigData("degrees C", 0, 151, 151, "degrees C", 1);    //Over temp (degC)
        this.minAuxVoltage = new ConfigData("V", 8, 15, 10, "V", 1);
        this.minIsolation = new ConfigData("%", 0, 99, 50, "%", 1);
	
        this.tachoPPR = new ConfigData("", 1, 6, 2, "", 1);
        this.fuelGaugeFull = new ConfigData("%", 0, 100, 80, "%", 1);
        this.fuelGaugeEmpty = new ConfigData("%", 0, 100, 20, "%", 1);
        this.tempGaugeHot = new ConfigData("%", 0, 100, 80, "%", 1);
        this.tempGaugeCold = new ConfigData("%", 0, 100, 20, "%", 1);
        this.bmsMinVoltage = new ConfigData("1.50 + 0.01nV", 1500, 4000, 3000, "mV", 1);//0,250,100
        this.bmsMaxVoltage = new ConfigData("2.00 + 0.01nV", 2000, 4500, 4200, "mV", 1);//0,250,180
        this.balanceVoltage = new ConfigData("2.00 + 0.01nV", 2000, 4520, 4180, "mV", 1);//0,252,251
	
        this.bmsHysteresis = new ConfigData("x0.01V", 0, 50, 20, "V", 0.01);
        this.bmsMinTemp = new ConfigData("n-40 degrees C1", -40, 101, -40, "degrees C", 1);//0,141,0
        this.bmsMaxTemp = new ConfigData("n-40 degrees C", -40, 101, 101, "degrees C", 1);//0,141,141
        this.maxChargeVoltage = new ConfigData("V", 0, 255, 100, "V", 1);
        this.maxChargeCurrent = new ConfigData("A", 0, 255, 10, "A", 1);
        this.altChargeVoltage = new ConfigData("V", 0, 255, 100, "V", 1);
        this.altChargeCurrent = new ConfigData("A", 0, 255, 20, "A", 1);
        this.sleepDelay = new ConfigData("minutes", 1, 6, 5, "minutes", 1);
	
        this.mpiFunction = new ConfigData("", 0, 3, 0, "", 1);
        this.mpo1Function = new ConfigData("", 0, 6, 0, "", 1);
        this.mpo2Function = new ConfigData("", 0, 6, 0, "", 1);
        this.parallelStrings = new ConfigData("", 1, 20, 1, "", 1);
        this.enablePrecharge = new ConfigData("", 0, 1, 1, "Yes(1)/No(0)", 1);
        this.stationaryMode = new ConfigData("", 0, 1, 0, "Yes(1)/No(0)", 1);
	
	//loadSettings();
    }
    
    public void writeSettings()
    {
	Writer writer = null;
	try
	{
	    writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream("bmsSettingsFile"), "utf-8"));
	    
	    writer.write("" + this.packCapacity.getDisplaySetting() + "\n");
	    writer.write("" + this.socWarning.getDisplaySetting() + "\n");
	    writer.write("" + this.fullVoltage.getDisplaySetting() + "\n");
	    writer.write("" + this.warnCurrent.getDisplaySetting() + "\n");
	    writer.write("" + this.tripCurrent.getDisplaySetting() + "\n");
	    writer.write("" + this.evmsTempWarning.getDisplaySetting() + "\n");
	    writer.write("" + this.minAuxVoltage.getDisplaySetting() + "\n");
	    writer.write("" + this.minIsolation.getDisplaySetting() + "\n");
	    
	    writer.write("" + this.tachoPPR.getDisplaySetting() + "\n");
	    writer.write("" + this.fuelGaugeFull.getDisplaySetting() + "\n");
	    writer.write("" + this.fuelGaugeEmpty.getDisplaySetting() + "\n");
	    writer.write("" + this.tempGaugeHot.getDisplaySetting() + "\n");
	    writer.write("" + this.tempGaugeCold.getDisplaySetting() + "\n");
	    writer.write("" + this.bmsMinVoltage.getDisplaySetting() + "\n");
	    writer.write("" + this.bmsMaxVoltage.getDisplaySetting() + "\n");
	    writer.write("" + this.balanceVoltage.getDisplaySetting() + "\n");
	    
	    writer.write("" + this.bmsHysteresis.getDisplaySetting() + "\n");
	    writer.write("" + this.bmsMinTemp.getDisplaySetting() + "\n");
	    writer.write("" + this.bmsMaxTemp.getDisplaySetting() + "\n");
	    writer.write("" + this.maxChargeVoltage.getDisplaySetting() + "\n");
	    writer.write("" + this.maxChargeCurrent.getDisplaySetting() + "\n");
	    writer.write("" + this.altChargeVoltage.getDisplaySetting() + "\n");
	    writer.write("" + this.altChargeCurrent.getDisplaySetting() + "\n");
	    writer.write("" + this.sleepDelay.getDisplaySetting() + "\n");
	    
	    writer.write("" + this.mpiFunction.getDisplaySetting() + "\n");
	    writer.write("" + this.mpo1Function.getDisplaySetting() + "\n");
	    writer.write("" + this.mpo2Function.getDisplaySetting() + "\n");
	    writer.write("" + this.parallelStrings.getDisplaySetting() + "\n");
	    writer.write("" + this.enablePrecharge.getDisplaySetting() + "\n");
	    writer.write("" + this.stationaryMode.getDisplaySetting());
	    //blank1
	    //blank2
	    
	    writer.flush();
	    writer.close();
	}
	catch(IOException e)
	{
		e.printStackTrace();
	}
	finally
	{
		try
		{
			writer.close();
		}
		catch(Exception e)
		{
		}
	}
    }
    
    public void loadSettings()
    {
	BufferedReader reader;
		
	try
	{
	    reader = new BufferedReader(new InputStreamReader(new FileInputStream("/home/pi/EAICS/bmsSettingsFile"), "utf-8"));
	    String st;
	    int ii = 0;
	    while ((st = reader.readLine()) != null)
	    {
		this.setSetting(ii, Integer.parseInt(st));
		ii++;
	    }
	}
        catch(FileNotFoundException e)
        {
            writeSettings();
        }
	catch(Exception e)
	{
	    e.printStackTrace();
	}
        finally
        {
            update();
        }
    }
    
    public double getMultiplier(int index)
    {
	double multiplier = 1;
	
	switch(index)
        {
            case 0:
                multiplier = 5;
                break;
            case 1:
                multiplier = 1;
                break;
            case 2:
                multiplier = 2;
                break;
            case 3:
                multiplier = 10;
                break;
            case 4:
                multiplier = 10;
                break;
            case 5:
                multiplier = 1;
                break;
            case 6:
                multiplier = 1;
                break;
            case 7:
                multiplier = 1;
                break;
            case 8:
                multiplier = 1;
                break;
            case 9:
                multiplier = 1;
                break;
            case 10:
                multiplier = 1;
                break;
            case 11:
                multiplier = 1;
                break;
            case 12:
                multiplier = 1;
                break;
            case 13:
                multiplier = 1;
                break;
            case 14:
                multiplier = 1;
                break;
            case 15:
                multiplier = 1;
                break;
            case 16:
                multiplier = 0.01;
                break;
            case 17:
                multiplier = 1;
                break;
            case 18:
                multiplier = 1;
                break;
            case 19:
                multiplier = 1;
                break;
            case 20:
                multiplier = 1;
                break;
            case 21:
                multiplier = 1;
                break;
            case 22:
                multiplier = 1;
                break;
            case 23:
                multiplier = 1;
                break;
            case 24:
                multiplier = 1;
                break;
            case 25:
                multiplier = 1;
                break;
            case 26:
                multiplier = 1;
                break;
            case 27:
                multiplier = 1;
                break;
            case 28:
                multiplier = 1;
                break;
            case 29:
                multiplier = 1;
                break;
            case 30:
                multiplier = 1;
                break;
            case 31:
                multiplier = 1;
                break;
        }
	
	return multiplier;
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
	
	writeSettings();
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
    
    public int getDisplaySetting(int index)
    {        
        int setting = 0;
	
	switch(index)
        {
            case 0:
                setting = this.packCapacity.getDisplaySetting();
                break;
            case 1:
                setting = this.socWarning.getDisplaySetting();
                break;
            case 2:
                setting = this.fullVoltage.getDisplaySetting();
                break;
            case 3:
                setting = this.warnCurrent.getDisplaySetting();
                break;
            case 4:
                setting = this.tripCurrent.getDisplaySetting();
                break;
            case 5:
                setting = this.evmsTempWarning.getDisplaySetting();
                break;
            case 6:
                setting = this.minAuxVoltage.getDisplaySetting();
                break;
            case 7:
                setting = this.minIsolation.getDisplaySetting();
                break;
            case 8:
                setting = this.tachoPPR.getDisplaySetting();
                break;
            case 9:
                setting = this.fuelGaugeFull.getDisplaySetting();
                break;
            case 10:
                setting = this.fuelGaugeEmpty.getDisplaySetting();
                break;
            case 11:
                setting = this.tempGaugeHot.getDisplaySetting();
                break;
            case 12:
                setting = this.tempGaugeCold.getDisplaySetting();
                break;
            case 13:
                setting = this.bmsMinVoltage.getDisplaySetting();
                break;
            case 14:
                setting = this.bmsMaxVoltage.getDisplaySetting();
                break;
            case 15:
                setting = this.balanceVoltage.getDisplaySetting();
                break;
            case 16:
                setting = this.bmsHysteresis.getDisplaySetting();
                break;
            case 17:
                setting = this.bmsMinTemp.getDisplaySetting();
                break;
            case 18:
                setting = this.bmsMaxTemp.getDisplaySetting();
                break;
            case 19:
                setting = this.maxChargeVoltage.getDisplaySetting();
                break;
            case 20:
                setting = this.maxChargeCurrent.getDisplaySetting();
                break;
            case 21:
                setting = this.altChargeVoltage.getDisplaySetting();
                break;
            case 22:
                setting = this.altChargeCurrent.getDisplaySetting();
                break;
            case 23:
                setting = this.sleepDelay.getDisplaySetting();
                break;
            case 24:
                setting = this.mpiFunction.getDisplaySetting();
                break;
            case 25:
                setting = this.mpo1Function.getDisplaySetting();
                break;
            case 26:
                setting = this.mpo2Function.getDisplaySetting();
                break;
            case 27:
                setting = this.parallelStrings.getDisplaySetting();
                break;
            case 28:
                setting = this.enablePrecharge.getDisplaySetting();
                break;
            case 29:
                setting = this.stationaryMode.getDisplaySetting();
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
    
    public String getDisplayUnits(int index)
    {
        String unit = "";
        
        switch(index)
        {
            case 0:
                unit = this.packCapacity.getDisplayUnits();
                break;
            case 1:
                unit = this.socWarning.getDisplayUnits();
                break;
            case 2:
                unit = this.fullVoltage.getDisplayUnits();
                break;
            case 3:
                unit = this.warnCurrent.getDisplayUnits();
                break;
            case 4:
                unit = this.tripCurrent.getDisplayUnits();
                break;
            case 5:
                unit = this.evmsTempWarning.getDisplayUnits();
                break;
            case 6:
                unit = this.minAuxVoltage.getDisplayUnits();
                break;
            case 7:
                unit = this.minIsolation.getDisplayUnits();
                break;
            case 8:
                unit = this.tachoPPR.getDisplayUnits();
                break;
            case 9:
                unit = this.fuelGaugeFull.getDisplayUnits();
                break;
            case 10:
                unit = this.fuelGaugeEmpty.getDisplayUnits();
                break;
            case 11:
                unit = this.tempGaugeHot.getDisplayUnits();
                break;
            case 12:
                unit = this.tempGaugeCold.getDisplayUnits();
                break;
            case 13:
                unit = this.bmsMinVoltage.getDisplayUnits();
                break;
            case 14:
                unit = this.bmsMaxVoltage.getDisplayUnits();
                break;
            case 15:
                unit = this.balanceVoltage.getDisplayUnits();
                break;
            case 16:
                unit = this.bmsHysteresis.getDisplayUnits();
                break;
            case 17:
                unit = this.bmsMinTemp.getDisplayUnits();
                break;
            case 18:
                unit = this.bmsMaxTemp.getDisplayUnits();
                break;
            case 19:
                unit = this.maxChargeVoltage.getDisplayUnits();
                break;
            case 20:
                unit = this.maxChargeCurrent.getDisplayUnits();
                break;
            case 21:
                unit = this.altChargeVoltage.getDisplayUnits();
                break;
            case 22:
                unit = this.altChargeCurrent.getDisplayUnits();
                break;
            case 23:
                unit = this.sleepDelay.getDisplayUnits();
                break;
            case 24:
                unit = this.mpiFunction.getDisplayUnits();
                break;
            case 25:
                unit = this.mpo1Function.getDisplayUnits();
                break;
            case 26:
                unit = this.mpo2Function.getDisplayUnits();
                break;
            case 27:
                unit = this.parallelStrings.getDisplayUnits();
                break;
            case 28:
                unit = this.enablePrecharge.getDisplayUnits();
                break;
            case 29:
                unit = this.stationaryMode.getDisplayUnits();
                break;
            case 30:
                unit = "";
                break;
            case 31:
                unit = "";
                break;
        }
        
        return unit;
    }
    
    public String getName(int index)
    {
        String name = "";
        
        switch(index)
        {
            case 0:
                name = "Pack Capacity";
                break;
            case 1:
                name = "SoC Warning";
                break;
            case 2:
                name = "Full Voltage";
                break;
            case 3:
                name = "Warning Current";
                break;
            case 4:
                name = "Trip Current";
                break;
            case 5:
                name = "EVMS Temp Warning";
                break;
            case 6:
                name = "Min Aux Voltage";
                break;
            case 7:
                name = "Min Isolation";
                break;
            case 8:
                name = "Tacho PPR";
                break;
            case 9:
                name = "Fuel Gauge Full";
                break;
            case 10:
                name = "Fuel Gauge Empty";
                break;
            case 11:
                name = "Temp Gauge Hot";
                break;
            case 12:
                name = "Temp Gauge Cold";
                break;
            case 13:
                name = "BMS Min Voltage";
                break;
            case 14:
                name = "BMS Max Voltage";
                break;
            case 15:
                name = "Balance Voltage";
                break;
            case 16:
                name = "BMS Hysteresis";
                break;
            case 17:
                name = "BMS Min Temp";
                break;
            case 18:
                name = "BMS Max Temp";
                break;
            case 19:
                name = "Max Charge Voltage";
                break;
            case 20:
                name = "Max Charge Current";
                break;
            case 21:
                name = "Alt Charge Voltage";
                break;
            case 22:
                name = "Alt Charge Current";
                break;
            case 23:
                name = "Sleep Delay";
                break;
            case 24:
                name = "MPI Function";
                break;
            case 25:
                name = "MPO 1 Function";
                break;
            case 26:
                name = "MPO 2 Function";
                break;
            case 27:
                name = "Parallel Strings";
                break;
            case 28:
                name = "Enable Precharge";
                break;
            case 29:
                name = "Stationary Mode";
                break;
            case 30:
                name = "Blank 1";
                break;
            case 31:
                name = "Blank 2";
                break;
        }
        
        return name;
    }
    
    public int getMaxValue(int index)
    {
        int maxValue = 0;
        
        switch(index)
        {
            case 0:
                maxValue = this.packCapacity.getMax();
                break;
            case 1:
                maxValue = this.socWarning.getMax();
                break;
            case 2:
                maxValue = this.fullVoltage.getMax();
                break;
            case 3:
                maxValue = this.warnCurrent.getMax();
                break;
            case 4:
                maxValue = this.tripCurrent.getMax();
                break;
            case 5:
                maxValue = this.evmsTempWarning.getMax();
                break;
            case 6:
                maxValue = this.minAuxVoltage.getMax();
                break;
            case 7:
                maxValue = this.minIsolation.getMax();
                break;
            case 8:
                maxValue = this.tachoPPR.getMax();
                break;
            case 9:
                maxValue = this.fuelGaugeFull.getMax();
                break;
            case 10:
                maxValue = this.fuelGaugeEmpty.getMax();
                break;
            case 11:
                maxValue = this.tempGaugeHot.getMax();
                break;
            case 12:
                maxValue = this.tempGaugeCold.getMax();
                break;
            case 13:
                maxValue = this.bmsMinVoltage.getMax();
                break;
            case 14:
                maxValue = this.bmsMaxVoltage.getMax();
                break;
            case 15:
                maxValue = this.balanceVoltage.getMax();
                break;
            case 16:
                maxValue = this.bmsHysteresis.getMax();
                break;
            case 17:
                maxValue = this.bmsMinTemp.getMax();
                break;
            case 18:
                maxValue = this.bmsMaxTemp.getMax();
                break;
            case 19:
                maxValue = this.maxChargeVoltage.getMax();
                break;
            case 20:
                maxValue = this.maxChargeCurrent.getMax();
                break;
            case 21:
                maxValue = this.altChargeVoltage.getMax();
                break;
            case 22:
                maxValue = this.altChargeCurrent.getMax();
                break;
            case 23:
                maxValue = this.sleepDelay.getMax();
                break;
            case 24:
                maxValue = this.mpiFunction.getMax();
                break;
            case 25:
                maxValue = this.mpo1Function.getMax();
                break;
            case 26:
                maxValue = this.mpo2Function.getMax();
                break;
            case 27:
                maxValue = this.parallelStrings.getMax();
                break;
            case 28:
                maxValue = this.enablePrecharge.getMax();
                break;
            case 29:
                maxValue = this.stationaryMode.getMax();
                break;
            case 30:
                maxValue = 0;
                break;
            case 31:
                maxValue = 0;
                break;
        }
        
        return maxValue;
    }
    
    public int getMinValue(int index)
    {
        int minValue = 0;
        
        switch(index)
        {
            case 0:
                minValue = this.packCapacity.getMin();
                break;
            case 1:
                minValue = this.socWarning.getMin();
                break;
            case 2:
                minValue = this.fullVoltage.getMin();
                break;
            case 3:
                minValue = this.warnCurrent.getMin();
                break;
            case 4:
                minValue = this.tripCurrent.getMin();
                break;
            case 5:
                minValue = this.evmsTempWarning.getMin();
                break;
            case 6:
                minValue = this.minAuxVoltage.getMin();
                break;
            case 7:
                minValue = this.minIsolation.getMin();
                break;
            case 8:
                minValue = this.tachoPPR.getMin();
                break;
            case 9:
                minValue = this.fuelGaugeFull.getMin();
                break;
            case 10:
                minValue = this.fuelGaugeEmpty.getMin();
                break;
            case 11:
                minValue = this.tempGaugeHot.getMin();
                break;
            case 12:
                minValue = this.tempGaugeCold.getMin();
                break;
            case 13:
                minValue = this.bmsMinVoltage.getMin();
                break;
            case 14:
                minValue = this.bmsMaxVoltage.getMin();
                break;
            case 15:
                minValue = this.balanceVoltage.getMin();
                break;
            case 16:
                minValue = this.bmsHysteresis.getMin();
                break;
            case 17:
                minValue = this.bmsMinTemp.getMin();
                break;
            case 18:
                minValue = this.bmsMaxTemp.getMin();
                break;
            case 19:
                minValue = this.maxChargeVoltage.getMin();
                break;
            case 20:
                minValue = this.maxChargeCurrent.getMin();
                break;
            case 21:
                minValue = this.altChargeVoltage.getMin();
                break;
            case 22:
                minValue = this.altChargeCurrent.getMin();
                break;
            case 23:
                minValue = this.sleepDelay.getMin();
                break;
            case 24:
                minValue = this.mpiFunction.getMin();
                break;
            case 25:
                minValue = this.mpo1Function.getMin();
                break;
            case 26:
                minValue = this.mpo2Function.getMin();
                break;
            case 27:
                minValue = this.parallelStrings.getMin();
                break;
            case 28:
                minValue = this.enablePrecharge.getMin();
                break;
            case 29:
                minValue = this.stationaryMode.getMin();
                break;
            case 30:
                minValue = 0;
                break;
            case 31:
                minValue = 0;
                break;
        }
        
        return minValue;
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
        msg2 = addToMsg(msg2, (int)Math.round(this.bmsMinVoltage.getSetting()/10.0) - 150);
        msg2 = addToMsg(msg2, (int)Math.round(this.bmsMaxVoltage.getSetting()/10.0) - 200);
        msg2 = addToMsg(msg2, (int)Math.round(this.balanceVoltage.getSetting()/10.0) - 200);
        String msg3 = "00000022#";
        
        msg3 = addToMsg(msg3, this.bmsHysteresis.getSetting());
        msg3 = addToMsg(msg3, this.bmsMinTemp.getSetting() + 40);
        msg3 = addToMsg(msg3, this.bmsMaxTemp.getSetting() + 40);
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
        
        writeSettings();
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
