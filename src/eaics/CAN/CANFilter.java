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
public class CANFilter 
{
	public EVMS evms = null;
	public ESC esc = null;
	
	public CANFilter()
	{
		this.evms = null;
		this.esc = new ESC();
	}
	
	public String run(CANMessage message)
	{
		String outString = "";

		//System.out.println("start");
		if(message.getFrameID() == 10)	//EVMS_1 Broadcast Status (Tx)
		{
			//System.out.println("*********old n shit");
			if(evms == null)
			{
				evms = new EVMS_1();
			}
			evms.setAll(message);
			//System.out.println(evms.toString());
			outString = evms.toString();
		}
		else if(message.getFrameID() == 30)	//EVMS_3 Broadcast Status (Tx)
		{
			if(evms == null)
			{
				evms = new EVMS_3();
			}
			evms.setAll(message);
			//System.out.println(evms.toString());
			outString = evms.toString();
		}
		else if(message.getFrameID() == 696969)	//MGM ESC module
		{
		    esc.setAll(message);
		    outString = esc.toString();
		}
		//System.out.println("end");

		return toString();
	}
	
	public String toString()
	{
	    return evms.toString() + " " + esc.toString();
	}
}