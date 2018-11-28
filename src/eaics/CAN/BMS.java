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
public class BMS 
{
        private final int BPID = 300;
	private int MODULE_ID;
	private int[] voltage;
	private int[] temp;
	
	public static final int NUMBER_OF_CELLS = 12;
	public static final int NUMBER_OF_TEMP_SENSORS = 2;
	
	public BMS(int inID)
	{
	    this.MODULE_ID = inID;
	    this.voltage = new int[NUMBER_OF_CELLS];
	    this.temp = new int[NUMBER_OF_TEMP_SENSORS];
	}
	
	public void setAll(CANMessage message)
	{
	    switch(message.getFrameID() - (BPID + 10 * MODULE_ID))
	    {
		case 1:
		    for (int ii = 0; ii < 4; ii++)
		    {
			this.voltage[ii] = (message.getByte(ii*2)<<8) + message.getByte(ii*2+1);
		    }
		    break;
		case 2:
		    for (int ii = 0; ii < 4; ii++)
		    {
			this.voltage[ii + 4] = (message.getByte(ii*2)<<8) + message.getByte(ii*2+1);
		    }
		    break;
		case 3:
		    for (int ii = 0; ii < 4; ii++)
		    {
			this.voltage[ii + 8] = (message.getByte(ii*2)<<8) + message.getByte(ii*2+1);
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
	    for(int ii = 0; ii < voltage.length; ii++)
	    {
		outstring += " V" + (ii + 1) + ": " + this.voltage[ii];
	    }
	    for(int ii = 0; ii < temp.length; ii++)
	    {
		outstring += " T" + (ii + 1) + ": " + this.temp[ii];
	    }
	    return outstring;
	}

	//Returns the voltage of Cell ii (i.e. cell 0 to cell 11) in Volts, i.e. divide by 1000
	public int getVoltage(int ii)
	{
	    return this.voltage[ii];
	}
	
	public int getTemp(int ii)
	{
	    return this.temp[ii];
	}
	
	/*
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
	*/
	public String getVoltagesString()
	{
	    String outString = "";
	    
	    for(int ii = 0; ii < voltage.length; ii++)
	    {
			outString += voltage[ii] / 1000.0;
			outString += ", ";
	    }
	    
	    return outString;
	}
	
	public String getTemperatureString()
	{
	    String outString = "";
	    
	    for(int ii = 0; ii < temp.length; ii++)
	    {
			outString += temp[ii];
			outString += ", ";
	    }
	    
	    return outString;
	}
}
