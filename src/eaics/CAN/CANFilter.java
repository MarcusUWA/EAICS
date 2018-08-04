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
	private BMS[] bms;
	
	public CANFilter()
	{
		this.evms_v2 = new EVMS_v2();
		this.evms_v3 = new EVMS_v3();
		this.esc = new ESC();
		this.bms = new BMS[16];
		for(int ii = 0; ii < 16; ii++)
		{
		    this.bms[ii] = new BMS();
		}
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
		case 301: case 302: case 303: case 304:   //Reply Data - BMS Module 0
		    bms[0].setAll(message);
		    break;
		case 311: case 312: case 313: case 314:   //Reply Data - BMS Module 1
		    bms[1].setAll(message);
		    break;
		case 321: case 322: case 323: case 324:   //Reply Data - BMS Module 2
		    bms[2].setAll(message);
		    break;
		case 331: case 332: case 333: case 334:   //Reply Data - BMS Module 3
		    bms[3].setAll(message);
		    break;
		case 341: case 342: case 343: case 344:   //Reply Data - BMS Module 4
		    bms[4].setAll(message);
		    break;
		case 351: case 352: case 353: case 354:   //Reply Data - BMS Module 5
		    bms[5].setAll(message);
		    break;
		case 361: case 362: case 363: case 364:   //Reply Data - BMS Module 6
		    bms[6].setAll(message);
		    break;
		case 371: case 372: case 373: case 374:   //Reply Data - BMS Module 7
		    bms[7].setAll(message);
		    break;
		case 381: case 382: case 383: case 384:   //Reply Data - BMS Module 8
		    bms[8].setAll(message);
		    break;
		case 391: case 392: case 393: case 394:   //Reply Data - BMS Module 9
		    bms[9].setAll(message);
		    break;
		case 401: case 402: case 403: case 404:   //Reply Data - BMS Module A
		    bms[10].setAll(message);
		    break;
		case 411: case 42: case 43: case 44:   //Reply Data - BMS Module B
		    bms[11].setAll(message);
		    break;
		case 421: case 422: case 423: case 424:   //Reply Data - BMS Module C
		    bms[12].setAll(message);
		    break;
		case 431: case 432: case 433: case 434:   //Reply Data - BMS Module D
		    bms[13].setAll(message);
		    break;
		case 441: case 442: case 443: case 444:   //Reply Data - BMS Module E
		    bms[14].setAll(message);
		    break;
		case 451: case 452: case 453: case 454:   //Reply Data - BMS Module F
		    bms[15].setAll(message);
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
	
	public BMS[] getBMS()
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