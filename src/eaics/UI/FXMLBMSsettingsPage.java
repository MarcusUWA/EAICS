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
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import eaics.Settings.BMSSettings;
import eaics.CAN.CANFilter;
import eaics.SER.LoadCell;
import eaics.Settings.EAICS_Settings;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

/**
 * FXML Controller class
 *
 * @author Troy
 */
public class FXMLBMSsettingsPage implements Initializable
{
    MainUIController gui;
    
    private CANFilter filter;
    private LoadCell loadCell;
    
    private int pageNumber;
    
    FXMLNumpadController numpad;
    
    private BMSSettings bmsSettings;
    
    // Titel Label
    
    @FXML
    private Label titleLabel;
    
    // Button name labels
    
    @FXML
    private Button nameLabel1;
    
    @FXML
    private Button nameLabel2;
    
    @FXML
    private Button nameLabel3;
    
    @FXML
    private Button nameLabel4;
    
    @FXML
    private Button nameLabel5;
    
    @FXML
    private Button nameLabel6;
    
    @FXML
    private Button nameLabel7;
    
    @FXML
    private Button nameLabel8;
    
    // Min Labels
    
    @FXML
    private Label minLabel1;
    
    @FXML
    private Label minLabel2;
    
    @FXML
    private Label minLabel3;
    
    @FXML
    private Label minLabel4;
    
    @FXML
    private Label minLabel5;
    
    @FXML
    private Label minLabel6;
    
    @FXML
    private Label minLabel7;
    
    @FXML
    private Label minLabel8;
    
    // Max Labels
    
    @FXML
    private Label maxLabel1;
    
    @FXML
    private Label maxLabel2;
    
    @FXML
    private Label maxLabel3;
    
    @FXML
    private Label maxLabel4;
    
    @FXML
    private Label maxLabel5;
    
    @FXML
    private Label maxLabel6;
    
    @FXML
    private Label maxLabel7;
    
    @FXML
    private Label maxLabel8;
    
    // Value Labels
    
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
    
    // Unit Labels
    
    @FXML
    private Label unitLabel1;
    
    @FXML
    private Label unitLabel2;
    
    @FXML
    private Label unitLabel3;
    
    @FXML
    private Label unitLabel4;
    
    @FXML
    private Label unitLabel5;
    
    @FXML
    private Label unitLabel6;
    
    @FXML
    private Label unitLabel7;
    
    @FXML
    private Label unitLabel8;
    
    public void updateLabels()
    {
	// Title Label
	titleLabel.setText("General Settings - Page " + ((pageNumber / 8) + 1));
	// Name Labels
	nameLabel1.setText("" + this.bmsSettings.getName(pageNumber + 0));
	nameLabel2.setText("" + this.bmsSettings.getName(pageNumber + 1));
	nameLabel3.setText("" + this.bmsSettings.getName(pageNumber + 2));
	nameLabel4.setText("" + this.bmsSettings.getName(pageNumber + 3));
	nameLabel5.setText("" + this.bmsSettings.getName(pageNumber + 4));
	nameLabel6.setText("" + this.bmsSettings.getName(pageNumber + 5));
	nameLabel7.setText("" + this.bmsSettings.getName(pageNumber + 6));
	nameLabel8.setText("" + this.bmsSettings.getName(pageNumber + 7));
	// Min Labels
	minLabel1.setText("" + this.bmsSettings.getMinValue(pageNumber + 0));
	minLabel2.setText("" + this.bmsSettings.getMinValue(pageNumber + 1));
	minLabel3.setText("" + this.bmsSettings.getMinValue(pageNumber + 2));
	minLabel4.setText("" + this.bmsSettings.getMinValue(pageNumber + 3));
	minLabel5.setText("" + this.bmsSettings.getMinValue(pageNumber + 4));
	minLabel6.setText("" + this.bmsSettings.getMinValue(pageNumber + 5));
	minLabel7.setText("" + this.bmsSettings.getMinValue(pageNumber + 6));
	minLabel8.setText("" + this.bmsSettings.getMinValue(pageNumber + 7));
	// Max Labels
	maxLabel1.setText("" + this.bmsSettings.getMaxValue(pageNumber + 0));
	maxLabel2.setText("" + this.bmsSettings.getMaxValue(pageNumber + 1));
	maxLabel3.setText("" + this.bmsSettings.getMaxValue(pageNumber + 2));
	maxLabel4.setText("" + this.bmsSettings.getMaxValue(pageNumber + 3));
	maxLabel5.setText("" + this.bmsSettings.getMaxValue(pageNumber + 4));
	maxLabel6.setText("" + this.bmsSettings.getMaxValue(pageNumber + 5));
	maxLabel7.setText("" + this.bmsSettings.getMaxValue(pageNumber + 6));
	maxLabel8.setText("" + this.bmsSettings.getMaxValue(pageNumber + 7));
	// Value Labels
	label1.setText("" + this.bmsSettings.getSetting(pageNumber + 0));
	label2.setText("" + this.bmsSettings.getSetting(pageNumber + 1));
	label3.setText("" + this.bmsSettings.getSetting(pageNumber + 2));
	label4.setText("" + this.bmsSettings.getSetting(pageNumber + 3));
	label5.setText("" + this.bmsSettings.getSetting(pageNumber + 4));
	label6.setText("" + this.bmsSettings.getSetting(pageNumber + 5));
	label7.setText("" + this.bmsSettings.getSetting(pageNumber + 6));
	label8.setText("" + this.bmsSettings.getSetting(pageNumber + 7));
	// Unit Label
	unitLabel1.setText("" + this.bmsSettings.getDisplayUnits(pageNumber + 0));
	unitLabel2.setText("" + this.bmsSettings.getDisplayUnits(pageNumber + 1));
	unitLabel3.setText("" + this.bmsSettings.getDisplayUnits(pageNumber + 2));
	unitLabel4.setText("" + this.bmsSettings.getDisplayUnits(pageNumber + 3));
	unitLabel5.setText("" + this.bmsSettings.getDisplayUnits(pageNumber + 4));
	unitLabel6.setText("" + this.bmsSettings.getDisplayUnits(pageNumber + 5));
	unitLabel7.setText("" + this.bmsSettings.getDisplayUnits(pageNumber + 6));
	unitLabel8.setText("" + this.bmsSettings.getDisplayUnits(pageNumber + 7));
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
    
    public void initData(LoadCell loadCell, int pageNumber)
    {
        this.filter = CANFilter.getInstance();
        this.loadCell = loadCell;
	this.bmsSettings = EAICS_Settings.getInstance().getBmsSettings();
	this.pageNumber = (pageNumber - 1) * 8;
	updateLabels();
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
    
    public BMSSettings getBMSSettings()
    {
	return this.bmsSettings;
    }
    
    @FXML
    private void handle1(ActionEvent event) 
    {
        openNumpad(pageNumber + 0);
    }
    
    @FXML
    private void handle2(ActionEvent event) 
    {
        openNumpad(pageNumber + 1);
    }
    
    @FXML
    private void handle3(ActionEvent event) 
    {
        openNumpad(pageNumber + 2);
    }
    
    @FXML
    private void handle4(ActionEvent event) 
    {
        openNumpad(pageNumber + 3);
    }
    
    @FXML
    private void handle5(ActionEvent event) 
    {
        openNumpad(pageNumber + 4);
    }
    
    @FXML
    private void handle6(ActionEvent event) 
    {
        openNumpad(pageNumber + 5);
    }
    
    @FXML
    private void handle7(ActionEvent event) 
    {
        openNumpad(pageNumber + 6);
    }
    
    @FXML
    private void handle8(ActionEvent event) 
    {
        openNumpad(pageNumber + 7);
    }
}
