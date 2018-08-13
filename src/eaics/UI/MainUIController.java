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
import eaics.IPaddress;
import eaics.SER.LoadCell;
import java.io.IOException;
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
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 * FXML Controller class
 */
public class MainUIController implements Initializable 
{    
    FXMLBatteryPageController batterys;
    //FXMLBatteryCellPage1Controller batterys;
    FXMLSettingsController settings;
    
    @FXML
    Button buttonSettings;
    
    //refresh rate in ms
    int refreshFrequency = 10;
    
    private CANFilter filter;
    private LoadCell loadCell;
    
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
    private void handleSettingsPressed(ActionEvent event) throws IOException
    {
        System.out.println("You clicked me! - Settings");        
        
        FXMLLoader loader = new FXMLLoader(getClass().getResource("FXMLSettings.fxml"));
        
        try 
	{
            Pane pane = loader.load();

            settings = loader.getController();
            settings.initSettings(this);
            settings.initData(filter, loadCell);
        
            Stage stage = new Stage();
        
            stage.initModality(Modality.WINDOW_MODAL);
            stage.initOwner(buttonSettings.getScene().getWindow());
        
            Scene scene = new Scene(pane);
        
            stage.setScene(scene);
            stage.setTitle("Settings!!");
            
            stage.setMaximized(true);
            stage.show();
        }
        catch (Exception e) 
        {
            System.out.println("Failed to open Settings Window");
        }
    }
    
    @FXML
    private void handleBatteryPressed(ActionEvent event) 
    {
        System.out.println("You clicked me! - Battery");
        
	FXMLLoader loader = new FXMLLoader(getClass().getResource("FXMLBatteryPage.fxml"));
        
        try 
	{
            Pane pane = loader.load();

            batterys = loader.getController();
            batterys.initSettings(this);
	    batterys.initData(filter, loadCell);
   
            Stage stage = new Stage();
 
            stage.initModality(Modality.WINDOW_MODAL);
            stage.initOwner(buttonSettings.getScene().getWindow());

            Scene scene = new Scene(pane);
  
            stage.setScene(scene);
            stage.setTitle("Battery!!");
            
            stage.setMaximized(true);
            stage.show();
        }
        catch (Exception e) 
        {
            System.out.println("Failed to open Battery Window");
	    e.printStackTrace();
        }
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
                EVMS evmsV3 = filter.getEVMS_v3();
		ESC[] esc = filter.getESC();
		BMS[] bms = filter.getBMS();
                
                //+------------------------------------------------------------+
		//EVMS - Electric Vehicle Management System
		//+------------------------------------------------------------+
		
		//socLabel.setText("!" + evmsV3.getCharge() + "%"); // evmsV2 only
		socLabel.setText("FIX");
			
		voltsLabel.setText("" + evmsV3.getVoltage());
		
		//ampsLabel.setText("!" + evmsV3.getCurrent());	    // evmsV2 only
		ampsLabel.setText("");
		
		auxVoltageLabel.setText("" + evmsV3.getAuxVoltage());
		
		//Leakage???
		
		battTempLabel.setText("" + evmsV3.getTemp());
		
		//Calculation variable
		//powerLabel.setText("!" + (evmsV3.getVoltage() * evmsV3.getCurrent() / 1000));	//evmsV2 only
		powerLabel.setText("FIX");
                
		
		//+------------------------------------------------------------+
		//ESC - Electronic Speed Controller
		//+------------------------------------------------------------+
		
		topRPMLabel.setText("" + esc[0].getRpm());
		
		ampsLabel.setText("" + esc[0].getBatteryCurrent());
                
                voltsLabel.setText("" + esc[0].getBatteryVoltage());
                
                capacityLabel.setText("" + esc[0].getRemainingBatteryCapacity());
		
		tachoLabel.setText("" + esc[0].getRpm());
		
		//Odometer???
		
		controllerTempLabel.setText("" + esc[0].getControllerTemp());
		
		motorTempLabel.setText("" + esc[0].getMotorTemp());
		//Battery Temperature???
		
		
		//+------------------------------------------------------------+
		//LC - Load Cell
		//+------------------------------------------------------------+
		
		thrustLabel.setText("" + loadCell.getWeight());

		
		//+------------------------------------------------------------+
		//BMS - Battery Managment System
		//+------------------------------------------------------------+
		
		//Get low Cell Voltage
                //lowCellLabel.setText("FIX" + bms.getMinVoltage());
		lowCellLabel.setText("FIX");
		
                //Get high Cell Voltage
                //highCellLabel.setText("FIX"+ bms.getMaxVoltage());
		highCellLabel.setText("FIX");
		
		
		//+------------------------------------------------------------+
            }
            
        }));
        refreshUI.setCycleCount(Timeline.INDEFINITE);
        refreshUI.play();
    }
}
