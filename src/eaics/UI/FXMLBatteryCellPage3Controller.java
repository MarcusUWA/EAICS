/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eaics.UI;

import eaics.CAN.BMS;
import eaics.CAN.CANFilter;
import eaics.CAN.CCB;
import eaics.CAN.ESC;
import eaics.CAN.EVMS;
import eaics.SER.LoadCell;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Duration;

/**
 * FXML Controller class
 */
public class FXMLBatteryCellPage3Controller implements Initializable 
{
    MainUIController gui;
    
    //refresh rate in ms
    int refreshFrequency = 1000;
    
    private CANFilter filter;
    private LoadCell loadCell;
    
    @FXML
    private Label cellVoltage_0_0;
    
    @FXML
    private Label cellVoltage_0_1;
    
    @FXML
    private Label cellVoltage_0_2;
    
    @FXML
    private Label cellVoltage_0_3;
    
    @FXML
    private Label cellVoltage_0_4;
    
    @FXML
    private Label cellVoltage_0_5;
    
    @FXML
    private Label cellVoltage_0_6;
    
    @FXML
    private Label cellVoltage_0_7;
    
    @FXML
    private Label cellVoltage_1_0;
    
    @FXML
    private Label cellVoltage_1_1;
    
    @FXML
    private Label cellVoltage_1_2;
    
    @FXML
    private Label cellVoltage_1_3;
    
    @FXML
    private Label cellVoltage_1_4;
    
    @FXML
    private Label cellVoltage_1_5;
    
    @FXML
    private Label cellVoltage_1_6;
    
    @FXML
    private Label cellVoltage_1_7;
    
    @FXML
    private Label cellVoltage_2_0;
    
    @FXML
    private Label cellVoltage_2_1;
    
    @FXML
    private Label cellVoltage_2_2;
    
    @FXML
    private Label cellVoltage_2_3;
    
    @FXML
    private Label cellVoltage_2_4;
    
    @FXML
    private Label cellVoltage_2_5;
    
    @FXML
    private Label cellVoltage_2_6;
    
    @FXML
    private Label cellVoltage_2_7;
    
    @FXML
    private Label cellVoltage_3_0;
    
    @FXML
    private Label cellVoltage_3_1;
    
    @FXML
    private Label cellVoltage_3_2;
    
    @FXML
    private Label cellVoltage_3_3;
    
    @FXML
    private Label cellVoltage_3_4;
    
    @FXML
    private Label cellVoltage_3_5;
    
    @FXML
    private Label cellVoltage_3_6;
    
    @FXML
    private Label cellVoltage_3_7;
    
    @FXML
    private Label cellVoltage_4_0;
    
    @FXML
    private Label cellVoltage_4_1;
    
    @FXML
    private Label cellVoltage_4_2;
    
    @FXML
    private Label cellVoltage_4_3;
    
    @FXML
    private Label cellVoltage_4_4;
    
    @FXML
    private Label cellVoltage_4_5;
    
    @FXML
    private Label cellVoltage_4_6;
    
    @FXML
    private Label cellVoltage_4_7;
    
    @FXML
    private Label cellVoltage_5_0;
    
    @FXML
    private Label cellVoltage_5_1;
    
    @FXML
    private Label cellVoltage_5_2;
    
    @FXML
    private Label cellVoltage_5_3;
    
    @FXML
    private Label cellVoltage_5_4;
    
    @FXML
    private Label cellVoltage_5_5;
    
    @FXML
    private Label cellVoltage_5_6;
    
    @FXML
    private Label cellVoltage_5_7;
    
    @FXML
    private Label cellVoltage_6_0;
    
    @FXML
    private Label cellVoltage_6_1;
    
    @FXML
    private Label cellVoltage_6_2;
    
    @FXML
    private Label cellVoltage_6_3;
    
    @FXML
    private Label cellVoltage_6_4;
    
    @FXML
    private Label cellVoltage_6_5;
    
    @FXML
    private Label cellVoltage_6_6;
    
    @FXML
    private Label cellVoltage_6_7;
    
    @FXML
    private Label cellVoltage_7_0;
    
    @FXML
    private Label cellVoltage_7_1;
    
    @FXML
    private Label cellVoltage_7_2;
    
    @FXML
    private Label cellVoltage_7_3;
    
    @FXML
    private Label cellVoltage_7_4;
    
    @FXML
    private Label cellVoltage_7_5;
    
    @FXML
    private Label cellVoltage_7_6;
    
    @FXML
    private Label cellVoltage_7_7;
    
    @FXML
    private Label cellVoltage_8_0;
    
    @FXML
    private Label cellVoltage_8_1;
    
    @FXML
    private Label cellVoltage_8_2;
    
    @FXML
    private Label cellVoltage_8_3;
    
    @FXML
    private Label cellVoltage_8_4;
    
    @FXML
    private Label cellVoltage_8_5;
    
    @FXML
    private Label cellVoltage_8_6;
    
    @FXML
    private Label cellVoltage_8_7;
    
    @FXML
    private Label cellVoltage_9_0;
    
    @FXML
    private Label cellVoltage_9_1;
    
    @FXML
    private Label cellVoltage_9_2;
    
    @FXML
    private Label cellVoltage_9_3;
    
    @FXML
    private Label cellVoltage_9_4;
    
    @FXML
    private Label cellVoltage_9_5;
    
    @FXML
    private Label cellVoltage_9_6;
    
    @FXML
    private Label cellVoltage_9_7;
    
    @FXML
    private Label cellVoltage_10_0;
    
    @FXML
    private Label cellVoltage_10_1;
    
    @FXML
    private Label cellVoltage_10_2;
    
    @FXML
    private Label cellVoltage_10_3;
    
    @FXML
    private Label cellVoltage_10_4;
    
    @FXML
    private Label cellVoltage_10_5;
    
    @FXML
    private Label cellVoltage_10_6;
    
    @FXML
    private Label cellVoltage_10_7;
    
    @FXML
    private Label cellVoltage_11_0;
    
    @FXML
    private Label cellVoltage_11_1;
    
    @FXML
    private Label cellVoltage_11_2;
    
    @FXML
    private Label cellVoltage_11_3;
    
    @FXML
    private Label cellVoltage_11_4;
    
    @FXML
    private Label cellVoltage_11_5;
    
    @FXML
    private Label cellVoltage_11_6;
    
    @FXML
    private Label cellVoltage_11_7;
    
    @FXML
    private Label cellVoltage_12_0;
    
    @FXML
    private Label cellVoltage_12_1;
    
    @FXML
    private Label cellVoltage_12_2;
    
    @FXML
    private Label cellVoltage_12_3;
    
    @FXML
    private Label cellVoltage_12_4;
    
    @FXML
    private Label cellVoltage_12_5;
    
    @FXML
    private Label cellVoltage_12_6;
    
    @FXML
    private Label cellVoltage_12_7;
    
    @FXML
    private javafx.scene.control.Button closeButton;
    
    @FXML 
    private ImageView switchImage;
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb)
    {
        
    }
    
    public void initData(CANFilter fil, LoadCell cell) 
    {
        this.filter = fil;
        this.loadCell = cell;
        
        updateScreen();
	
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
    
    public void updateScreen()
    {
        EVMS evmsV3 = filter.getEVMS_v3();
        ESC[] esc = filter.getESC();
        BMS[] bms = filter.getBMS();
        
        CCB ccb = filter.getCCB();
        
        //CCB
        FileInputStream input = null;
        Image image;
        if(ccb.getCCB3() == true) {
            try {
                input = new FileInputStream("/home/pi/EAICS/images/switch_on.jpg");
            } catch (FileNotFoundException ex) {
                Logger.getLogger(MainUIController.class.getName()).log(Level.SEVERE, null, ex);
            }
                            
            image = new Image(input);
            switchImage.setImage(image);
        }
        else {
            try {
                input = new FileInputStream("/home/pi/EAICS/images/switch_off.jpg");
            } catch (FileNotFoundException ex) {
                Logger.getLogger(MainUIController.class.getName()).log(Level.SEVERE, null, ex);
            }
                            
            image = new Image(input);
            switchImage.setImage(image);
        }

        //+------------------------------------------------------------+
        //BMS Module 17 (switch set to 0): 1 - 12 Cells
        //+------------------------------------------------------------+

        cellVoltage_0_0.setText((bms[16].getVoltage(0) / 1000.0) + "V");

        cellVoltage_1_0.setText((bms[16].getVoltage(1) / 1000.0) + "V");

        cellVoltage_2_0.setText((bms[16].getVoltage(2) / 1000.0) + "V");

        cellVoltage_3_0.setText((bms[16].getVoltage(3) / 1000.0) + "V");

        cellVoltage_4_0.setText((bms[16].getVoltage(4) / 1000.0) + "V");

        cellVoltage_5_0.setText((bms[16].getVoltage(5) / 1000.0) + "V");

        cellVoltage_6_0.setText((bms[16].getVoltage(6) / 1000.0) + "V");

        cellVoltage_7_0.setText((bms[16].getVoltage(7) / 1000.0) + "V");

        cellVoltage_8_0.setText((bms[16].getVoltage(8) / 1000.0) + "V");

        cellVoltage_9_0.setText((bms[16].getVoltage(9) / 1000.0) + "V");

        cellVoltage_10_0.setText((bms[16].getVoltage(10) / 1000.0) + "V");

        cellVoltage_11_0.setText((bms[16].getVoltage(11) / 1000.0) + "V");

        //+------------------------------------------------------------+
        //BMS Module 18 (switch set to 1): 1 - 12 Cells
        //+------------------------------------------------------------+

        cellVoltage_0_1.setText((bms[17].getVoltage(0) / 1000.0) + "V");

        cellVoltage_1_1.setText((bms[17].getVoltage(1) / 1000.0) + "V");

        cellVoltage_2_1.setText((bms[17].getVoltage(2) / 1000.0) + "V");

        cellVoltage_3_1.setText((bms[17].getVoltage(3) / 1000.0) + "V");

        cellVoltage_4_1.setText((bms[17].getVoltage(4) / 1000.0) + "V");

        cellVoltage_5_1.setText((bms[17].getVoltage(5) / 1000.0) + "V");

        cellVoltage_6_1.setText((bms[17].getVoltage(6) / 1000.0) + "V");

        cellVoltage_7_1.setText((bms[17].getVoltage(7) / 1000.0) + "V");

        cellVoltage_8_1.setText((bms[17].getVoltage(8) / 1000.0) + "V");

        cellVoltage_9_1.setText((bms[17].getVoltage(9) / 1000.0) + "V");

        cellVoltage_10_1.setText((bms[17].getVoltage(10) / 1000.0) + "V");

        cellVoltage_11_1.setText((bms[17].getVoltage(11) / 1000.0) + "V");

        //+------------------------------------------------------------+
        //BMS Module 19 (switch set to 2): 1 - 12 Cells
        //+------------------------------------------------------------+

        cellVoltage_0_2.setText((bms[18].getVoltage(0) / 1000.0) + "V");

        cellVoltage_1_2.setText((bms[18].getVoltage(1) / 1000.0) + "V");

        cellVoltage_2_2.setText((bms[18].getVoltage(2) / 1000.0) + "V");

        cellVoltage_3_2.setText((bms[18].getVoltage(3) / 1000.0) + "V");

        cellVoltage_4_2.setText((bms[18].getVoltage(4) / 1000.0) + "V");

        cellVoltage_5_2.setText((bms[18].getVoltage(5) / 1000.0) + "V");

        cellVoltage_6_2.setText((bms[18].getVoltage(6) / 1000.0) + "V");

        cellVoltage_7_2.setText((bms[18].getVoltage(7) / 1000.0) + "V");

        cellVoltage_8_2.setText((bms[18].getVoltage(8) / 1000.0) + "V");

        cellVoltage_9_2.setText((bms[18].getVoltage(9) / 1000.0) + "V");

        cellVoltage_10_2.setText((bms[18].getVoltage(10) / 1000.0) + "V");

        cellVoltage_11_2.setText((bms[18].getVoltage(11) / 1000.0) + "V");

        //+------------------------------------------------------------+
        //BMS Module 20 (switch set to 3): 1 - 12 Cells
        //+------------------------------------------------------------+

        cellVoltage_0_3.setText((bms[19].getVoltage(0) / 1000.0) + "V");

        cellVoltage_1_3.setText((bms[19].getVoltage(1) / 1000.0) + "V");

        cellVoltage_2_3.setText((bms[19].getVoltage(2) / 1000.0) + "V");

        cellVoltage_3_3.setText((bms[19].getVoltage(3) / 1000.0) + "V");

        cellVoltage_4_3.setText((bms[19].getVoltage(4) / 1000.0) + "V");

        cellVoltage_5_3.setText((bms[19].getVoltage(5) / 1000.0) + "V");

        cellVoltage_6_3.setText((bms[19].getVoltage(6) / 1000.0) + "V");

        cellVoltage_7_3.setText((bms[19].getVoltage(7) / 1000.0) + "V");

        cellVoltage_8_3.setText((bms[19].getVoltage(8) / 1000.0) + "V");

        cellVoltage_9_3.setText((bms[19].getVoltage(9) / 1000.0) + "V");

        cellVoltage_10_3.setText((bms[19].getVoltage(10) / 1000.0) + "V");

        cellVoltage_11_3.setText((bms[19].getVoltage(11) / 1000.0) + "V");

        //+------------------------------------------------------------+
        //BMS Module 21 (switch set to 4): 1 - 12 Cells
        //+------------------------------------------------------------+

        cellVoltage_0_4.setText((bms[20].getVoltage(0) / 1000.0) + "V");

        cellVoltage_1_4.setText((bms[20].getVoltage(1) / 1000.0) + "V");

        cellVoltage_2_4.setText((bms[20].getVoltage(2) / 1000.0) + "V");

        cellVoltage_3_4.setText((bms[20].getVoltage(3) / 1000.0) + "V");

        cellVoltage_4_4.setText((bms[20].getVoltage(4) / 1000.0) + "V");

        cellVoltage_5_4.setText((bms[20].getVoltage(5) / 1000.0) + "V");

        cellVoltage_6_4.setText((bms[20].getVoltage(6) / 1000.0) + "V");

        cellVoltage_7_4.setText((bms[20].getVoltage(7) / 1000.0) + "V");

        cellVoltage_8_4.setText((bms[20].getVoltage(8) / 1000.0) + "V");

        cellVoltage_9_4.setText((bms[20].getVoltage(9) / 1000.0) + "V");

        cellVoltage_10_4.setText((bms[20].getVoltage(10) / 1000.0) + "V");

        cellVoltage_11_4.setText((bms[20].getVoltage(11) / 1000.0) + "V");

        //+------------------------------------------------------------+
        //BMS Module 22 (switch set to 5): 1 - 12 Cells
        //+------------------------------------------------------------+

        cellVoltage_0_5.setText((bms[21].getVoltage(0) / 1000.0) + "V");

        cellVoltage_1_5.setText((bms[21].getVoltage(1) / 1000.0) + "V");

        cellVoltage_2_5.setText((bms[21].getVoltage(2) / 1000.0) + "V");

        cellVoltage_3_5.setText((bms[21].getVoltage(3) / 1000.0) + "V");

        cellVoltage_4_5.setText((bms[21].getVoltage(4) / 1000.0) + "V");

        cellVoltage_5_5.setText((bms[21].getVoltage(5) / 1000.0) + "V");

        cellVoltage_6_5.setText((bms[21].getVoltage(6) / 1000.0) + "V");

        cellVoltage_7_5.setText((bms[21].getVoltage(7) / 1000.0) + "V");

        cellVoltage_8_5.setText((bms[21].getVoltage(8) / 1000.0) + "V");

        cellVoltage_9_5.setText((bms[21].getVoltage(9) / 1000.0) + "V");

        cellVoltage_10_5.setText((bms[21].getVoltage(10) / 1000.0) + "V");

        cellVoltage_11_5.setText((bms[21].getVoltage(11) / 1000.0) + "V");

        //+------------------------------------------------------------+
        //BMS Module 23 (switch set to 6): 1 - 12 Cells
        //+------------------------------------------------------------+

        cellVoltage_0_6.setText((bms[22].getVoltage(0) / 1000.0) + "V");

        cellVoltage_1_6.setText((bms[22].getVoltage(1) / 1000.0) + "V");

        cellVoltage_2_6.setText((bms[22].getVoltage(2) / 1000.0) + "V");

        cellVoltage_3_6.setText((bms[22].getVoltage(3) / 1000.0) + "V");

        cellVoltage_4_6.setText((bms[22].getVoltage(4) / 1000.0) + "V");

        cellVoltage_5_6.setText((bms[22].getVoltage(5) / 1000.0) + "V");

        cellVoltage_6_6.setText((bms[22].getVoltage(6) / 1000.0) + "V");

        cellVoltage_7_6.setText((bms[22].getVoltage(7) / 1000.0) + "V");

        cellVoltage_8_6.setText((bms[22].getVoltage(8) / 1000.0) + "V");

        cellVoltage_9_6.setText((bms[22].getVoltage(9) / 1000.0) + "V");

        cellVoltage_10_6.setText((bms[22].getVoltage(10) / 1000.0) + "V");

        cellVoltage_11_6.setText((bms[22].getVoltage(11) / 1000.0) + "V");

        //+------------------------------------------------------------+
        //BMS Module 24 (switch set to 7): 1 - 12 Cells
        //+------------------------------------------------------------+

        cellVoltage_0_7.setText((bms[23].getVoltage(0) / 1000.0) + "V");

        cellVoltage_1_7.setText((bms[23].getVoltage(1) / 1000.0) + "V");

        cellVoltage_2_7.setText((bms[23].getVoltage(2) / 1000.0) + "V");

        cellVoltage_3_7.setText((bms[23].getVoltage(3) / 1000.0) + "V");

        cellVoltage_4_7.setText((bms[23].getVoltage(4) / 1000.0) + "V");

        cellVoltage_5_7.setText((bms[23].getVoltage(5) / 1000.0) + "V");

        cellVoltage_6_7.setText((bms[23].getVoltage(6) / 1000.0) + "V");

        cellVoltage_7_7.setText((bms[23].getVoltage(7) / 1000.0) + "V");

        cellVoltage_8_7.setText((bms[23].getVoltage(8) / 1000.0) + "V");

        cellVoltage_9_7.setText((bms[23].getVoltage(9) / 1000.0) + "V");

        cellVoltage_10_7.setText((bms[23].getVoltage(10) / 1000.0) + "V");

        cellVoltage_11_7.setText((bms[23].getVoltage(11) / 1000.0) + "V");
    }

    public void initSettings(MainUIController mainGui) 
    {
        gui = mainGui;
    }
    
    @FXML
    private void closeButtonAction(ActionEvent event)
    {
        Stage stage = (Stage) closeButton.getScene().getWindow();
        stage.close();
    }
}
