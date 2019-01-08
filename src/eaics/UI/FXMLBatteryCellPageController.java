/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eaics.UI;

import eaics.CAN.Battery.BMS;
import eaics.CAN.CANFilter;
import eaics.CAN.Battery.CCB;
import eaics.CAN.ESC.ESC;
import eaics.CAN.Battery.EVMS;
import eaics.SER.LoadCell;
import eaics.Settings.EVMSSettings;
import eaics.Settings.EAICS_Settings;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import javafx.util.Duration;

/**
 * FXML Controller class
 *
 */
public class FXMLBatteryCellPageController implements Initializable 
{
    private int pageNumber;
    //refresh rate in ms
    int refreshFrequency = 1000;
    
    private CANFilter filter;
    
    @FXML
    private Label title;
    
    @FXML
    private Label bms0;
    
    @FXML
    private Label bms1;
    
    @FXML
    private Label bms2;
    
    @FXML
    private Label bms3;
    
    @FXML
    private Label bms4;
    
    @FXML
    private Label bms5;
    
    @FXML
    private Label bms6;
    
    @FXML
    private Label bms7;
    
    @FXML
    private javafx.scene.control.Button closeButton;
    
    @FXML 
    private ImageView switchImage;
    
    @FXML
    private List<Label> cellLabelList;
    
    public void initData(int pageNumber) 
    {
	this.pageNumber = (pageNumber) * 8;
        this.filter = CANFilter.getInstance();
        
        updateScreen();
	
	Timeline refreshUI;
        refreshUI = new Timeline(new KeyFrame(Duration.millis(refreshFrequency), new EventHandler<ActionEvent>() 
	{
            int count = 0;
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
        /*
        EVMS evmsV3 = filter.getEVMS();
        ESC[] esc = filter.getESC();
        BMS[] bms = filter.getBMS();
        CCB[] ccb = filter.getCCB();
        
        //CCB
        FileInputStream input = null;
        Image image;
        if(ccb[0].isCCB_On()) //CCB1
	{
            try 
            {
                input = new FileInputStream("/home/pi/EAICS/images/switch_on.jpg");
            } 
            catch (FileNotFoundException ex) 
            {
                ex.printStackTrace();
            }
                            
            image = new Image(input);
            switchImage.setImage(image);
        }
        else 
	{
            try 
	    {
                input = new FileInputStream("/home/pi/EAICS/images/switch_off.jpg");
            } 
	    catch (FileNotFoundException ex) 
	    {
                ex.printStackTrace();
            }
                            
            image = new Image(input);
            switchImage.setImage(image);
        }
	
	title.setText("Battery Box " + (pageNumber/8 + 1));
	
	//+------------------------------------------------------------+
        //BMS Module Titles
        //+------------------------------------------------------------+
	
	bms0.setText("" + (pageNumber + 0));
	bms1.setText("" + (pageNumber + 1));
	bms2.setText("" + (pageNumber + 2));
	bms3.setText("" + (pageNumber + 3));
	bms4.setText("" + (pageNumber + 4));
	bms5.setText("" + (pageNumber + 5));
	bms6.setText("" + (pageNumber + 6));
	bms7.setText("" + (pageNumber + 7));

        
	int bmsModuleNum, cellNum, count;
	double cellVoltage;
	EVMSSettings bmsSettings = EAICS_Settings.getInstance().getEVMSSettings();
	int bmsMaxVoltage = bmsSettings.getSetting(14);
	int bmsBalanceVoltage = bmsSettings.getSetting(15);
	int bmsMinVoltage = bmsSettings.getSetting(13);
	
	count = 0;
        for (Label label : cellLabelList) 
	{
	    bmsModuleNum = count / 12;	//integer division
	    cellNum = count % 12;
	    
	    cellVoltage = (bms[pageNumber + bmsModuleNum].getVoltage(cellNum) / 1000.0);
            
	    label.setText(cellVoltage + "V");
	    
	    cellVoltage *= 1000.0;
	    
	    if(cellVoltage > bmsMaxVoltage)	//Too high
	    {
		label.setStyle("-fx-text-fill: red;");
	    }
	    else if(cellVoltage <= bmsMaxVoltage && cellVoltage >= bmsBalanceVoltage)   //Warning, almost too high
	    {
		label.setStyle("-fx-text-fill: orange;");		
	    }
	    else if(cellVoltage < bmsMinVoltage)	//Too low
	    {
		label.setStyle("-fx-text-fill: yellow;");
	    }
	    else    //Normal
	    {
		label.setStyle("-fx-text-fill: black;");
	    }
	    
	    count++;
        }*/
    }

    @FXML
    private void closeButtonAction(ActionEvent event) 
    {
        Stage stage = (Stage) closeButton.getScene().getWindow();
        stage.close();
    }
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) 
    {
        
    }
}