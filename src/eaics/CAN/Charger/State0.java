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
public class State0 implements State
{
    ChargerGBT chargerGBT;
    
    public State0(ChargerGBT chargerGBT)
    {
        this.chargerGBT = chargerGBT;
    }
    
    @Override
    public void action(CANMessage message)
    {
        final int CANID = message.getFrameID();
        
        if(CANID == 0x1826F456)
        {
            System.out.println("Received Charger Handshake Message (CHM), send BMS Handshake Messages (BHM). Increment to State 1");
            chargerGBT.startSendHandshake();
            chargerGBT.setState(chargerGBT.getState1());
        }
        else if(CANID == 0x101AF456)
        {
            //Ignore - Charge just blasts these while stopping charge
        }
        else if(CANID == 0x81FFF456)
        {
            //System.out.println("Charger Timeout");
        }
        else if(CANID == 0x1801F456)
        {
            //Ignore - error messages
        }
        else if(CANID == 0x81FF456)
        {
            //Ignore - charger error messages
        }
        else
        {
            System.out.println("Incorrect state=|"+0+"|, for CHM ID=" + Integer.toHexString(CANID));
            //throw new IllegalStateException("Incorrect state=|"+0+"|, for CHM ID=" + CANID);
        }
    }
}
