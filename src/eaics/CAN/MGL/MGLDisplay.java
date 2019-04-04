/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eaics.CAN.MGL;

import eaics.CAN.CANFilter;
import eaics.CAN.MiscCAN.CANHandler;
import eaics.CAN.MiscCAN.CANMessage;
import eaics.Settings.SettingsEAICS;
import eaics.Settings.TYPEVehicle;
import java.io.IOException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Markcuz
 */
public class MGLDisplay {
    
    private ScheduledExecutorService displayExecutor;
    
    boolean amSending = false;

    private CANFilter filter;
    private CANHandler handler;
    
    short bankAngle; //resolution: 0.1 deg
    short pitchAngle; //resolution: 0.1 deg
    short yawAngle; //resolution: 0.1 deg
    int gndSpeed; //resolution: knots (mph originally)
    
    int oilTemp = 0;
    int oilPress = 0;
    int ff1pc = 0, ff1pr = 0, ff2pc = 0, ff2pr = 0;
    int aux1 = 0; int aux2 = 0;
    int fuelPressure = 0, coolant = 0, fuelLevel1 = 0, fuelLevel2 = 0, temp = 0, voltage = 0, map = 0, current =0;
    
    
    public MGLDisplay(CANFilter filter) {

        bankAngle = 0; //resolution: 0.1 deg
        pitchAngle = 0; //resolution: 0.1 deg
        yawAngle = 0; //resolution: 0.1 deg
        gndSpeed = 0; //resolution: knots (mph originally)
        
        this.filter = filter;
        
        if(SettingsEAICS.getInstance().getGeneralSettings().getVeh()!=TYPEVehicle.WAVEFLYER) {
            this.handler = filter.getCANHandler(SettingsEAICS.getInstance().getCanSettings().getDisplayCAN());
        }
        else {
            handler = filter.getCANHandler(0);
        }
        if(SettingsEAICS.getInstance().getGeneralSettings().getVeh()!=TYPEVehicle.TRIFAN) {
            runDisplay();
        }
    }
    
    
    private void send4s() throws IOException {
        int[] send = new int[]{
            (int)ff1pc%256, //Fuel Flow 1 Pulse Count
            (int)ff1pc/256,
            (int)ff1pr%256,//Fuel Flow 1 pulse ratio
            (int)ff1pr/256,
            (int)ff2pc%256, //Fuel Flow 2 Pulse Count
            (int)ff2pc/256,
            (int)ff2pr%256,//Fuel Flow 3 pulse ratio
            (int)ff2pr/256,
        };
         //sent every 4 secs, Fuel Pressure?
        handler.writeMessageSFF(0x201, send);
        
    }
    
    private void send500ms() throws IOException {
        //sent every 500ms the next few seconds
        //temperature messages for 12 TC sensors in degrees C, 2 bytes per reading, LSB first
        handler.writeMessageSFF(0x202, new int[]{100,0,0,0,0,0,0,0});
        handler.writeMessageSFF(0x203, new int[]{0,0,0,0,0,0,0,0});
        handler.writeMessageSFF(0x204, new int[]{0,0,0,0,0,0,0,0});
        
        int [] byte5 = new int[]{
            //(int)filter.getEVMS().getVoltage()%256, //Oil Temp LSB - Now Using Voltage
            //(int)filter.getEVMS().getVoltage()/256, //Oil Temp MSB
            //(int)(filter.getCurrentSensor().getCurrent()*1000)%256, //Oil Pressure LSB
            //(int)(filter.getCurrentSensor().getCurrent()*1000)/256, //Oil Pressure MSB
            oilTemp%256,
            oilTemp/256,
            oilPress%256,
            oilPress/256,
            aux1%256, //Aux1 LSB
            aux1/256, //Aux1 MSB
            aux2%256, //Aux2 LSB
            aux2/256  //Aux2 MSB
        };
        
        handler.writeMessageSFF(0x205, byte5);
        
        int [] byte6 = new int[]{
            fuelPressure%256, //Fuel Pressure LSB
            fuelPressure/256, //Fuel Pressure MSB
            coolant%256, //Coolant LSB
            coolant/256, //Coolant MSB
            fuelLevel1%256, //Fuel Level1 LSB
            fuelLevel1/256, //Fuel Level1 MSB
            fuelLevel2%256, //Fuel Level2 LSB
            fuelLevel2/256  //Fuel Level2 MSB
        };
        handler.writeMessageSFF(0x206, byte6);

        int [] byte7 = new int[]{
            temp%256, //Temperature
            temp/256, //Temperature
            voltage%256, //Voltage 
            voltage/256, //Voltage
            0,0,0,0 //unused
        };
        
        handler.writeMessageSFF(0x207, byte7);
    }
    
    private void send200ms() throws IOException {
        
        //sent every 200ms
        
        int [] msg = new int[]{
            filter.getESC()[0].getRpm()%256, //RPM1 LSB
            filter.getESC()[0].getRpm()/256,  //RPM1 LSB
            0, //RPM2 LSB
            0,  //RPM2 LSB
            map%256, //MAP
            map/256,  //MAP
            current%256, //Current
            current/256   //Current
        };
        
        handler.writeMessageSFF(0x208, msg);
    }
    
    public void runDisplay() {

        displayExecutor = null;
        Runnable Id = new Runnable() { 

            int count = 0;

            @Override
            public void run() {
                
                if(amSending) {
                    //every 200ms
                    if(count%2==1) {
                        try {
                            send200ms();
                        } catch (IOException ex) {
                            Logger.getLogger(MGLDisplay.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                    //every 500ms
                    if(count%5==1) {
                        try {
                            send500ms();
                        } catch (IOException ex) {
                            Logger.getLogger(MGLDisplay.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                    //every 4s
                    if((count%40)==1){
                        try {
                            send4s();
                        } catch (IOException ex) {
                            Logger.getLogger(MGLDisplay.class.getName()).log(Level.SEVERE, null, ex);
                        }
                        count = 0;
                    }
                    count++;
                }
            }
        };
        displayExecutor = Executors.newScheduledThreadPool(1);
        displayExecutor.scheduleAtFixedRate(Id, 0, 100, TimeUnit.MILLISECONDS);
    }
    
    public void stopDisplay() {
          amSending = false;
    }
    
    public void startDisplay() {
          amSending = true;
    }
    
    public void processMessage(CANMessage message) {
        switch (message.getFrameID()) {
            case 0x11:
                break;
            case 0x12:
                bankAngle = (short) ((message.getByte(0)+message.getByte(1)*256));
                bankAngle = (short) (bankAngle/10);
                
                pitchAngle = (short) ((message.getByte(2)+message.getByte(3)*256));
                pitchAngle = (short) (pitchAngle/10);
                
                yawAngle = (short) ((message.getByte(4)+message.getByte(5)*256));
                yawAngle = (short) (yawAngle/10);
                
                gndSpeed = message.getByte(6)+message.getByte(7)*256;
                gndSpeed = (int) (gndSpeed*0.868976);
                
                break;
            default:
                break;
        }
        
    }
        public short getBankAngle() {
        return bankAngle;
    }

    public short getPitchAngle() {
        return pitchAngle;
    }

    public short getYawAngle() {
        return yawAngle;
    }

    public int getGndSpeed() {
        return gndSpeed;
    }

    public void setFf1pc(int ff1pc) {
        this.ff1pc = ff1pc;
    }

    public void setFf1pr(int ff1pr) {
        this.ff1pr = ff1pr;
    }

    public void setFf2pc(int ff2pc) {
        this.ff2pc = ff2pc;
    }

    public void setFf2pr(int ff2pr) {
        this.ff2pr = ff2pr;
    }

    public void setAux1(int aux1) {
        this.aux1 = aux1;
    }

    public void setAux2(int aux2) {
        this.aux2 = aux2;
    }

    public void setFuelPressure(int fuelPressure) {
        this.fuelPressure = fuelPressure;
    }

    public void setCoolant(int coolant) {
        this.coolant = coolant;
    }

    public void setFuelLevel1(int fuelLevel1) {
        this.fuelLevel1 = fuelLevel1;
    }

    public void setFuelLevel2(int fuelLevel2) {
        this.fuelLevel2 = fuelLevel2;
    }

    public void setTemp(int temp) {
        this.temp = temp;
    }

    public void setVoltage(int voltage) {
        this.voltage = voltage;
    }

    public void setMap(int map) {
        this.map = map;
    }

    public void setCurrent(int current) {
        this.current = current;
    }

    public void setOilTemp(int oilTemp) {
        this.oilTemp = oilTemp;
    }

    public void setOilPress(int oilPress) {
        this.oilPress = oilPress;
    }
    
    
}
