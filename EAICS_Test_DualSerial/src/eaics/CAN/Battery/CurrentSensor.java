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
public class CurrentSensor 
{
    //in milliamps
    private int current;

    public CurrentSensor(){
        this.current = 0;
    }

    //in milliamps
    public void setAll(CANMessage message){
        this.current = (message.getByte(0) << 16) + (message.getByte(1) << 8) + message.getByte(2);
        this.current = this.current - 8388608;
    }

    public int getCurrent(){
        return this.current;
    }
}
