/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eaics.CAN.Charger.GBT;

import static eaics.CAN.Charger.GBT.ChargerGBT.Int2String;
import eaics.CAN.MiscCAN.CANMessage;

/**
 *
 * @author Troy Burgess
 */
public class State2 implements State
{
    ChargerGBT chargerGBT;
    
    public State2(ChargerGBT chargerGBT)
    {
        this.chargerGBT = chargerGBT;
    }
    
    @Override
    public void action(CANMessage message)
    {
        final int CANID = message.getFrameID();
        int[] data = message.getByteData();
        
        if(CANID == 0x1CECF456)
        {
            if(data[0]==0x11) // Charger is waiting for BMS's ID
            {
                if(data[1] == 0x07) 
                {
                    System.out.println("(Charger) Acknowledging Pre-ID received from BMS");
                    System.out.println("Continue to state 3");
                    chargerGBT.sendIdentificationParams();
                    chargerGBT.setState(chargerGBT.getState3());
                }
            }
        }
        else
        {
            System.out.println("Incorrect state=|"+2+"|, for CHM ID=" + Integer.toHexString(CANID));
            //throw new IllegalStateException("Incorrect state=|"+2+"|, for CHM ID=" + CANID);
        }
    }
}
