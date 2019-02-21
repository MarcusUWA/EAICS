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
public enum TYPEVehicle {
    TRIFAN("XTITrifan"), 
    TRIKE_Prototype("ABM4-Y1"), 
    WAVEFLYER("ElectroNauticWaveFlyer"),
    VERTICAL_TESTRIG("EA VerticalTestRig"),
    TESTING("EATesting")
    ;
    
    private final String text;

    /**
     * @param text
     */
    TYPEVehicle(final String text) {
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
