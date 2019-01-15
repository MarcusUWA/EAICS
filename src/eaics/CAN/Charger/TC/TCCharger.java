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
    
    float outputVoltage;
    float outputCurrent;

    private SettingsEVMS settings;
    
    //Warnings
    boolean hardwareFailure;
    boolean tempFailure;
    boolean inputFailure;
    boolean startFailure;
    boolean commsFailure;


    public TCCharger() {
        this.outputVoltage = 0;
        this.outputCurrent = 0;
        
        this.hardwareFailure = false;
        this.tempFailure = false;
        this.inputFailure = false;
        this.startFailure = false;
        this.commsFailure = false;
        

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

