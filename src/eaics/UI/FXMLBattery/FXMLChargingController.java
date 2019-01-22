/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
// This is myyyy swamp
// TODO...
// 1) HOW TO RUN CHARGER SO THAT IT CONTINUES EVEN AFTER USER NAVIGATES AWAY FROM CHARGING PAGE
// 2) HOW TO UPDATE VALUES EVERY X SECONDS OR WITH UPDATE FUNCTION IN CHARGER GBT/TC
package eaics.UI.FXMLBattery;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.Initializable;
import javafx.fxml.FXML; // Allows the injection of FXML code, e.g. the @FXML
import javafx.stage.Stage; // Allows a change of screen

import eaics.CAN.CANFilter; // Access variables sent via CAN
import eaics.Settings.SettingsEAICS; // Access charging typesa and other settings
import eaics.Settings.TYPECharger;
import javafx.animation.KeyFrame; // Used to control the screen updating
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;

import javafx.scene.control.Button; // Used to control button behaviour
import javafx.scene.control.Label; // Used to control label behaviour
import javafx.scene.control.ChoiceBox; // Used to control the choice box for charge protocol selection
import javafx.scene.control.ToggleButton;
import javafx.util.Duration;
/**
 * FXML Controller class
 * 
 * @author Alex
 */
public class FXMLChargingController implements Initializable {
    // Used to initiate 
    private CANFilter filter;
    
    SettingsEAICS settings;
    
    private int timeToRefresh = 5000; // Time to refresh the screen
    
    // Initialise the Labels and buttons on the GUI
    @FXML // 
    private Label ObservedCurrent; // Unit of measurement is amps
    @FXML
    private Label ObservedVoltage; // Unit of measurement is volts
    @FXML
    private Label ObservedPower; // Unit of measurement is kilowatts
    @FXML
    private Label TimeCharging; // Unit of measurement is minutes, how long the vehicle has been charging
    @FXML
    private Label ETACharging; // Unit of measurement is minutes
    @FXML
    private Label ChargerMaxVoltage; // Unit of measurement is volts        
    @FXML
    private Label ChargerMinVoltage; // Unit of measurement is volts        
    @FXML
    private Label ChargerMaxCurrent; // Unit of measurement is amps
    @FXML
    private Label ChargerMinCurrent; // Unit of measurement is amps
    
    @FXML
    private Label chargeCurrent;
    @FXML 
    private Label chargeVoltage;
    
    
    @FXML
    Button BackButton; // Used to navigate back
    @FXML
    Button SetChargeProtocol; // Confirms the charge protocol to be set
    @FXML
    ToggleButton StartStop; // Used to start and stop the charging process
    
    @FXML
    ChoiceBox ChargeSelection; // Allows the charging protocol to be selected
    
    @Override // Note: Forced function on bootup (don't need it because variables might not have been initialised yet)
    public void initialize(URL url, ResourceBundle rb) 
    {
        // TODO
    }    
    
    public void initialiseChargingScene() { 
        settings = SettingsEAICS.getInstance();
        filter = CANFilter.getInstance(); // Get instance of filter and settings
       
        ChargeSelection.getItems().setAll((Object[])TYPECharger.values());
        ChargeSelection.setValue(settings.getGeneralSettings().getChargerType());
        
        Timeline refreshUI;
        refreshUI = new Timeline(new KeyFrame(Duration.millis(timeToRefresh), new EventHandler<ActionEvent>()
	{
            @Override
            public void handle(ActionEvent event) 
	    {
		updateScreen();
            }
        }));
        refreshUI.setCycleCount(Timeline.INDEFINITE);
        refreshUI.play();
    }
    
    public void updateScreen() { 
        if(settings.getGeneralSettings().getChargerType()==TYPECharger.GBT) {
            
            float observedVoltage  = filter.getChargerGBT().getObservedVoltage();
            float observedCurrent = filter.getChargerGBT().getObservedCurrent();
            // Ensure that the return float is converted to string with 1dp
            ChargerMaxVoltage.setText(String.format("%.1f",filter.getChargerGBT().getMaxChargerVoltage()));
            ChargerMinVoltage.setText(String.format("%.1f",filter.getChargerGBT().getMinChargerVoltage()));
            ChargerMaxCurrent.setText(String.format("%.1f",filter.getChargerGBT().getMaxChargerCurrent()));
            ChargerMinCurrent.setText(String.format("%.1f",filter.getChargerGBT().getMinChargerCurrent()));
            ObservedCurrent.setText(String.format("%.1f",filter.getChargerGBT().getObservedCurrent()));
            ObservedVoltage.setText(String.format("%.1f",filter.getChargerGBT().getObservedVoltage()));

            // Power [kW] = current*voltage/1000
            ObservedPower.setText(String.format("%.1f",
                    filter.getChargerGBT().getObservedCurrent()*filter.getChargerGBT().getObservedVoltage()/1000.0));

            // Integer is converted to string
            TimeCharging.setText(""+filter.getChargerGBT().getTimeOnCharge());
        }
        else if(settings.getGeneralSettings().getChargerType()==TYPECharger.TC) {
            ChargerMaxVoltage.setText("N/A");
            ChargerMinVoltage.setText("N/A");
            ChargerMaxCurrent.setText("N/A");
            ChargerMinCurrent.setText("N/A");
            
            ObservedCurrent.setText(String.format("%.1f",filter.getChargerTC().getOutputCurrent()));
            ObservedVoltage.setText(String.format("%.1f",filter.getChargerTC().getOutputVoltage()));

            // Power [kW] = current*voltage/1000
            ObservedPower.setText(String.format("%.1f",
                    filter.getChargerTC().getOutputCurrent()*filter.getChargerTC().getOutputVoltage()/1000.0));

            // Integer is converted to string
            TimeCharging.setText("N/A");
        }
        
        else {
            ChargerMaxVoltage.setText("N/A");
            ChargerMinVoltage.setText("N/A");
            ChargerMaxCurrent.setText("N/A");
            ChargerMinCurrent.setText("N/A");
            
            ObservedCurrent.setText("N/A");
            ObservedVoltage.setText("N/A");

            // Power [kW] = current*voltage/1000
            ObservedPower.setText("N/A");

            // Integer is converted to string
            TimeCharging.setText("N/A");
        }
        
        chargeVoltage.setText(String.format(""+settings.getEVMSSettings().getSetting(19)));
        chargeCurrent.setText(String.format(""+settings.getEVMSSettings().getSetting(20)));
    }
    
    @FXML // Delete the charging screne and go back to the previous scene.
    private void closeButtonAction(ActionEvent event) {
        Stage stage = (Stage) BackButton.getScene().getWindow();
        stage.close();
    }
    
    @FXML // Used to set the charge protocol that is in the 
    private void setChargeProtocol(ActionEvent event) {
        settings.getGeneralSettings().setChargerType((TYPECharger) ChargeSelection.getValue());
    }
    
    @FXML // Used to start or stop the charge
    private void startStopCharging(ActionEvent event) {
        if(StartStop.isSelected()) {
            StartStop.setText("Stop Charging");
            
            if(settings.getGeneralSettings().getChargerType()==TYPECharger.GBT) {
                //Start charger here!!!
            }
            else if(settings.getGeneralSettings().getChargerType()==TYPECharger.TC) {
                filter.getChargerTC().runCharger(1);
            }
            else {
                System.out.println("No Charger Selected");
            }
        }
        else {
            
            StartStop.setText("Start Charging");
            
            if(settings.getGeneralSettings().getChargerType()==TYPECharger.GBT) {
                //Stop charger here!!!
            }
            else if(settings.getGeneralSettings().getChargerType()==TYPECharger.TC) {
                filter.getChargerTC().stopCharger(1);
            }
            else {
                System.out.println("No Charger Selected");
            }
        }

    }
}
