/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eaics.UI;

import eaics.BMSSettings;
import eaics.CAN.BMS;
import eaics.CAN.CANFilter;
import eaics.CAN.CurrentSensor;
import eaics.CAN.ESC;
import eaics.CAN.EVMS_v3;
import eaics.IPaddress;
import eaics.SER.LoadCell;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import javafx.util.Duration;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
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
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 * FXML Controller class
 */
public class MainUIController implements Initializable 
{    
    FXMLBatteryPageController batterys;
    FXMLSettingsController settings;
    
    @FXML
    Button buttonSettings;
    
    //refresh rate in ms
    int refreshFrequency = 10;
    
    int timeout = 2000;
    
    private CANFilter filter;
    private LoadCell loadCell;
    
    private int status = 0;
    
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
    private Label leftMotorPowerLabel;
    
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
    private Label bottomMotorPowerLabel;
    
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
    private Label topMotorPowerLabel;
    
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
    private Label rightMotorPowerLabel;
    
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
    private ImageView wifi_icon;
    
    @FXML
    private ImageView can_icon;
    
    @FXML 
    private ImageView status_icon;
    
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
    
    public void initData(CANFilter fil, LoadCell cell) 
    {
        this.filter = fil;
        this.loadCell = cell;
	
	int maxProgress = 10000;
	int maxTime = 2*60; //2 hours
       
        Timeline refreshUI;
        refreshUI = new Timeline(new KeyFrame(Duration.millis(refreshFrequency), new EventHandler<ActionEvent>() 
	{	    
            @Override
            public void handle(ActionEvent event) 
	    {
                EVMS_v3 evmsV3 = (EVMS_v3) filter.getEVMS_v3();
		ESC[] esc = filter.getESC();
		BMS[] bms = filter.getBMS();
		BMSSettings bmsSettings = filter.getBMSSettings();
                CurrentSensor currentSensor = filter.getCurrentSensor();
                
                FileInputStream input = null;
                Image image;
		
		// Warnings ----------------------------------------------------
             /*
                //Wifi connection icon
                IPaddress ipAddress = new IPaddress();
        
                String[] splited = ipAddress.getIPaddress().split("\\s+");
        
                if (splited.length == 1){   
                    
                    try {
                        input = new FileInputStream("/home/pi/EAICS/images/wifi-on.jpg");
                    } catch (FileNotFoundException ex) {
                        Logger.getLogger(MainUIController.class.getName()).log(Level.SEVERE, null, ex);
                    }
                            
                    image = new Image(input);
                    wifi_icon.setImage(image);
                }
                else {
                    try {
                        input = new FileInputStream("/home/pi/EAICS/images/wifi-off.png");
                    } catch (FileNotFoundException ex) {
                        Logger.getLogger(MainUIController.class.getName()).log(Level.SEVERE, null, ex);
                    }
                            
                    image = new Image(input);
                    wifi_icon.setImage(image);
                 
                }
                */
                //TODO: Need to conenct this properly...
                //CAN-Bus connections Icon
                /*
                if(counter<timeout) {
                    try {
                        input = new FileInputStream("/home/pi/EAICS/images/CAN-conn.png");
                    } catch (FileNotFoundException ex) {
                        Logger.getLogger(MainUIController.class.getName()).log(Level.SEVERE, null, ex);
                    }
                            
                    image = new Image(input);
                    can_icon.setImage(image);
                }
                else {
                    try {
                        input = new FileInputStream("/home/pi/EAICS/images/CAN-disconn.png");
                    } catch (FileNotFoundException ex) {
                        Logger.getLogger(MainUIController.class.getName()).log(Level.SEVERE, null, ex);
                    }
                            
                    image = new Image(input);
                    can_icon.setImage(image);
                }
                */
                
                //EVMS Status Icons
		if(evmsV3.getStatus()!=status) {
                    status = evmsV3.getStatus();

                    switch(status) {
                        //idle state
                        case 0:
                            try {
                                input = new FileInputStream("/home/pi/EAICS/images/idle.png");
                            } catch (FileNotFoundException ex) {
                                Logger.getLogger(MainUIController.class.getName()).log(Level.SEVERE, null, ex);
                            }
                            
                            image = new Image(input);
                            status_icon.setImage(image);
                            break;
                        
                        //precharging
                        case 1:
                            try {
                                input = new FileInputStream("/home/pi/EAICS/images/pre-charge.png");
                            } catch (FileNotFoundException ex) {
                                Logger.getLogger(MainUIController.class.getName()).log(Level.SEVERE, null, ex);
                            }
                            
                            image = new Image(input);
                            status_icon.setImage(image);
                            break;
                            
                        case 2:
                            try {
                                input = new FileInputStream("/home/pi/EAICS/images/running.png");
                            } catch (FileNotFoundException ex) {
                                Logger.getLogger(MainUIController.class.getName()).log(Level.SEVERE, null, ex);
                            }
                            
                            image = new Image(input);
                            status_icon.setImage(image);
                            break;
                            
                        case 3:
                            try {
                                input = new FileInputStream("/home/pi/EAICS/images/charge.png");
                            } catch (FileNotFoundException ex) {
                                Logger.getLogger(MainUIController.class.getName()).log(Level.SEVERE, null, ex);
                            }
                            
                            image = new Image(input);
                            status_icon.setImage(image);
                            break;
                            
                        case 4:
                            try {
                                input = new FileInputStream("/home/pi/EAICS/images/setup.png");
                            } catch (FileNotFoundException ex) {
                                Logger.getLogger(MainUIController.class.getName()).log(Level.SEVERE, null, ex);
                            }
                            
                            image = new Image(input);
                            status_icon.setImage(image);
                            break;
                            
                        case 5:
                            try {
                                input = new FileInputStream("/home/pi/EAICS/images/stopped.png");
                            } catch (FileNotFoundException ex) {
                                Logger.getLogger(MainUIController.class.getName()).log(Level.SEVERE, null, ex);
                            }
                            
                            image = new Image(input);
                            status_icon.setImage(image);
                            break;
                        default:
                            break;     
                    }
                }
                
		// Errors
		if(evmsV3.getError() != 0 && !filter.getHasWarnedError())
		{
		    Alert alert = new Alert(AlertType.INFORMATION);
		    alert.setHeaderText("WARNING!!!");
                    
                    switch(evmsV3.getError()) {
                        case 1: 
                            alert.setContentText("EVMS Error: Corrupt Settings!");
                            break;
                        case 2:
                            alert.setContentText("EVMS Error: Overcurrent Warning!");
                            break;
                        case 3:
                            alert.setContentText("EVMS Error: Overcurrent Shutdown!");
                            break;
                        case 4:
                            alert.setContentText("EVMS Error: Low Cell Warning!");
                            break;
                        case 5:
                            alert.setContentText("EVMS Error: BMS Shutdown!");
                            break;
                        case 6:
                            alert.setContentText("EVMS Error: High Cell Warning!");
                            break;    
                        case 7:
                            alert.setContentText("EVMS Error: Charge Ended!");
                            break;
                        case 8:
                            alert.setContentText("EVMS Error: BMS Over-Temp!");
                            break;
                        case 9:
                            alert.setContentText("EVMS Error: BMS Under-Temp!");
                            break;
                        case 10:
                            alert.setContentText("EVMS Error: Low SoC!");
                            break;
                        case 11:
                            alert.setContentText("EVMS Error: EVMS Over Temperature!");
                            break;
                        case 12:
                            alert.setContentText("EVMS Error: Isolation Error!");
                            break;
                        case 13:
                            alert.setContentText("EVMS Error: Low 12V!");
                            break;
                        case 14:
                            alert.setContentText("EVMS Error: Precharge Failed!");
                            break;
                        case 16:
                            alert.setContentText("EVMS Error: Contactor Switch Error!");
                            break;
                        case 17:
                            alert.setContentText("EVMS Error: CAN Error!");
                            break;
                        default:
                            break;
                        
                    }
		    alert.show();
                    filter.setHasWarnedError(true);	    
		}
                
		// Check if the charger is off
		if(evmsV3.getHeadlights() == 1 && !filter.getHasWarnedChargerOff())
		{
		    Alert alert = new Alert(AlertType.INFORMATION);
		    alert.setHeaderText("WARNING");
		    alert.setContentText("Charger is off");
		    alert.show();
                    filter.setHasWarnedChargerOff(true);    
		}
		
                
                //+------------------------------------------------------------+
		//EVMS - Electric Vehicle Management System
		//+------------------------------------------------------------+
		
		//Calculation variable
		double kwPower = (evmsV3.getVoltage() * (currentSensor.getCurrent() / 1000)) / 1000;
                
		powerLabel.setText("" + String.format("%.2f", kwPower));
		powerIndicator.setProgress((kwPower / (400*500)));
		
                double time = evmsV3.getAmpHours() / currentSensor.getCurrent();
                time *= 60;
		if(Double.isNaN(time))
                {
                    timeLabel.setText("--");
                }
                else if(Double.isInfinite(time))
                {
                    timeLabel.setText("--");
                }
                else
                {
                    timeLabel.setText("" + String.format("%.2f", time));
                }
                
                if(time > maxTime)
                {
                    timeIndicator.setProgress(0.999999);
                }
                else
                {
                    timeIndicator.setProgress(time / maxTime);
                }
		
		
		//+------------------------------------------------------------+
		//ESC - Electronic Speed Controller - Left Wing
		//+------------------------------------------------------------+
		
		leftRPMLabel.setText("" + esc[0].getRpm());
		
		leftControllerTempLabel.setText("" + esc[0].getControllerTemp());
		
		leftMotorTempLabel.setText("" + esc[0].getMotorTemp());
                
                double kwPowerLeftMotor = esc[0].getBatteryVoltage() * esc[0].getBatteryCurrent() / 1000;
                leftMotorPowerLabel.setText("" + String.format("%.2f", kwPowerLeftMotor));
                
                leftRPM.setProgress((double)esc[0].getRpm() / maxProgress);
		
		
                //+------------------------------------------------------------+
		//ESC - Electronic Speed Controller - Bottom
		//+------------------------------------------------------------+
		
		bottomRPMLabel.setText("" + esc[1].getRpm());
		
		bottomControllerTempLabel.setText("" + esc[1].getControllerTemp());
		
		bottomMotorTempLabel.setText("" + esc[1].getMotorTemp());
                
                double kwBottomRightMotor = esc[1].getBatteryVoltage() * esc[1].getBatteryCurrent() / 1000;
                leftMotorPowerLabel.setText("" + String.format("%.2f", kwBottomRightMotor));
                
                bottomRPM.setProgress((double)esc[1].getRpm() / maxProgress);
		
		
                //+------------------------------------------------------------+
		//ESC - Electronic Speed Controller - Top
		//+------------------------------------------------------------+
		
		topRPMLabel.setText("" + esc[2].getRpm());
		
		topControllerTempLabel.setText("" + esc[2].getControllerTemp());
		
		topMotorTempLabel.setText("" + esc[2].getMotorTemp());
                
                double kwPowerTopMotor = esc[2].getBatteryVoltage() * esc[2].getBatteryCurrent() / 1000;
                leftMotorPowerLabel.setText("" + String.format("%.2f", kwPowerTopMotor));
                
                topRPM.setProgress((double)esc[2].getRpm() / maxProgress);
		
		
                //+------------------------------------------------------------+
		//ESC - Electronic Speed Controller - Right Wing
		//+------------------------------------------------------------+
		
		rightRPMLabel.setText("" + esc[3].getRpm());
		
		rightControllerTempLabel.setText("" + esc[3].getControllerTemp());
		
		rightMotorTempLabel.setText("" + esc[3].getMotorTemp());
                
                double kwPowerRightMotor = esc[3].getBatteryVoltage() * esc[3].getBatteryCurrent() / 1000;
                leftMotorPowerLabel.setText("" + String.format("%.2f", kwPowerRightMotor));
                
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
