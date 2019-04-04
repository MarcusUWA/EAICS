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
 * @author Markcuz
 */
public class BMSElectro {
    
    final int canAddress = 0xB0;
    private SettingsEAICS settings;
        private final int requestID = 0x80;
        private final int baseResponseID = 0xA0;
	private int MODULE_ID;
	private int[] voltage;
	private int[] temp;
        
        private int serialNumber;
        private int lowestCellRecorded;
        private int highestCellRecorded;
        private int lowestTemp;
        private int highestTemp;
        
	public static final int NUMBER_OF_CELLS = 24;
	public static final int NUMBER_OF_TEMP_SENSORS = 4;
        
        private ScheduledExecutorService displayExecutor;
        
        CANFilter filter;
	
	public BMSElectro(CANFilter filter, int inID) {
            this.settings = SettingsEAICS.getInstance();
	    this.MODULE_ID = inID;
	    this.voltage = new int[NUMBER_OF_CELLS];
	    this.temp = new int[NUMBER_OF_TEMP_SENSORS];
            this.filter = filter;
            
            //startPolling();
	}
	
	public void setAll(CANMessage message) {
            int packetNum = message.getByte(0);
            
            switch(packetNum) {
                case 0: case 1: case 2: case 3: //pack voltages and temps
                    for(int i = 0; i<6; i++) {
                        voltage[packetNum*6+i] = message.getByte(i+2);
                    }
                    temp[packetNum] = message.getByte(1);
                    break;
                case 4: //serial number
                    break;
                case 5: //max/min data
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
                filter.getCANHandler(SettingsEAICS.getInstance().getCanSettings().getBmsCAN()).writeMessage(canAddress, new int[]{MODULE_ID, 0, 0, 0, 0, 0, 0, 0 });
            }
        };
        displayExecutor = Executors.newScheduledThreadPool(1);
        displayExecutor.scheduleAtFixedRate(Id, 0, 200, TimeUnit.MILLISECONDS);
        }
}
