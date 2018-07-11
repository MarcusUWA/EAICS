/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eaics.CAN;

/**
 *
 * @author Troy
 */
public abstract class EVMS 
{
	private int voltage;
	private double auxVoltage;	//Auxiliary voltage (tenths of a volt)
	private int leakage;	//Insulation integrity/leakage (0-100%)
	private int temp;	//degrees C
	
	public EVMS()
	{
		this.voltage = 0;
		this.auxVoltage = 0;
		this.leakage = 0;
		this.temp = 0;
	}
	
	public abstract void setAll(CANMessage message);
	
	public int getVoltage()
	{
		return voltage;
	}
	
	public void setVoltage(int voltage)
	{
		this.voltage = voltage;
	}
	
	public double getAuxVoltage()
	{
		return auxVoltage;
	}
	
	public void setAuxVoltage(double auxVoltage)
	{
		this.auxVoltage = auxVoltage;
	}
	
	public int getLeakage()
	{
		return leakage;
	}
	
	public void setLeakage(int leakage)
	{
		this.leakage = leakage;
	}
	
	public int getTemp()
	{
		return temp;
	}
	
	public void setTemp(int temp)
	{
		this.temp = temp;
	}
	
	@Override
	public String toString()
	{
		return " Volt: " + voltage + " Aux Volt: " + auxVoltage + " Leakage: " + leakage + " Temp: " + temp;
	}
}