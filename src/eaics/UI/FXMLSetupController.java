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

/**
 * FXML Controller class
 *
 * @author Troy
 */
public class FXMLSetupController implements Initializable 
{
    MainUIController gui;
    
    FXMLNumpadController numpad;
    
    private BMSSettings bmsSettings;    
        
    @FXML
    private javafx.scene.control.Button closeButton;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) 
    {
        // TODO
        //System.out.println("setupCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCC");
        this.bmsSettings = new BMSSettings();
    }    
    
    public void initSettings(MainUIController settingsGui) 
    {
        gui = settingsGui;
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
            numpad.initSettings(gui, this.bmsSettings, index);
        
            Stage stage = new Stage();
        
            stage.initModality(Modality.WINDOW_MODAL);
            stage.initOwner(closeButton.getScene().getWindow());
        
            Scene scene = new Scene(pane);
        
            stage.setScene(scene);
            stage.setTitle("Numpad!!!");
            
            stage.setMaximized(true);
            stage.show();
        }        
        catch (Exception e) 
        {
            System.out.println("Failed to open Numpad Window");
            e.printStackTrace();
        }
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
    
    @FXML
    private void handleTachoPPR(ActionEvent event) 
    {
        openNumpad(8);
    }
    
    @FXML
    private void handleFuelGaugeFull(ActionEvent event) 
    {
        openNumpad(9);
    }
    
    @FXML
    private void handleFuelGaugeEmpty(ActionEvent event) 
    {
        openNumpad(10);
    }
    
    @FXML
    private void handleTempGaugeHot(ActionEvent event) 
    {
        openNumpad(11);
    }
    
    @FXML
    private void handleTempGaugeCold(ActionEvent event) 
    {
        openNumpad(12);
    }
    
    @FXML
    private void handleBMSMinVoltage(ActionEvent event) 
    {
        openNumpad(13);
    }
    
    @FXML
    private void handleBMSMaxVoltage(ActionEvent event) 
    {
        openNumpad(14);
    }
    
    @FXML
    private void handleBalanceVoltage(ActionEvent event) //////////////////////////where is this?
    {
        openNumpad(15);
    }
    
    @FXML
    private void handleBMSHysteresis(ActionEvent event) //////////////////////////where is this? 
    {
        openNumpad(16);
    }
    
    @FXML
    private void handleBMSMinTemp(ActionEvent event) 
    {
        openNumpad(17);
    }
    
    @FXML
    private void handleBMSMaxTemp(ActionEvent event) 
    {
        openNumpad(18);
    }
    
    @FXML
    private void handleMaxChargeVoltage(ActionEvent event) 
    {
        openNumpad(19);
    }
    
    @FXML
    private void handleMaxChargeCurrent(ActionEvent event) 
    {
        openNumpad(20);
    }
    
    @FXML
    private void handleAltChargeVoltage(ActionEvent event)  //////////////////////////where is this?
    {
        openNumpad(21);
    }
    
    @FXML
    private void handleAltChargeCurrent(ActionEvent event)  //////////////////////////where is this?
    {
        openNumpad(22);
    }
    
    @FXML
    private void handleSleepDelay(ActionEvent event)  //////////////////////////where is this?
    {
        openNumpad(23);
    }
    
    @FXML
    private void handleMPIFunction(ActionEvent event)  //////////////////////////where is this?
    {
        openNumpad(24);
    }
    
    @FXML
    private void handleMPO1Function(ActionEvent event)  //////////////////////////where is this?
    {
        openNumpad(25);
    }
    
    @FXML
    private void handleMPO2Function(ActionEvent event)  //////////////////////////where is this?
    {
        openNumpad(26);
    }
    
    @FXML
    private void handleParallelStrings(ActionEvent event)  //////////////////////////where is this?
    {
        openNumpad(27);
    }
    
    @FXML
    private void handleEnablePrecharge(ActionEvent event) 
    {
        openNumpad(28);
    }
    
    @FXML
    private void handleStationaryMode(ActionEvent event) 
    {
        openNumpad(29);
    }
    
    @FXML
    private void handleResetSoC(ActionEvent event) 
    {
        System.out.println("Reset SoC");
        openNumpad(0);/////////////////////////////////change this later!!!!
    }
}
