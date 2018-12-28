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
public class State10 implements State
{
    ChargerGBT chargerGBT;
    
    public State10(ChargerGBT chargerGBT)
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
            if(data[0]==0x11) 
            {
                if(data[1] == 0x02) 
                {
                    System.out.println("(Charger) Acknowledgment OK to receive charge update");
                    System.out.println("Continue to state 11");
                    chargerGBT.setState(chargerGBT.getState11());
                }
            }
        }
        else
        {
            System.out.println("Incorrect state=|"+10+"|, for CHM ID=" + Integer.toHexString(CANID));
            //throw new IllegalStateException("Incorrect state=|"+10+"|, for CHM ID=" + CANID);
        }
    }
}
