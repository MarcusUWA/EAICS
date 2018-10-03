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
public class ESC 
{
    private int escID;
    
    private double batteryVoltage;
    private double batteryCurrent;
    private int rpm;
    private int odometer;
    private int controllerTemp;
    private int motorTemp;
    private int battTemp;
    private double requestedOutputPWM;
    private double realOutputPWM;
    private int warnings;
    private int failures;
    private int remainingBatteryCapacity;
    private int throttleCommand;
    
    public ESC(int escID)
    {
		this.escID = escID;
		
		//Packet 1 - 7 data bytes
		this.batteryVoltage = 0.0;
		this.batteryCurrent = 0.0;
		this.rpm = 0;
		//Packet 2 - 7 data bytes
		this.odometer = 0;
		this.controllerTemp = 0;
		this.motorTemp = 0;
		this.battTemp = 0;
		//Packet 3 - 8 data bytes
		this.requestedOutputPWM = 0.0;
		this.realOutputPWM = 0.0;
		this.warnings = 0;
		this.failures = 0;
		//Packet 4 - 8 bytes
		this.remainingBatteryCapacity = 0;
		//Incoming Packet
		this.throttleCommand = 0;
    }
    
    public void setAll(CANMessage message)
    {
		switch(message.getFrameID() - (346095617 + 4 * this.escID))
		{
			case 0:
				this.batteryVoltage = (message.getByte(0) + 256*message.getByte(1))/57.45;
				this.batteryCurrent = (message.getByte(2) + 256*message.getByte(3))/10.0;
				this.rpm = (message.getByte(4) + 256*message.getByte(5) + 65536*message.getByte(6))*10;
				break;
			case 1:
				this.odometer = (message.getByte(0) + 256*message.getByte(1) + 65536*message.getByte(2) + 16777216*message.getByte(3));
				this.controllerTemp = message.getByte(4);
				this.motorTemp = message.getByte(5);
				this.battTemp = message.getByte(6);
				break;
			case 3:
				this.requestedOutputPWM = (message.getByte(0) + 256*message.getByte(1))/10.0;
				this.realOutputPWM = (message.getByte(2) + 256*message.getByte(3))/10.0;
				this.warnings = (message.getByte(4) + 256*message.getByte(5));
				this.failures = (message.getByte(6) + 256*message.getByte(7));
				break;
			case 4:
				this.remainingBatteryCapacity = message.getByte(0) + 256*message.getByte(1);
				break;
		}		
    }
    
    public double getBatteryVoltage()
    {
		return this.batteryVoltage;
    }
    
    public double getBatteryCurrent()
    {
		return this.batteryCurrent;
    }
    
    public int getRpm()
    {
		return this.rpm;
    }
    
    public int getOdometer()
    {
		return this.odometer;
    }
    
    public int getControllerTemp()
    {
		return this.controllerTemp;
    }
    
    public int getMotorTemp()
    {
		return this.motorTemp;
    }
    
    public int getBattTemp()
    {
		return this.battTemp;
    }
    
    public double getRequestedOuputPWM()
    {
		return this.requestedOutputPWM;
    }
    
    public double getRealOutputPWM()
    {
		return this.realOutputPWM;
    }
    
    public int getWarnings()
    {
		return this.warnings;
    }
    
    public int getFailures()
    {
		return this.failures;
    }
    
    public int getRemainingBatteryCapacity()
    {
		return this.remainingBatteryCapacity;
    }
    
    public void setThrottleCommand(int throttleCommand)
    {
		this.throttleCommand = throttleCommand;
    }
    
    public int getThrottleCommand()
    {
		return this.throttleCommand;
    }
    
    public String getLoggingString()
    {
		String outString = "";
		
		outString += batteryVoltage + ", ";
		outString += batteryCurrent + ", ";
		outString += rpm + ", ";
		outString += odometer + ", ";
		outString += controllerTemp + ", ";
		outString += motorTemp + ", ";
		outString += battTemp + ", ";
		outString += requestedOutputPWM + ", ";
		outString += realOutputPWM + ", ";
		outString += warnings + ", ";
		outString += failures + ", ";
		outString += remainingBatteryCapacity + ", ";
		outString += throttleCommand + ", ";
		
		return outString;
    }
}
