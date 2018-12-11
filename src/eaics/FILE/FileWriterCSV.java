package eaics.FILE;

import eaics.CAN.EVMS;
import eaics.CAN.EVMS_v3;
import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;

/**
 *
 * @author Troy
 */
public class FileWriterCSV implements FileWriter
{
    //Objects for file reading
    private FileOutputStream fileStrm;
    private OutputStreamWriter writer;
    private BufferedWriter bufWriter;
    
    // type = 0: log all
    // type = 1: rpm logger
    
    public FileWriterCSV(String fileName, int type)
    {	
		try
		{
			fileStrm = new FileOutputStream("/home/pi/Logging/"+ fileName);		//Open the file
			writer = new OutputStreamWriter(fileStrm, "utf-8");		//Create writer to write stream
			bufWriter = new BufferedWriter(writer);		//To write the stream one line at a time
			
			if(type == 0)
			{
				writeHeadings();
			}
			else if(type == 1)
			{
				writeRPMHeadings();
			}
		}
		catch(IOException e)
		{
			e.printStackTrace();
		}
    }
    
    private void writeHeadings()
    {
		String columnNames = "";
		
		columnNames += "Date & Time, ";
		columnNames += "Current, ";
		columnNames += EVMS.getLoggingHeadings();
		columnNames += EVMS_v3.getLoggingHeadings();
		
		for(int ii = 1; ii <= 24; ii++)
		{
			for(int jj = 1; jj <= 12; jj++)
			{
				columnNames += "BMS " + ii + " Cell " + jj + " Voltage, ";			    
			}
			columnNames += "BMS " + ii + " Temp 1, ";
			columnNames += "BMS " + ii + " Temp 2, ";
		}
		for(int ii = 1; ii <= 4; ii++)
		{
			columnNames += "ESC " + ii + " Battery Voltage, ";
			columnNames += "ESC " + ii + " Battery Current, ";
			columnNames += "ESC " + ii + " RPM, ";
			columnNames += "ESC " + ii + " Odometer, ";
			columnNames += "ESC " + ii + " Controller Temperature, ";
			columnNames += "ESC " + ii + " Motor Temperature, ";
			columnNames += "ESC " + ii + " Battery Temperature, ";
			columnNames += "ESC " + ii + " Requested Output PWM, ";
			columnNames += "ESC " + ii + " Real Output PWM, ";
			columnNames += "ESC " + ii + " Warnings, ";
			columnNames += "ESC " + ii + " Failures, ";
			columnNames += "ESC " + ii + " Remaining Battery Capacity, ";
			columnNames += "ESC " + ii + " Throttle Command, ";			
		}
                
		columnNames += "Load Cell Total, ";
                
                columnNames += "Calibration Factor, ";
                
                for(int i = 1; i <= 4; i++) {
                    columnNames += "Load Cell "+i+",";
		}
                
                columnNames += "\n";

		write(columnNames);
    }
    
    private void writeRPMHeadings()
    {
		String columnNames = "";
		
		columnNames += "Date & Time, ";
		columnNames += "Current, ";
		columnNames += EVMS.getLoggingHeadings();
		columnNames += EVMS_v3.getLoggingHeadings();
		
		for(int ii = 1; ii <= 1; ii++)
		{
			columnNames += "ESC " + ii + " Battery Voltage, ";
			columnNames += "ESC " + ii + " Battery Current, ";
			columnNames += "ESC " + ii + " RPM, ";
			columnNames += "ESC " + ii + " Odometer, ";
			columnNames += "ESC " + ii + " Controller Temperature, ";
			columnNames += "ESC " + ii + " Motor Temperature, ";
			columnNames += "ESC " + ii + " Battery Temperature, ";
			columnNames += "ESC " + ii + " Requested Output PWM, ";
			columnNames += "ESC " + ii + " Real Output PWM, ";
			columnNames += "ESC " + ii + " Warnings, ";
			columnNames += "ESC " + ii + " Failures, ";
			columnNames += "ESC " + ii + " Remaining Battery Capacity, ";
			columnNames += "ESC " + ii + " Throttle Command, ";			
		}
		columnNames += "Power (kw)\n";

		write(columnNames);
    }
    
    @Override
    public void write(String data) 
    {
		try
		{
			bufWriter.write(data);
                        //System.out.println(data);
			bufWriter.flush();	//flush the writer
		}
		catch(IOException e)
		{
			e.printStackTrace();
		}
	}
		
	@Override
	public void close()
	{
		try
		{
			bufWriter.close();	    
		}
		catch(Exception e)
		{
			
		}	
    }
}