/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eaics.Settings;

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
 * @author Troy
 */
public class SettingsEAICS {
    private static SettingsEAICS instance;
    
    private String filePath;
    
    private SettingsGeneral generalSettings;
    private SettingsEVMS evmsSettings;
    private SettingsPixhawk pixHawkSettings;
    private SettingsCAN canSettings;
    
    private static final String SETTINGS_HEADING = "+-----------------------------------------------+\n";
    private static final String SETTINGS_FOOTER = "+^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^+\n";
    private static final String SETTINGS_GENERAL = "GENERAL_SETTINGS";
    private static final String SETTINGS_BMS = "BMS_SETTINGS";
    private static final String SETTINGS_PIXHAWK = "PIXHAWK_SETTINGS";
    private static final String SETTINGS_CAN = "CANBUS_SETTINGS";
    
    public boolean settingsLoaded = false;
    
    private SettingsEAICS()
    {
        System.out.println("Settings init");
        
        this.filePath = "/home/pi/EAICS/settingsFile.conf";
        this.generalSettings = new SettingsGeneral();
        this.evmsSettings = new SettingsEVMS();
        this.pixHawkSettings = new SettingsPixhawk();
        this.canSettings = new SettingsCAN();
        
    }
    
    public static SettingsEAICS getInstance() 
    {	
	if(instance == null)
	{
	    synchronized(SettingsEAICS.class)
	    {
                if(instance == null)
                {
                    instance = new SettingsEAICS();
                }
	    }
	}
	
	return instance;
    }
    
    public SettingsEVMS getEVMSSettings() {
        return evmsSettings;
    }
    
    public SettingsPixhawk getPixHawkSettings() {
        return pixHawkSettings;
    }

    public SettingsGeneral getGeneralSettings() {
        return generalSettings;
    }

    public SettingsCAN getCanSettings() {
        return canSettings;
    }
       
    public void update() {
        System.out.println("Update Settings");
        generalSettings.update();
        evmsSettings.update();
	pixHawkSettings.update();
        canSettings.update();
	writeSettings();	
    }
    
    public void loadSettings() {
        System.out.println("Load Settings");
	BufferedReader reader;
		
	try {
	    reader = new BufferedReader(new InputStreamReader(new FileInputStream(filePath), "utf-8"));
	    String inFileString;
            
            boolean readingGeneral = false;
            String generalFileString = "";
            
            boolean readingBMS = false;
            String bmsFileString = "";
            
            boolean readingPixHawk = false;
            String pixHawkFileString = "";
            
            boolean readingCan = false;
            String canFileString = "";
            
	    while ((inFileString = reader.readLine()) != null) {
		if(inFileString.contains("+---")) {
		    inFileString = reader.readLine();	// skip heading lines
		}
                
                if(inFileString.contains(SETTINGS_GENERAL)) {
		    readingGeneral = true;
		}
		
		if(inFileString.contains(SETTINGS_BMS)) {
		    readingBMS = true;
		}

		if(inFileString.contains(SETTINGS_PIXHAWK))
		{
		    readingPixHawk = true;
		}
                
                if(inFileString.contains(SETTINGS_CAN))
		{
		    readingCan = true;
		}
                
                
                if(readingGeneral) { 
                    if(inFileString.contains("+^^^")) {
                       readingGeneral = false;
                       continue;
                    }
                    generalFileString += inFileString + "\n";  
                }
                
                if(readingCan) { 
                    if(inFileString.contains("+^^^")) {
                       readingCan = false;
                       continue;
                    }
                    canFileString += inFileString + "\n";  
                }
                
                if(readingBMS) { 
                    if(inFileString.contains("+^^^")) {
                       readingBMS = false;
                       continue;
                    }
                    
		    String[] line = inFileString.split("\\s+");
		    
                    for(int ii = 0; ii < line.length; ii++) {
                        if(line[ii].startsWith("#")) {
                            bmsFileString += line[ii].substring(1) + "\n";
                        }
                    }
		    
                }
                
                if(readingPixHawk) {
                    
                    if(inFileString.contains("+^^^")) {
                       readingPixHawk = false;
                       continue;
                    }
                    
		    String[] line = inFileString.split("\\s+");
		    
                    for(int ii = 0; ii < line.length; ii++) {
                        if(line[ii].startsWith("#")) {
                            String temp = line[ii].substring(1);
                            pixHawkFileString += temp + "\n";
                        }
                    }
                }
	    }
            
            canSettings.setSettings(canFileString);
            generalSettings.setSettings(generalFileString);
            evmsSettings.setSettings(bmsFileString);
            pixHawkSettings.setSettings(pixHawkFileString);

            settingsLoaded = true;
	}
        catch(FileNotFoundException e){
            writeSettings();
        }
	catch(Exception e){
	    e.printStackTrace();
	}
        finally {
            update();
        }
        
        
    }
    
    public void writeSettings() {
        System.out.println("Write Settings");
	Writer writer = null;
	try {
	    writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(filePath), "utf-8"));
	    
            writer.write(SETTINGS_HEADING);
	    writer.write("\t\t" + SETTINGS_GENERAL + "\n");
	    writer.write(SETTINGS_HEADING);
            
            writer.write(generalSettings.getSettingsFileString());
            
            writer.write(SETTINGS_FOOTER);
            
            writer.write(SETTINGS_HEADING);
	    writer.write("\t\t" + SETTINGS_CAN + "\n");
	    writer.write(SETTINGS_HEADING);
            
            writer.write(canSettings.getSettingsFileString());
            
            writer.write(SETTINGS_FOOTER);
           
	    writer.write(SETTINGS_HEADING);
	    writer.write("\t\t" + SETTINGS_BMS + "\n");
	    writer.write(SETTINGS_HEADING);
            writer.write(evmsSettings.getSettingsFileString());
            
            writer.write(SETTINGS_FOOTER);
            
            writer.write("\n\n");
            
	    writer.write(SETTINGS_HEADING);
	    writer.write("\t\t" + SETTINGS_PIXHAWK + "\n");
	    writer.write(SETTINGS_HEADING);
            writer.write(pixHawkSettings.getSettingsFileString());
            
            writer.write(SETTINGS_FOOTER);
            
	    
	    writer.flush();
	    writer.close();
	}
	catch(IOException e) {
            e.printStackTrace();
	}
	finally {
            try {
                writer.close();
            }
            catch(Exception e) {
                System.out.println("ERROR: Failed to close settings file");
                System.out.println("ERROR Message: "+e.getMessage());
            }
	}
    }
}
