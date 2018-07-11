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
	private EVMS_v2 evms_v2;
	private EVMS_v3 evms_v3;
	private ESC esc;
	private BMS bms;
	
	public CANFilter()
	{
		this.evms_v2 = new EVMS_v2();
		this.evms_v3 = new EVMS_v3();
		this.esc = new ESC();
		this.bms = new BMS();
	}
	
	public void run(CANMessage message)
	{
	    switch (message.getFrameID()) 
	    {
	    	case 10:			  //EVMS_v2 Broadcast Status (Tx)
		    evms_v2.setAll(message);
		    break;
	    	case 30:			  //EVMS_v3 Broadcast Status (Tx)
		    evms_v3.setAll(message);
		    break;
	    	case 696969:			  //MGM ESC module
		    esc.setAll(message);
		    break;
		case 301: case 302: case 303: case 304:   //BMS Reply Data
		    bms.setAll(message);
		    break;
	    }
	}
	
	public EVMS getEVMS_v2()
	{
	    return this.evms_v2;
	}
	
	public EVMS getEVMS_v3()
	{
	    return this.evms_v3;
	}
	
	public ESC getESC()
	{
	    return this.esc;
	}
	
	public BMS getBMS()
	{
	    return this.bms;
	}
	
	//Using the old EVMS 1, change this to 3 if using the newer verision.
	@Override
	public String toString()
	{
	    return evms_v2.toString() + " " + esc.toString() + " " + bms.toString();
	}
}