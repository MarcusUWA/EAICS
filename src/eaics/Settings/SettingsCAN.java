/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eaics.Settings;

import eaics.CAN.CANFilter;

/**
 *
 * @author Markcuz
 */
public class SettingsCAN implements Settings{
    int bmsCAN;
    int escCAN;
    int chargerCAN;
    int prechargeCAN;
    int displayCAN;
    int minidaqCAN;
    int throttleCAN;
    
    public SettingsCAN(){
       bmsCAN = 0;
       escCAN = 0;
       chargerCAN = 0;
       prechargeCAN = 0;
       displayCAN = 0;
       minidaqCAN = 0;
       throttleCAN = 0;
    }
    
    @Override
    public String getSettingsFileString() {
        String settingsFileString = "";
        
        settingsFileString += "BMSCANPort:" + "\t\t\t" + "#";
        settingsFileString += bmsCAN + "\n";
        
	settingsFileString += "ESCCANPort:" + "\t\t\t" + "#";
        settingsFileString += escCAN + "\n";
        
        settingsFileString += "ChargerCANPort:" + "\t\t\t" + "#";
        settingsFileString += chargerCAN + "\n";
        
        settingsFileString += "PrechargeCANPort:" + "\t\t\t" + "#";
        settingsFileString += prechargeCAN + "\n";
        
        settingsFileString += "DisplayCANPort:" + "\t\t\t" + "#";
        settingsFileString += displayCAN + "\n";
        
        settingsFileString += "MiniDAQCANPort:" + "\t\t\t" + "#";
        settingsFileString += minidaqCAN + "\n";
        
        settingsFileString += "ThrottleCANPort:" + "\t\t\t" + "#";
        settingsFileString += throttleCAN + "\n";
        
        return settingsFileString;
    }

    @Override
    public void setSettings(String fileString) {
        String[] lines = fileString.split("\\r?\\n");
        for (String line : lines) {
            String[] split = line.split("#");
            
            if("BMSCANPort:".equals(split[0].trim())) {
                bmsCAN = Integer.parseInt(split[1].trim());
            }
            
            if("ESCCANPort:".equals(split[0].trim())) {
                escCAN = Integer.parseInt(split[1].trim());
            }
            
            if("ChargerCANPort:".equals(split[0].trim())) {
                chargerCAN = Integer.parseInt(split[1].trim());
            }
            
            if("PrechargeCANPort:".equals(split[0].trim())) {
                prechargeCAN = Integer.parseInt(split[1].trim());
            }
            
            if("DisplayCANPort:".equals(split[0].trim())) {
                displayCAN = Integer.parseInt(split[1].trim());
            }
            if("MiniDAQCANPort:".equals(split[0].trim())) {
                minidaqCAN = Integer.parseInt(split[1].trim());
            }
            if("ThrottleCANPort:".equals(split[0].trim())) {
                throttleCAN = Integer.parseInt(split[1].trim());
            }

            
        } 
    }

    public int getBmsCAN() {
        return bmsCAN;
    }

    public int getEscCAN() {
        return escCAN;
    }

    public int getChargerCAN() {
        return chargerCAN;
    }

    public int getPrechargeCAN() {
        return prechargeCAN;
    }

    public int getDisplayCAN() {
        return displayCAN;
    }

    public int getMinidaqCAN() {
        return minidaqCAN;
    }

    public int getThrottleCAN() {
        return throttleCAN;
    }

    public void setBmsCAN(int bmsCAN) {
        this.bmsCAN = bmsCAN;
    }

    public void setEscCAN(int escCAN) {
        this.escCAN = escCAN;
    }

    public void setChargerCAN(int chargerCAN) {
        this.chargerCAN = chargerCAN;
    }

    public void setPrechargeCAN(int prechargeCAN) {
        this.prechargeCAN = prechargeCAN;
    }

    public void setDisplayCAN(int displayCAN) {
        this.displayCAN = displayCAN;
    }

    public void setMinidaqCAN(int minidaqCAN) {
        this.minidaqCAN = minidaqCAN;
    }

    public void setThrottleCAN(int throttleCAN) {
        this.throttleCAN = throttleCAN;
    }
    
    

    @Override
    public void update() 
    {
         // TODO
    }

}
