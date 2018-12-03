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
public class Throttle {
    private int throttle;
    
    public Throttle() {
        throttle = 0;
    }

    public void setMsg(String msg) {
        String[] msgArray = msg.split(",");

        if(msgArray.length > 3) {
            try {
                this.throttle =  Integer.parseInt(msgArray[3]);
            }
            catch (NumberFormatException e) {
                this.throttle = 0;
            }
        }
    }
    
    public int getThrottle() {
        return throttle;
    }
    
    @Override
    public String toString() {
        return "" + throttle;
    }
}
