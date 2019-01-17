/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eaics.CAN.Charger.TC;

import eaics.CAN.CANFilter;
import eaics.CAN.MiscCAN.CANMessage;
import eaics.Settings.SettingsEAICS;
import eaics.Settings.SettingsEVMS;
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
public class TCCharger {
    
    CANFilter filter;
    float outputVoltage;
    float outputCurrent;

    private SettingsEVMS settings;
    
    //Warnings
    boolean hardwareFailure;
    boolean tempFailure;
    boolean inputFailure;
    boolean startFailure;
    boolean commsFailure;
    
    float chargeVoltage;
    float chargeCurrent;

    private ScheduledExecutorService chargerExecutor;

    boolean chargeStatus;

    public TCCharger(CANFilter filter) {
        
        this.filter = filter;
        this.outputVoltage = 0;
        this.outputCurrent = 0;
        
        this.hardwareFailure = false;
        this.tempFailure = false;
        this.inputFailure = false;
        this.startFailure = false;
        this.commsFailure = false;
        
        this.settings = SettingsEAICS.getInstance().getEVMSSettings();

        this.chargeVoltage = settings.getSetting(19);
        this.chargeCurrent = settings.getSetting(20);
    }
    
    //only one message with ID: 0x18FF50E5
    public void setMessage(CANMessage message) {
        outputVoltage = (float) ((message.getByte(0)*256+message.getByte(1))/10.0);
        outputCurrent = (float) ((message.getByte(2)*256+message.getByte(3))/10.0);
        System.out.println("Output Voltage: "+outputVoltage+"Output Current: "+outputCurrent);
        
        int statusByte = message.getByte(4);
        System.out.println("Status Byte: "+statusByte);
        
        if((statusByte&0x01) == 0x01) {
            hardwareFailure = true;
        }
        else {
            hardwareFailure = false;
        }
        
        if((statusByte&0x02) == 0x02) {
            tempFailure = true;
        }
        else {
            tempFailure = false;
        }
        
        if((statusByte&0x04) == 0x04) {
            inputFailure = true;
        }
        else {
            inputFailure = false;
        }
        
        if((statusByte&0x08) == 0x08) {
            startFailure = true;
        }
        else {
            startFailure = false;
        }
        
        if((statusByte&0x10) == 0x10) {
            commsFailure = true;
        }
        else {
            commsFailure = false;
        }
    }
    
    public void runCharger() {
        chargerExecutor = null;
        
        System.out.println("Starting Charger...");
        
        Runnable Id = new Runnable() { 
            
            @Override
            public void run() {
                chargeVoltage = settings.getSetting(19)*10;
                chargeCurrent = settings.getSetting(20)*10;
                
                chargeStatus = true;
                
                try {
                    filter.getCANHandler(0).writeMessage(
                            0x1806E5F4, 
                            new int[]{
                                (int)chargeVoltage/256,
                                (int)chargeVoltage%256,
                                (int)chargeCurrent/256,
                                (int)chargeCurrent%256,
                                0,
                                0,
                                0,
                                0
                            }
                    );
                } catch (IOException ex) {
                    System.out.println("Failed...");
                    //Logger.getLogger(TCCharger.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        };
        chargerExecutor = Executors.newScheduledThreadPool(1);
        chargerExecutor.scheduleAtFixedRate(Id, 0, 500, TimeUnit.MILLISECONDS);
    }
    
    public void stopCharger() {
        try {
            filter.getCANHandler(0).writeMessage(0x1806E5F4, new int[]{
                (int)chargeVoltage/256,
                (int)chargeVoltage%256,
                (int)chargeCurrent/256,
                (int)chargeCurrent%256,
                1,0,0,0});
        } catch (IOException ex) {
            Logger.getLogger(TCCharger.class.getName()).log(Level.SEVERE, null, ex);
        }
        if(chargerExecutor!=null) {
            chargerExecutor.shutdown();
        }
    }
    


    public float getOutputVoltage() {
        return outputVoltage;
    }

    public float getOutputCurrent() {
        return outputCurrent;
    }

    public boolean isHardwareFailure() {
        return hardwareFailure;
    }

    public boolean isTempFailure() {
        return tempFailure;
    }

    public boolean isInputFailure() {
        return inputFailure;
    }

    public boolean isStartFailure() {
        return startFailure;
    }

    public boolean isCommsFailure() {
        return commsFailure;
    }
    
    
}

