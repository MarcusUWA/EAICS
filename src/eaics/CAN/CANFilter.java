/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eaics.CAN;

import eaics.BMSSettings;

/**
 *
 * @author Troy
 */
public class CANFilter 
{
	private EVMS_v2 evms_v2;
	private EVMS_v3 evms_v3;
	private ESC[] esc;
	private BMS[] bms;
	private BMSSettings bmsSettings;
	
	public CANFilter()
	{
		this.evms_v2 = new EVMS_v2();
		this.evms_v3 = new EVMS_v3();
		this.esc = new ESC[4];
		for(int ii = 0; ii < 4; ii++)
		{
		    this.esc[ii] = new ESC(ii);
		}
		this.bms = new BMS[24];
		for(int ii = 0; ii < 24; ii++)
		{
		    this.bms[ii] = new BMS(ii);
		}
		this.bmsSettings = new BMSSettings();
	}
	
	public void run(CANMessage message)
	{
	    //System.out.println("CANFilter:run: FrameID: " + message.getFrameID());
	    switch (message.getFrameID()) 
	    {
	    	case 10:			  //EVMS_v2 Broadcast Status (Tx)
		    evms_v2.setAll(message);
		    break;
	    	case 30:			  //EVMS_v3 Broadcast Status (Tx)
		    evms_v3.setAll(message);
		    break;
	    	case 346095617: case 346095618: case 346095619: case 346095620:	  //MGM ESC module Top
		    //System.out.println("CANFilter:run Incoming info for ESC[0]");
		    esc[0].setAll(message);
		    break;
		case 2222: case 2223: case 2224: case 2225:	  //MGM ESC module Bottom
		    esc[1].setAll(message);
		    break;
		case 3333: case 3334: case 3335: case 3336:	  //MGM ESC module Left
		    esc[2].setAll(message);
		    break;
		case 4444: case 4445: case 4446: case 4447:	  //MGM ESC module Right
		    esc[3].setAll(message);
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
		case 411: case 412: case 413: case 414:   //Reply Data - BMS Module B
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
		case 461: case 462: case 463: case 464:   //Reply Data - BMS Module 0 + 16
		    bms[16].setAll(message);
		    break;
		case 471: case 472: case 473: case 474:   //Reply Data - BMS Module 1 + 16
		    bms[17].setAll(message);
		    break;
		case 481: case 482: case 483: case 484:   //Reply Data - BMS Module 2 + 16
		    bms[18].setAll(message);
		    break;
		case 491: case 492: case 493: case 494:   //Reply Data - BMS Module 3 + 16
		    bms[19].setAll(message);
		    break;
		case 501: case 502: case 503: case 504:   //Reply Data - BMS Module 4 + 16
		    bms[20].setAll(message);
		    break;
		case 511: case 512: case 513: case 514:   //Reply Data - BMS Module 5 + 16
		    bms[21].setAll(message);
		    break;
		case 521: case 522: case 523: case 524:   //Reply Data - BMS Module 6 + 16
		    bms[22].setAll(message);
		    break;
		case 531: case 532: case 533: case 534:   //Reply Data - BMS Module 7 + 16
		    bms[23].setAll(message);
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
	
	public ESC[] getESC()
	{
	    return this.esc;
	}
	
	public BMS[] getBMS()
	{
	    return this.bms;
	}
	
	public BMSSettings getBMSSettings()
	{
	    return this.bmsSettings;
	}
}