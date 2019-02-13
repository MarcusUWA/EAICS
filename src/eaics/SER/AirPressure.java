/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eaics.SER;

/**
 *
 * @author Markcuz
 */
public class AirPressure {

    double sensorValue;
    
    public AirPressure() {
        sensorValue = 0;
    }
    
    public void setMsg(String msg) {
        String[] msgArray = msg.split(",");
            
        if(msgArray.length == 17) {
            try {
                sensorValue = Double.parseDouble(msgArray[16]);
            }
            catch (NumberFormatException e) {
                sensorValue = 0;
            }
        }

        else {
            System.out.println("Invalid Serial Length: "+msgArray.length);
        }
    }

    public double getSensorValue() {
        return sensorValue;
    }
    
    
}
