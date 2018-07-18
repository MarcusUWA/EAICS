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
import eaics.IPaddress;
import eaics.SER.LoadCell;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
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
    
    MainUIController gui;
    
    //refresh rate in ms
    int refreshFrequency = 10;
    
    private CANFilter filter;
    private LoadCell loadCell;
    
    private String msg;
    
    //@FXML
    //private Label label1;
    
    @FXML
    private Label labelLAN;
    
    @FXML
    private Label labelWiFi;
    
    @FXML
    private Label label_IPaddress1;
    
    @FXML
    private Label label_IPaddress2;
    
    @FXML
    private javafx.scene.control.Button closeButton;
    
    @FXML
    private void closeButtonAction(ActionEvent event)
    {
        Stage stage = (Stage) closeButton.getScene().getWindow();
        stage.close();
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
    private void handleSendBMSCANMsg(ActionEvent event) throws IOException
    {
        System.out.println("Sending a CAN msg!"); //delete this after testing please.
	
	//msg = "7DF#0201050000000000";
        //msg = "00a#005f41008077004f"; //message to test EVMS
        //msg = "";
        //unsigned int exampleVoltage = 3600; // millivolts
        //data[0] = exampleVoltage >> 8;
        //data[1] = exampleVoltage & 0xFF;
        //CanTX(300 + 10*moduleID, data);
        String temp = "";
        int exampleVoltage = 3600; // millivolts
        int moduleID = 2;
        msg = "00000";
        msg += Integer.toHexString(300 + 10*moduleID);
	
        msg += "#";
	
        temp += Integer.toHexString(exampleVoltage >> 8);
	if(temp.length() == 1)
	{
	    msg += "0";
	}
	msg += temp;
	temp = "";
	
	temp += Integer.toHexString(exampleVoltage & 0xFF);
	if(temp.length() == 1)
	{
	    msg += "0";
	}
	msg += temp;
	temp = "";
	
        System.out.println("Message>>"+msg+"<<");
	final Process loadCellProgram = Runtime.getRuntime().exec("/home/pi/bin/CANsend can0 " + msg);
    }
    
    @FXML
    private void handleSendEVMSCANMsg(ActionEvent event) throws IOException
    {
        System.out.println("Sending a CAN msg!"); //delete this after testing please.
        msg = "00a#005f41008077004f"; //message to test EVMS
        System.out.println("Message>>"+msg+"<<");
	final Process loadCellProgram = Runtime.getRuntime().exec("/home/pi/bin/CANsend can0 " + msg);
    }
    
    @FXML
    private void handleSendGenericCANMsg(ActionEvent event) throws IOException
    {
        System.out.println("Sending a CAN msg!"); //delete this after testing please.
        msg = "7DF#0201050000000000";
        System.out.println("Message>>"+msg+"<<");
	final Process loadCellProgram = Runtime.getRuntime().exec("/home/pi/bin/CANsend can0 " + msg);
    }
    
    @FXML
    private void handleRefreshIP(ActionEvent event) throws IOException
    {
        IPaddress ipAddress = new IPaddress();
        
        String[] splited = ipAddress.getIPaddress().split("\\s+");
        
        if (splited.length == 1)
        {
            labelWiFi.setText("WiFi");
            label_IPaddress1.setText(splited[0]);            
        }
        else if(splited.length == 2)
        {
            labelWiFi.setText("WiFi");
            label_IPaddress1.setText(splited[1]);
            labelLAN.setText("LAN");
            label_IPaddress2.setText(splited[0]);
        }
        
        
        
        //label_IPaddress.setText("hello");
    }
    
    public void initSettings(MainUIController mainGui) 
    {
        gui = mainGui;
    }

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) 
    {
        // TODO
	System.out.println("hello in Settings");
        //
    }    
    
}
