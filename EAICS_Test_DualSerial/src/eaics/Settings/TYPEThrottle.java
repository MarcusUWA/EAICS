/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eaics.Settings;

/**
 *
 * @author Markcuz
 */
public enum TYPEThrottle {
    SER("Serial"), 
    CAN("CANBus"), 
    None("None")
    ;
    
    private final String text;

    /**
     * @param text
     */
    TYPEThrottle(final String text) {
        this.text = text;
    }

    /* (non-Javadoc)
     * @see java.lang.Enum#toString()
     */
    @Override
    public String toString() {
        return text;
    }
}
