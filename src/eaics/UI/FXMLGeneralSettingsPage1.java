/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eaics.UI;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import eaics.BMSSettings;
import eaics.CAN.CANFilter;
import eaics.SER.LoadCell;
import javafx.scene.control.Label;

/**
 * FXML Controller class
 *
 * @author Troy
 */
public class FXMLGeneralSettingsPage1 implements Initializable, FXMLGeneralSettingsPage
{
    MainUIController gui;
    
    private CANFilter filter;
    private LoadCell loadCell;
    
    FXMLNumpadController numpad;
    
    private BMSSettings bmsSettings;
    
    @FXML
    private Label packCapacityLabel;
    
    @FXML
    private Label socWarningLabel;
    
    @FXML
    private Label fullVoltageLabel;
    
    @FXML
    private Label warningCurrentLabel;
    
    @FXML
    private Label tripCurrentLabel;
    
    @FXML
    private Label evmsTempWarningLabel;
    
    @FXML
    private Label minAuxVoltageLabel;
    
    @FXML
    private Label minIsolationLabel;
    
    @Override
    public void updateLabels()
    {
	packCapacityLabel.setText("" + this.bmsSettings.getSetting(0));
	socWarningLabel.setText("" + this.bmsSettings.getSetting(1));
	fullVoltageLabel.setText("" + this.bmsSettings.getSetting(2));
	warningCurrentLabel.setText("" + this.bmsSettings.getSetting(3));
	tripCurrentLabel.setText("" + this.bmsSettings.getSetting(4));
	evmsTempWarningLabel.setText("" + this.bmsSettings.getSetting(5));
	minAuxVoltageLabel.setText("" + this.bmsSettings.getSetting(6));
	minIsolationLabel.setText("" + this.bmsSettings.getSetting(7));
    }
        
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
    
    public void initSettings(MainUIController settingsGui) 
    {
        gui = settingsGui;
    }
    
    public void initData(CANFilter filter, LoadCell loadCell)
    {
        this.filter = filter;
        this.loadCell = loadCell;
	this.bmsSettings = filter.getBMSSettings();
    }
    
    @FXML
    private void closeButtonAction(ActionEvent event)
    {
        Stage stage = (Stage) closeButton.getScene().getWindow();
        stage.close();
    }
    
    private void openNumpad(int index)
    {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("FXMLNumpad.fxml"));
        
        try 
        {
            Pane pane = loader.load();
            numpad = loader.getController();
            numpad.initSettings(gui, index, this);
        
            Stage stage = new Stage();
        
            stage.initModality(Modality.WINDOW_MODAL);
            stage.initOwner(closeButton.getScene().getWindow());
        
            Scene scene = new Scene(pane);
        
            stage.setScene(scene);
            stage.setTitle("Numpad!!!");
            
            stage.show();
        }        
        catch (Exception e) 
        {
            System.out.println("Failed to open Numpad Window");
            e.printStackTrace();
        }
    }
    
    @Override
    public BMSSettings getBMSSettings()
    {
	return this.bmsSettings;
    }
    
    @FXML
    private void handlePackCapcity(ActionEvent event) 
    {
        openNumpad(0);
    }
    
    @FXML
    private void handleSoCWarning(ActionEvent event) 
    {
        openNumpad(1);
    }
    
    @FXML
    private void handleFullVoltage(ActionEvent event) 
    {
        openNumpad(2);
    }
    
    @FXML
    private void handleWarnCurrent(ActionEvent event) 
    {
        openNumpad(3);
    }
    
    @FXML
    private void handleTripCurrent(ActionEvent event) 
    {
        openNumpad(4);
    }
    
    @FXML
    private void handleEVMSTempWarning(ActionEvent event) 
    {
        openNumpad(5);
    }
    
    @FXML
    private void handleMinAuxVoltage(ActionEvent event) 
    {
        openNumpad(6);
    }
    
    @FXML
    private void handleMinIsolation(ActionEvent event) 
    {
        openNumpad(7);
    }
}
