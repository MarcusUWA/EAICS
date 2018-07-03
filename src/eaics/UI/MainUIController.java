/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eaics.UI;

import eaics.CAN.BMS;
import eaics.CAN.CANFilter;
import eaics.CAN.ESC;
import eaics.CAN.EVMS;
import eaics.SER.LoadCell;
import java.net.URL;
import javafx.util.Duration;
import java.util.ResourceBundle;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;

/**
 * FXML Controller class
 */
public class MainUIController implements Initializable 
{    
    
    //refresh rate in ms
    int refreshFrequency = 10;
    
    CANFilter filter;
    LoadCell loadCell;
    
    @FXML
    private Label tachoLabel;
    @FXML
    private Label socLabel;
    @FXML
    private Label powerLabel;
    
    @FXML
    private Label voltsLabel;
    @FXML
    private Label ampsLabel;
    @FXML
    private Label lowCellLabel;
    @FXML
    private Label highCellLabel;
    
    @FXML
    private Label battTempLabel; 
    
    @FXML
    private Label leftRPMLabel;
    @FXML
    private Label rightRPMLabel;
    @FXML
    private Label topRPMLabel;
    @FXML
    private Label bottomRPMLabel;
    
    @FXML
    private Label motorTempLabel;
    
    @FXML
    private Label controllerTempLabel;
    
    @FXML
    private Label capacityLabel;
    @FXML
    private Label timeLabel;
    @FXML
    private Label thrustLabel; 
    
    @FXML
    private Label auxVoltageLabel; 
    
    
    
    @FXML
    private void handleSettingsPressed(ActionEvent event) 
    {
        System.out.println("You clicked me! - Settings");
        //FXMLLoader loader = new FXMLLoader(getClass().getResource("FXMLSettings.fxml"));
    }
    
    @FXML
    private void handleBatteryPressed(ActionEvent event) 
    {
        System.out.println("You clicked me! - Battery");
        //FXMLLoader loader = new FXMLLoader(getClass().getResource("FXMLBattery.fxml"));
    }
    
    
    @FXML
    private void handleTelemetryPressed(ActionEvent event) 
    {
        System.out.println("You clicked me! - Telemetry");
        //FXMLLoader loader = new FXMLLoader(getClass().getResource("FXMLTelemetry.fxml"));
    }
    
    
    public void initData(CANFilter fil, LoadCell cell) 
    {
        this.filter = fil;
        this.loadCell = cell;
    }
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) 
    {
        // TODO
       
        Timeline refreshUI;
        refreshUI = new Timeline(new KeyFrame(Duration.millis(refreshFrequency), new EventHandler<ActionEvent>() 
	{
            int count = 0;
            @Override
            public void handle(ActionEvent event) 
	    {
                EVMS evms = filter.getEVMS();
		ESC esc = filter.getESC();
		BMS bms = filter.getBMS();
                
                //+------------------------------------------------------------+
		//EVMS - Electric Vehicle Management System
		//+------------------------------------------------------------+
		
		socLabel.setText("!" + evms.getCharge() + "%");
		
		voltsLabel.setText("!" + evms.getVoltage());
		
		ampsLabel.setText("!" + evms.getCurrent());
		
		auxVoltageLabel.setText("!" + evms.getAuxVoltage());
		
		//Leakage???
		
		battTempLabel.setText("!" + evms.getTemp());
		
		//Calculation variable
		powerLabel.setText("!" + (evms.getVoltage() * evms.getCurrent() / 1000));
                
		
		//+------------------------------------------------------------+
		//ESC - Electronic Speed Controller
		//+------------------------------------------------------------+
		
		//Voltage???
		
		//Current???
		
		tachoLabel.setText("!" + esc.getRpm());
		
		//Odometer???
		
		controllerTempLabel.setText("!" + esc.getControllerTemp());
		
		motorTempLabel.setText("!" + esc.getMotorTemp());
		
		//Battery Temperature???
		
		
		//+------------------------------------------------------------+
		//LC - Load Cell
		//+------------------------------------------------------------+
		
		thrustLabel.setText("" + loadCell.getWeight());

		
		//+------------------------------------------------------------+
		//BMS - Battery Managment System
		//+------------------------------------------------------------+
		
		//Get low Cell Voltage
                lowCellLabel.setText("!" + bms.getMinVoltage());
		
                //Get high Cell Voltage
                highCellLabel.setText("!"+ bms.getMaxVoltage());
		
		
		//+------------------------------------------------------------+
            }
            
        }));
        refreshUI.setCycleCount(Timeline.INDEFINITE);
        refreshUI.play();
    }    

}
