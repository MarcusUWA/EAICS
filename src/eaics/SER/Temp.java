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
public class Temp {

    int numSensors;
    double tempSensors[];
    
    public Temp(int number) {
        numSensors = number;
        tempSensors = new double[number];
        
        for(int i = 0; i<number; i++) {
            tempSensors[i] = 0;
        }
    }
    
    public void setMsg(String msg) {
            String[] msgArray = msg.split(",");
            
            if(msgArray.length == 16) {
                for(int i = 0; i<numSensors; i++) {
                    try {
                        tempSensors[i] = Double.parseDouble(msgArray[10+i]);
                    }
                    catch (NumberFormatException e) {
                        tempSensors[i] = 0;
                    }
                }
            }
            
            else {
                System.out.println("Invalid Serial Length: "+msgArray.length);
            }
    }

    public double[] getTempSensors() {
        return tempSensors;
    }
    
}
