/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eaics.UI;

import eaics.CAN.CANFilter;
import eaics.CAN.Charger.GBT.ChargerGBT;
import eaics.CAN.MGL.MGLDisplay;
import eaics.LOGGING.Logging;
import eaics.Settings.IPAddress;
import eaics.SER.LoadCell;
import eaics.SER.Serial;
import eaics.Settings.SettingsEAICS;
import eaics.UI.FXMLSettings.FXMLAdvZEVASettingsPage;
import eaics.UI.FXMLSettings.FXMLCalibrateLoadCellController;
import eaics.UI.FXMLSettings.FXMLConnectWifiController;
import eaics.UI.FXMLSettings.FXMLGeneralSettingsController;
import eaics.UI.FXMLSettings.FXMLLoggingPageController;
import eaics.UI.FXMLSettings.FXMLNumpadController;
import eaics.UI.FXMLSettings.FXMLSendLogsController;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ToggleButton;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author Troy
 */
public class FXMLSettingsController implements Initializable {
    String version = "3.7.0";
    
    FXMLAdvZEVASettingsPage advZEVASettingsPage;
    FXMLConnectWifiController wifiConnectController;
    FXMLNumpadController numpad;
    FXMLSendLogsController sendLogsController;
    
    FXMLGeneralSettingsController genSettings;
    
    FXMLLoggingPageController loggingController;
    
    ChargerGBT chg;
    
    FXMLCalibrateLoadCellController calib;
    Serial serial;
    
    private Logging logging;
    
    @FXML
    Button buttonStopLogging;
    
    @FXML
    Button buttonKillProgram;
    
    @FXML
    Button buttonResetWarnings;
    
    @FXML
    Button buttonResetSOC;
    
    @FXML 
    Button wifiConnect;
    
    @FXML 
    ToggleButton toggleGBT;
    
    MainUIController gui;
    
    //refresh rate in ms
    int refreshFrequency = 10;
    
    private CANFilter filter;
    private LoadCell loadCell;
    
    private String msg;
    
    @FXML
    private Label labelLANIP;
    
    @FXML
    private Label labelWifiIP;
    
    @FXML
    private Label labelWifiSSID;
    
    @FXML
    private Label softwareVersionLabel;
    
    @FXML
    private Button closeButton;
    
    @FXML 
    private Label pixhawkIPLabel;
    
    @FXML
    private void handleKillProgram(ActionEvent event) throws IOException
    {
        //filter.stopLogging();
        //final Process killPIXHAWK_Program = Runtime.getRuntime().exec("sudo pkill mavproxy.py");
        //final Process killCAN_Program = Runtime.getRuntime().exec("sudo killall ReadCAN");
        //final Process killLoadCell_Program = Runtime.getRuntime().exec("sudo pkill LoadCell");
        final Process killEAICS_Program = Runtime.getRuntime().exec("sudo pkill java");
    }
    
    @FXML
    private void handleResetWarnings(ActionEvent event) {
        filter.resetAllWarnings();
    }
    
    @FXML
    private void closeButtonAction(ActionEvent event)
    {
        Stage stage = (Stage) closeButton.getScene().getWindow();
        stage.close();
    }
    
    @FXML
    private void toggleButtonAction(ActionEvent event)
    {
        //chg.setChargeMode(toggleGBT.isSelected());
    }
    
    @FXML   
    private void handleAdvancedZEVASettingsPage(ActionEvent event) {   
        openSettingsPage();
    }

    private void openSettingsPage() {
        
	FXMLLoader loader = new FXMLLoader(getClass().getResource("FXMLSettings/FXMLAdvZEVASettingsPage.fxml"));
	
        try  {
            Pane pane = loader.load();

            advZEVASettingsPage = loader.getController();
            advZEVASettingsPage.initData(1);
	    advZEVASettingsPage.updateLabels();
        
            Stage stage = new Stage();
        
            stage.initModality(Modality.WINDOW_MODAL);
            stage.initOwner(closeButton.getScene().getWindow());
        
            Scene scene = new Scene(pane);
        
            stage.setScene(scene);
            stage.setTitle("ZEVA Advanced Settings Page " + 1);
            
            stage.show();
        }
        catch (Exception e) 
        {
            System.out.println("Failed to open ZEVA Advanced Settings Page");
            e.printStackTrace();
        }
    }
    
    @FXML
    private void handleResetSOC(ActionEvent event) throws IOException {
        filter.getCANHandler(0).writeMessage(0x00000026, new int[]{100});
        //filter.getCANHandler(0).writeMessage(0x00000026, new int[]{90});
    }

    @FXML
    private void handleRefreshIP(ActionEvent event) throws IOException {
        IPAddress ipAddress = new IPAddress();
        
        ipAddress.updateIPAddress();
        
        labelWifiIP.setText(ipAddress.getWifiIP());
        labelWifiSSID.setText(ipAddress.getWifiSSID());
        labelLANIP.setText(ipAddress.getLANIP());
        
        pixhawkIPLabel.setText(SettingsEAICS.getInstance().getPixHawkSettings().getIpAddress());
    }
    
    @FXML   
    private void handleWifiSetup(ActionEvent event) {   
        FXMLLoader loader = new FXMLLoader(getClass().getResource("FXMLSettings/FXMLConnectWifi.fxml"));
        try  {
            Pane pane = loader.load();

            wifiConnectController = loader.getController();
            wifiConnectController.setupKeyboard();
            wifiConnectController.setupSSID();
            
            Stage stage = new Stage();
        
            stage.initModality(Modality.WINDOW_MODAL);
            stage.initOwner(closeButton.getScene().getWindow());

            Scene scene = new Scene(pane);

            stage.setScene(scene);
            stage.setTitle("Wifi Setup!!");

            stage.show();
        }
        
        catch (Exception e)  {
            System.out.println("Failed to open Wifi Window");
            e.printStackTrace();
        }
    }    
    
    @FXML   
    private void handleSendLogging(ActionEvent event) {   
        FXMLLoader loader = new FXMLLoader(getClass().getResource("FXMLSettings/FXMLSendLogs.fxml"));
        try  {
            Pane pane = loader.load();

            sendLogsController = loader.getController();
            sendLogsController.setupKeyboard();
        
            Stage stage = new Stage();
        
            stage.initModality(Modality.WINDOW_MODAL);
            stage.initOwner(closeButton.getScene().getWindow());

            Scene scene = new Scene(pane);

            stage.setScene(scene);
            stage.setTitle("Logging!!");

            stage.show();
        }
        
        catch (Exception e)  {
            System.out.println("Failed to open Logging Window");
            e.printStackTrace();
        }
    }   
    
    @FXML
    private void handleAdjustLogging(ActionEvent event)  {
	FXMLLoader loader = new FXMLLoader(getClass().getResource("FXMLSettings/FXMLLoggingPage.fxml"));
        
        try  {
            Pane pane = loader.load();

            loggingController = loader.getController();
	    loggingController.initData(this.logging);
   
            Stage stage = new Stage();
 
            stage.initModality(Modality.WINDOW_MODAL);
            stage.initOwner(closeButton.getScene().getWindow());

            Scene scene = new Scene(pane);
  
            stage.setScene(scene);
            stage.setTitle("Adjust Logging!!");
            
            stage.setX(175.0);
            stage.setY(20.0);
            
            stage.show();
        }
        catch (Exception e)  {
            System.out.println("Failed to open Logging Window");
	    e.printStackTrace();
        }
    } 

    
    public void initData(LoadCell loadCell, Serial serial, Logging logging) {
        this.filter = CANFilter.getInstance();
        this.loadCell = loadCell;
        this.serial = serial;
        this.chg = filter.getChargerGBT();
        this.logging = logging;
        
        mgl = filter.getMgl();
        
        pixhawkIPLabel.setText(SettingsEAICS.getInstance().getPixHawkSettings().getIpAddress());
        
	softwareVersionLabel.setText(version);
    }

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) 
    {   
        try {
            handleRefreshIP(new ActionEvent());
        }
        catch(IOException e) {
            e.printStackTrace();
        }
    }  
    
    @FXML
    private void handleUpdatePixhawk(ActionEvent event) throws IOException {
        
        numpad = new FXMLNumpadController();
        
        FXMLLoader loader = new FXMLLoader(getClass().getResource("FXMLSettings/FXMLNumpad.fxml"));
        
        try {
            Pane pane = loader.load();
            numpad = loader.getController();
            numpad.initSettings(FXMLNumpadController.CONFIG_IPADDRESS);
        
            Stage stage = new Stage();
        
            stage.initModality(Modality.WINDOW_MODAL);
            stage.initOwner(closeButton.getScene().getWindow());
        
            Scene scene = new Scene(pane);
        
            stage.setScene(scene);
            stage.setTitle("Numpad!!!");
            
            stage.show();
        }        
        catch (Exception e) {
            System.out.println("Failed to open Numpad Window");
            e.printStackTrace();
        }
    }
    
    @FXML
    private void handleCalibrateLoadCell(ActionEvent event) {
        
        calib = new FXMLCalibrateLoadCellController();
        
        FXMLLoader loader = new FXMLLoader(getClass().getResource("FXMLSettings/FXMLCalibrateLoadCell.fxml"));
        try {
            Pane pane = loader.load();
            calib = loader.getController();
            calib.init(serial, loadCell);
        
            Stage stage = new Stage();
        
            stage.initModality(Modality.WINDOW_MODAL);
            stage.initOwner(closeButton.getScene().getWindow());
        
            Scene scene = new Scene(pane);
        
            stage.setScene(scene);
            stage.setTitle("Calibrate Load Cell");
            
            stage.show();
        }        
        catch (Exception e) {
            System.out.println("Failed to open LoadCell Calibrarion Window");
            e.printStackTrace();
        }
    }
    
    @FXML
    private void handleGeneralSettings(ActionEvent event) {
        
        genSettings = new FXMLGeneralSettingsController();
        
        FXMLLoader loader = new FXMLLoader(getClass().getResource("FXMLSettings/FXMLGeneralSettings.fxml"));
        try {
            Pane pane = loader.load();
            genSettings = loader.getController();
            genSettings.setupScreen();
        
            Stage stage = new Stage();
        
            stage.initModality(Modality.WINDOW_MODAL);
            stage.initOwner(closeButton.getScene().getWindow());
        
            Scene scene = new Scene(pane);
        
            stage.setScene(scene);
            stage.setTitle("General Settings");
            
            stage.show();
        }        
        catch (Exception e) {
            System.out.println("Failed to open General Settings Window");
            e.printStackTrace();
        }
    }
    
    @FXML
    ToggleButton testMGL;
    
    MGLDisplay mgl;
    
    @FXML 
    private void handleTestMGL(ActionEvent event) {

        if(testMGL.isSelected()) {
            mgl.startDisplay();
            testMGL.setText("Stop Display");
        }
        else {
            mgl.stopDisplay();
            testMGL.setText("Start Display");
        }
        
    }
}
