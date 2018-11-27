/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eaics.UI.Trike;

import eaics.UI.Trifan.TrifanMainUIController;
import eaics.CAN.BMS;
import eaics.CAN.CANFilter;
import eaics.CAN.CurrentSensor;
import eaics.CAN.ESC;
import eaics.CAN.EVMS_v3;
import eaics.SER.LoadCell;
import eaics.Settings.IPAddress;
import eaics.UI.FXMLBatteryPageController;
import eaics.UI.FXMLSettingsController;
import eaics.UI.MainUI;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
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
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.Slider;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Duration;

/**
 * FXML Controller class
 *
 * @author Troy
 */
public class TrikeMainUIController extends MainUI
{
    @FXML
    Button buttonSettings;
    @FXML
    Button buttonTare;
    @FXML
    Button buttonBattery;
    
    @FXML
    private Label timeLabel;
    @FXML
    private Label powerLabel;
    @FXML
    private Label voltageLabel;
    @FXML
    private Label throttleLabel;
    @FXML
    private Label rpmLabel;
    @FXML
    private Label currentLabel;
    @FXML
    private Label ipLabel;
    @FXML
    private Label controllerTempLabel;
    @FXML
    private Label motorTempLabel;
    @FXML
    private Label auxLabel;
    @FXML
    private Label thrustLabel;
    
    @FXML
    private Slider thottleSlider;
    
    @FXML
    private ProgressBar rpmPB;
    
    @FXML
    private ImageView wifi_icon;
    @FXML
    private ImageView can_icon;
    @FXML 
    private ImageView status_icon;
    
    public void refreshIP() throws IOException 
    {
        ip.updateIPAddress();
        
        if(ip.getLANIP().length()>5) 
        {
            ipLabel.setText(ip.getLANIP());
        }
        else if(ip.getWifiIP().length()>5) 
        {
            ipLabel.setText(ip.getWifiIP());
        }
        else 
        {
            ipLabel.setText("Not Connected");
        }
    }
    
    @FXML
    private void handleSettingsPressed(ActionEvent event) throws IOException
    {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("FXMLSettings.fxml"));
        
        try 
	{
            Pane pane = loader.load();

            settings = loader.getController();
            //settings.initSettings(this);
            settings.initData(loadCell);
        
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
	FXMLLoader loader = new FXMLLoader(getClass().getResource("FXMLBatteryPage.fxml"));
        
        try 
	{
            Pane pane = loader.load();

            batterys = loader.getController();
            //batterys.initSettings(this);
	    batterys.initData(loadCell);
   
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
    private void handleTarePressed(ActionEvent event) 
    {
        //send
    }
    
    public void initData(LoadCell cell) throws IOException 
    {
	this.filter = CANFilter.getInstance();
        this.loadCell = cell;
	
	int maxProgress = 10000;
	int maxTime = 2*60; //2 hours
        
        refreshIP();
        
        Timeline refreshUI;
        refreshUI = new Timeline(new KeyFrame(Duration.millis(refreshFrequency), new EventHandler<ActionEvent>() 
	{	    
            @Override
            public void handle(ActionEvent event) 
	    {
                EVMS_v3 evmsV3 = (EVMS_v3) filter.getEVMS_v3();
		ESC[] esc = filter.getESC();
		BMS[] bms = filter.getBMS();
                CurrentSensor currentSensor = filter.getCurrentSensor();
                
                FileInputStream input = null;
                Image image;
                
                //EVMS Status Icons
		if(evmsV3.getStatus() != status) 
                {
                    status = evmsV3.getStatus();

                    switch(status) 
                    {
                        //idle state
                        case 0:
                            try 
                            {
                                input = new FileInputStream("/home/pi/EAICS/images/idle.png");
                            } 
                            catch (FileNotFoundException ex) 
                            {
                                Logger.getLogger(TrifanMainUIController.class.getName()).log(Level.SEVERE, null, ex);
                            }
                            
                            image = new Image(input);
                            status_icon.setImage(image);
                            break;
                        
                        //precharging
                        case 1:
                            try 
                            {
                                input = new FileInputStream("/home/pi/EAICS/images/pre-charge.png");
                            } 
                            catch (FileNotFoundException ex) 
                            {
                                Logger.getLogger(TrifanMainUIController.class.getName()).log(Level.SEVERE, null, ex);
                            }
                            
                            image = new Image(input);
                            status_icon.setImage(image);
                            break;
                            
                        case 2:
                            try 
                            {
                                input = new FileInputStream("/home/pi/EAICS/images/running.png");
                            } 
                            catch (FileNotFoundException ex) 
                            {
                                Logger.getLogger(TrifanMainUIController.class.getName()).log(Level.SEVERE, null, ex);
                            }
                            
                            image = new Image(input);
                            status_icon.setImage(image);
                            break;
                            
                        case 3:
                            try 
                            {
                                input = new FileInputStream("/home/pi/EAICS/images/charge.png");
                            } 
                            catch (FileNotFoundException ex) 
                            {
                                Logger.getLogger(TrifanMainUIController.class.getName()).log(Level.SEVERE, null, ex);
                            }
                            
                            image = new Image(input);
                            status_icon.setImage(image);
                            break;
                            
                        case 4:
                            try 
                            {
                                input = new FileInputStream("/home/pi/EAICS/images/setup.png");
                            } 
                            catch (FileNotFoundException ex) 
                            {
                                Logger.getLogger(TrifanMainUIController.class.getName()).log(Level.SEVERE, null, ex);
                            }
                            
                            image = new Image(input);
                            status_icon.setImage(image);
                            break;
                            
                        case 5:
                            try 
                            {
                                input = new FileInputStream("/home/pi/EAICS/images/stopped.png");
                            } 
                            catch (FileNotFoundException ex) 
                            {
                                Logger.getLogger(TrifanMainUIController.class.getName()).log(Level.SEVERE, null, ex);
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
		    Alert alert = new Alert(Alert.AlertType.INFORMATION);
		    alert.setHeaderText("WARNING!!!");
                    
                    switch(evmsV3.getError()) 
                    {
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
		    Alert alert = new Alert(Alert.AlertType.INFORMATION);
		    alert.setHeaderText("WARNING");
		    alert.setContentText("DC-DC is off");
		    alert.show();
                    filter.setHasWarnedChargerOff(true);    
		}
		
                
                //+------------------------------------------------------------+
		//EVMS - Electric Vehicle Management System
		//+------------------------------------------------------------+
		
		//Calculation variable
		double kwPower = (evmsV3.getVoltage() * (currentSensor.getCurrent() / 1000)) / 1000;
                
		powerLabel.setText("" + String.format("%.2f", kwPower));
		
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
		
                voltageLabel.setText(Integer.toString((int)evmsV3.getVoltage()));
                currentLabel.setText(Integer.toString(currentSensor.getCurrent()));
		
		//+------------------------------------------------------------+
		//ESC - Electronic Speed Controller
		//+------------------------------------------------------------+
		
		rpmLabel.setText("" + esc[0].getRpm());
		
		controllerTempLabel.setText("" + esc[0].getControllerTemp());
		
		motorTempLabel.setText("" + esc[0].getMotorTemp());
                
                double kwPowerLeftMotor = esc[0].getBatteryVoltage() * esc[0].getBatteryCurrent() / 1000;
                powerLabel.setText("" + String.format("%.2f", kwPowerLeftMotor));
                
                rpmPB.setProgress((double)esc[0].getRpm() / maxProgress);
		
		
		//+------------------------------------------------------------+
		//LC - Load Cell
		//+------------------------------------------------------------+
		
		thrustLabel.setText("" + String.format("%.2f", loadCell.getWeight()));
                
		
		//+------------------------------------------------------------+
                
                
                auxLabel.setText("" + String.format("%.2f", evmsV3.getAuxVoltage()));
            }
            
        }));
        refreshUI.setCycleCount(Timeline.INDEFINITE);
        refreshUI.play();
    }
}
