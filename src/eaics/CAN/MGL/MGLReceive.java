/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eaics.CAN.MGL;

import eaics.CAN.CANFilter;
import eaics.CAN.MiscCAN.CANMessage;

/**
 *
 * @author Markcuz
 */
public class MGLReceive {
        
    short bankAngle; //resolution: 0.1 deg
    short pitchAngle; //resolution: 0.1 deg
    short yawAngle; //resolution: 0.1 deg
    int gndSpeed; //resolution: knots (mph originally)
    
    public MGLReceive() {
                
        bankAngle = 0; //resolution: 0.1 deg
        pitchAngle = 0; //resolution: 0.1 deg
        yawAngle = 0; //resolution: 0.1 deg
        gndSpeed = 0; //resolution: knots (mph originally)
        
    }
    
    public void processMessage(CANMessage message) {
        switch (message.getFrameID()) {
            case 0x11:
                break;
            case 0x12:
                bankAngle = (short) ((message.getByte(0)+message.getByte(1)*256));
                bankAngle = (short) (bankAngle/10);
                
                pitchAngle = (short) ((message.getByte(2)+message.getByte(3)*256));
                pitchAngle = (short) (pitchAngle/10);
                
                yawAngle = (short) ((message.getByte(4)+message.getByte(5)*256));
                yawAngle = (short) (yawAngle/10);
                
                gndSpeed = message.getByte(6)+message.getByte(7)*256;
                gndSpeed = (int) (gndSpeed*0.868976);
                
                break;
            default:
                break;
        }
        
    }
        public short getBankAngle() {
        return bankAngle;
    }

    public short getPitchAngle() {
        return pitchAngle;
    }

    public short getYawAngle() {
        return yawAngle;
    }

    public int getGndSpeed() {
        return gndSpeed;
    }
    
}
