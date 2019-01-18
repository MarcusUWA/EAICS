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
import javafx.util.Duration;
/**
 * FXML Controller class
 * 
 * @author Alex
 */
public class FXMLChargingController implements Initializable {
    // Used to initiate 
    private CANFilter filter;
    
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
    Button BackButton; // Used to navigate back
    @FXML
    Button SetChargeProtocol; // Confirms the charge protocol to be set
    @FXML
    Button SetUserChargeParameters; // Sets user specified current and voltage
    @FXML
    Button StartStop; // Used to start and stop the charging process
    
    @FXML
    ChoiceBox ChargeSelection; // Allows the charging protocol to be selected
    
    @Override // Note: Forced function on bootup (don't need it because variables might not have been initialised yet)
    public void initialize(URL url, ResourceBundle rb) 
    {
        // TODO
    }    
    
    public void InitialiseChargingScene() // Used to initialise the scene
    { // TODO i made a huge assumption and thought that the user would only come to this page on start up.
        // Used to initialise the choice box
        SettingsEAICS settings = SettingsEAICS.getInstance();
        // Set the values of the charge selection
        String tempArray[];
        int indexOfType = 2;
        tempArray = new String[settings.getGeneralSettings().getChargerEnumList().length]; // Get the number of elements in the enum
        for(int i = 0; i < tempArray.length; i++) 
        { // Loop through and convert each enum element to a string and stick it into the tempArray
            tempArray[i] = settings.getGeneralSettings().getChargerEnumList()[i].toString();
            if (tempArray[i] == settings.getGeneralSettings().getChargerType().toString())
            {
                indexOfType = i;
                System.out.println("FOund match at index "+i);
            }
            //tempArray[i] = Arrays.toString(settings.getGeneralSettings().getChargerEnumList());
        }
        ChargeSelection.getItems().setAll(tempArray); // settings.getGeneralSettings().getChargerType());
        
        // Pull what the SettingsGeneral has the charging protocol at 
        ChargeSelection.getSelectionModel().select(indexOfType);
        
        // Set the ChargeSelection ChoiceBox to be grey when not selected and green when selected
        
        // Ensure the Start/Stop button is matching the charging state
        if (settings.getGeneralSettings().isCharging == true) // It is charging
        {
            StartStop.setText("Stop");
            StartStop.setStyle("-fx-background-color: #FF0000; ");   
        } else // It is idle
        {
            StartStop.setText("Start");
            StartStop.setStyle("-fx-background-color: #33CC00; ");
        }
        
        Timeline refreshUI;
        refreshUI = new Timeline(new KeyFrame(Duration.millis(timeToRefresh), new EventHandler<ActionEvent>()
	{
            @Override
            public void handle(ActionEvent event) 
	    {
		UpdateScreen();
            }
        }));
        refreshUI.setCycleCount(Timeline.INDEFINITE);
        refreshUI.play();
    }
    
    public void UpdateScreen() // Updates the page with new values and checks if charging is still occuring
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
        System.out.print("Close button pressed on Charging Page, close page");
        Stage stage = (Stage) BackButton.getScene().getWindow();
        stage.close();
    }
    @FXML // Used to set the charge protocol that is in the 
    private void setChargeProtocol(ActionEvent event)
    {
        System.out.print("User has clicked button to select charging protocol, they have selected |" + ChargeSelection.getValue() + "|");
        ChargeSelection.setStyle("-fx-background-color: #33CC00; ");
        // Get the selected value in the ChargeSelection ChoiceBox
        //ChargeSelection.getValue();  
    }
    @FXML // Used to start or stop the charge
    private void startStopCharging(ActionEvent event)
    {
        SettingsEAICS settings = SettingsEAICS.getInstance();
        
        //ChargeSelection.getStyle()
        if (settings.getGeneralSettings().isCharging == true) // It is charging
        {
            System.out.print("Start and stop button pressed and the system was charging");
            // Stop charging, determine which protocol is being used and then stop charging
            
            switch(settings.getGeneralSettings().getChargerType()) 
            {
                case None:
                    System.out.println("System in None charger type state, no way to stop as it is handled by the BMS");
                    // TODO, how do I stop this if it is not controlled by the PI
                    break;
                case GBT:
                    System.out.println("System in GBT charger type state");
                    
                    break;
                case TC: 
                    System.out.println("System in TC charger type state");

                default:
                    break;
            } 
            
            settings.getGeneralSettings().isCharging = false;
            // Stop charging, change text to Start and change colour to green
            StartStop.setText("Start");
            StartStop.setStyle("-fx-background-color: #33CC00; ");
        } else // It is not charging
        {
            System.out.println("User has selected Stop/Start button when it is not charging");
            settings.getGeneralSettings().isCharging = true;
            
            // Check to see if the user has selected a protocol TODO CHECK THIS WORKS
            if (ChargeSelection.getStyle().contains("33CC00") == true)
            { // It has been set
                // Determine what protocol the user wants to select
                if (ChargeSelection.getSelectionModel().getSelectedItem() == "None")
                {
                    System.out.println("User selected None charger type state");
                    settings.getGeneralSettings().setChargerType(TYPECharger.None);
                    // do nothing the pi is not involved

                } else if (ChargeSelection.getSelectionModel().getSelectedItem() == "GBT")
                {
                    System.out.println("User selected GBT charger type state");
                    settings.getGeneralSettings().setChargerType(TYPECharger.GBT);
                    

                } else if (ChargeSelection.getSelectionModel().getSelectedItem() == "TC")
                {
                    System.out.println("User selected TC charger type state");
                    settings.getGeneralSettings().setChargerType(TYPECharger.TC);

                } else 
                {
                    System.out.println("ERROR: unknown user selection");
                }
                
                // Start charging, change text to Stop and change colour to red
                StartStop.setText("Stop");
                StartStop.setStyle("-fx-background-color: #FF0000; ");
                
            } else
            { // It has not been set, TODO MIGHT BE WORTH CHANGING Stop/Start BUTTON
                    System.out.println("User tried to start charging but did not select a protocol");
            }
        }
    }
}
