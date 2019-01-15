/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eaics.CAN.Charger.TC;

import eaics.CAN.CANFilter;
import eaics.Settings.SettingsEAICS;
import eaics.Settings.SettingsEVMS;
import java.io.IOException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Markcuz
 */
public class TCSend {
    
    private CANFilter filter;

    float chargeVoltage;
    float chargeCurrent;

    private ScheduledExecutorService chargerExecutor;

    private SettingsEVMS settings;

    boolean chargeStatus;

    public TCSend() {

        this.settings = SettingsEAICS.getInstance().getEVMSSettings();

        this.chargeVoltage = settings.getSetting(19);
        this.chargeCurrent = settings.getSetting(20);
        
        filter = CANFilter.getInstance();
    }
    public void runCharger() {
        chargerExecutor = null;
        
        System.out.println("Starting Charger...");
        
        Runnable Id = new Runnable() { 
            
            @Override
            public void run() {
                chargeVoltage = settings.getSetting(19)*10;
                chargeCurrent = settings.getSetting(20)*10;
                
                chargeStatus = true;
                
                try {
                    filter.getCANHandler(0).writeMessage(
                            0x1806E5F4, 
                            new int[]{
                                (int)chargeVoltage/256,
                                (int)chargeVoltage%256,
                                (int)chargeCurrent/256,
                                (int)chargeCurrent%256,
                                0,
                                0,
                                0,
                                0
                            }
                    );
                } catch (IOException ex) {
                    System.out.println("Failed...");
                    //Logger.getLogger(TCCharger.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        };
        chargerExecutor = Executors.newScheduledThreadPool(1);
        chargerExecutor.scheduleAtFixedRate(Id, 0, 500, TimeUnit.MILLISECONDS);
    }
    
    public void stopCharger() {
        try {
            filter.getCANHandler(0).writeMessage(0x1806E5F4, new int[]{
                (int)chargeVoltage/256,
                (int)chargeVoltage%256,
                (int)chargeCurrent/256,
                (int)chargeCurrent%256,
                1,0,0,0});
        } catch (IOException ex) {
            Logger.getLogger(TCCharger.class.getName()).log(Level.SEVERE, null, ex);
        }
        if(chargerExecutor!=null) {
            chargerExecutor.shutdown();
        }
    }
}
