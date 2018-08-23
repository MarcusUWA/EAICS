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
public class EVMS_v3 extends EVMS
{
	private int headlights;
	private int ampHours;
    
	public EVMS_v3()
	{
		super();
		this.headlights = 0;
		this.ampHours = 0;
	}
	
	@Override
	public void setAll(CANMessage message)
	{
		//super.setCharge(message.getByte(1));
		//super.setCurrent( (((message.getByte(3) & 0xF0) >> 4) + (message.getByte(4) << 4)) - 2048 );	//current has 2028 added to it		
		this.ampHours = message.getByte(2) + (message.getByte(1) << 8);
		super.setVoltage(message.getByte(4) + (message.getByte(3) << 8));
		super.setAuxVoltage(message.getByte(5) / 10.0);	//tenths of a volt
		this.headlights = message.getByte(6) & 0x80;
		super.setLeakage(message.getByte(6) & 0xEF);
		super.setTemp(message.getByte(7) - 40);	    //40 degree offset
	}
	
	public int getHeadlights()
	{
	    return this.headlights;
	}
	
	public void setHeadlights(int headlights)
	{
	    this.headlights = headlights;
	}
	
	public int getAmpHours()
	{
	    return this.ampHours;
	}
	
	public void setAmpHours(int ampHours)
	{
	    this.ampHours = ampHours;
	}
	
	@Override
	public String toString()
	{
	    return "AmpHours: " + ampHours + " Headlights " + headlights + super.toString();
	}
	
	@Override
	public String getLoggingString()
	{
	    String outString = "";
	    outString += super.getLoggingString();
	    outString += headlights + ", " + ampHours + ", ";
	    return outString;
	}
	
	public static String getLoggingHeadings()
	{
	    String outString = "";
	    outString = "Headlights, Amp Hours, ";
	    return outString;
	}
}