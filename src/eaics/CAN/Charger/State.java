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
public interface State 
{
    public void action(CANMessage message);        
}
