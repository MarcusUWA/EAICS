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
	private EVMS_1 evms_1;
	private EVMS_3 evms_3;
	private ESC esc;
	private BMS bms;
	
	public CANFilter()
	{
		this.evms_1 = new EVMS_1();
		this.evms_3 = new EVMS_3();
		this.esc = new ESC();
		this.bms = new BMS();
	}
	
	public String run(CANMessage message)
	{
	    switch (message.getFrameID()) 
	    {
	    	case 10:			  //EVMS_1 Broadcast Status (Tx)
		    evms_1.setAll(message);
		    break;
	    	case 30:			  //EVMS_3 Broadcast Status (Tx)
		    evms_3.setAll(message);
		    break;
	    	case 696969:			  //MGM ESC module
		    esc.setAll(message);
		    break;
		case 301: case 302: case 303: case 304:   //BMS Reply Data
		    bms.setAll(message);
		    break;
	    }

	    return toString();
	}
	
	public EVMS getEVMS()
	{
	    return this.evms_1;
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
	public String toString()
	{
	    return evms_1.toString() + " " + esc.toString();
	}
}