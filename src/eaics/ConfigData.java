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
    
    public ConfigData(String unit, int min, int max)
    {
        this.unit = unit;
        this.min = min;
        this.max = max;
        this. setting = 0;
    }
    
    public int getSetting()
    {
        return this.setting;
    }
    
    public void setSetting(int setting)
    {
        this.setting = setting;
    }
    
    public String getUnit()
    {
        return this.unit;
    }
}
