/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eaics.MiscCAN;

/**
 *
 */
public class CANMessage
{
	private double time;
	private int busNum;
	private int frameID;
        private String frameID_HEX;
	private int byteData[];
	private int numData;
        private String raw;
        private String busName;
	
	
	public CANMessage()
	{
                this.raw = "";
		this.time = 0;
		this.busNum = 0;
		this.frameID = 0;
                this.frameID_HEX = "";
		this.byteData = new int[8];
		this.numData = 0;
	}
        
        //This will be an alternate constructor that will read the raw data string given from the C program.
	public void newMessage(String rawDataString)
	{	
                this.raw = rawDataString;
		//System.out.println("CANMessage:newMessage: " + rawDataString);
		String[] splitStr = rawDataString.trim().split("\\s+");
		//System.out.println("0: " + splitStr[0] + " 1: " + splitStr[1] + " 2: " + splitStr[2] + " 3: " + splitStr[3]);
                //System.out.println("4: " + splitStr[4] + " 5: " + splitStr[5] + " 6: " + splitStr[6] + " 7: " + splitStr[7]);
		setTime(splitStr[0]);
		setBusInt(splitStr[1]);
		setFrameID(splitStr[2]);
		String temp = splitStr[3].substring(1);
		temp = temp.substring(0, temp.length() - 1);
		this.numData = Integer.parseInt(temp);
		setByteData(splitStr, this.numData);
	}
        
        public void newMessage(String busName, int frameID, int[] byteData)
        {
            this.busName = busName;
            this.frameID = frameID;
            this.byteData = byteData;
            this.numData = byteData.length;            
        }
        
        private static int[] convertToIntArray(byte[] input)
        {
            int[] ret = new int[input.length];
            for (int i = 0; i < input.length; i++)
            {
                ret[i] = input[i] & 0xff; // Range 0 to 255, not -128 to 127
            }
            return ret;
        }
        
	
        
        public String getRaw()
        {
            return this.raw;
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
		this.busNum = Integer.parseInt(rawDataString.charAt(3) + "");
	}
	
	public int getBusInt()
	{
		return busNum;
	}
	
	public void setFrameID(String rawDataString)
	{
            this.frameID_HEX = rawDataString;
            this.frameID = Integer.parseInt(rawDataString, 16);
	}
	
	public int getFrameID()
	{
		return frameID;
	}
        
        public String getFrameID_HEX()
        {
            return frameID_HEX;
        }
	
	public void setByteData(String rawDataString[], int numData)
	{
		this.byteData = new int[numData];//splitStr[4]
                
                for(int ii = 0; ii < numData; ii++)
                {
                    this.byteData[ii] = Integer.parseInt(rawDataString[4 + ii], 16) & 0xff;
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
