/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eaics.CAN.Charger;

import eaics.CAN.MiscCAN.CANMessage;

/**
 *
 * @author Troy Burgess
 */
public class WaitingToStopState implements State
{
    ChargerGBT chargerGBT;
    
    public WaitingToStopState(ChargerGBT chargerGBT)
    {
        this.chargerGBT = chargerGBT;
    } 
    
    public void action(CANMessage message)
    {        
        final int CANID = message.getFrameID();
        int[] data = message.getByteData();
        
        if(CANID == 0x1801F456)
        {
            System.out.println("Stop charging accpeted - go to state 0");
            chargerGBT.setState(chargerGBT.getState0());
        }
    }        
}