/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eaics.UI;

import eaics.SER.LoadCell;
import eaics.SER.Serial;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import javafx.util.Duration;

/**
 * FXML Controller class
 *
 * @author Markcuz
 */
public class FXMLCalibrateLoadCellController implements Initializable {

    private Serial serial;
    private LoadCell loadcell;
    
    //refreshRate in ms
    private final int refreshRate = 200;
    
    @FXML
    Label calibrationFactor;
    
    @FXML
    Label loadValue;
    
    @FXML 
    Button closeButton;
        
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    
    
    public void init(Serial serial, LoadCell loadcell) {
        this.serial = serial;
        this.loadcell = loadcell;
        
        
        Timeline refreshUI;
        refreshUI = new Timeline(new KeyFrame(Duration.millis(refreshRate), new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
               
                loadValue.setText("" + String.format("%.2f",loadcell.getWeight()));
                //loadValue.setText("5");
                calibrationFactor.setText("" + String.format("%.2f",loadcell.getCalibration()));
                //calibrationFactor.setText("100");
            }
            
        }));
        refreshUI.setCycleCount(Timeline.INDEFINITE);
        refreshUI.play();
        
    }
    
    @FXML
    private void handleIncreaseCalibration(ActionEvent event) {
        serial.writeData("+");
    }
    
    @FXML
    private void handleDecreaseCalibration(ActionEvent event) {
        serial.writeData("-");
    }
    
    @FXML
    private void handleTare(ActionEvent event) {
        serial.writeData("0");
    }
    
    @FXML
    private void handleExit(ActionEvent event) {
        Stage stage = (Stage) closeButton.getScene().getWindow();
        stage.close();
    }
}
