/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eaics.Settings;

import java.io.IOException;

/**
 *
 * @author troy
 */
public class SettingsPixhawk implements Settings
{
    private String ipAddress;
    
    public SettingsPixhawk()
    {
        this.ipAddress = "192.168.201.113"; //default ip address
    }
    
    public String getIpAddress()
    {
        return ipAddress;
    }
    
    public void setIpAddress(String ip)
    {
        ipAddress = ip;
    }
    
    @Override
    public String getSettingsFileString()
    {
        String settingsFileString = "";
        
	settingsFileString += "IP Address:" + "\t\t\t" + "#";
        settingsFileString += this.ipAddress + "\n";
        
        return settingsFileString;
    }
    
    @Override
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
    
    @Override
    public void update() {
	//Nothing to update yet
    }
    
    public void completeUpdatePixhawk(String newIP) throws IOException {
        final Process pixHawkKill = Runtime.getRuntime().exec("pkill mavproxy.py");
        final Process pixHawkProgram = Runtime.getRuntime().exec("sudo mavproxy.py --master=/dev/ttyACM0 --baudrate 57600 --out " + newIP + ":14550 --aircraft MyCopter");
        setIpAddress(newIP);       
    }
}
