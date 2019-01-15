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
        else if(CANID == 0x1812F456) // The charger is sending back observed voltage/current
        {
            System.out.println("(Charger) Charger OK it is sending observed current/voltage");
            // Concatenate hex values, such that the second byte of data is first, then divide by 10 and ensure that it is type casted to a float as data is originally an int
            chargerGBT.setObservedVoltage((float) ((data[1]<<8+data[0]%256)/10.0));
            // to calculate current it is 400 - data/10
            chargerGBT.setObservedCurrent((float) (400.0 - (float) ((data[3]<<8+data[2]%256)/10.0)));
            // convert each bit to a minute. NOTE: the range is 0 to 600 minutes
            chargerGBT.setTimeOnCharge((data[5]<<8+data[4]%256));

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
