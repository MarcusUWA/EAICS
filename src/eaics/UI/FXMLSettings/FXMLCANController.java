/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eaics.UI.FXMLSettings;

import eaics.Settings.SettingsCAN;
import eaics.Settings.SettingsEAICS;
import eaics.Settings.TYPEBms;
import eaics.Settings.TYPECharger;
import eaics.Settings.TYPEDisplay;
import eaics.Settings.TYPEEsc;
import eaics.Settings.TYPEThrottle;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author Markcuz
 */
public class FXMLCANController implements Initializable {

    SettingsCAN settings;
    
    @FXML
    ChoiceBox bms;
    @FXML
    ChoiceBox esc;
    @FXML
    ChoiceBox chg;
    @FXML
    ChoiceBox prechg;
    @FXML
    ChoiceBox display;
    @FXML
    ChoiceBox miniDAQ;
    @FXML
    ChoiceBox thr;
    
    @FXML
    Button backButton;
    
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        settings = SettingsEAICS.getInstance().getCanSettings();
    }    
    
    public void setupScreen() {
        
        Integer[] arr = {0,1};
        
        bms.getItems().addAll((Object[])arr);
        bms.setValue(settings.getBmsCAN());
        
        esc.getItems().addAll((Object[])arr);
        esc.setValue(settings.getEscCAN());
        
        chg.getItems().addAll((Object[])arr);
        chg.setValue(settings.getChargerCAN());
        
        prechg.getItems().addAll((Object[])arr);
        prechg.setValue(settings.getPrechargeCAN());
        
        display.getItems().addAll((Object[])arr);
        display.setValue(settings.getDisplayCAN());
        
        miniDAQ.getItems().addAll((Object[])arr);
        miniDAQ.setValue(settings.getMinidaqCAN());
        
        thr.getItems().addAll((Object[])arr);
        thr.setValue(settings.getThrottleCAN());
    }

    @FXML
    private void closeButtonAction(ActionEvent event) {
        
        settings.setBmsCAN((int)bms.getValue());
        settings.setEscCAN((int)esc.getValue());
        settings.setChargerCAN((int)chg.getValue());
        settings.setPrechargeCAN((int)prechg.getValue());
        settings.setDisplayCAN((int)display.getValue());
        settings.setMinidaqCAN((int)miniDAQ.getValue());
        settings.setThrottleCAN((int)thr.getValue());

        SettingsEAICS.getInstance().writeSettings();
        
        Stage stage = (Stage) backButton.getScene().getWindow();
        stage.close();
    }
    
}
