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
public class State5 implements State
{
    ChargerGBT chargerGBT;
    
    public State5(ChargerGBT chargerGBT)
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
                    System.out.println("(Charger) Acknowledgment of Pre-Parameter settings");
                    System.out.println("Continue to state 6");
                    chargerGBT.sendParameterSettings();
                    chargerGBT.setState(chargerGBT.getState6());
                }
            }
        }
        else if(CANID == 0x1807F456)
        {
            //ignore because just date and time from charger
        }
        else
        {
            System.out.println("Incorrect state=|"+5+"|, for CHM ID=" + Integer.toHexString(CANID));
            //throw new IllegalStateException("Incorrect state=|"+5+"|, for CHM ID=" + CANID);
        }
    }
}
