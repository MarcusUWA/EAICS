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
 * @author troyg
 */
public class PixHawkSettings 
{
    private String ipAddress;
    
    public PixHawkSettings()
    {
        this.ipAddress = "192.168.201.113"; //default ip address
        loadSettings();
    }
    
    public String getIpAddress()
    {
        return ipAddress;
    }
    
    public void writeSettings()
    {
	Writer writer = null;
	try
	{
	    writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream("pixHawkSettingsFile"), "utf-8"));
	    
	    writer.write("" + this.ipAddress + "\n");
	    
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
    
    public void loadSettings()
    {
	BufferedReader reader;
		
	try
	{
	    reader = new BufferedReader(new InputStreamReader(new FileInputStream("/home/pi/EAICS/pixHawkSettingsFile"), "utf-8"));
	    String st;
	    int ii = 0;
	    while ((st = reader.readLine()) != null)
	    {
                this.ipAddress = st;
		ii++;
	    }
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
}
