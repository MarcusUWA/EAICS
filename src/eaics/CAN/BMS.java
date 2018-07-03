/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eaics.CAN;

import java.util.Arrays;
import java.util.List;

/**
 *
 * @author Troy
 */
public class BMS 
{
        private final int BPID = 300;
	
	private int[] voltage;
	private int[] temp;
	
	public BMS()
	{
	    this.voltage = new int[12];
	    this.temp = new int[2];
	}
	
	public void setAll(CANMessage message)
	{
	    
	    switch(message.getFrameID() - BPID)
	    {
		case 1:
		    for (int ii = 0; ii < 4; ii++)
		    {
			this.voltage[ii] = (message.getByte(ii*2)<<8) + message.getByte(ii*2+1);
		    }
		    break;
		case 2:
		    for (int ii = 4; ii < 8; ii++)
		    {
			this.voltage[ii] = (message.getByte(ii*2)<<8) + message.getByte(ii*2+1);
		    }
		    break;
		case 3:
		    for (int ii = 8; ii < 12; ii++)
		    {
			this.voltage[ii] = (message.getByte(ii*2)<<8) + message.getByte(ii*2+1);
		    }
		    break;
		case 4:
		    this.temp[0] = message.getByte(0) - 40; // removing the +40 degree C offset
		    this.temp[1] = message.getByte(1) - 40; // removing the +40 degree C offset
		    break;		    
	    }
	}
	
	@Override
	public String toString()
	{
	    String outstring = "";
	    for(int ii = 0; ii < 12; ii++)
	    {
		outstring += " V" + (ii + 1) + ": " + this.voltage[ii];
	    }
	    for(int ii = 0; ii < 2; ii++)
	    {
		outstring += " T" + (ii + 1) + ": " + this.temp[ii];
	    }
	    return outstring;
	}

	public int getVoltage(int ii)
	{
	    return this.voltage[ii - 1];
	}
	
	public int getTemp(int ii)
	{
	    return this.temp[ii - 1];
	}
	
	public int getMaxVoltage()
	{
	    int max = voltage[0];
	    for(int ii = 1; ii < voltage.length; ii++)
	    {
		if(voltage[ii] > max)
		{
		    max = voltage[ii];
		}
	    }
	    return max;
	}
	
	public int getMinVoltage()
	{
	    int min = voltage[0];
	    for(int ii = 1; ii < voltage.length; ii++)
	    {
		if(voltage[ii] < min)
		{
		    min = voltage[ii];
		}
	    }
	    return min;
	}
	
	public int getMaxTemp()
	{
	    int max;
	    if(temp[0] > temp[1])
	    {
		max = temp[0];
	    }
	    else
	    {
		max = temp[1];
	    }
	    return max;
	}
	
	public int getMinTemp()
	{
	    int min;
	    if(temp[0] < temp[1])
	    {
		min = temp[0];
	    }
	    else
	    {
		min = temp[1];
	    }
	    return min;
	}
}
