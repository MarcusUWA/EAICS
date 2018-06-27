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
	private EVMS evms = null;
	private ESC esc = null;
	
	public CANFilter()
	{
		this.evms = null;
		this.esc = null;
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
		    if(esc == null)
		    {
			esc = new ESC();
		    }
		    esc.setAll(message);
		    outString = esc.toString();
		}
		//System.out.println("end");

		return outString;
	}
}