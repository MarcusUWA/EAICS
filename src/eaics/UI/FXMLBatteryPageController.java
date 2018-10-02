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
import eaics.SER.LoadCell;
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
    
    FXMLBatteryCellPage1Controller cellPage1;
    FXMLBatteryCellPage2Controller cellPage2;
    FXMLBatteryCellPage3Controller cellPage3;
    
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
    private javafx.scene.control.Button closeButton;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) 
    {
	// TODO
    }
    
    public void initData(CANFilter fil, LoadCell cell) 
    {
        this.filter = fil;
        this.loadCell = cell;
        
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
        BMSSettings bmsSettings = filter.getBMSSettings();

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

        socLabel.setText("" + Math.round((evmsV3.getAmpHours() / bmsSettings.getDisplaySetting(0)) * 100));
        
        double kwPower = (evmsV3.getVoltage() * (currentSensor.getCurrent() / 1000));

        powerLabel.setText("" + String.format("%.2f", kwPower));

        int maxV = 0;
        for(int ii = 0; ii < bms.length; ii++)
        {
            if(bms[ii].getMaxVoltage() > maxV)
            {
                maxV = bms[ii].getMaxVoltage();
            }
        }
        highCellLabel.setText("" + maxV / 1000.0);

        int lowV = 100000;
        for(int ii = 0; ii < bms.length; ii++)
        {
            if(bms[ii].getMinVoltage() < lowV)
            {
                lowV = bms[ii].getMinVoltage();
            }
        }
        lowCellLabel.setText("" + lowV / 1000.0);
        
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
        FXMLLoader loader = new FXMLLoader(getClass().getResource("FXMLBatteryCellPage1.fxml"));
        
        try 
        {
            Pane pane = loader.load();

            cellPage1 = loader.getController();
            cellPage1.initSettings(gui);
	    cellPage1.initData(filter, loadCell);
        
            Stage stage = new Stage();
        
            stage.initModality(Modality.WINDOW_MODAL);
            stage.initOwner(buttonCellPage1.getScene().getWindow());
        
            Scene scene = new Scene(pane);
        
            stage.setScene(scene);
            stage.setTitle("Battery Cell Page 1");
            
            stage.show();
        }
        
        catch (Exception e) 
        {
            System.out.println("Failed to open Battery Cell Page 1");
            e.printStackTrace();
        }
    }
    
    @FXML
    private void handleCellPage2(ActionEvent event) throws IOException
    {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("FXMLBatteryCellPage2.fxml"));
        
        try 
        {
            Pane pane = loader.load();

            cellPage2 = loader.getController();
            cellPage2.initSettings(gui);
	    cellPage2.initData(filter, loadCell);
        
            Stage stage = new Stage();
        
            stage.initModality(Modality.WINDOW_MODAL);
            stage.initOwner(buttonCellPage2.getScene().getWindow());
        
            Scene scene = new Scene(pane);
        
            stage.setScene(scene);
            stage.setTitle("Battery Cell Page 2");
            
            stage.show();
        }
        
        catch (Exception e) 
        {
            System.out.println("Failed to open Battery Cell Page 2");
            e.printStackTrace();
        }
    }
    
    @FXML
    private void handleCellPage3(ActionEvent event) throws IOException
    {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("FXMLBatteryCellPage3.fxml"));
        
        try 
        {
            Pane pane = loader.load();
	    
            cellPage3 = loader.getController();
            cellPage3.initSettings(gui);
	    cellPage3.initData(filter, loadCell);
        
            Stage stage = new Stage();
        
            stage.initModality(Modality.WINDOW_MODAL);
            stage.initOwner(buttonCellPage3.getScene().getWindow());
        
            Scene scene = new Scene(pane);
        
            stage.setScene(scene);
            stage.setTitle("Battery Cell Page 3");
            
            stage.show();
        }
        
        catch (Exception e) 
        {
            System.out.println("Failed to open Battery Cell Page 3");
            e.printStackTrace();
        }
    }
    
}
