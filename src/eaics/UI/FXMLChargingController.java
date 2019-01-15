/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
// This is myyyy swamp

package eaics.UI;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.Initializable;
import javafx.fxml.FXML; // Allows the injection of FXML code, e.g. the @FXML
import javafx.stage.Stage; // Allows a change of screen

import eaics.CAN.CANFilter; // Access variables sent via CAN
import eaics.Settings.SettingsEAICS; // Access charging typesa and other settings
import javafx.event.ActionEvent;

import javafx.scene.control.Button; // Used to control button behaviour
import javafx.scene.control.Label; // Used to control label behaviour
import javafx.scene.control.ChoiceBox; // Used to control the choice box for charge protocol selection
/**
 * FXML Controller class
 * 
 * @author Alex
 */
public class FXMLChargingController implements Initializable {
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
    Button BackButton; // Used to navigate back
    @FXML
    ChoiceBox ChargeSelection; // ALlows the charging protocol to be selected
    @FXML
    Button SetUserChargeParameters; // Sets user specified current and voltage
    
    @Override // Note: Forced function on bootup (don't need it because variables might not have been initialised yet)
    public void initialize(URL url, ResourceBundle rb) 
    {
        // TODO
    }    
    
    public void InitialiseChargerSelection() // Used to initialise the choice box
    {
        SettingsEAICS settings = SettingsEAICS.getInstance();
        
        // Set the values of the charge selection
        ChargeSelection.getItems().setAll(settings.getGeneralSettings().getChargerType());
        
        // Set the default to last selected charging mode, NOTE: none is always indexed as 0
        ChargeSelection.getSelectionModel().select(0);
    }
    public void UpdateStatistics() // Updates the page with new values
    { 
        CANFilter filter = CANFilter.getInstance(); // Get instance of filter and settings
        SettingsEAICS settings = SettingsEAICS.getInstance();
        
        float chargeVoltage = settings.getEVMSSettings().getSetting(19);
        float chargeCurrent = settings.getEVMSSettings().getSetting(20);
                
        float observedVoltage  = filter.getCharger().getObservedVoltage();
        float observedCurrent = filter.getCharger().getObservedCurrent();

        // Ensure that the return float is converted to string with 1dp
        ChargerMaxVoltage.setText(String.format("%.1f",filter.getCharger().getMaxChargerVoltage()));
        ChargerMinVoltage.setText(String.format("%.1f",filter.getCharger().getMinChargerVoltage()));
        ChargerMaxCurrent.setText(String.format("%.1f",filter.getCharger().getMaxChargerCurrent()));
        ChargerMinCurrent.setText(String.format("%.1f",filter.getCharger().getMinChargerCurrent()));
        ObservedCurrent.setText(String.format("%.1f",filter.getCharger().getObservedCurrent()));
        ObservedVoltage.setText(String.format("%.1f",filter.getCharger().getObservedVoltage()));
        // Power [kW] = current*voltage/1000
        ObservedPower.setText(String.format("%.1f",
                filter.getCharger().getObservedCurrent()*filter.getCharger().getObservedVoltage()/1000.0));
        // Integer is converted to string
        TimeCharging.setText(String.format("d",filter.getCharger().getTimeOnCharge()));
        ETACharging.setText("something");
    }
    @FXML // Delete the charging screne and go back to the previous scene.
    private void closeButtonAction(ActionEvent event)
    {
        Stage stage = (Stage) BackButton.getScene().getWindow();
        stage.close();
    }
    
}
