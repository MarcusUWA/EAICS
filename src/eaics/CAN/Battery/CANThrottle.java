/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eaics.CAN.Battery;

import THROTTLE.Throttle;
import eaics.CAN.CANFilter;
import eaics.CAN.MiscCAN.CANMessage;

/**
 *
 * @author Markcuz
 */
public class CANThrottle extends Throttle{
    
    final int DAQNum;
    final int DAQInput;
    
    int lowerThresh;
    int upperThresh;
    
    int rawValue;

    public CANThrottle(CANFilter filter, int daqNum, int inputNum, int lowerThresh, int upperThresh) {
        super(filter);
        this.DAQNum = daqNum;
        this.DAQInput = inputNum;
        
        this.lowerThresh = lowerThresh;
        this.upperThresh = upperThresh;
    }
    
    public void setAll(CANMessage message) {
        if(isUsingManualThrottle) {
            if(message.getByte(0)==DAQNum) {
                rawValue = message.getByte(DAQInput);
                throttleSetting = (rawValue-lowerThresh)*1024/(upperThresh-lowerThresh);

                if(throttleSetting<0) {
                    throttleSetting = 0;
                }
                else if(throttleSetting>1024) {
                    throttleSetting=1024;
                }
            }
        }
        //System.out.println("Raw Value: "+rawValue);
    }

    public int getRawValue() {
        return rawValue;
    }
    
    
}
