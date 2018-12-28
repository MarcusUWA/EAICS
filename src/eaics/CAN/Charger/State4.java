/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eaics.CAN.Charger;

import eaics.MiscCAN.CANMessage;

/**
 *
 * @author Troy Burgess
 */
public class State4 implements State
{
    ChargerGBT chargerGBT;
    
    public State4(ChargerGBT chargerGBT)
    {
        this.chargerGBT = chargerGBT;
    }  
    
    @Override
    public void action(CANMessage message)
    {
        final int CANID = message.getFrameID();
        int[] data = message.getByteData();
        
        if(CANID == 0x1801F456)
        {
            if(data[0] == 0xAA) // Charger has accepted BMS's ID
            {
                System.out.println("Charger has received BRM, increment to state 5");
                System.out.println("Completed handshake phase, continue to charging parameter setting stage");
                chargerGBT.sendPreParameterSettings();
                chargerGBT.setState(chargerGBT.getState5());
            }
        }
        else if(CANID == 0x1807F456)
        {
            //ignore because just date and time from charger
        }
        else
        {
            System.out.println("Incorrect state=|"+4+"|, for CHM ID=" + Integer.toHexString(CANID));
            //throw new IllegalStateException("Incorrect state=|"+4+"|, for CHM ID=" + CANID);
        }
    }
}
