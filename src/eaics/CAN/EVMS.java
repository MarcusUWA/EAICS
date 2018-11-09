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
	private double voltage;
	private double auxVoltage;	//Auxiliary voltage (tenths of a volt)
	private int leakage;	//Insulation integrity/leakage (0-100%)
	private int temp;	//degrees C
        
        private int error;
        
        private int status;
	
	public EVMS()
	{
		this.voltage = 0;
		this.auxVoltage = 0;
		this.leakage = 0;
		this.temp = 0;
	}
	
	public abstract void setAll(CANMessage message);
	
	public static String getLoggingHeadings()
	{
	    return "Auxillary Voltage, Leakage, EVMS Temperature, EVMS Voltage, ";
	}
	
	public String getLoggingString()
	{
	    String outString = "";
	    
	    outString += auxVoltage + ", " + leakage + ", " + temp + ", " + voltage + ", ";
	    
	    return outString;
	}
	
	public double getVoltage()
	{
		return voltage;
	}
	
	public void setVoltage(double voltage)
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
        
        public void setStatus(int status) {
            this.status = status;
        }
        
        public int getStatus() {
            return status;
        }
        
        public void setError(int error) {
            this.error = error;
        }
        
        public int getError() {
            return error;
        }
 	
	@Override
	public String toString()
	{
		return " Volt: " + voltage + " Aux Volt: " + auxVoltage + " Leakage: " + leakage + " Temp: " + temp;
	}
}