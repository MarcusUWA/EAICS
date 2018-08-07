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
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Duration;

/**
 * FXML Controller class
 *
 * @author Troy
 */
public class FXMLSettingsController implements Initializable 
{
    FXMLSetupController setup;
    FXMLConnectWifiController wifiConnectController;
    
    @FXML
    Button buttonSetup;
    
    @FXML 
    Button wifiConnect;
    
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
    private Button buttonUpdateSoftware;
    
    @FXML
    private javafx.scene.control.Button closeButton;
    
    @FXML
    private void closeButtonAction(ActionEvent event)
    {
        Stage stage = (Stage) closeButton.getScene().getWindow();
        stage.close();
    }

    @FXML   
    private void handleEnterSetup(ActionEvent event)
    {   
        FXMLLoader loader = new FXMLLoader(getClass().getResource("FXMLSetup.fxml"));
        
        try 
        {
            Pane pane = loader.load();

            setup = loader.getController();
            setup.initSettings(gui);
            setup.initData(filter, loadCell);
        
            Stage stage = new Stage();
        
            stage.initModality(Modality.WINDOW_MODAL);
            stage.initOwner(buttonSetup.getScene().getWindow());
        
            Scene scene = new Scene(pane);
        
            stage.setScene(scene);
            stage.setTitle("Battery Management Setup!!");
            
            stage.show();
        }
        
        catch (Exception e) 
        {
            System.out.println("Failed to open Setup Window");
            e.printStackTrace();
        }
    }    
    
    @FXML
    private void handleSendBMSCANMsg(ActionEvent event) throws IOException
    {
        System.out.println("Sending a CAN msg!"); //delete this after testing please.
	
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
    private void handleSendInd0(ActionEvent event) throws IOException
    {
        System.out.println("Sending a CAN msg!"); //delete this after testing please.
	
        String temp = "";
        int exampleVoltage = 3600; // millivolts
        int moduleID = 0 + 16;
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
    private void handleSendInd1(ActionEvent event) throws IOException
    {
        System.out.println("Sending a CAN msg!"); //delete this after testing please.
	
        String temp = "";
        int exampleVoltage = 3600; // millivolts
        int moduleID = 1 + 16;
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
    private void handleSendESC1(ActionEvent event) throws IOException
    {
        System.out.println("Sending a CAN msg!"); //delete this after testing please.
        msg = "14a10001#39178b00be0000"; //message to test ESC 1st packet
        System.out.println("Message>>"+msg+"<<");
	final Process loadCellProgram = Runtime.getRuntime().exec("/home/pi/bin/CANsend can0 " + msg);
    }
    
    @FXML
    private void handleSendESC2(ActionEvent event) throws IOException
    {
        System.out.println("Sending a CAN msg!"); //delete this after testing please.
        msg = "14a10002#bdc8ffff1b0000"; //message to test ESC 2nd packet
        System.out.println("Message>>"+msg+"<<");
	final Process loadCellProgram = Runtime.getRuntime().exec("/home/pi/bin/CANsend can0 " + msg);
    }
    
    @FXML
    private void handleSendESC3(ActionEvent event) throws IOException
    {
        System.out.println("Sending a CAN msg!"); //delete this after testing please.
        msg = "14a10003#a701a70100000000"; //message to test ESC 3rd packet
        System.out.println("Message>>"+msg+"<<");
	final Process loadCellProgram = Runtime.getRuntime().exec("/home/pi/bin/CANsend can0 " + msg);
    }
    
    @FXML
    private void handleSendESC4(ActionEvent event) throws IOException
    {
        System.out.println("Sending a CAN msg!"); //delete this after testing please.
        msg = "14a10004#6426010000000100"; //message to test ESC 4th packet
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
    }
    
    @FXML   
    private void handleWifiSetup(ActionEvent event)
    {   
        FXMLLoader loader = new FXMLLoader(getClass().getResource("FXMLConnectWifi.fxml"));
        
        try 
        {
            Pane pane = loader.load();

            wifiConnectController = loader.getController();
            wifiConnectController.test();
        
            Stage stage = new Stage();
        
            stage.initModality(Modality.WINDOW_MODAL);
            stage.initOwner(buttonSetup.getScene().getWindow());
        
            Scene scene = new Scene(pane);
        
            stage.setScene(scene);
            stage.setTitle("Wifi Setup!!");
            
            stage.show();
        }
        
        catch (Exception e) 
        {
            System.out.println("Failed to open Wifi Window");
            e.printStackTrace();
        }
    }    
    
    public void initSettings(MainUIController mainGui) 
    {
        gui = mainGui;
    }
    
    public void initData(CANFilter filter, LoadCell loadCell)
    {
        this.filter = filter;
        this.loadCell = loadCell;
    }

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) 
    {   
        try
        {
            handleRefreshIP(new ActionEvent());
        }
        catch(IOException e)
        {
            e.printStackTrace();
        }
    }  
    
    @FXML
    private void updateButtonAction(ActionEvent event) throws IOException
    {
        Runtime.getRuntime().exec("sudo wget http://robotics.ee.uwa.edu.au/courses/des/rasp/images-pi1/EAICS.jar");
        Runtime.getRuntime().exec("cp EAICS.jar /home/pi/EAICS/dist/EAICS.jar");
    }    
}
