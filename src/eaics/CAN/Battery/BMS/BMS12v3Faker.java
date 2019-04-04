/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eaics.CAN.Battery.BMS;

import eaics.CAN.MiscCAN.CANHandler;

/**
 *
 * @author Markcuz
 */
public class BMS12v3Faker {
    private CANHandler handler;
    int fakeVoltage; // in mV
    
    public BMS12v3Faker(CANHandler handler, int fakeVoltage) {
        this.fakeVoltage = fakeVoltage;
        this.handler = handler;
    }
    
    public void sendBMSMessage(int id) {
        int[] data = new int[]{
            (fakeVoltage/256)%256,
            fakeVoltage%256,
            (fakeVoltage/256)%256,
            fakeVoltage%256,
            (fakeVoltage/256)%256,
            fakeVoltage%256,
            (fakeVoltage/256)%256,
            fakeVoltage%256
        };
        
        int[] temp = new int[]{
            0,40,0,40
        };
        
                
        handler.writeMessage(id+1, data);
        handler.writeMessage(id+2, data);
        handler.writeMessage(id+3, data);
        handler.writeMessage(id+4, temp);
    }
}
