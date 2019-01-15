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
public class State8 implements State
{
    ChargerGBT chargerGBT;
    
    public State8(ChargerGBT chargerGBT)
    {
        this.chargerGBT = chargerGBT;
    }    
    
    @Override
    public void action(CANMessage message)
    {
        final int CANID = message.getFrameID();
        int[] data = message.getByteData(); // Receive information about the max/min characteristics of the charger
       
        if(CANID == 0x1808F456)
        {
            
            System.out.println("(Charger) Sending Charger stats");
            System.out.println("Continue to state 9 READY to charge");
            chargerGBT.startSendReadyToCharge();
            chargerGBT.setState(chargerGBT.getState9());
            
            // Concatenate hex values, such that the second byte of data is first, then divide by 10 and ensure that it is type casted to a float as data is originally an int
            chargerGBT.setMaxChargerVoltage((float) ((data[1]<<8+data[0]%256)/10.0)); 
            System.out.println("MaxVoltage = " + (float) ((data[1]<<8+data[0]%256)/10.0)); // TODO remove later 
            chargerGBT.setMinChargerVoltage((float) ((data[3]<<8+data[2]%256)/10.0)); 
            // to calculate current it is 400 - data/10
            chargerGBT.setMaxChargerCurrent((float) (400.0 - (float) ((data[5]<<8+data[4]%256)/10.0)));
            System.out.println("MaxCurrent = " + (float) (400.0 - (float) ((data[5]<<8+data[4]%256)/10.0))); // TODO remove later
            chargerGBT.setMinChargerCurrent((float) (400.0 - (float) ((data[7]<<8+data[6]%256)/10.0)));
        }
        else
        {
            System.out.println("Incorrect state=|"+8+"|, for CHM ID=" + Integer.toHexString(CANID));
            //throw new IllegalStateException("Incorrect state=|"+8+"|, for CHM ID=" + CANID);
        }
    }
}
