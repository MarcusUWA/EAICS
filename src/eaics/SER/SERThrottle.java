/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eaics.SER;

import THROTTLE.Throttle;
import eaics.CAN.CANFilter;

public class SERThrottle extends Throttle{

    public SERThrottle(CANFilter filter) {
        super(filter);
    }
    
    public void setMsg(String msg) {
        if(this.isUsingManualThrottle) {
            String[] msgArray = msg.split(",");

            if(msgArray.length == 17)  {
                try  {
                    this.throttleSetting =  Integer.parseInt(msgArray[3]);
                }
                catch (NumberFormatException e)  {
                    this.throttleSetting = 0;
                }
            }
        }
    }
    
    @Override
    public String toString() 
    {
        return "" + throttleSetting;
    }

}
