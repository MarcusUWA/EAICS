/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eaics.CAN.Battery;

import eaics.CAN.MiscCAN.CANMessage;

/**
 *
 * @author Troy
 */
public class EVMS
{
    private double voltage;
    private double auxVoltage;	//Auxiliary voltage (tenths of a volt)
    private int leakage;	//Insulation integrity/leakage (0-100%)
    private int temp;           //degrees C
    
    private int headlights;
    private double ampHours;
    
    private int error;
    private int status;
    
    private int charge;     // EVMS version 2 only
    private int current;    // EVMS version 2 only

    public EVMS(){
        this.voltage = 0;
        this.auxVoltage = 0.0;
        this.leakage = 0;
        this.temp = 0;
        
        this.headlights = 0;
        this.ampHours = 0;
        
        this.error = 0;
        this.status = 0;
        
        this.charge = 0;
        this.current = 0;
    }
    
    public void setEVMS_v3(CANMessage message)
    {
        double batteryVoltage = message.getByte(4) + (message.getByte(3) << 8);
        this.voltage = batteryVoltage / 10.0;
        
        this.auxVoltage = message.getByte(5) / 10.0;    //tenths of a volt
        
        this.leakage = message.getByte(6) & 0xEF;

        this.temp = message.getByte(7) - 40;            //40 degree offset    
        
        this.headlights = (message.getByte(6) & 0x80) >> 7;
        
        this.ampHours = message.getByte(2) + (message.getByte(1) << 8);
        this.ampHours = this.ampHours / 10.0;

        this.error = (message.getByte(0) & 0xF8) >>3;
        
        this.status = message.getByte(0) & 0x07;      
    }
    
    public void setEVMS_v2(CANMessage message)
    {
        this.voltage = message.getByte(2) + ((message.getByte(3) & 0x0F) << 8);
        this.auxVoltage = message.getByte(5) / 10.0;        //tenths of a volt
        this.leakage = message.getByte(6);
        this.temp = message.getByte(7);
        
        this.charge = message.getByte(1);
        this.current = (((message.getByte(3) & 0xF0) >> 4) + (message.getByte(4) << 4)) - 2048;	//current has 2028 added to it      
    }

    public double getVoltage() {
        return voltage;
    }

    public void setVoltage(double voltage) {
        this.voltage = voltage;
    }

    public double getAuxVoltage() {
        return auxVoltage;
    }

    public void setAuxVoltage(double auxVoltage) {
        this.auxVoltage = auxVoltage;
    }

    public int getLeakage() {
        return leakage;
    }

    public void setLeakage(int leakage) {
        this.leakage = leakage;
    }

    public int getTemp() {
        return temp;
    }

    public void setTemp(int temp) {
        this.temp = temp;
    }

    public int getHeadlights() {
        return headlights;
    }

    public void setHeadlights(int headlights) {
        this.headlights = headlights;
    }

    public double getAmpHours() {
        return ampHours;
    }

    public void setAmpHours(double ampHours) {
        this.ampHours = ampHours;
    }

    public int getError() {
        return error;
    }

    public void setError(int error) {
        this.error = error;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}