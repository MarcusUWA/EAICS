/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eaics.Settings;

import eaics.CAN.MiscCAN.CANHandler;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Troy
 */
public class EAICS_Settings 
{
    private static EAICS_Settings instance;
    
    private String filePath;
    private EVMSSettings evmsSettings;
    private PixHawkSettings pixHawkSettings;
    
    private static final String SETTINGS_HEADING = "+-----------------------------------------------+\n";
    private static final String SETTINGS_BMS = "BMS_SETTINGS";
    private static final String SETTINGS_PIXHAWK = "PIXHAWK_SETTINGS";
    
    private EAICS_Settings()
    {
        this.filePath = "/home/pi/EAICS/settingsFile.conf";
        this.evmsSettings = new EVMSSettings();
        this.pixHawkSettings = new PixHawkSettings();
    }
    
    public static EAICS_Settings getInstance() 
    {	
	if(instance == null)
	{
	    synchronized(EAICS_Settings.class)
	    {
                if(instance == null)
                {
                    instance = new EAICS_Settings();
                }
	    }
	}
	
	return instance;
    }
    
    public EVMSSettings getEVMSSettings()
    {
        return evmsSettings;
    }
    
    public PixHawkSettings getPixHawkSettings()
    {
        return pixHawkSettings;
    }
    
    public void update() 
    {
        System.out.println("Update");
        evmsSettings.update();
	pixHawkSettings.update();
	writeSettings();	
    }
    
    public void loadSettings() 
    {
        System.out.println("Load Settings");
	BufferedReader reader;
		
	try
	{
	    reader = new BufferedReader(new InputStreamReader(new FileInputStream(filePath), "utf-8"));
	    String inFileString;
	    int count = 0;
            
            boolean readingBMS = false;
            String bmsFileString = "";
            
            boolean readingPixHawk = false;
            String pixHawkFileString = "";
            
	    while ((inFileString = reader.readLine()) != null)
	    {
		if(inFileString.contains("+---"))
		{
		    inFileString = reader.readLine();	// skip heading lines
		}
		
		if(inFileString.contains(SETTINGS_BMS))
		{
		    readingBMS = true;
		    count = 0;
		}

		if(inFileString.contains(SETTINGS_PIXHAWK))
		{
		    readingPixHawk = true;
		    count = 0;
		}
                
                if(readingBMS)
                {
		    String[] line = inFileString.split("\\s+");
		    
                    if(count < 30)
                    {
			for(int ii = 0; ii < line.length; ii++)
			{
			    if(line[ii].startsWith("#"))
			    {
				String temp = line[ii].substring(1);
				bmsFileString += temp + "\n";
				count++;
			    }
			}
                    }
                    else
                    {
                        readingBMS = false;
                    }
		    
                }
                
                if(readingPixHawk)
                {
		    String[] line = inFileString.split("\\s+");
		    
                    if(count < 1)
                    {
			for(int ii = 0; ii < line.length; ii++)
			{
			    if(line[ii].startsWith("#"))
			    {
				String temp = line[ii].substring(1);
				pixHawkFileString += temp + "\n";
				count++;
			    }
			}
                    }
                    else
                    {
                        readingPixHawk = false;
                    }
                }
	    }
            
            evmsSettings.setSettings(bmsFileString);
            pixHawkSettings.setSettings(pixHawkFileString);
	}
        catch(FileNotFoundException e)
        {
            writeSettings();
        }
	catch(Exception e)
	{
	    e.printStackTrace();
	}
        finally
        {
            update();
        }
    }
    
    public void writeSettings()
    {
        System.out.println("Write Settings");
	Writer writer = null;
	try
	{
	    writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(filePath), "utf-8"));
	    
	    writer.write(SETTINGS_HEADING);
	    writer.write("\t\t" + SETTINGS_BMS + "\n");
	    writer.write(SETTINGS_HEADING);
            writer.write(evmsSettings.getSettingsFileString());
            
            writer.write("\n\n");
            
	    writer.write(SETTINGS_HEADING);
	    writer.write("\t\t" + SETTINGS_PIXHAWK + "\n");
	    writer.write(SETTINGS_HEADING);
            writer.write(pixHawkSettings.getSettingsFileString());
	    
	    writer.flush();
	    writer.close();
	}
	catch(IOException e)
	{
            e.printStackTrace();
	}
	finally
	{
            try
            {
                    writer.close();
            }
            catch(Exception e)
            {
            }
	}
    }
}
