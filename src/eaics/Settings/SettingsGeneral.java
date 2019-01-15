/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eaics.Settings;

/**
 *
 * @author Markcuz
 */
public class SettingsGeneral implements Settings {
    
    public boolean isCharging = false; // Used to determine if the system is charging or not
    
    public enum ChargerType {
        None, TC, GBT
    }
    
    public enum ESCType{
        MGM, None
    }
    
    public enum BMSType {
        ZEVA2, ZEVA3, ELECAERO
    }
    
    public enum ThrottleType {
        SER, CAN, None
    }
    
    boolean mglConnected;
    boolean bmsFaker;
    int numBatteryModules;
    
    ChargerType chg;
    ESCType esc;
    BMSType bms;
    ThrottleType thr;
    
    public SettingsGeneral(){
        mglConnected = false;
        bmsFaker = true;
        
        numBatteryModules = 4; //default for one box 96s, one module = 24 BMS
        chg = ChargerType.None;
        esc = ESCType.MGM;
        bms = BMSType.ZEVA3;
        thr = ThrottleType.SER;
    }
    
    @Override
    public String getSettingsFileString() {
        String settingsFileString = "";
        
	settingsFileString += "Charger Type:" + "\t\t\t" + "#";
        settingsFileString += chg + "\n";
        
        settingsFileString += "ESC Type:" + "\t\t\t" + "#";
        settingsFileString += esc + "\n";
        
        settingsFileString += "BMS Type:" + "\t\t\t" + "#";
        settingsFileString += bms + "\n";
        
        settingsFileString += "BMS Type:" + "\t\t\t" + "#";
        settingsFileString += thr + "\n";
        
        return settingsFileString;
    }

    @Override
    public void setSettings(String fileString) {
        int ii = 0;
        String[] lines = fileString.split("\\r?\\n");
        for (String line : lines) {

        }
        
        update();
    }

    @Override
    public void update() 
    {
         // TODO
    }
    public boolean isMglConnected() 
    {
        return mglConnected;
    }
    public boolean isBmsFaker() 
    {
        return bmsFaker;
    }
    public int getNumBatteryModules() 
    {
        return numBatteryModules;
    }
    public ChargerType getChargerType() 
    {
        return chg;
    }
    public ChargerType[] getChargerEnumList() 
    {
        return ChargerType.values();
    }
    public ESCType getEsc() 
    {
        return esc;
    }
    public BMSType getBms() 
    {
        return bms;
    }
    public ThrottleType getThr() 
    {
        return thr;
    }
    
    public void setChargerType(ChargerType chg) 
    {
        this.chg = chg;
    }
    
    
}
