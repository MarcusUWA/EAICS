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
public enum SERType {
    THROTTLE("Throttle"), 
    TEMP("Temp"), 
    UNDEF("Undefined"), 
    ;
        
    private final String text;

    /**
     * @param text
     */
    SERType(final String text) {
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
