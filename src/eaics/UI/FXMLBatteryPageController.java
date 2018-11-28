/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eaics.UI;

import eaics.Settings.BMSSettings;
import eaics.CAN.BMS;
import eaics.CAN.CANFilter;
import eaics.CAN.CurrentSensor;
import eaics.CAN.ESC;
import eaics.CAN.EVMS_v3;
import eaics.SER.LoadCell;
import eaics.Settings.EAICS_Settings;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Duration;

/**
 * FXML Controller class
 *
 * @author Troy
 */
public class FXMLBatteryPageController implements Initializable 
{
    MainUIController gui;
    
    private CANFilter filter;
    private LoadCell loadCell;
    
    private int timeToRefresh = 5000;
    
    private static final int numberOfCellPages = 3;
    FXMLBatteryCellPageController[] cellPage = new FXMLBatteryCellPageController[numberOfCellPages];
    
    @FXML
    Button buttonCellPage1;
    
    @FXML
    Button buttonCellPage2;
    
    @FXML
    Button buttonCellPage3;
    
    @FXML
    private Label ampsLabel;
    
    @FXML
    private Label voltsLabel;
    
    @FXML
    private Label timeLabel;
    
    @FXML
    private Label highCellLabel;
    
    @FXML
    private Label lowCellLabel;
    
    @FXML
    private Label capacityLabel;
    
    @FXML
    private Label powerLabel;
    
    @FXML
    private Label socLabel;
    
    @FXML
    private Label theHighBMSLabel;    
    @FXML
    private Label theHighCellLabel;
    
    @FXML
    private Label theLowBMSLabel;    
    @FXML
    private Label theLowCellLabel;
    
    @FXML
    private Label deltaLabel;
    
    @FXML
    private javafx.scene.control.Button closeButton;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) 
    {
	// TODO
    }
    
    public void initData(LoadCell cell) 
    {
        this.filter = CANFilter.getInstance();
        this.loadCell = cell;
	for(int ii = 0; ii < numberOfCellPages; ii++)
	{
	    this.cellPage[ii] = new FXMLBatteryCellPageController();
	}
        
        updateScreen();
        
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
    
    public void updateScreen()
    {
        EVMS_v3 evmsV3 = (EVMS_v3)filter.getEVMS_v3();
        ESC[] esc = filter.getESC();
        BMS[] bms = filter.getBMS();
        CurrentSensor currentSensor = filter.getCurrentSensor();
        BMSSettings bmsSettings = EAICS_Settings.getInstance().getBmsSettings();

        //+------------------------------------------------------------+
        //BMS Module 8 (switch set to 8): 1 - 12 Cells
        //+------------------------------------------------------------+

        ampsLabel.setText("" + currentSensor.getCurrent());

        voltsLabel.setText("" + evmsV3.getVoltage()); //How many packs are connected, i.e. 3

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
	    timeLabel.setText("" + time);
	}

        capacityLabel.setText("" + evmsV3.getAmpHours());

        socLabel.setText("" + Math.round((evmsV3.getAmpHours() / bmsSettings.getSetting(0)) * 100));
        
        double kwPower = (evmsV3.getVoltage() * (currentSensor.getCurrent() / 1000));

        powerLabel.setText("" + String.format("%.2f", kwPower));

	int maxVoltage = -1;
	int maxCellNumber = -1;
	int maxBmsNumber = -1;
	
	for(int ii = 0; ii < bms.length; ii++)
	{
	    for(int jj = 0; jj < BMS.NUMBER_OF_CELLS; jj++)
	    {
		int tempVoltage = bms[ii].getVoltage(jj);
		if(tempVoltage > maxVoltage)
	        {
	    	    maxVoltage = tempVoltage;
		    maxCellNumber = jj;
		    maxBmsNumber = ii;
		}
	    }
	}
	
        highCellLabel.setText("" + maxVoltage / 1000.0);
	theHighBMSLabel.setText("" + maxBmsNumber);
	theHighCellLabel.setText("" + maxCellNumber);
	
	int minVoltage = 10000;
	int minCellNumber = -1;
	int minBmsNumber = -1;
	
	for(int ii = 0; ii < bms.length; ii++)
	{
	    for(int jj = 0; jj < BMS.NUMBER_OF_CELLS; jj++)
	    {
		int tempVoltage = bms[ii].getVoltage(jj);
		if(tempVoltage < minVoltage)
	        {
	    	    minVoltage = tempVoltage;
		    minCellNumber = jj;
		    minBmsNumber = ii;
		}
	    }
	}

        lowCellLabel.setText("" + minVoltage / 1000.0);
	theLowBMSLabel.setText("" + minBmsNumber);
	theLowCellLabel.setText("" + minCellNumber);
	
	int delta = maxVoltage - minVoltage;
	deltaLabel.setText("" + delta);
    }

    public void initSettings(MainUIController mainGui) 
    {
        gui = mainGui;
    }
    
    @FXML
    private void closeButtonAction(ActionEvent event)
    {
        Stage stage = (Stage) closeButton.getScene().getWindow();
        stage.close();
    }
    
    @FXML
    private void handleCellPage1(ActionEvent event) throws IOException
    {
        openCellPage(0);
    }
    
    @FXML
    private void handleCellPage2(ActionEvent event) throws IOException
    {
	openCellPage(1);        
    }
    
    @FXML
    private void handleCellPage3(ActionEvent event) throws IOException
    {
        openCellPage(2);
    }
    
    private void openCellPage(int pageNumber)
    {
	FXMLLoader loader = new FXMLLoader(getClass().getResource("FXMLBatteryCellPage.fxml"));
        
        try 
        {
            Pane pane = loader.load();
	    
            cellPage[pageNumber] = loader.getController();
            cellPage[pageNumber].initSettings(gui);
	    cellPage[pageNumber].initData(loadCell, pageNumber);
        
            Stage stage = new Stage();
        
            stage.initModality(Modality.WINDOW_MODAL);
            stage.initOwner(buttonCellPage1.getScene().getWindow());
        
            Scene scene = new Scene(pane);
        
            stage.setScene(scene);
            stage.setTitle("Battery Cell Page " + (pageNumber + 1));
            
            stage.show();
        }
        catch (Exception e) 
        {
            System.out.println("Failed to open Battery Cell Page " + (pageNumber + 1));
        }	
    }
}
