/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eaics.CAN.Battery;

import eaics.CAN.CANFilter;
import eaics.CAN.MiscCAN.CANMessage;

public class CCB 
{
    private boolean isCCB_On;
    
    CANFilter filter;

    public CCB(CANFilter filter) {
	filter = this.filter;
    }
    
    public void setAll(CANMessage message){
	isCCB_On = ((message.getByte(0)&0x01) == 1);
    }
    
    public boolean isCCB_On(){
	return isCCB_On;
    }
}
