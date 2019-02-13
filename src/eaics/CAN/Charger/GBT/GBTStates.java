/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eaics.CAN.Charger.GBT;

/**
 *
 * @author Markcuz
 */
public enum GBTStates {
    PreHandshake(1), 
    Handshake(2), 
    ;
        
    private final int value;

    /**
     * @param text
     */
    GBTStates(final int state) {
        this.value = state;
    }
    
    public int getValue() {
        return value;
    }
}
