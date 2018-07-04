/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eaics.UI;

import eaics.CAN.BMS;
import eaics.CAN.CANFilter;
import eaics.CAN.ESC;
import eaics.CAN.EVMS;
import eaics.SER.LoadCell;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import javafx.util.Duration;

/**
 * FXML Controller class
 *
 * @author Troy
 */
public class FXMLSettingsController implements Initializable 
{
    //refresh rate in ms
    int refreshFrequency = 10;
    
    CANFilter filter;
    LoadCell loadCell;
    
    private String msg;
    
    @FXML
    private Label label1;
    
    @FXML
    private javafx.scene.control.Button closeButton;
    
    @FXML
    private void closeButtonAction(ActionEvent event)
    {
	Node source = (Node) event.getSource();
	Stage stage = (Stage) source.getScene().getWindow();
	stage.close();
	//get a handle to the stage
	//Stage stage = (Stage) closeButton.getScene().getWindow();
	//close the stage
	//stage.close();
    }
    /*
    @FXML
    private void handleMainMenuPressed(ActionEvent event) throws IOException
    {
        System.out.println("You clicked me! - Main Menu");
        //FXMLLoader loader = new FXMLLoader(getClass().getResource("MainUI.fxml"));
	
	Parent main_page_parent = FXMLLoader.load(getClass().getResource("MainUI.fxml"));
	Scene main_page_scene = new Scene(main_page_parent);
	Stage app_stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
	app_stage.setScene(main_page_scene);
	app_stage.show();
    }
    */
    @FXML
    private void handleSendCANMsg(ActionEvent event) throws IOException
    {
        System.out.println("Sending a CAN msg!"); //delete this after testing please.
	
	msg = "7DF#0201050000000000";
	final Process loadCellProgram = Runtime.getRuntime().exec("/home/pi/bin/CANsend can0 " + msg);
    }
    
    public void initData(CANFilter fil, LoadCell cell) 
    {
        this.filter = fil;
        this.loadCell = cell;
    }

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) 
    {
        // TODO
	
	Timeline refreshUI;
        refreshUI = new Timeline(new KeyFrame(Duration.millis(refreshFrequency), new EventHandler<ActionEvent>() 
	{
            int count = 0;
            @Override
            public void handle(ActionEvent event) 
	    {
                label1.setText("" + count);
		count++;
		count %= 100;
            }
            
        }));
        refreshUI.setCycleCount(Timeline.INDEFINITE);
        refreshUI.play();
    }    
    
}