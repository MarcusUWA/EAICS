/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eaics.UI;

import eaics.CAN.CANFilter;
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
 *
 * @author Markcuz
 */
public class MainUIController implements Initializable {
    
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
    private void handleSettingsPressed(ActionEvent event) {
        System.out.println("You clicked me!");
        //FXMLLoader loader = new FXMLLoader(getClass().getResource("FXMLSettings.fxml"));
    }
    
    @FXML
    private void handleBatteryPressed(ActionEvent event) {
        System.out.println("You clicked me!");
        //FXMLLoader loader = new FXMLLoader(getClass().getResource("FXMLBattery.fxml"));
    }
    
    
    @FXML
    private void handleTelemetryPressed(ActionEvent event) {
        System.out.println("You clicked me!");
        //FXMLLoader loader = new FXMLLoader(getClass().getResource("FXMLTelemetry.fxml"));
    }
    
    
    public void initData(CANFilter fil, LoadCell cell) {
        this.filter = fil;
        this.loadCell = cell;
    }
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
       
       Timeline refreshUI;
        refreshUI = new Timeline(new KeyFrame(Duration.millis(refreshFrequency), new EventHandler<ActionEvent>() {
            int count = 0;
            @Override
            public void handle(ActionEvent event) {
                
                tachoLabel.setText(""+filter.esc.getRpm());
                
                //socLabel.setText(""+filter.evms.getCharge());
                
                powerLabel.setText(""+count);
                count++;
                
                //powerLabel.setText(""+(filter.evms.getVoltage()*filter.evms.getCurrent()/1000));
                
               // voltsLabel.setText(""+filter.evms.getVoltage());
                //ampsLabel.setText(""+filter.evms.getCurrent());
                
                //@todo get low Cell Voltage
                lowCellLabel.setText("--");
                //@todo get high Cell Voltage
                highCellLabel.setText("--");
                
                thrustLabel.setText(""+loadCell.getWeight());
                
                //battTempLabel.setText(""+filter.evms.getTemp());
                
                //auxVoltageLabel.setText(""+filter.evms.getAuxVoltage());
                
                motorTempLabel.setText(""+filter.esc.getMotorTemp());
                
                controllerTempLabel.setText(""+filter.esc.getControllerTemp());
            }
            
        }));
        refreshUI.setCycleCount(Timeline.INDEFINITE);
        refreshUI.play();
    }    

}
