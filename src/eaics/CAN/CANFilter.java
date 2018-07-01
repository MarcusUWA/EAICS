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
	private EVMS_1 evms_1 = null;
	private EVMS_3 evms_3 = null;
	private ESC esc = null;
	
	public CANFilter()
	{
		this.evms_1 = new EVMS_1();
		this.evms_3 = new EVMS_3();
		this.esc = new ESC();
	}
	
	public String run(CANMessage message)
	{
		String outString = "";

		if(message.getFrameID() == 10)	//EVMS_1 Broadcast Status (Tx)
		{
		    evms_1.setAll(message);
		    outString = evms_1.toString();
		}
		else if(message.getFrameID() == 30)	//EVMS_3 Broadcast Status (Tx)
		{
		    evms_3.setAll(message);
		    outString = evms_3.toString();
		}
		else if(message.getFrameID() == 696969)	//MGM ESC module
		{
		    esc.setAll(message);
		    outString = esc.toString();
		}

		return toString();
	}
	
	public EVMS getEVMS()
	{
	    return evms_1;
	}
	
	public ESC getESC()
	{
	    return esc;
	}
	
	//Using the old EVMS 1, change this to 3 if using the newer verision.
	public String toString()
	{
	    return evms_1.toString() + " " + esc.toString();
	}
}