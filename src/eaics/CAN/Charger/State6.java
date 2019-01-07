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
public class State6 implements State
{
    ChargerGBT chargerGBT;
    
    public State6(ChargerGBT chargerGBT)
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
            if(data[0]==0x13) 
            {
                if(data[1] == 0x0D) // Don't think we need this second check???? Remove later if no problem 
                {
                    System.out.println("(Charger) Acknowledgment of Parameter settings");
                    System.out.println("Continue to state 7");
                    chargerGBT.setState(chargerGBT.getState7());
                }
            }
        }
        else
        {
            System.out.println("Incorrect state=|"+6+"|, for CHM ID=" + Integer.toHexString(CANID));
            //throw new IllegalStateException("Incorrect state=|"+6+"|, for CHM ID=" + CANID);
        }
    }
}
