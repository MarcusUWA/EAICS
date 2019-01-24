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
    }
    
    public void setMsg(String msg) {
        
        String[] msgArray = msg.split(",");
            
            if(msgArray.length == 7) {
                for(int i =0; i<6; i++) {
                    tempSensors[i]=Double.parseDouble(msgArray[i+1]);
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
