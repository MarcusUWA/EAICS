/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eaics.CAN.Charger;

import static eaics.CAN.Charger.ChargerGBT.Int2String;
import eaics.MiscCAN.CANMessage;

/**
 *
 * @author Troy Burgess
 */
public class State12 implements State
{
    ChargerGBT chargerGBT;
    
    public State12(ChargerGBT chargerGBT)
    {
        this.chargerGBT = chargerGBT;
    } 
    
    @Override
    public void action(CANMessage message)
    {
        final int CANID = message.getFrameID();
        int[] data = message.getByteData();
        
        if(CANID == 0x101AF456)
        {
            System.out.println("Stop charging accpeted - go to state 0");
            chargerGBT.setState(chargerGBT.getState0());
        }
    }
}
