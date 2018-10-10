/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eaics.UI;

import eaics.CAN.CANFilter;
import eaics.IPaddress;
import eaics.SER.LoadCell;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author Troy
 */
public class FXMLSettingsController implements Initializable 
{
    FXMLBMSsettingsPage bmsSettingsPage;
    
    FXMLConnectWifiController wifiConnectController;
    
    @FXML
    Button buttonStopLogging;
    
    @FXML
    Button buttonKillProgram;
    
    @FXML
    Button buttonResetWarnings;
    
    @FXML
    Button buttonGeneralSettingsPage1;
    
    @FXML
    Button buttonGeneralSettingsPage2;
    
    @FXML
    Button buttonGeneralSettingsPage3;
    
    @FXML
    Button buttonGeneralSettingsPage4;
    
    @FXML
    Button buttonResetSOC;
    
    @FXML 
    Button wifiConnect;
    
    MainUIController gui;
    
    //refresh rate in ms
    int refreshFrequency = 10;
    
    private CANFilter filter;
    private LoadCell loadCell;
    
    private String msg;
    
    @FXML
    private Label labelLAN;
    
    @FXML
    private Label label_IPaddress1;
    
    @FXML
    private Label label_IPaddress2;
    
    @FXML
    private Label softwareVersionLabel;
    
    @FXML
    private Button buttonUpdateSoftware;
    
    @FXML
    private javafx.scene.control.Button closeButton;
    
    @FXML
    private void handleKillProgram(ActionEvent event) throws IOException
    {
        filter.stopLogging();
        //final Process killPIXHAWK_Program = Runtime.getRuntime().exec("sudo pkill mavproxy.py");
        //final Process killCAN_Program = Runtime.getRuntime().exec("sudo killall ReadCAN");
        //final Process killLoadCell_Program = Runtime.getRuntime().exec("sudo pkill LoadCell");
        final Process killEAICS_Program = Runtime.getRuntime().exec("sudo pkill java");
    }
    
    @FXML
    private void handleResetWarnings(ActionEvent event)
    {
        filter.resetAllWarnings();
    }
    
    @FXML
    private void closeButtonAction(ActionEvent event)
    {
        Stage stage = (Stage) closeButton.getScene().getWindow();
        stage.close();
    }

    @FXML   
    private void handleEnterGeneralSettingsPage1(ActionEvent event)
    {   
        openSettingsPage(1);
    }
    
    @FXML   
    private void handleEnterGeneralSettingsPage2(ActionEvent event)
    {   
        openSettingsPage(2);
    }
    
    @FXML   
    private void handleEnterGeneralSettingsPage3(ActionEvent event)
    {   
        openSettingsPage(3);
    }
    
    @FXML   
    private void handleEnterGeneralSettingsPage4(ActionEvent event)
    {   
        openSettingsPage(4);
    }
    
    private void openSettingsPage(int pageNumber)
    {
	FXMLLoader loader = new FXMLLoader(getClass().getResource("FXMLBMSsettingsPage.fxml"));
	
        try 
        {
            Pane pane = loader.load();

            bmsSettingsPage = loader.getController();
            bmsSettingsPage.initSettings(gui);
            bmsSettingsPage.initData(filter, loadCell, pageNumber);
	    bmsSettingsPage.updateLabels();
        
            Stage stage = new Stage();
        
            stage.initModality(Modality.WINDOW_MODAL);
            stage.initOwner(buttonGeneralSettingsPage2.getScene().getWindow());
        
            Scene scene = new Scene(pane);
        
            stage.setScene(scene);
            stage.setTitle("General Settings Page " + pageNumber);
            
            stage.show();
        }
        catch (Exception e) 
        {
            System.out.println("Failed to open General Settings Page " + pageNumber);
            e.printStackTrace();
        }
    }
    
    @FXML
    private void handleResetSOC(ActionEvent event) throws IOException
    {
        System.out.println("Sending a CAN msg!"); //delete this after testing please.
        msg = "00000026#64"; //message to test ESC 1st packet
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
	//int moduleID = 13;
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
        int moduleID = 2 + 16;
	//int moduleID = 14;
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
    private void handleRefreshIP(ActionEvent event) throws IOException
    {
        IPaddress ipAddress = new IPaddress();
        
        String[] splited = ipAddress.getIPaddress().split("\\s+");
        
        if (splited.length == 1)
        {
            label_IPaddress1.setText("WiFi");
            label_IPaddress2.setText(splited[0]);            
        }
        else if(splited.length == 2)
        {
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
            stage.initOwner(buttonGeneralSettingsPage1.getScene().getWindow());

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
	softwareVersionLabel.setText("3.0.0.1");
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
        Runtime.getRuntime().exec("sudo wget http://robotics.ee.uwa.edu.au/courses/des/rasp/images-pi1/EAICS.jar -o /home/pi/EAICS/dist/download/newEAICS.jar");
        Runtime.getRuntime().exec("sudo cp /home/pi/EAICS/dist/download/newEAICS.jar /home/pi/EAICS/dist/EAICS.jar");
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setHeaderText("UPDATING");
        alert.setContentText("Update complete");
        alert.show();
    }

    @FXML
    private void handleStopLogging(ActionEvent event)
    {
        filter.stopLogging();
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setHeaderText("LOGGING");
        alert.setContentText("Logging has been stopped");
        alert.show();
    }
}