/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eaics;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Writer;

/**
 *
 * @author troy
 */
public class Settings 
{
    private String filePath;
    private BMSSettings bmsSettings;
    private PixHawkSettings pixHawkSettings;
    
    private static final String SETTINGS_BMS = "BMS_SETTINGS";
    private static final String SETTINGS_PIXHAWK = "PIXHAWK_SETTINGS";
    
    public Settings()
    {
        this.filePath = "/home/pi/EAICS/settingsFile.conf";
        this.bmsSettings = new BMSSettings();
        this.pixHawkSettings = new PixHawkSettings();
        writeSettings();
    }
    
    public BMSSettings getBmsSettings()
    {
        return bmsSettings;
    }
    
    public PixHawkSettings getPixHawkSettings()
    {
        return pixHawkSettings;
    }
    
    public void loadSettings()
    {
	BufferedReader reader;
		
	try
	{
	    reader = new BufferedReader(new InputStreamReader(new FileInputStream(filePath), "utf-8"));
	    String st;
	    int count = 0;
            
            boolean readingBMS = false;
            String bmsFileString = "";
            
            boolean readingPixHawk = false;
            String pixHawkFileString = "";
            
	    while ((st = reader.readLine()) != null)
	    {
                if(st.equals(SETTINGS_BMS))
                {
                    readingBMS = true;
                    count = 0;
                }
                
                if(readingBMS)
                {
                    if(count < 30)
                    {
                        bmsFileString += st;
                        count++;
                    }
                    else
                    {
                        readingBMS = false;
                    }
                }
                
                if(st.equals(SETTINGS_PIXHAWK))
                {
                    readingPixHawk = true;
                    count = 0;
                }
                
                if(readingPixHawk)
                {
                    if(count < 1)
                    {
                        pixHawkFileString += st;
                        count++;
                    }
                    else
                    {
                        readingPixHawk = false;
                    }
                }
	    }
            
            bmsSettings.setSettings(bmsFileString);
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
            //update();
        }
    }
    
    public void writeSettings()
    {
	Writer writer = null;
	try
	{
	    writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(filePath), "utf-8"));
	    
	    writer.write(SETTINGS_BMS);
            writer.write("\n");
            writer.write(bmsSettings.getSettingsFileString());
            
            writer.write("\n\n");
            
            writer.write(SETTINGS_PIXHAWK);
            writer.write("\n");
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
