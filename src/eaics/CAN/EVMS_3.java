/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eaics.CAN;

/**
 *
 * @author Troy
 */
public class EVMS_3 extends EVMS
{
	public EVMS_3()
	{
		super();
	}
	
	@Override
	public void setAll(CANMessage message)
	{
		super.setCharge(message.getByte(1));		
		super.setVoltage( message.getByte(2) + ((message.getByte(3) & 0x0F) << 8) );	
		super.setCurrent( (((message.getByte(3) & 0xF0) >> 4) + (message.getByte(4) << 4)) - 2048 );	//current has 2028 added to it		
		super.setAuxVoltage(message.getByte(5) / 10.0);	//tenths of a volt
		super.setLeakage(message.getByte(6));
		super.setTemp(message.getByte(7));
	}
}