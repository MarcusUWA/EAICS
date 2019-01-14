/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eaics.UI;

import eaics.CAN.CANFilter;
import eaics.Settings.EAICS_Settings;
import eaics.Settings.EVMSSettings;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;
import javafx.util.Duration;

/**
 * FXML Controller class
 *
 * @author Markcuz
 */
public class FXMLCellPageController implements Initializable {
    
    int refreshFrequency = 1000;
    private CANFilter filter;
    
    int noModules = 8;
    int currentTab = 0;
    
    @FXML
    TabPane tabPane;
    
    @FXML
    Button closeButton;
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        
    }    
    
    public void initData(){
        this.filter = CANFilter.getInstance();
        
        for(int i = 0; i<noModules; i++) {
            tabPane.getTabs().add(createTab(i));
        }
	
	Timeline refreshUI;
        refreshUI = new Timeline(new KeyFrame(Duration.millis(refreshFrequency), new EventHandler<ActionEvent>() 
	{
            int count = 0;
            @Override
            public void handle(ActionEvent event) 
	    {
		updateScreen();
            }
            
        }));
        refreshUI.setCycleCount(Timeline.INDEFINITE);
        refreshUI.play();
    }
    
    public void updateScreen() {
        
    }
    
    private Tab createTab(int num) {
        
        Tab tab = new Tab("M" + num);
        
        VBox vbox = new VBox();
        vbox.setAlignment(Pos.CENTER);
        vbox.setSpacing(10.0);
        
        Label title = new Label();
        title.setStyle("-fx-font-size: 24px;"
                + "-fx-font-weight: bold");
        title.setAlignment(Pos.CENTER);
        title.setText("Battery Module: "+num);
     
        GridPane gridPane = new GridPane();
        gridPane.setAlignment(Pos.CENTER);
        gridPane.setVgap(20.0);
        gridPane.setHgap(20.0);
        
        final int numCols = 6;
        final int numRows = 4;
        
        //headings for the leftmost column
        for(int i = 0; i<numRows; i++) {
            Label cellName = new Label();
            cellName.setStyle("-fx-font-size: 24px;"
                + "-fx-font-weight: bold");
            
            cellName.setText("Cell"+(i+1));
            
            gridPane.add(cellName, 0, i+1);
        }
        
        for(int i = 0; i<numCols; i++) {
            Label cellName = new Label();
            cellName.setStyle("-fx-font-size: 24px;"
                + "-fx-font-weight: bold");
            cellName.setText("Col"+(i+1));
            gridPane.add(cellName, i+1, 0);
        }
        
        Label tempLabel = new Label();
        tempLabel.setStyle("-fx-font-size: 24px;"
                + "-fx-font-weight: bold");
        tempLabel.setText("Temp ");
        
        gridPane.add(tempLabel, numCols+2, 0);
        
        EVMSSettings bmsSettings = EAICS_Settings.getInstance().getEVMSSettings();
	int bmsMaxVoltage = bmsSettings.getSetting(14);
	int bmsBalanceVoltage = bmsSettings.getSetting(15);
	int bmsMinVoltage = bmsSettings.getSetting(13);
        
        for(int i = 0; i<numRows; i++) {
            for (int j = 0; j<numCols; j++) {
                Label voltage = new Label();
                int temp = i*6+j;
                
                if(temp > bmsMaxVoltage/1000)	{
                    voltage.setStyle("-fx-text-fill: red;"
                    +"-fx-font-size: 24px;");
                }
                else if(temp <= bmsMaxVoltage/1000 && temp >= bmsBalanceVoltage/1000) {
                    voltage.setStyle("-fx-text-fill: orange;"
                    +"-fx-font-size: 24px;");		
                }
                else if(temp < bmsMinVoltage/1000) {
                    voltage.setStyle("-fx-text-fill: yellow;"
                    +"-fx-font-size: 24px;");
                }
                else {
                    voltage.setStyle("-fx-text-fill: black;"
                    +"-fx-font-size: 24px;");
                }
                
                voltage.setText(temp+"V");
                gridPane.add(voltage, j+1, i+1);
            }
        }
        
        vbox.getChildren().add(title);
        vbox.getChildren().add(gridPane);
        
        //tab.setContent(gridPane);
        
        tab.setContent(vbox);
        return tab; 
    }
    
    @FXML
    private void closeButtonAction(ActionEvent event) {
        Stage stage = (Stage) closeButton.getScene().getWindow();
        stage.close();
    }
}

