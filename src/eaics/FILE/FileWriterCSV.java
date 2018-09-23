/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
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
    
    public FileWriterCSV(String fileName)
    {	
	try
	{
	    fileStrm = new FileOutputStream(fileName);		//Open the file
	    writer = new OutputStreamWriter(fileStrm, "utf-8");		//Create writer to write stream
	    bufWriter = new BufferedWriter(writer);		//To write the stream one line at a time
	    
	    writeHeadings();
	}
	catch(IOException e)
	{
	    e.printStackTrace();
	}
    }
    
    private void writeHeadings()
    {
	String columnNames = "";
	
	columnNames += "Date, Time, ";
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
	columnNames += "Load Cell\n";

	write(columnNames);
    }
    
    @Override
    public void write(String data) 
    {
	try
	{
	    bufWriter.write(data);
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
