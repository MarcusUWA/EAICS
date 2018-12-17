/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eaics.CAN;

import eaics.MiscCAN.CANHandler;
import eaics.MiscCAN.CANMessage;
import eaics.Settings.BMSSettings;
import java.io.IOException;
import java.util.Date;
import java.util.concurrent.ScheduledExecutorService;

/**
 *
 * @author Troy
 */
public class CANFilter 
{
    private static CANFilter instance;
    
    private CANHandler bus0CANHandler;
    private CANHandler bus1CANHandler;
    
    public static final int numOfESC = 4;
    public static final int numOfBMS = 24;
    public static final int numOfCCB = 3;

    private EVMS_v2 evms_v2;
    private EVMS_v3 evms_v3;
    private ESC[] esc;
    private BMS[] bms;
    private CurrentSensor currentSensor;
    private CCB[] ccb;
    private ChargerGBT chargerGBT;
    
    private Date dateTimeCAN0;
    private long lastPacketRecievedCANbus0;
    private Date dateTimeCAN1;
    private long lastPacketRecievedCANbus1;
    
    private boolean hasCANBus0TimedOut;
    private boolean hasCANBus1TimedOut;
    
    private boolean hasWarnedCAN0Timeout;
    private boolean hasWarnedCAN1Timeout;

    //Warnings
    private boolean hasWarnedError;
    private boolean hasWarnedChargerOff;

    //Logging
    ScheduledExecutorService executor;

    public static CANFilter getInstance()
    {
	if(instance == null)
	{
	    synchronized(CANFilter.class)
	    {
                if(instance == null)
                {
                    instance = new CANFilter();
                }
	    }
	}

	return instance;
    }

    private CANFilter()
    {
        bus0CANHandler = new CANHandler("can0");
        try
        {
            bus0CANHandler.openPort();
            bus0CANHandler.startReading();
        }
        catch(IOException e)
        {
            e.printStackTrace();
        }
        /*
        bus1CANHandler = new CANHandler("can1");
        try
        {
            bus1CANHandler.openPort();
            bus1CANHandler.startReading();
        }
        catch(IOException e)
        {
            e.printStackTrace();
        }
        */
        this.evms_v2 = new EVMS_v2();
        this.evms_v3 = new EVMS_v3();

        this.esc = new ESC[numOfESC];
        for(int ii = 0; ii < numOfESC; ii++)
        {
            this.esc[ii] = new ESC(ii);
        }

        this.bms = new BMS[numOfBMS];
        for(int ii = 0; ii < 24; ii++)
        {
            this.bms[ii] = new BMS(ii);
        }

        this.currentSensor = new CurrentSensor();

        this.ccb = new CCB[numOfCCB];
        for(int ii = 0; ii < numOfCCB; ii++)
        {
            this.ccb[ii] = new CCB();
        }

        this.chargerGBT = new ChargerGBT(this);

        this.hasWarnedError = false;
        this.hasWarnedChargerOff = false;

        this.dateTimeCAN0 = new Date();
        this.lastPacketRecievedCANbus0 = dateTimeCAN0.getTime();
        this.dateTimeCAN1 = new Date();
        this.lastPacketRecievedCANbus1 = dateTimeCAN1.getTime();
        this.hasCANBus0TimedOut = false;
        this.hasCANBus0TimedOut = false;
        this.hasWarnedCAN0Timeout = false;
        this.hasWarnedCAN1Timeout = false;
    }

    public void run(CANMessage message)
    {
        /*
        System.out.println("Filtering... CANID: "+message.getFrameID());
        
        StringBuilder sb = new StringBuilder();
        sb.append("Data: ");
        
        for(int i = 0; i<message.getByteData().length; i++) {   
            sb.append(String.format("%02X ", message.getByte(i)));
        }
        
        System.out.println(sb.toString());
        */
	switch (message.getFrameID()) 
	{
            //Begin EVMS CAN Messages
	    case 10:			  //EVMS_v2 Broadcast Status (Tx)
		evms_v2.setAll(message);
		break;
	    case 30:			  //EVMS_v3 Broadcast Status (Tx)
		evms_v3.setAll(message);
		break;
                
                
            //Begin Current Sensor CAN Messages
	    case 40:
		currentSensor.setAll(message);
		break;

                
            //Begin CCB CAN Messages
	    case 80://EVMS -> CCB1
		break;
	    case 81://CCB1 -> EVMS
		ccb[0].setAll(message);
		break;

	    case 82://EVMS -> CCB2
		break;
	    case 83://CCB2 -> EVMS
		ccb[1].setAll(message);
		break;    

	    case 84://EVMS -> CCB3
		break;
	    case 85://CCB3 -> EVMS
		ccb[2].setAll(message);
                break;

                
            //Begin BMS Module Information
            case 300: case 310: case 320: case 330: case 340: case 350: case 360: case 370:
            case 380: case 390: case 400: case 410: case 420: case 430: case 440: case 450:
            case 460: case 470: case 480: case 490: case 500: case 510: case 520: case 530:
                
                break;  //EVMS to BMS polling messages, just ignore these.
                
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
            
                
            //Begin ESC CAN Messages
	    case 346095617: case 346095618: case 346095619: case 346095620:	  //MGM ESC module Left -- offset by 0 in MGM
		esc[0].setAll(message);
		break;
	    case 346095622: case 346095623: case 346095624: case 346095625:	  //MGM ESC module Bottom -- offset by 4 in MGM
		esc[1].setAll(message);
		break;
	    case 346095627: case 346095628: case 346095629: case 346095630:	  //MGM ESC module Top -- offset by 8 in MGM
		esc[2].setAll(message);
		break;
	    case 346095632: case 346095633: case 346095634: case 346095635:	  //MGM ESC module Right -- offset by 12 in MGM
		esc[3].setAll(message);
		break;
            
            
            //Begin Throttle CAN Messages
            case 346095616: //HEX: "14A10000"
                break;//throttle command
                
                //ADD GBT charger can packet passing to:
            case 0x1826F456: case 0x1801F456: case 0x1CECF456: case 0x1807F456: case 0x1808F456: case 0x100AF456:
                chargerGBT.handleCharger(message);
                break;
                
            case 0x182756F4:    //BHM
                break;
                
            case 0x1CEC56F4:    //BRM
                break;
                
            case 0x1CEB56F4:    //BRM (data about the BMS)
                break;
                
            case 0x81FF456:     //Timeout
                System.out.println("Timeout");
                break;
                
	    default:
                System.out.println("Unknown packet, Frame ID: " + Integer.toHexString(message.getFrameID()));
		break;
	}
    }
    
    public long getLastPacketRecievedCANbus0()
    {
	return this.lastPacketRecievedCANbus0;
    }
    
    public long getLastPacketRecievedCANbus1()
    {
	return this.lastPacketRecievedCANbus1;
    }
    
    public boolean hasCANBus0TimedOut()
    {
	return this.hasCANBus0TimedOut;
    }
    
    public void can0Timeout()
    {
	this.hasCANBus0TimedOut = true;
    }
    
    public boolean hasCANBus1TimedOut()
    {
	return this.hasCANBus1TimedOut;
    }
    
    public void can1Timeout()
    {
	this.hasCANBus1TimedOut = true;
    }
    
    public boolean hasWarnedCAN0Timeout()
    {
	return this.hasWarnedCAN0Timeout;
    }
    
    public void hasWarnedCAN0Timeout(boolean hasWarnedCAN0Timeout)
    {
	this.hasWarnedCAN0Timeout = hasWarnedCAN0Timeout;
    }
    
    public boolean hasWarnedCAN1Timeout()
    {
	return this.hasWarnedCAN1Timeout;
    }
    
    public void hasWarnedCAN1Timeout(boolean hasWarnedCAN1Timeout)
    {
	this.hasWarnedCAN1Timeout = hasWarnedCAN1Timeout;
    }

    public boolean getHasWarnedError()
    {
	return hasWarnedError;
    }

    public void setHasWarnedError(boolean hasWarnedError)
    {
	this.hasWarnedError = hasWarnedError;
    }

    public boolean getHasWarnedChargerOff()
    {
	return hasWarnedChargerOff;
    }

    public void setHasWarnedChargerOff(boolean hasWarnedChargerOff)
    {
	this.hasWarnedChargerOff = hasWarnedChargerOff;
    }

    public void resetAllWarnings()
    {
	this.hasWarnedError = false;
	this.hasWarnedChargerOff = false;
	this.hasWarnedCAN0Timeout = false;
	this.hasWarnedCAN1Timeout = false;
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

    public CCB[] getCCB() 
    {
	return this.ccb;
    }

    public CurrentSensor getCurrentSensor()
    {
	return this.currentSensor;
    }
    
    public ChargerGBT getCharger()
    {
        return this.chargerGBT;
    }

    public void setLoggingExecutor(ScheduledExecutorService executor)
    {
	this.executor = executor;
    }

    public void stopLogging()
    {
	this.executor.shutdown();
    }
    
    public CANHandler getCANHandler(int busNum)
    {
        CANHandler handler;
        
        switch(busNum)
        {
            case 0:
                handler = this.bus0CANHandler;
                break;
            case 1:
                handler = this.bus1CANHandler;
                break;
            default:
                throw new IllegalArgumentException("This bus number does not exist");
        }
        return handler;
    }
}