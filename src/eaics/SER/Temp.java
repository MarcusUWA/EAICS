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
        
    }

    public double[] getTempSensors() {
        return tempSensors;
    }
    
}
