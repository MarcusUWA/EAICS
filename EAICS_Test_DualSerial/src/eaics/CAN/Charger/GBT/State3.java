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
public class State3 implements State
{
    ChargerGBT chargerGBT;
    
    public State3(ChargerGBT chargerGBT)
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
            if(data[0] == 0x13) 
            {
                System.out.println("(Charger) ID settings acknowledged");
                System.out.println("Continue to state 4");
                chargerGBT.setState(chargerGBT.getState4());
            }
        }
        else
        {
            System.out.println("Incorrect state=|"+3+"|, for CHM ID=" + Integer.toHexString(CANID));
            //throw new IllegalStateException("Incorrect state=|"+3+"|, for CHM ID=" + CANID);
        }
    }    
}
