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
public class State9 implements State
{
    ChargerGBT chargerGBT;
    
    public State9(ChargerGBT chargerGBT)
    {
        this.chargerGBT = chargerGBT;
    } 
    
    @Override
    public void action(CANMessage message)
    {
        final int CANID = message.getFrameID();
        int[] data = message.getByteData();
        
        if(CANID == 0x100AF456)
        {
            if(data[0] == 0xAA) 
            {
                chargerGBT.stopSendReadyToCharge();
                chargerGBT.startSendChargingPacket();
                System.out.println("(Charger) Charger Ready to charge");
                System.out.println("Continue to state 10");
                chargerGBT.setState(chargerGBT.getState10());
            }
            else if(data[0] == 0x00)
            {
                //System.out.println("Charger is not ready for charging");
            }
        }
        else
        {
            System.out.println("Incorrect state=|"+9+"|, for CHM ID=" + Integer.toHexString(CANID));
            //throw new IllegalStateException("Incorrect state=|"+9+"|, for CHM ID=" + CANID);
        }
    }
}
