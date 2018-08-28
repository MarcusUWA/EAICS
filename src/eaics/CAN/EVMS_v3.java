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
	private double ampHours;
    
	public EVMS_v3()
	{
		super();
		this.headlights = 0;
		this.ampHours = 0;
	}
	
	@Override
	public void setAll(CANMessage message)
	{
		this.ampHours = message.getByte(2) + (message.getByte(1) << 8);
                this.ampHours = this.ampHours / 10.0;
                
                double batteryVoltage = message.getByte(4) + (message.getByte(3) << 8);
                
		super.setVoltage(batteryVoltage / 10.0);
                
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
	
	public double getAmpHours()
	{
	    return this.ampHours;
	}
	
	public void setAmpHours(double ampHours)
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