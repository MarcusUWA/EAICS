/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eaics.UI.FXMLSettings;
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
import eaics.Settings.SettingsEVMS;
import eaics.Settings.SettingsEAICS;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

/**
 * FXML Controller class
 *
 * @author Markcuz & Troy
 */
public class FXMLAdvZEVASettingsPage implements Initializable{
    private int pageIndex;
    FXMLNumpadController numpad;
    private SettingsEVMS bmsSettings;
    
    // Title Label
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
    
    @FXML
    private Button closeButton;
    
    public void updateLabels() {
	// Title Label
	titleLabel.setText("Advanced ZEVA Settings - Page " + ((pageIndex / 8) + 1));
	// Name Labels
	nameLabel1.setText("" + this.bmsSettings.getName(pageIndex + 0));
	nameLabel2.setText("" + this.bmsSettings.getName(pageIndex + 1));
	nameLabel3.setText("" + this.bmsSettings.getName(pageIndex + 2));
	nameLabel4.setText("" + this.bmsSettings.getName(pageIndex + 3));
	nameLabel5.setText("" + this.bmsSettings.getName(pageIndex + 4));
	nameLabel6.setText("" + this.bmsSettings.getName(pageIndex + 5));
	nameLabel7.setText("" + this.bmsSettings.getName(pageIndex + 6));
	nameLabel8.setText("" + this.bmsSettings.getName(pageIndex + 7));
	// Min Labels
	minLabel1.setText("" + this.bmsSettings.getMinValue(pageIndex + 0));
	minLabel2.setText("" + this.bmsSettings.getMinValue(pageIndex + 1));
	minLabel3.setText("" + this.bmsSettings.getMinValue(pageIndex + 2));
	minLabel4.setText("" + this.bmsSettings.getMinValue(pageIndex + 3));
	minLabel5.setText("" + this.bmsSettings.getMinValue(pageIndex + 4));
	minLabel6.setText("" + this.bmsSettings.getMinValue(pageIndex + 5));
	minLabel7.setText("" + this.bmsSettings.getMinValue(pageIndex + 6));
	minLabel8.setText("" + this.bmsSettings.getMinValue(pageIndex + 7));
	// Max Labels
	maxLabel1.setText("" + this.bmsSettings.getMaxValue(pageIndex + 0));
	maxLabel2.setText("" + this.bmsSettings.getMaxValue(pageIndex + 1));
	maxLabel3.setText("" + this.bmsSettings.getMaxValue(pageIndex + 2));
	maxLabel4.setText("" + this.bmsSettings.getMaxValue(pageIndex + 3));
	maxLabel5.setText("" + this.bmsSettings.getMaxValue(pageIndex + 4));
	maxLabel6.setText("" + this.bmsSettings.getMaxValue(pageIndex + 5));
	maxLabel7.setText("" + this.bmsSettings.getMaxValue(pageIndex + 6));
	maxLabel8.setText("" + this.bmsSettings.getMaxValue(pageIndex + 7));
	// Value Labels
	label1.setText("" + this.bmsSettings.getSetting(pageIndex + 0));
	label1.setStyle("-fx-font-weight: bold;");
	label2.setText("" + this.bmsSettings.getSetting(pageIndex + 1));
	label2.setStyle("-fx-font-weight: bold;");
	label3.setText("" + this.bmsSettings.getSetting(pageIndex + 2));
	label3.setStyle("-fx-font-weight: bold;");
	label4.setText("" + this.bmsSettings.getSetting(pageIndex + 3));
	label4.setStyle("-fx-font-weight: bold;");
	label5.setText("" + this.bmsSettings.getSetting(pageIndex + 4));
	label5.setStyle("-fx-font-weight: bold;");
	label6.setText("" + this.bmsSettings.getSetting(pageIndex + 5));
	label6.setStyle("-fx-font-weight: bold;");
	label7.setText("" + this.bmsSettings.getSetting(pageIndex + 6));
	label7.setStyle("-fx-font-weight: bold;");
	label8.setText("" + this.bmsSettings.getSetting(pageIndex + 7));
	label8.setStyle("-fx-font-weight: bold;");
	// Unit Label
	unitLabel1.setText("" + this.bmsSettings.getDisplayUnits(pageIndex + 0));
	unitLabel2.setText("" + this.bmsSettings.getDisplayUnits(pageIndex + 1));
	unitLabel3.setText("" + this.bmsSettings.getDisplayUnits(pageIndex + 2));
	unitLabel4.setText("" + this.bmsSettings.getDisplayUnits(pageIndex + 3));
	unitLabel5.setText("" + this.bmsSettings.getDisplayUnits(pageIndex + 4));
	unitLabel6.setText("" + this.bmsSettings.getDisplayUnits(pageIndex + 5));
	unitLabel7.setText("" + this.bmsSettings.getDisplayUnits(pageIndex + 6));
	unitLabel8.setText("" + this.bmsSettings.getDisplayUnits(pageIndex + 7));
    }
       
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    
    
    public void initData(int pageNumber) {
	this.bmsSettings = SettingsEAICS.getInstance().getEVMSSettings();
	this.pageIndex = (pageNumber - 1) * 8;
	updateLabels();
    }
    
    @FXML
    private void closeButtonAction(ActionEvent event){
        Stage stage = (Stage) closeButton.getScene().getWindow();
        stage.close();
    }
    
    private void openNumpad(int index) {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("FXMLNumpad.fxml"));
        
        try {
            Pane pane = loader.load();
            numpad = loader.getController();
            numpad.importAdvZEVA(this);
            numpad.setBMSIndex(index, FXMLNumpadController.CONFIG_BMS_NUMPAD);
        
            Stage stage = new Stage();
        
            stage.initModality(Modality.WINDOW_MODAL);
            stage.initOwner(closeButton.getScene().getWindow());
        
            Scene scene = new Scene(pane);
        
            stage.setScene(scene);
            stage.setTitle("Numpad");
            
            stage.show();
        }        
        catch (Exception e)  {
            System.out.println("Failed to open Numpad Window");
            e.printStackTrace();
        }
    }
    
    public SettingsEVMS getBMSSettings(){
	return this.bmsSettings;
    }
    
    @FXML 
    private void next(ActionEvent event) {
        if(pageIndex<17) {
            pageIndex += 8;
            updateLabels();
        }
    }
    
    @FXML 
    private void back(ActionEvent event) {
        if(pageIndex>7) {
            pageIndex -= 8;
            updateLabels();
        }
    }
    
    @FXML
    private void handle1(ActionEvent event) {
        openNumpad(pageIndex + 0);
    }
    
    @FXML
    private void handle2(ActionEvent event) {
        openNumpad(pageIndex + 1);
    }
    
    @FXML
    private void handle3(ActionEvent event) {
        openNumpad(pageIndex + 2);
    }
    
    @FXML
    private void handle4(ActionEvent event) {
        openNumpad(pageIndex + 3);
    }
    
    @FXML
    private void handle5(ActionEvent event) {
        openNumpad(pageIndex + 4);
    }
    
    @FXML
    private void handle6(ActionEvent event) {
        openNumpad(pageIndex + 5);
    }
    
    @FXML
    private void handle7(ActionEvent event) {
        openNumpad(pageIndex + 6);
    }
    
    @FXML
    private void handle8(ActionEvent event) {
        openNumpad(pageIndex + 7);
    }
}
