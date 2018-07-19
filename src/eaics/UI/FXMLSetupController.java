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

/**
 * FXML Controller class
 *
 * @author Troy
 */
public class FXMLSetupController implements Initializable 
{
    MainUIController gui;
    
    FXMLNumpadController numpad;
    
    private String unit;
    private Double stateOfCharge;
    
    
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
    }    
    
    public void initSettings(MainUIController settingsGui) 
    {
        gui = settingsGui;
    }
    
    @FXML
    private void closeButtonAction(ActionEvent event)
    {
        System.out.println("Reset SoC to >>" + stateOfCharge.toString() + unit);
        Stage stage = (Stage) closeButton.getScene().getWindow();
        stage.close();
    }
    
    @FXML
    private void handleResetSoC(ActionEvent event) 
    {
        stateOfCharge = new Double(100.0);
        unit = "%";
        
        System.out.println("Reset SoC");
        
        FXMLLoader loader = new FXMLLoader(getClass().getResource("FXMLNumpad.fxml"));
        
        try 
        {
            Pane pane = loader.load();
            numpad = loader.getController();
            numpad.initSettings(gui, stateOfCharge, unit);
        
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
}
