/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eaics.CAN.Charger;

import static eaics.CAN.Charger.ChargerGBT.Int2String;
import eaics.CAN.MiscCAN.CANMessage;

/**
 *
 * @author Troy Burgess
 */
public class State7 implements State
{
    ChargerGBT chargerGBT;
    
    public State7(ChargerGBT chargerGBT)
    {
        this.chargerGBT = chargerGBT;
    } 
    
    @Override
    public void action(CANMessage message)
    {
        final int CANID = message.getFrameID();
        int[] data = message.getByteData();
        
        if(CANID == 0x1807F456)
        {
            System.out.println("(Charger) Sending Time Sync Data");
            System.out.println("Continue to state 8");
            chargerGBT.setState(chargerGBT.getState8());
        }
        else
        {
            System.out.println("Incorrect state=|"+7+"|, for CHM ID=" + Integer.toHexString(CANID));
            //throw new IllegalStateException("Incorrect state=|"+7+"|, for CHM ID=" + CANID);
        }
    }
}
