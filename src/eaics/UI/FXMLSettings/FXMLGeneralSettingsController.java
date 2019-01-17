/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eaics.UI.FXMLSettings;

import eaics.Settings.SettingsEAICS;
import eaics.Settings.SettingsGeneral;
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
import javafx.scene.control.ToggleButton;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author Markcuz
 */
public class FXMLGeneralSettingsController implements Initializable {

    SettingsGeneral settings;
    
    @FXML
    ChoiceBox bmsType;
    @FXML
    ChoiceBox chgType;
    @FXML
    ChoiceBox dispType;
    @FXML
    ChoiceBox escType;
    @FXML
    ChoiceBox thrType;
    
    @FXML
    ToggleButton zevaFaker;
    
    @FXML
    ChoiceBox numModules;
    
    @FXML
    Button backButton;
    
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        settings = SettingsEAICS.getInstance().getGeneralSettings();
    }    
    
    public void setupScreen() {
        bmsType.getItems().setAll((Object[])TYPEBms.values());
        chgType.getItems().setAll((Object[])TYPECharger.values());
        dispType.getItems().setAll((Object[])TYPEDisplay.values());
        escType.getItems().setAll((Object[])TYPEEsc.values());
        thrType.getItems().setAll((Object[])TYPEThrottle.values());
        
        bmsType.setValue(settings.getBms());      
        chgType.setValue(settings.getChargerType());
        dispType.setValue(settings.getDisp());
        escType.setValue(settings.getEsc());
        thrType.setValue(settings.getThr());
        
        zevaFaker.setSelected(settings.isBmsFaker());
        changeToggleText();
        
        Integer[] moduleArr = new Integer[24];
        
        for(int i = 0; i<24; i++) {
            moduleArr[i] = i;
        }
        
        numModules.getItems().addAll((Object[])moduleArr);
        numModules.setValue(settings.getNumBatteryModules());
    }
    
    @FXML
    private void changeToggleText() {
        zevaFaker.setText(Boolean.toString(zevaFaker.isSelected()));
    }
    
    @FXML
    private void closeButtonAction(ActionEvent event) {
        
        settings.setBms((TYPEBms) bmsType.getValue());
        settings.setChargerType((TYPECharger) chgType.getValue());
        settings.setDisp((TYPEDisplay) dispType.getValue());
        settings.setEsc((TYPEEsc) escType.getValue());
        settings.setThr((TYPEThrottle) thrType.getValue());
        
        settings.setNumBatteryModules((int) numModules.getValue());
        
        settings.setBmsFaker(zevaFaker.isSelected());
        
        SettingsEAICS.getInstance().writeSettings();
        
        Stage stage = (Stage) backButton.getScene().getWindow();
        stage.close();
    }
}
