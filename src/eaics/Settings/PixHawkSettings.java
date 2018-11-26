/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eaics.Settings;

/**
 *
 * @author troy
 */
public class PixHawkSettings 
{
    private String ipAddress;
    
    public PixHawkSettings()
    {
        this.ipAddress = "192.168.201.113"; //default ip address
    }
    
    public String getIpAddress()
    {
        return ipAddress;
    }
    
    public String getSettingsFileString()
    {
        String settingsFileString = "";
        
	settingsFileString += "IP Address:" + "\t\t\t" + "#";
        settingsFileString += this.ipAddress + "\n";
        
        return settingsFileString;
    }
    
    public void setSettings(String fileString)
    {
        int ii = 0;
        String[] lines = fileString.split("\\r?\\n");
        for (String line : lines) 
        {
            this.ipAddress = line;
            ii++;
        }        
    }
}
