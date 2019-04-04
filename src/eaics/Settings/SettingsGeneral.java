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
    boolean bmsFaker;
    int numBatteryModules;
    int numEsc;
    int numCCB;
    
    TYPECharger chg;
    TYPEEsc esc;
    TYPEBms bms;
    TYPEThrottle thr;
    TYPEDisplay disp;
    TYPEVehicle veh;
    
    public SettingsGeneral(){
        bmsFaker = false;
        
        this.numBatteryModules = 1; //default for one box 96s, one module = 24 BMS
        
        this.numEsc = 1;
        
        this.chg = TYPECharger.None;
        this.esc = TYPEEsc.MGM;
        this.thr = TYPEThrottle.SER;
        this.bms = TYPEBms.ZEVA3;
        this.disp = TYPEDisplay.MGL;
        this.veh = TYPEVehicle.TRIKE_Prototype;
    }
    
    @Override
    public String getSettingsFileString() {
        String settingsFileString = "";
        
        settingsFileString += "VehicleType:" + "\t\t\t" + "#";
        settingsFileString += veh + "\n";
        
	settingsFileString += "ChargerType:" + "\t\t\t" + "#";
        settingsFileString += chg + "\n";
        
        settingsFileString += "ESCType:" + "\t\t\t" + "#";
        settingsFileString += esc + "\n";
        
        settingsFileString += "BMSType:" + "\t\t\t" + "#";
        settingsFileString += bms + "\n";
        
        settingsFileString += "ThrottleType:" + "\t\t\t" + "#";
        settingsFileString += thr + "\n";
        
        settingsFileString += "DisplayType:" + "\t\t\t" + "#";
        settingsFileString += disp + "\n";
        
        settingsFileString += "NumBatteryModules:" + "\t\t\t" + "#";
        settingsFileString += numBatteryModules + "\n";
        
        settingsFileString += "NumESC:" + "\t\t\t" + "#";
        settingsFileString += numEsc + "\n";
        
        settingsFileString += "BMSFaker:" + "\t\t\t" + "#";
        
        if(bmsFaker) {
            settingsFileString += 1 + "\n";
        }
        else {
            settingsFileString += 0 + "\n";
        }
        
        return settingsFileString;
    }

    @Override
    public void setSettings(String fileString) {
        String[] lines = fileString.split("\\r?\\n");
        for (String line : lines) {
            String[] split = line.split("#");
            
            if("VehicleType:".equals(split[0].trim())) {
                if(split[1].contains(TYPEVehicle.TRIFAN.toString())) {
                    veh = TYPEVehicle.TRIFAN;
                }
                else if(split[1].contains(TYPEVehicle.VERTICAL_TESTRIG.toString())) {
                    veh = TYPEVehicle.VERTICAL_TESTRIG;
                }
                else if(split[1].contains(TYPEVehicle.WAVEFLYER.toString())) {
                    veh = TYPEVehicle.WAVEFLYER;
                }
                else if(split[1].contains(TYPEVehicle.TRIKE_Prototype.toString())) {
                    veh = TYPEVehicle.TRIKE_Prototype;
                }
                else {
                    veh = TYPEVehicle.TESTING;
                }  
                System.out.println("Vehicle type: "+veh);
            }

            if("ChargerType:".equals(split[0].trim())) {
                if(split[1].contains(TYPECharger.GBT.toString())) {
                    chg = TYPECharger.GBT;
                }
                else if(split[1].contains(TYPECharger.TC.toString())) {
                    chg = TYPECharger.TC;
                }
                else {
                    chg = TYPECharger.None;
                }  
            }
            
            else if("ESCType:".equals(split[0].trim())) {
                if(split[1].contains(TYPEEsc.MGM.toString())) {
                    esc = TYPEEsc.MGM;
                }
                else {
                    esc = TYPEEsc.None;
                }
            }
            
            else if("BMSType:".equals(split[0].trim())) {
                if(split[1].contains(TYPEBms.ZEVA2.toString())) {
                    bms = TYPEBms.ZEVA2;
                }
                else if(split[1].contains(TYPEBms.ZEVA3.toString())) {
                    bms = TYPEBms.ZEVA3;
                }
                else {
                    bms = TYPEBms.ELECAERO;
                }  
            }
            
            else if("ThrottleType:".equals(split[0].trim())) {
                if(split[1].contains(TYPEThrottle.SER.toString())) {
                    thr = TYPEThrottle.SER;
                }
                else if(split[1].contains(TYPEThrottle.CAN.toString())) {
                    thr = TYPEThrottle.CAN;
                }
                else {
                    thr = TYPEThrottle.None;
                }  
            }
            
            if("DisplayType:".equals(split[0].trim())) {
                if(split[1].contains(TYPEDisplay.MGL.toString())) {
                    disp = TYPEDisplay.MGL;
                }
                else {
                    disp = TYPEDisplay.None;
                }  
            }
            
            if("NumBatteryModules:".equals(split[0].trim())) {
                numBatteryModules = Integer.parseInt(split[1].trim());
            }
            
             if("NumESC:".equals(split[0].trim())) {
                numEsc = Integer.parseInt(split[1].trim());
            }
             
              if("BMSFaker:".equals(split[0].trim())) {
                if(Integer.parseInt(split[1].trim())==1) {
                    bmsFaker = true;
                }
                else {
                    bmsFaker = false;
                }
            }
        } 
        update();
    }

    @Override
    public void update() 
    {
         // TODO
    }
    
    public boolean isBmsFaker()  {
        return bmsFaker;
    }
    public int getNumBatteryModules()  {
        return numBatteryModules;
    }
    public TYPECharger getChargerType()  {
        return chg;
    }
    public TYPECharger[] getChargerEnumList()  {
        return TYPECharger.values();
    }
    public TYPEEsc getEsc()  {
        return esc;
    }
    public TYPEBms getBms()  {
        return bms;
    }
    public TYPEThrottle getThr()  {
        return thr;
    }
    
    public void setChargerType(TYPECharger chg)  {
        this.chg = chg;
    }
    
    public TYPEDisplay getDisp() {
        return disp;
    }

    public void setBmsFaker(boolean bmsFaker) {
        this.bmsFaker = bmsFaker;
    }

    public void setNumBatteryModules(int numBatteryModules) {
        this.numBatteryModules = numBatteryModules;
    }

    public void setChg(TYPECharger chg) {
        this.chg = chg;
    }

    public void setEsc(TYPEEsc esc) {
        this.esc = esc;
    }

    public void setBms(TYPEBms bms) {
        this.bms = bms;
    }

    public void setThr(TYPEThrottle thr) {
        this.thr = thr;
    }

    public void setDisp(TYPEDisplay disp) {
        this.disp = disp;
    }

    public void setNumEsc(int numEsc) {
        this.numEsc = numEsc;
    }

    public int getNumEsc() {
        return numEsc;
    }

    public TYPEVehicle getVeh() {
        return veh;
    }
}
