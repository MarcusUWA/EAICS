/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eaics.UI.FXMLBattery;

import eaics.SER.AirPressure;
import eaics.SER.Serial;
import eaics.SER.Temp;
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
public class FXMLAuxTempController implements Initializable {
    
    @FXML
    Label temp1;
    @FXML
    Label temp2;
    @FXML
    Label temp3;
    @FXML
    Label temp4;
    @FXML
    Label temp5;
    @FXML
    Label temp6;
    
    @FXML
    Label airSpeed;
    
    @FXML
    Button backButton;
    
    int timeToRefresh = 500;
    
    Temp temp;
    AirPressure press;
   
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    
    
    public void init() {
        
        temp = Serial.getInstance().getTemp();
        press = Serial.getInstance().getAirPress();
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
    
    private void updateScreen() {
        
        temp1.setText(String.format("%.1f",temp.getTempSensors()[0]));
        temp2.setText(String.format("%.1f",temp.getTempSensors()[1]));
        temp3.setText(String.format("%.1f",temp.getTempSensors()[2]));
        temp4.setText(String.format("%.1f",temp.getTempSensors()[3]));
        temp5.setText(String.format("%.1f",temp.getTempSensors()[4]));
        temp6.setText(String.format("%.1f",temp.getTempSensors()[5]));
        
        airSpeed.setText(String.format("%.1f",press.getSensorValue()));
    }
    
    @FXML // Delete the charging screne and go back to the previous scene.
    private void closeButtonAction(ActionEvent event) {
        Stage stage = (Stage) backButton.getScene().getWindow();
        stage.close();
    }
}
