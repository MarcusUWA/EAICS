package eaics.LOGGING;

import eaics.CAN.Battery.BMS.BMS12v3;
import eaics.CAN.CANFilter;
import eaics.CAN.Battery.CurrentSensor;
import eaics.CAN.ESC.ESC;
import eaics.CAN.Battery.EVMS;
import eaics.CAN.Charger.TC.TCCharger;
import eaics.CAN.MGL.MGLDisplay;
import eaics.FILE.FileWriter;
import eaics.FILE.FileWriterCSV;
import eaics.SER.LoadCell;
import eaics.SER.Serial;
import eaics.SER.Throttle;
import eaics.Settings.SettingsEAICS;
import eaics.Settings.TYPECharger;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 *
 * @author Troy Burgess
 */
public class Logging 
{
    private FileWriter fileWriter;  
    private ScheduledExecutorService executor;
    private Runnable Logger;
    
    private CANFilter filter;
    private SimpleDateFormat formatterDate;
    private SimpleDateFormat formatterTime;
    private LoadCell loadCell;
    private Throttle throttle;
    
    private boolean isLogging;
    
    public Logging(Serial comms)
    {
        this.filter = CANFilter.getInstance();
        this.loadCell = comms.getCell();
        this.throttle = comms.getThrottle();
        formatterDate = new SimpleDateFormat("yyyy/MM/dd");
	formatterTime = new SimpleDateFormat("HH:mm:ss");

        startNewLogFile();        
    }
    public void setLogging(boolean state) {
        isLogging = state;
    }
    public boolean getLogging() {
        return isLogging;
    }
    
    public void startNewLogFile()
    {
	String filename = new SimpleDateFormat("yyyy-MM-dd hh-mm-ss'.csv'").format(new Date());
        this.fileWriter = new FileWriterCSV("/home/pi/Logging/"+ filename);
        writeHeadings();
        writeLoggins();     
        isLogging = true;
    }
    
    public void changeLoggingRate(int milliseconds) //must be less than 60 seconds i.e. 60 000 milliseconds
    {
        if(milliseconds > 0 && milliseconds <= 60000)
        {
            stopLogging();
            executor = Executors.newScheduledThreadPool(1);
            executor.scheduleAtFixedRate(Logger, 0, milliseconds, TimeUnit.MILLISECONDS);   // Run every second
        }
    }
    
    public void startLogging()
    {
        //stopLogging();
        startNewLogFile();
        isLogging = true;
    }
    
    public void stopLogging()
    {
        isLogging = false;
        try
        {
            this.executor.shutdown();
        }
        catch(Exception e)
        {
            //Try to stop logging, may be already stopped.            
        }
    }
    
    private void writeLoggins()
    {
        Logger = new Runnable() 
	{
	    private Date date = new Date();

	    @Override
	    public void run() 
	    {
                String columnData = "";
		Date date = new Date();
		columnData += formatterDate.format(date) + " " + formatterTime.format(date) + ", ";
                
		//Current Sensor
		CurrentSensor currentSensor = filter.getCurrentSensor();
		columnData += currentSensor.getCurrent();
		columnData += ", ";
                
		//EVMS
		EVMS evms = (EVMS)filter.getEVMS();
                columnData += evms.getVoltage() + ", ";
                columnData += evms.getAuxVoltage() + ", ";
                columnData += evms.getLeakage() + ", ";
                columnData += evms.getTemp() + ", ";
		columnData += evms.getHeadlights() + ", ";
                columnData += evms.getAmpHours() + ", ";
                
		//BMS
		BMS12v3 bms[] = filter.getBMS();
		for(int ii = 0; ii < bms.length; ii++)
		{
		    columnData += bms[ii].getVoltagesString();
		    columnData += bms[ii].getTemperatureString();
		}
                
		//ESC
		ESC esc[] = filter.getESC();
		for(int ii = 0; ii < esc.length; ii++)
		{
		    columnData += esc[ii].getLoggingString();
		}

		//Load Cell
                columnData += loadCell.getWeight()+",";

                columnData += loadCell.getCalibration()+",";

                for(int i = 0; i < 4; i++) {
                    columnData += loadCell.getLoadCells(i)+",";
                }
                
                columnData += loadCell.getWeight()+",";
                
                MGLDisplay mgl = filter.getMgl();
                columnData += mgl.getBankAngle()+",";
                columnData += mgl.getPitchAngle()+",";
                columnData += mgl.getYawAngle()+",";
                columnData += mgl.getGndSpeed()+",";
                
                if(SettingsEAICS.getInstance().getGeneralSettings().getChargerType()==TYPECharger.TC) {
                    TCCharger tc = filter.getChargerTC();
                    columnData += tc.getOutputVoltage()+",";
                    columnData += tc.getOutputCurrent()+",";
                    columnData += tc.getStatusByte();
                }

                columnData +="\n";
		fileWriter.write(columnData);
	    }
	};

	executor = Executors.newScheduledThreadPool(1);
	executor.scheduleAtFixedRate(Logger, 0, 1, TimeUnit.SECONDS);   // Run every second   
    }
        
    private void writeHeadings()
    {
        String columnHeadings = "";

        columnHeadings += "Date & Time, ";
        columnHeadings += "Current, ";
        columnHeadings += "Volt, Aux Volt, Leakage, Temp, ";
        columnHeadings += "Headlights, Amp Hours, ";

        for(int ii = 1; ii <= CANFilter.NUM_OF_BMS; ii++)
        {
            for(int jj = 1; jj <= BMS12v3.NUMBER_OF_CELLS; jj++)
            {
                columnHeadings += "BMS " + ii + " Cell " + jj + " Voltage, ";			    
            }
            columnHeadings += "BMS " + ii + " Temp 1, ";
            columnHeadings += "BMS " + ii + " Temp 2, ";
        }
        
        for(int ii = 1; ii <= CANFilter.NUM_OF_ESC; ii++)
        {
            columnHeadings += "ESC " + ii + " Battery Voltage, ";
            columnHeadings += "ESC " + ii + " Battery Current, ";
            columnHeadings += "ESC " + ii + " RPM, ";
            columnHeadings += "ESC " + ii + " Odometer, ";
            columnHeadings += "ESC " + ii + " Controller Temperature, ";
            columnHeadings += "ESC " + ii + " Motor Temperature, ";
            columnHeadings += "ESC " + ii + " Battery Temperature, ";
            columnHeadings += "ESC " + ii + " Requested Output PWM, ";
            columnHeadings += "ESC " + ii + " Real Output PWM, ";
            columnHeadings += "ESC " + ii + " Warnings, ";
            columnHeadings += "ESC " + ii + " Failures, ";
            columnHeadings += "ESC " + ii + " Remaining Battery Capacity, ";
            columnHeadings += "ESC " + ii + " Throttle Command, ";			
        }

        columnHeadings += "Load Cell Total, ";

        columnHeadings += "Calibration Factor, ";

        for(int i = 1; i <= 4; i++) 
        {
            columnHeadings += "Load Cell "+i+",";
        }
        
        columnHeadings += "Weight, ";
        
        columnHeadings += "Bank Angle, ";
        columnHeadings += "Pitch Angle, ";
        columnHeadings += "Yaw Angle, ";
        columnHeadings += "Ground Speed, ";
        
        columnHeadings += "Charging Voltage, ";
        columnHeadings += "Charging Current, ";
        columnHeadings += "Status Byte";
      
        columnHeadings += "\n";

        fileWriter.write(columnHeadings);
    }       
}