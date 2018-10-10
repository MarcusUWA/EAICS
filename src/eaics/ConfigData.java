/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eaics;

/**
 *
 * @author Troy
 */
public class ConfigData 
{
    private String unit;
    private int min;
    private int max;
    private int setting;
    private String displayUnits;
    private int multiplier;
    
    public ConfigData(String unit, int min, int max, int initialSetting, String displayUnits, int multiplier)
    {
        this.unit = unit;
        this.min = min;
        this.max = max;
        this.setting = initialSetting;
	this.displayUnits = displayUnits;
	this.multiplier = multiplier;
    }
    
    public int getMultiplier()
    {
        return this.multiplier;
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
        return this.displayUnits;
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
