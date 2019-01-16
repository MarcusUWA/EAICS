/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eaics.UI.FXMLBattery;

import eaics.CAN.CANFilter;
import eaics.Settings.TYPEBms;
import eaics.Settings.SettingsEAICS;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import javafx.util.Duration;

/**
 * FXML Controller class
 *
 * @author Markcuz
 */
public class FXMLCellPageController implements Initializable {
    
    int refreshFrequency = 1000;
    
    private CANFilter filter;
    private SettingsEAICS settings;
    
    int noModules = 8;
    int currentModule = 0;
    
    @FXML
    Button closeButton;
    
    @FXML
    ChoiceBox moduleSelector;
    
    @FXML
    Label B0C0;
    @FXML
    Label B0C1;
    @FXML
    Label B0C2;
    @FXML
    Label B0C3;
    @FXML
    Label B0C4;
    @FXML
    Label B0C5;
    @FXML
    Label B1C0;
    @FXML
    Label B1C1;
    @FXML
    Label B1C2;
    @FXML
    Label B1C3;
    @FXML
    Label B1C4;
    @FXML
    Label B1C5;
    @FXML
    Label B2C0;
    @FXML
    Label B2C1;
    @FXML
    Label B2C2;
    @FXML
    Label B2C3;
    @FXML
    Label B2C4;
    @FXML
    Label B2C5;
    @FXML
    Label B3C0;
    @FXML
    Label B3C1;
    @FXML
    Label B3C2;
    @FXML
    Label B3C3;
    @FXML
    Label B3C4;
    @FXML
    Label B3C5;
    
    @FXML
    Label T0;
    @FXML
    Label T1;
    @FXML
    Label T2;
    @FXML
    Label T3;
    
    @FXML
    Label LabelMax;
    
    @FXML
    Label LabelMin;
    
    @FXML
    Label LabelAve;
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    
    
    public void initData(){
        this.filter = CANFilter.getInstance();
        this.settings = SettingsEAICS.getInstance();
        
        noModules = settings.getGeneralSettings().getNumBatteryModules();
        
        populateChoiceBox();
	
	Timeline refreshUI;
        refreshUI = new Timeline(new KeyFrame(Duration.millis(refreshFrequency), new EventHandler<ActionEvent>()  {
            @Override
            public void handle(ActionEvent event) {
		updateScreen();
            }
            
        }));
        refreshUI.setCycleCount(Timeline.INDEFINITE);
        refreshUI.play();
    }
    
    @FXML
    public void handleNext() {
        if(currentModule<(noModules-1)) {
            currentModule++;
            moduleSelector.setValue(currentModule);
        } 
    }
    
    @FXML
    public void handlePrev() {
        if(currentModule>0) {
            currentModule--;
            moduleSelector.setValue(currentModule);
        } 
    }
    
    public void populateChoiceBox() {
        noModules = settings.getGeneralSettings().getNumBatteryModules();
        
        Integer[] moduleArr = new Integer[noModules];
        
        for(int i = 0; i<noModules; i++) {
            moduleArr[i] = i;
        }
        
        moduleSelector.getItems().addAll(moduleArr);
        moduleSelector.setValue(0);
        
    }
    
    public void updateScreen() {
        currentModule = (int) moduleSelector.getValue();
        
        //todo: Add colour to labels
        /*
        SettingsEVMS bmsSettings = SettingsEAICS.getInstance().getEVMSSettings();
        int bmsMaxVoltage = bmsSettings.getSetting(14);
        int bmsBalanceVoltage = bmsSettings.getSetting(15);
        int bmsMinVoltage = bmsSettings.getSetting(13);

        if(voltage > bmsMaxVoltage){
            //red
        }
        else if(voltage <= bmsMaxVoltage && voltage >= bmsBalanceVoltage) {
            //orange
        }
        else if(voltage < bmsMinVoltage) {
            //yellow
        }
        else {
            //black
        }
        */
        if(settings.getGeneralSettings().getBms()==TYPEBms.ZEVA3) {
            B0C0.setText(String.format("%.2f", filter.getBMS()[0+currentModule*2].getVoltage(0)/1000.0)+" V");
            B0C1.setText(String.format("%.2f", filter.getBMS()[0+currentModule*2].getVoltage(1)/1000.0)+" V");
            B0C2.setText(String.format("%.2f", filter.getBMS()[0+currentModule*2].getVoltage(2)/1000.0)+" V");
            B0C3.setText(String.format("%.2f", filter.getBMS()[0+currentModule*2].getVoltage(3)/1000.0)+" V");
            B0C4.setText(String.format("%.2f", filter.getBMS()[0+currentModule*2].getVoltage(4)/1000.0)+" V");
            B0C5.setText(String.format("%.2f", filter.getBMS()[0+currentModule*2].getVoltage(5)/1000.0)+" V");
            
            B1C0.setText(String.format("%.2f", filter.getBMS()[0+currentModule*2].getVoltage(6)/1000.0)+" V");
            B1C1.setText(String.format("%.2f", filter.getBMS()[0+currentModule*2].getVoltage(7)/1000.0)+" V");
            B1C2.setText(String.format("%.2f", filter.getBMS()[0+currentModule*2].getVoltage(8)/1000.0)+" V");
            B1C3.setText(String.format("%.2f", filter.getBMS()[0+currentModule*2].getVoltage(9)/1000.0)+" V");
            B1C4.setText(String.format("%.2f", filter.getBMS()[0+currentModule*2].getVoltage(10)/1000.0)+" V");
            B1C5.setText(String.format("%.2f", filter.getBMS()[0+currentModule*2].getVoltage(11)/1000.0)+" V");
            
            B2C0.setText(String.format("%.2f", filter.getBMS()[1+currentModule*2].getVoltage(0)/1000.0)+" V");
            B2C1.setText(String.format("%.2f", filter.getBMS()[1+currentModule*2].getVoltage(1)/1000.0)+" V");
            B2C2.setText(String.format("%.2f", filter.getBMS()[1+currentModule*2].getVoltage(2)/1000.0)+" V");
            B2C3.setText(String.format("%.2f", filter.getBMS()[1+currentModule*2].getVoltage(3)/1000.0)+" V");
            B2C4.setText(String.format("%.2f", filter.getBMS()[1+currentModule*2].getVoltage(4)/1000.0)+" V");
            B2C5.setText(String.format("%.2f", filter.getBMS()[1+currentModule*2].getVoltage(5)/1000.0)+" V");
            
            B3C0.setText(String.format("%.2f", filter.getBMS()[1+currentModule*2].getVoltage(6)/1000.0)+" V");
            B3C1.setText(String.format("%.2f", filter.getBMS()[1+currentModule*2].getVoltage(7)/1000.0)+" V");
            B3C2.setText(String.format("%.2f", filter.getBMS()[1+currentModule*2].getVoltage(8)/1000.0)+" V");
            B3C3.setText(String.format("%.2f", filter.getBMS()[1+currentModule*2].getVoltage(9)/1000.0)+" V");
            B3C4.setText(String.format("%.2f", filter.getBMS()[1+currentModule*2].getVoltage(10)/1000.0)+" V");
            B3C5.setText(String.format("%.2f", filter.getBMS()[1+currentModule*2].getVoltage(11)/1000.0)+" V");
            
            
            float max = 0, min = 10000, ave = 0, sum = 0;
            for(int i = 0; i< 12; i++) {
                for(int j = 0; j<2; j++) {
                    int voltage = filter.getBMS()[j+currentModule*2].getVoltage(i);
                    
                    if(voltage<min) {
                        min = voltage;
                    }
                    
                    if(voltage>max) {
                        max = voltage;
                    }

                    sum = sum + voltage;
                }
            }
            
            ave = (float) (sum/24.0);
            
            LabelMax.setText(String.format("%.2f", max/1000.0)+" V");
            LabelMin.setText(String.format("%.2f", min/1000.0)+" V");
            LabelAve.setText(String.format("%.2f", max/1000.0)+" V");
        }
    }
    
    @FXML
    private void closeButtonAction(ActionEvent event) {
        Stage stage = (Stage) closeButton.getScene().getWindow();
        stage.close();
    }
    
    
}

