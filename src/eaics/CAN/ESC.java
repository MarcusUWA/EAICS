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
    private int voltage;
    private int current;
    private int rpm;
    private int odometer;
    private int controllerTemp;
    private int motorTemp;
    private int battTemp;
    
    public ESC()
    {
        this.voltage = 0;
        this.current = 0;
        this.rpm = 0;
        this.odometer = 0;
        this.controllerTemp = 0;
        this.motorTemp = 0;
        this.battTemp = 0;
    }
    
    public void setAll(CANMessage message)
    {
        this.voltage = (message.getByte(0) + 256*message.getByte(1))/5745; //This is concerning :/ 57,45?? =5745 or 57450?
        this.current = (message.getByte(2) + 256*message.getByte(3))/10;
        this.rpm = (message.getByte(4) + 256*message.getByte(5) + 65536*message.getByte(6))*10;
        this.odometer = (message.getByte(0) + 256*message.getByte(1) + 65536*message.getByte(2) + 16777216*message.getByte(3));
        this.controllerTemp = message.getByte(4);
        this.motorTemp = message.getByte(5);
        this.battTemp = message.getByte(6);
    }
    
    @Override
    public String toString()
    {
	return "Volt: " + voltage + "Cur: " + current + "RPM: " + rpm + "O: " + odometer + "CT: " + controllerTemp + "MT: " + motorTemp + "BT: " + battTemp;
    }
    
    public int getVoltage()
    {
	return this.voltage;
    }
    
    public int getCurrent()
    {
	return this.current;
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
    
}
