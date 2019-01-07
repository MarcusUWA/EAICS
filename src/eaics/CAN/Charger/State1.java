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
public class State1 implements State
{
    ChargerGBT chargerGBT;
    
    public State1(ChargerGBT chargerGBT)
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
            if(data[0]==0x00) // Charger is waiting for BMS's ID
            {
                System.out.println("Received Charger Identification Message (CRM), increment to state 2");
                chargerGBT.stopSendHandshake();
                chargerGBT.sendTransportCommManagement();
                chargerGBT.setState(chargerGBT.getState2());
            }
        }
        else if(CANID == 0x1826F456)
        {
            //I don't know what this packet is, ask Alex.            
        }
        else if(CANID == 0x182756F4)
        {
            //Handshake - EAICS is sending these packets to the charger
        }
        else
        {
            System.out.println("Incorrect state=|"+1+"|, for CHM ID=" + Integer.toHexString(CANID));
            //throw new IllegalStateException("Incorrect state=|"+1+"|, for CHM ID=" + CANID);
        }
    }
}