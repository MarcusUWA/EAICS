/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eaics.UI;

import eaics.BMSSettings;
import eaics.CAN.BMS;
import eaics.CAN.CANFilter;
import eaics.CAN.ESC;
import eaics.CAN.EVMS_v3;
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
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.ProgressIndicator;
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
    
    // Big Labels - Top of Page ----------------------------------------------
    
    @FXML
    private Label powerLabel;
    
    @FXML
    private Label timeLabel;
    
    // ESC (Left Wing) ---------------------------------------------------------
    
    @FXML
    private Label leftRPMLabel;
    
    @FXML
    private Label leftControllerTempLabel;
    
    @FXML
    private Label leftMotorTempLabel;
    
    @FXML
    private ProgressBar leftRPM;
    
    // ESC (Bottom Wing) -------------------------------------------------------
    
    @FXML
    private Label bottomRPMLabel;
    
    @FXML
    private Label bottomControllerTempLabel;
    
    @FXML
    private Label bottomMotorTempLabel;
    
    @FXML
    private ProgressBar bottomRPM;
    
    // ESC (Top Wing) ----------------------------------------------------------
    
    @FXML
    private Label topRPMLabel;
    
    @FXML
    private Label topControllerTempLabel;
    
    @FXML
    private Label topMotorTempLabel;
    
    @FXML
    private ProgressBar topRPM;
    
    // ESC (Right Wing) --------------------------------------------------------
    
    @FXML
    private Label rightRPMLabel;
    
    @FXML
    private Label rightControllerTempLabel;
    
    @FXML
    private Label rightMotorTempLabel;
    
    @FXML
    private ProgressBar rightRPM;
    
    // Thrust Label - Bottom of Page -------------------------------------------
    
    @FXML
    private Label thrustLabel;
    
    @FXML
    private ProgressIndicator timeIndicator;

    @FXML
    private ProgressIndicator powerIndicator;
    
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
	
	int maxProgress = 10000;
	int maxTime = 1000;
       
        Timeline refreshUI;
        refreshUI = new Timeline(new KeyFrame(Duration.millis(refreshFrequency), new EventHandler<ActionEvent>() 
	{
            //Warnings
	    boolean hasWarnedAuxVoltageLow = false;
	    boolean hasWarnedChargerOn = false;
	    
	    //int count = 0;
	    
            @Override
            public void handle(ActionEvent event) 
	    {
                EVMS_v3 evmsV3 = (EVMS_v3) filter.getEVMS_v3();
		ESC[] esc = filter.getESC();
		BMS[] bms = filter.getBMS();
		BMSSettings bmsSettings = filter.getBMSSettings();
		
		// Warnings ----------------------------------------------------
		
		// Minimum Auxillary Voltage
		if(filter.getEVMS_v3().getAuxVoltage() < bmsSettings.getDisplaySetting(6) && !hasWarnedAuxVoltageLow)
		{
		    Alert alert = new Alert(AlertType.INFORMATION);
		    alert.setHeaderText("WARNING");
		    alert.setContentText("Auxillary voltage is low");
		    alert.show();
		    hasWarnedAuxVoltageLow = true;		    
		}
		
		// Check if the charger is on
		if(evmsV3.getHeadlights() == 1 && !hasWarnedChargerOn)
		{
		    Alert alert = new Alert(AlertType.INFORMATION);
		    alert.setHeaderText("WARNING");
		    alert.setContentText("Charger is on");
		    alert.show();
		    hasWarnedChargerOn = true;		    
		}
		
                
                //+------------------------------------------------------------+
		//EVMS - Electric Vehicle Management System
		//+------------------------------------------------------------+
		
		//Calculation variable
		//powerLabel.setText("!" + (evmsV3.getVoltage() * evmsV3.getCurrent() / 1000));	//evmsV2 only
		powerLabel.setText("?");
		powerIndicator.setProgress(0.75);
		
		timeLabel.setText("" + evmsV3.getAmpHours());
		timeIndicator.setProgress(evmsV3.getAmpHours() / maxTime);
                
		
		//+------------------------------------------------------------+
		//ESC - Electronic Speed Controller - Left Wing
		//+------------------------------------------------------------+
		
		leftRPMLabel.setText("" + esc[0].getRpm());
		
		leftControllerTempLabel.setText("" + esc[0].getControllerTemp());
		
		leftMotorTempLabel.setText("" + esc[0].getMotorTemp());
                
                leftRPM.setProgress((double)esc[0].getRpm() / maxProgress);
		
		
                //+------------------------------------------------------------+
		//ESC - Electronic Speed Controller - Bottom
		//+------------------------------------------------------------+
		
		bottomRPMLabel.setText("" + esc[1].getRpm());
		
		bottomControllerTempLabel.setText("" + esc[1].getControllerTemp());
		
		bottomMotorTempLabel.setText("" + esc[1].getMotorTemp());
                
                bottomRPM.setProgress((double)esc[1].getRpm() / maxProgress);
		
		
                //+------------------------------------------------------------+
		//ESC - Electronic Speed Controller - Top
		//+------------------------------------------------------------+
		
		topRPMLabel.setText("" + esc[2].getRpm());
		
		topControllerTempLabel.setText("" + esc[2].getControllerTemp());
		
		topMotorTempLabel.setText("" + esc[2].getMotorTemp());
                
                topRPM.setProgress((double)esc[2].getRpm() / maxProgress);
		
		
                //+------------------------------------------------------------+
		//ESC - Electronic Speed Controller - Right Wing
		//+------------------------------------------------------------+
		
		rightRPMLabel.setText("" + esc[3].getRpm());
		
		rightControllerTempLabel.setText("" + esc[3].getControllerTemp());
		
		rightMotorTempLabel.setText("" + esc[3].getMotorTemp());
                
                rightRPM.setProgress((double)esc[3].getRpm() / maxProgress);
		
		
		//+------------------------------------------------------------+
		//LC - Load Cell
		//+------------------------------------------------------------+
		
		thrustLabel.setText("" + loadCell.getWeight());
                
		
		//+------------------------------------------------------------+
            }
            
        }));
        refreshUI.setCycleCount(Timeline.INDEFINITE);
        refreshUI.play();
    }
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) 
    {
        
    }
}
