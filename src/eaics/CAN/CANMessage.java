/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eaics.CAN;

/**
 *
 * @author Markcuz
 */
public class CANMessage
{
	private double time;
	private int busInt;
	private int frameID;
	private int byteData[];
	private int numData;
	
	
	public CANMessage()
	{
		this.time = 0;
		this.busInt = 0;
		this.frameID = 0;
		this.byteData = new int[8];
		this.numData = 0;
	}
	//This will be an alternate constructor that will read the raw data string given from the C program.
	public void newMessage(String rawDataString)
	{		
		//System.out.println("CANMessage:newMessage: " + rawDataString);
		String[] splitStr = rawDataString.trim().split("\\s+");
		//System.out.println("0: " + splitStr[0] + " 1: " + splitStr[1] + " 2: " + splitStr[2] + " 3: " + splitStr[3]);
		setTime(splitStr[0]);
		setBusInt(splitStr[1]);
		setFrameID(splitStr[2]);
		String temp = splitStr[3].substring(1);
		temp = temp.substring(0, temp.length() - 1);
		this.numData = Integer.parseInt(temp);
		setByteData(splitStr, this.numData);
	}
	
	public void setTime(String rawDataString)
	{
		this.time = Double.parseDouble(rawDataString);
	}
	
	public double getTime()
	{
		return time;
	}
	
	public void setBusInt(String rawDataString)
	{
		this.busInt = Integer.parseInt(rawDataString.charAt(3) + "");
	}
	
	public int getBusInt()
	{
		return busInt;
	}
	
	public void setFrameID(String rawDataString)
	{
		this.frameID = Integer.parseInt(rawDataString, 16);
	}
	
	public int getFrameID()
	{
		return frameID;
	}
	
	public void setByteData(String rawDataString[], int numData)
	{
		this.byteData = new int[numData];//splitStr[4]
		
		if(numData == 2)
		{
			this.byteData[0] = Integer.parseInt(rawDataString[4], 16) & 0xff;
			this.byteData[1] = Integer.parseInt(rawDataString[5], 16) & 0xff;
		}
		else if(numData == 8)
		{
			this.byteData[0] = Integer.parseInt(rawDataString[4], 16) & 0xff;
			this.byteData[1] = Integer.parseInt(rawDataString[5], 16) & 0xff;
			this.byteData[2] = Integer.parseInt(rawDataString[6], 16) & 0xff;
			this.byteData[3] = Integer.parseInt(rawDataString[7], 16) & 0xff;
			this.byteData[4] = Integer.parseInt(rawDataString[8], 16) & 0xff;
			this.byteData[5] = Integer.parseInt(rawDataString[9], 16) & 0xff;
			this.byteData[6] = Integer.parseInt(rawDataString[10], 16) & 0xff;
			this.byteData[7] = Integer.parseInt(rawDataString[11], 16) & 0xff;
		}
	}
	
	public int[] getByteData()
	{
		return byteData;
	}
	
	public int getByte(int index)
	{
		return byteData[index];
	}
	
	public String toString()
	{
		String outString = "";
		
		outString = outString + "+-----------------------------------------------------------+\n";
		outString = outString + "Time: " + this.getTime() + "\n";
		outString = outString + "Bus: " + this.getBusInt() + "\n";
		outString = outString + "Frame ID: " + this.getFrameID() + "\n";
		outString = outString + Integer.toHexString(this.getByte(0)) + " ";
		outString = outString + Integer.toHexString(this.getByte(1)) + " ";
		
		if(numData == 8)
		{
		    outString = outString + Integer.toHexString(this.getByte(2)) + " ";
		    outString = outString + Integer.toHexString(this.getByte(3)) + " ";
		    outString = outString + Integer.toHexString(this.getByte(4)) + " ";
		    outString = outString + Integer.toHexString(this.getByte(5)) + " ";
		    outString = outString + Integer.toHexString(this.getByte(6)) + " ";
		    outString = outString + Integer.toHexString(this.getByte(7));
		    
		}
		
		outString = outString + "\n+-----------------------------------------------------------+\n";
	
		return outString;
	}
}
