/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eaics.UI;

import eaics.UI.FXMLSettings.FXMLLoggingPageController;
import eaics.UI.Trike.FXMLLoadProfileController;
import eaics.CAN.CANFilter;
import eaics.LOGGING.Logging;
import eaics.SER.LoadCell;
import eaics.SER.Serial;
import eaics.SER.Throttle;
import eaics.Settings.IPAddress;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.Initializable;

/**
 *
 * @author troyg
 */
public abstract class MainUIController implements Initializable
{
    public static final int refreshFrequency = 50;    //refresh rate in ms
    public static final int timeout = 2000;
    
    protected boolean manualOverride = false;
    
    protected CANFilter filter;
    protected LoadCell loadCell;
    protected Serial serial;
    protected Throttle throttle;
    
    protected FXMLBatteryPageController batteryPageController;
    public FXMLSettingsController settingsPageController;
    protected FXMLLoadProfileController loadProfileController;
    protected FXMLLoggingPageController loggingPageController;
    
    protected int status = 0;
    protected IPAddress ip;
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) 
    {
        // TODO
        ip = new IPAddress();
    }
    
    public abstract void refreshIP() throws IOException;
    
    public abstract void initData(Logging logging) throws IOException ;
}