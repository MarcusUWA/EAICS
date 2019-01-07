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
public class State11 implements State
{
    ChargerGBT chargerGBT;
    
    public State11(ChargerGBT chargerGBT)
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
            if(data[0] == 0x11)
            {
                if(data[1] == 0x02)
                {
                    chargerGBT.sendBatteryChargingState(); //This MAY be so slow, this is sending after 3 CEC's were sent
                }
            }
        }
        else if(CANID == 0x1812F456)
        {
            System.out.println("Happy Packets");
            System.out.println("(Charger) Charger OK, updating details");
        }
        else if(CANID == 0x100AF456)
        {
            //Don't know what this packet is, Alex fix
        }
        else if(CANID == 0x101956F4)
        {
            System.out.println("(Charger) Charger request stop charging");
            chargerGBT.stopCharging();
            chargerGBT.setState(chargerGBT.getState12());
        }
        else if(CANID == 0x101AF456)
        {
            chargerGBT.setState(chargerGBT.getState12());
        }
        else
        {
            System.out.println("Incorrect state=|"+11+"|, for CHM ID=" + Integer.toHexString(CANID));
            //throw new IllegalStateException("Incorrect state=|"+11+"|, for CHM ID=" + CANID);
        }
    }
}
