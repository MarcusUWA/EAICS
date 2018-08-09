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
public class FXMLGeneralSettingsPage2 implements Initializable, FXMLGeneralSettingsPage
{
    MainUIController gui;
    
    private CANFilter filter;
    private LoadCell loadCell;
    
    FXMLNumpadController numpad;
    
    private BMSSettings bmsSettings;
    
    @FXML
    private Label label1;
    
    @FXML
    private Label label2;
    
    @FXML
    private Label label3;
    
    @FXML
    private Label label4;
    
    @FXML
    private Label label5;
    
    @FXML
    private Label label6;
    
    @FXML
    private Label label7;
    
    @FXML
    private Label label8;
    
    @Override
    public void updateLabels()
    {
	label1.setText("" + this.bmsSettings.getSetting(8));
	label2.setText("" + this.bmsSettings.getSetting(9));
	label3.setText("" + this.bmsSettings.getSetting(10));
	label4.setText("" + this.bmsSettings.getSetting(11));
	label5.setText("" + this.bmsSettings.getSetting(12));
	label6.setText("" + this.bmsSettings.getSetting(13));
	label7.setText("" + this.bmsSettings.getSetting(14));
	label8.setText("" + this.bmsSettings.getSetting(15));
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
    private void handle1(ActionEvent event) 
    {
        openNumpad(8);
    }
    
    @FXML
    private void handle2(ActionEvent event) 
    {
        openNumpad(9);
    }
    
    @FXML
    private void handle3(ActionEvent event) 
    {
        openNumpad(10);
    }
    
    @FXML
    private void handle4(ActionEvent event) 
    {
        openNumpad(11);
    }
    
    @FXML
    private void handle5(ActionEvent event) 
    {
        openNumpad(12);
    }
    
    @FXML
    private void handle6(ActionEvent event) 
    {
        openNumpad(13);
    }
    
    @FXML
    private void handle7(ActionEvent event) 
    {
        openNumpad(14);
    }
    
    @FXML
    private void handle8(ActionEvent event) 
    {
        openNumpad(15);
    }
}
