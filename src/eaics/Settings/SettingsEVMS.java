/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eaics.Settings;

import eaics.CAN.CANFilter;
import eaics.CAN.MiscCAN.CANHandler;

/**
 *
 * @author Troy
 */
public class SettingsEVMS implements Settings {
    private SettingsConfigData packCapacity;
    private SettingsConfigData socWarning;
    private SettingsConfigData fullVoltage;
    private SettingsConfigData warnCurrent;
    private SettingsConfigData tripCurrent;
    private SettingsConfigData evmsTempWarning;
    private SettingsConfigData minAuxVoltage;
    private SettingsConfigData minIsolation;
    private SettingsConfigData tachoPPR;
    private SettingsConfigData fuelGaugeFull;
    private SettingsConfigData fuelGaugeEmpty;
    private SettingsConfigData tempGaugeHot;
    private SettingsConfigData tempGaugeCold;
    private SettingsConfigData bmsMinVoltage;
    private SettingsConfigData bmsMaxVoltage;
    private SettingsConfigData balanceVoltage;
    private SettingsConfigData bmsHysteresis;
    private SettingsConfigData bmsMinTemp;
    private SettingsConfigData bmsMaxTemp;
    private SettingsConfigData maxChargeVoltage;
    private SettingsConfigData maxChargeCurrent;
    private SettingsConfigData altChargeVoltage;
    private SettingsConfigData altChargeCurrent;
    private SettingsConfigData sleepDelay;
    private SettingsConfigData mpiFunction;
    private SettingsConfigData mpo1Function;
    private SettingsConfigData mpo2Function;
    private SettingsConfigData parallelStrings;
    private SettingsConfigData enablePrecharge;
    private SettingsConfigData stationaryMode;
    
    public SettingsEVMS() {
        //ConfigData(min, max, initial, unit)
        this.packCapacity = new SettingsConfigData(5, 1250, 10, "Ah");
        this.socWarning = new SettingsConfigData(0, 99, 20, "%");
        this.fullVoltage = new SettingsConfigData(10, 502, 402, "V");
        this.warnCurrent = new SettingsConfigData(10, 1210, 500, "A");
        this.tripCurrent = new SettingsConfigData(10, 1210, 500, "A");
        this.evmsTempWarning = new SettingsConfigData(0, 151, 100, "degrees C");    //Over temp (degC)
        this.minAuxVoltage = new SettingsConfigData(8, 15, 10, "V");
        this.minIsolation = new SettingsConfigData(0, 99, 20, "%");
	
        this.tachoPPR = new SettingsConfigData(1, 6, 2, "");
        this.fuelGaugeFull = new SettingsConfigData(0, 100, 80, "%");
        this.fuelGaugeEmpty = new SettingsConfigData(0, 100, 20, "%");
        this.tempGaugeHot = new SettingsConfigData(0, 100, 80, "%");
        this.tempGaugeCold = new SettingsConfigData(0, 100, 20, "%");
        this.bmsMinVoltage = new SettingsConfigData(1500, 4000, 3000, "mV");//0,250,100
        this.bmsMaxVoltage = new SettingsConfigData(2000, 4500, 4200, "mV");//0,250,180
        this.balanceVoltage = new SettingsConfigData(2000, 4520, 4180, "mV");//0,252,251
	
        this.bmsHysteresis = new SettingsConfigData(0, 500, 100, "mV");//divide!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
        this.bmsMinTemp = new SettingsConfigData(-40, 101, -40, "degrees C");//0,141,0
        this.bmsMaxTemp = new SettingsConfigData(-40, 101, 101, "degrees C");//0,141,141
        this.maxChargeVoltage = new SettingsConfigData(0, 511, 400, "V");//0,255,100
        this.maxChargeCurrent = new SettingsConfigData(0, 127, 10, "A");//0,255,10
        this.altChargeVoltage = new SettingsConfigData(0, 511, 400, "V");//0,255,100
        this.altChargeCurrent = new SettingsConfigData(0, 127, 10, "A");//0,255,20
        this.sleepDelay = new SettingsConfigData(1, 6, 6, "minutes, 6=Off");
	
        this.mpiFunction = new SettingsConfigData(0, 3, 0, "");
        this.mpo1Function = new SettingsConfigData(0, 6, 0, "");
        this.mpo2Function = new SettingsConfigData(0, 6, 0, "");
        this.parallelStrings = new SettingsConfigData(1, 20, 3, "");
        this.enablePrecharge = new SettingsConfigData(0, 1, 1, "Yes(1)/No(0)");
        this.stationaryMode = new SettingsConfigData(0, 1, 0, "Yes(1)/No(0)");
    }
    
    @Override
    public String getSettingsFileString() {
        String settingsFileString = "";
        
	settingsFileString += "Pack Capacity:" + "\t\t\t\t#";
        settingsFileString += this.packCapacity.getSetting() + "\n";
	settingsFileString += "SOC Warning:" + "\t\t\t\t#";
        settingsFileString += this.socWarning.getSetting() + "\n";
	settingsFileString += "Full Voltage:" + "\t\t\t\t#";
        settingsFileString += this.fullVoltage.getSetting() + "\n";
	settingsFileString += "Warning Current:" + "\t\t\t#";
        settingsFileString += this.warnCurrent.getSetting() + "\n";
	settingsFileString += "Trip Current:" + "\t\t\t\t#";
        settingsFileString += this.tripCurrent.getSetting() + "\n";
	settingsFileString += "EVMS Temperature Warning:" + "\t\t#";
        settingsFileString += this.evmsTempWarning.getSetting() + "\n";
	settingsFileString += "Minimum Auxillary Voltage:" + "\t\t#";
        settingsFileString += this.minAuxVoltage.getSetting() + "\n";
	settingsFileString += "Minimum Isolation:" + "\t\t\t#";
        settingsFileString += this.minIsolation.getSetting() + "\n";
        
	settingsFileString += "Tacho PPR:" + "\t\t\t\t#";
        settingsFileString += this.tachoPPR.getSetting() + "\n";
	settingsFileString += "Fuel Gauge Full:" + "\t\t\t#";
        settingsFileString += this.fuelGaugeFull.getSetting() + "\n";
	settingsFileString += "Fuel Gauge Empty:" + "\t\t\t#";
        settingsFileString += this.fuelGaugeEmpty.getSetting() + "\n";
	settingsFileString += "Temperature Gauge Hot:" + "\t\t\t#";
        settingsFileString += this.tempGaugeHot.getSetting() + "\n";
	settingsFileString += "Temperature Gauge Cold:" + "\t\t\t#";
        settingsFileString += this.tempGaugeCold.getSetting() + "\n";
	settingsFileString += "BMS Minimum Voltage:" + "\t\t\t#";
        settingsFileString += this.bmsMinVoltage.getSetting() + "\n";
	settingsFileString += "BMS Maximum Voltage:" + "\t\t\t#";
        settingsFileString += this.bmsMaxVoltage.getSetting() + "\n";
	settingsFileString += "Balance Voltage:" + "\t\t\t#";
        settingsFileString += this.balanceVoltage.getSetting() + "\n";
  
	settingsFileString += "BMS Hysteresis:" + "\t\t\t\t#";
        settingsFileString += this.bmsHysteresis.getSetting() + "\n";
	settingsFileString += "BMS Minimum Temperature:" + "\t\t#";
        settingsFileString += (this.bmsMinTemp.getSetting() + 40) + "\n";
	settingsFileString += "BMS Maximum Temperature:" + "\t\t#";
        settingsFileString += (this.bmsMaxTemp.getSetting() + 40) + "\n";
	settingsFileString += "Maximum Charge Voltage:" + "\t\t\t#";
        settingsFileString += this.maxChargeVoltage.getSetting() + "\n";
	settingsFileString += "Maximum Charge Current:" + "\t\t\t#";
        settingsFileString += this.maxChargeCurrent.getSetting() + "\n";
	settingsFileString += "Alt Charge Voltage:" + "\t\t\t#";
        settingsFileString += this.altChargeVoltage.getSetting() + "\n";
	settingsFileString += "Alt Charge Current:" + "\t\t\t#";
        settingsFileString += this.altChargeCurrent.getSetting() + "\n";
	settingsFileString += "Sleep Delay:" + "\t\t\t\t#";
        settingsFileString += this.sleepDelay.getSetting() + "\n";
        
	settingsFileString += "MPI Function:" + "\t\t\t\t#";
        settingsFileString += this.mpiFunction.getSetting() + "\n";
	settingsFileString += "MPO1 Function:" + "\t\t\t\t#";
        settingsFileString += this.mpo1Function.getSetting() + "\n";
	settingsFileString += "MPO2 Function:" + "\t\t\t\t#";
        settingsFileString += this.mpo2Function.getSetting() + "\n";
	settingsFileString += "Parallel Strings:" + "\t\t\t#";
        settingsFileString += this.parallelStrings.getSetting() + "\n";
	settingsFileString += "Enable Precharge:" + "\t\t\t#";
        settingsFileString += this.enablePrecharge.getSetting() + "\n";
	settingsFileString += "Stationary Mode:" + "\t\t\t#";
        settingsFileString += this.stationaryMode.getSetting() + "\n";
        
        return settingsFileString;
    }
    
    //only passing the values.....
    @Override
    public void setSettings(String fileString)
    {
        int ii = 0;
        String[] lines = fileString.split("\\r?\\n");
        for (String line : lines) 
        {
            if(ii == 17 || ii == 18)
            {
                this.setSetting(ii, (Integer.parseInt(line) - 40));
            }
            else
            {
                this.setSetting(ii, Integer.parseInt(line));
            }
            ii++;
        }
        
        update();
    }
    
    @Override
    public void update() {
        CANHandler handle = CANFilter.getInstance().getCANHandler(0);
        
        int[] msg1 = {
            this.packCapacity.getSetting()/5,
            this.socWarning.getSetting(),
            this.fullVoltage.getSetting()/2,
            this.warnCurrent.getSetting()/10,
            this.tripCurrent.getSetting()/10,
            this.evmsTempWarning.getSetting(),
            this.minAuxVoltage.getSetting(),
            this.minIsolation.getSetting()
        };
        
        int[] msg2 = {
            this.tachoPPR.getSetting(),
            this.fuelGaugeFull.getSetting(),
            this.fuelGaugeEmpty.getSetting(),
            this.tempGaugeHot.getSetting(),
            this.tempGaugeCold.getSetting(),
            this.bmsMinVoltage.getSetting()/10 - 150,
            this.bmsMaxVoltage.getSetting()/10 - 200,
            this.balanceVoltage.getSetting()/10 - 200
        };     
        
        int[] msg3 = {  
            this.bmsHysteresis.getSetting()/10,
            this.bmsMinTemp.getSetting() + 40,
            this.bmsMaxTemp.getSetting() + 40,
            this.maxChargeVoltage.getSetting()%256,
            (this.maxChargeVoltage.getSetting()/256)*128 + this.maxChargeCurrent.getSetting(),    
            this.altChargeVoltage.getSetting()%256,
            (this.altChargeVoltage.getSetting()/256)*128 + this.altChargeCurrent.getSetting(),
            this.sleepDelay.getSetting()
        };     
        
        int[] msg4 = {  
            this.mpiFunction.getSetting(),
            this.mpo1Function.getSetting(),
            this.mpo2Function.getSetting(),
            this.parallelStrings.getSetting(),
            this.enablePrecharge.getSetting(),
            this.stationaryMode.getSetting()
        };     
        
        handle.writeMessage(0x20, msg1);
        handle.writeMessage(0x21, msg2);
        handle.writeMessage(0x22, msg3);
        handle.writeMessage(0x23, msg4);
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
    
    public int getSetting(int index)
    {        
        int setting = 0;
	
	switch(index)
        {
            case 0:
                setting = this.packCapacity.getSetting(); //Ah
                break;
            case 1:
                setting = this.socWarning.getSetting(); //%
                break;
            case 2:
                setting = this.fullVoltage.getSetting(); //Volts
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
}
