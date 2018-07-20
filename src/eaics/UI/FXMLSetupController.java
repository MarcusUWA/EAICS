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
    Button buttonResetSoC;
    
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
            stage.initOwner(buttonResetSoC.getScene().getWindow());
        
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
    private void handleResetSoC(ActionEvent event) 
    {
        System.out.println("Reset SoC");
        openNumpad(0);/////////////////////////////////change this later!!!!
    }
}
