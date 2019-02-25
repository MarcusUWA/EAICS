/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eaics.CAN.Battery.BMS;

import eaics.CAN.CANFilter;
import eaics.CAN.MiscCAN.CANMessage;
import eaics.Settings.SettingsEAICS;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 *
 * @author Troy
 */
public class BMS12v3 {
    
    private SettingsEAICS settings;
        private final int BPID = 300;
	private int MODULE_ID;
	private int[] voltage;
	private int[] temp;
        
	
	public static final int NUMBER_OF_CELLS = 12;
	public static final int NUMBER_OF_TEMP_SENSORS = 2;
        
        private ScheduledExecutorService displayExecutor;
        
        CANFilter filter;
	
	public BMS12v3(CANFilter filter, int inID) {
            this.settings = SettingsEAICS.getInstance();
	    this.MODULE_ID = inID;
	    this.voltage = new int[NUMBER_OF_CELLS];
	    this.temp = new int[NUMBER_OF_TEMP_SENSORS];
            this.filter = filter;
            
            startPolling();
	}
	
	public void setAll(CANMessage message)
	{
	    switch(message.getFrameID() - (BPID + 10 * MODULE_ID))
	    {
		case 1:
		    for (int ii = 0; ii < 4; ii++)
		    {
			this.voltage[ii] = (message.getByte(ii*2)<<8) + message.getByte(ii*2+1);
		    }
		    break;
		case 2:
		    for (int ii = 0; ii < 4; ii++)
		    {
			this.voltage[ii + 4] = (message.getByte(ii*2)<<8) + message.getByte(ii*2+1);
		    }
		    break;
		case 3:
		    for (int ii = 0; ii < 4; ii++)
		    {
			this.voltage[ii + 8] = (message.getByte(ii*2)<<8) + message.getByte(ii*2+1);
		    }
		    break;
		case 4:
		    this.temp[0] = message.getByte(0) - 40; // removing the +40 degree C offset
		    this.temp[1] = message.getByte(1) - 40; // removing the +40 degree C offset
		    break;
                case 0:
                    //the request packet...
                    break;
                    
	    }
	}
	
	@Override
	public String toString()
	{
	    String outstring = "";
	    for(int ii = 0; ii < voltage.length; ii++)
	    {
		outstring += " V" + (ii + 1) + ": " + this.voltage[ii];
	    }
	    for(int ii = 0; ii < temp.length; ii++)
	    {
		outstring += " T" + (ii + 1) + ": " + this.temp[ii];
	    }
	    return outstring;
	}

	//Returns the voltage of Cell ii (i.e. cell 0 to cell 11) in Volts, i.e. divide by 1000
	public int getVoltage(int ii)
	{
	    return this.voltage[ii];
	}
	
	public int getTemp(int ii)
	{
	    return this.temp[ii];
	}

	public String getVoltagesString()
	{
	    String outString = "";
	    
	    for(int ii = 0; ii < voltage.length; ii++)
	    {
                outString += voltage[ii] / 1000.0;
                outString += ", ";
	    }
	    
	    return outString;
	}
	
	public String getTemperatureString()
	{
	    String outString = "";
	    
	    for(int ii = 0; ii < temp.length; ii++)
	    {
                outString += temp[ii];
                outString += ", ";
	    }
	    
	    return outString;
	}
        
        private void startPolling() {
            displayExecutor = null;
            Runnable Id = new Runnable() { 

            int count = 0;

            @Override
            public void run() {
                filter.getCANHandler(0).writeMessage(BPID+10*MODULE_ID, new int[]{0, 0, 0, 0, 0, 0, 0, 0 });
            }
        };
        displayExecutor = Executors.newScheduledThreadPool(1);
        displayExecutor.scheduleAtFixedRate(Id, 0, 1000, TimeUnit.MILLISECONDS);
        }
}
