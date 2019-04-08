/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eaics.SER;

/**
 * A class to grab info from the GPS.
 * @author lunarcapital
 */
public class SERGps {
    
    private float speed; //in knots
    
    /**
     * Passes in a string read from the serial class.
     * Will be parsed by this class as GPS data if it valid.
     * @param msg a string containing readable serial data
     */
    public void setMsg(String msg) {
        String[] msgArray = msg.split(",");
        
        if (msgArray.length == 13 && msgArray[0].equals("$GPRMC")) { //ignore non-GPS speed messages.
            try {
                this.speed = Float.parseFloat(msgArray[7]);
            } catch (NumberFormatException e) {
                this.speed = 0;
            }
        }
    }
    
    public float getSpeed() {
        return speed;
    }
    
}
