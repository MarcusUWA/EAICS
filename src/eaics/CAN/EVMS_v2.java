/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eaics.CAN;

import eaics.MiscCAN.CANMessage;

/**
 *
 * @author Troy
 */
public class EVMS_v2 extends EVMS
{
	private int charge;	//Battery State of Charge (0-100%)
	private int current;
		
	public EVMS_v2()
	{
		super();
		this.charge = 0;
		this.current = 0;
	}
	
	@Override
	public void setAll(CANMessage message)
	{
		this.charge = message.getByte(1);
		super.setVoltage( message.getByte(2) + ((message.getByte(3) & 0x0F) << 8) );	
		this.current = (((message.getByte(3) & 0xF0) >> 4) + (message.getByte(4) << 4)) - 2048;	//current has 2028 added to it
		super.setAuxVoltage(message.getByte(5) / 10.0);	//tenths of a volt
		super.setLeakage(message.getByte(6));
		super.setTemp(message.getByte(7));
	}
	
	public int getCharge()
	{
		return charge;		
	}
	
	public void setCharge(int charge)
	{
		this.charge = charge;
	}
	
	public int getCurrent()
	{
		return current;
	}
	
	public void setCurrent(int current)
	{
		this.current = current;
	}
	
	@Override
	public String toString()
	{
	    return "Charge: " + charge + " Current " + current + super.toString();
	}
}