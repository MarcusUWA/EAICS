/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eaics.SER;

/**
 *
 * @author Troy
 */
public class LoadCell
{
	private long time;
	private double weight;
	private String units;
	private int unknownInt;
	private double unknownDouble;
	private int unknownZero;
	private boolean isUnread;

	private double calibration;
	private int count;
	private boolean isCal;
	private boolean first;
	
	public LoadCell()
	{
		this.time = 0;
		this.weight = 0.0;
		this.units = "";
		this.unknownInt = 0;
		this.unknownDouble = 0.0;
		this.unknownZero = 0;
		this.isUnread = false;

		this.calibration = 0.0;
		this.count = 0;
		this.isCal = false;
		//this.first = true;
	}
	
	public void setMsg(String msg)
	{
		String[] msgArray = msg.split(",");
                
                if(msgArray.length > 2) {
                    try {
                        this.time =  Long.parseLong(msgArray[0]);
                    }
                    catch (NumberFormatException e) {
                        this.time = 0;
                    }
                    try {
                        this.weight = Double.parseDouble(msgArray[1]);
                    }
                    catch (NumberFormatException e) {
                        this.weight = 0;
                    }
                    this.units = msgArray[2];
                    //this.unknownInt = Integer.parseInt(msgArray[3]);
                    //this.unknownDouble = Double.parseDouble(msgArray[4]);
                    this.isUnread = true;

                    if(count > 6)
                    {
                            if(isCal == false)
                            {
                                    this.calibration = this.weight;
                                    isCal = true;
                            }
                    }

                    else
                    {
                            count++;
                    }
                }
	}

	public boolean isUnread()
	{
		return this.isUnread;
	}

	public String getMsg()
	{
		String outString = "";
		if(isUnread == true)
		{
			isUnread = false;
			outString = toString();
		}

		return outString;
	}
        
        public double getWeight() 
	{
            return (weight-calibration);
        }
	
	@Override
	public String toString()
	{
		return "" + time + " " + (weight - calibration) + "" + units;
	}	
}