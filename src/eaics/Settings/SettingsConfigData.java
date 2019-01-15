/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eaics.Settings;

/**
 *
 * @author Troy
 */
public class SettingsConfigData 
{
    private int min;
    private int max;
    private int setting;
    private String units;
    
    public SettingsConfigData(int min, int max, int initialSetting, String units)
    {
        this.min = min;
        this.max = max;
        this.setting = initialSetting;
	this.units = units;
    }
    
    
    public int getSetting()
    {
        return this.setting;
    }
    
    public void setSetting(int setting)
    {
	if(setting <= max && setting >= min)
        {
            this.setting = setting;
        }
    }
    
    public String getDisplayUnits()
    {
        return this.units;
    }
    
    public int getMax()
    {
	return this.max;
    }
    
    public int getMin()
    {
	return this.min;
    }
}
