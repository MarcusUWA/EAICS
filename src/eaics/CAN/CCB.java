/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eaics.CAN;

/**
 *
 * @author Markcuz
 */
public class CCB {
    
    private boolean[] ccb;

    public CCB(int size) {
        ccb = new boolean[size];
    }
    
    public void setAll(int ID, CANMessage message){
        switch (ID) {
            case 81:
                ccb[0] = ((message.getByte(0)&0x01)==1);
                break;
            case 83:
                ccb[1] = ((message.getByte(0)&0x01)==1);
                break;
            case 85:
                ccb[2] = ((message.getByte(0)&0x01)==1);
                break;
            default:
                break;
        } 
    }
    public boolean getCCB1() {
        return ccb[0];
    }
    
    public boolean getCCB2() {
        return ccb[1];
    }
    
    public boolean getCCB3() {
        return ccb[2];
    }
}
