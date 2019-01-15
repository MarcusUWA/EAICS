/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eaics.UI;

import eaics.CAN.CANFilter;
import eaics.Settings.SettingsEAICS;
import eaics.Settings.SettingsEVMS;
import eaics.Settings.SettingsGeneral.BMSType;
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
import javafx.scene.layout.VBox;
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
    private SettingsEAICS settings;
    
    int noModules = 8;
    int currentTab = 0;
    
    int count = 0;
    
    @FXML
    TabPane tabPane;
    
    @FXML
    Button closeButton;
    
    @FXML
    GridPane gridPane[];
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        
    }    
    
    public void initData(){
        this.filter = CANFilter.getInstance();
        this.settings = SettingsEAICS.getInstance();
        
        noModules = settings.getGeneralSettings().getNumBatteryModules();
        
        gridPane = new GridPane[noModules];
        
        for(int i = 0; i<noModules; i++) {
            tabPane.getTabs().add(createTab(i));        
        }
	
	Timeline refreshUI;
        refreshUI = new Timeline(new KeyFrame(Duration.millis(refreshFrequency), new EventHandler<ActionEvent>()  {
            @Override
            public void handle(ActionEvent event) {
		updateScreen();
            }
            
        }));
        refreshUI.setCycleCount(Timeline.INDEFINITE);
        refreshUI.play();
    }
    
    public void updateScreen() {
       for(int i = 0; i<noModules; i++) {
           gridPane[i] = updateGrid(i);
       }
       count++;
    }
    
    private GridPane updateGrid(int num) {
        
        GridPane gridPane;
            
        gridPane = new GridPane();
        gridPane.setAlignment(Pos.CENTER);
        gridPane.setVgap(10.0);
        gridPane.setHgap(10.0);
        
        final int numCols = 6;
        final int numRows = 4;
        
        //headings for the leftmost column
        for(int i = 0; i<numRows; i++) {
            Label cellName = new Label();
            cellName.setStyle("-fx-font-size: 18px;"
                + "-fx-font-weight: bold");
            
            cellName.setText("Col"+(i+1));
            
            gridPane.add(cellName, 0, i+1);
        }
        
        for(int i = 0; i<numCols; i++) {
            Label cellName = new Label();
            cellName.setStyle("-fx-font-size: 18px;"
                + "-fx-font-weight: bold");
            cellName.setText("Row"+(i+1));
            gridPane.add(cellName, i+1, 0);
        }
        
        Label tempLabel = new Label();
        tempLabel.setStyle("-fx-font-size: 18px;"
                + "-fx-font-weight: bold");
        tempLabel.setText("Temp ");
        
        gridPane.add(tempLabel, numCols+2, 0);
        
        SettingsEVMS bmsSettings = SettingsEAICS.getInstance().getEVMSSettings();
	int bmsMaxVoltage = bmsSettings.getSetting(14);
	int bmsBalanceVoltage = bmsSettings.getSetting(15);
	int bmsMinVoltage = bmsSettings.getSetting(13);
        
        for(int i = 0; i<numRows; i++) {
            for (int j = 0; j<numCols; j++) {
                Label labelVoltage = new Label();
                
                int voltage = 0;
                
                if(settings.getGeneralSettings().getBms()==BMSType.ZEVA3) {  
                    voltage = filter.getBMS()[(num*2)+(i/2)].getVoltage((i%2)*6+j)+count;
                }

                if(voltage > bmsMaxVoltage)	{
                    labelVoltage.setStyle("-fx-text-fill: red;"
                    +"-fx-font-size: 18px;");
                }
                else if(voltage <= bmsMaxVoltage && voltage >= bmsBalanceVoltage) {
                    labelVoltage.setStyle("-fx-text-fill: orange;"
                    +"-fx-font-size: 18px;");		
                }
                else if(voltage < bmsMinVoltage) {
                    labelVoltage.setStyle("-fx-text-fill: yellow;"
                    +"-fx-font-size: 18px;");
                }
                else {
                    labelVoltage.setStyle("-fx-text-fill: black;"
                    +"-fx-font-size: 18px;");
                }
                
                labelVoltage.setText(voltage+" V");
                gridPane.add(labelVoltage, j+1, i+1);
            }
        }
        
        return gridPane;
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

        gridPane[num] = updateGrid(num);

        vbox.getChildren().add(title);
        vbox.getChildren().add(gridPane[num]);
        
        tab.setContent(vbox);
        
        return tab; 
    }
    
    @FXML
    private void nextButtonAction(ActionEvent event) {
    }
    
    @FXML
    private void prevButtonAction(ActionEvent event) {
       if(currentTab>1) {
           currentTab--;
       }
    }
    
    @FXML
    private void closeButtonAction(ActionEvent event) {
        Stage stage = (Stage) closeButton.getScene().getWindow();
        stage.close();
    }
    
    
}

