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
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import eaics.Settings.SettingsEVMS;
import eaics.Settings.SettingsEAICS;
import eaics.UI.MainUIController;
import java.io.IOException;

/**
 * FXML Controller class
 *
 * @author Troy
 */
public class FXMLNumpadController implements Initializable 
{    
    MainUIController gui;
    private int config;
    private boolean first;
    private int bmsSettingsIndex;
    
    public static final int CONFIG_NUMPAD = 0;
    public static final int CONFIG_BMS_NUMPAD = 1;
    public static final int CONFIG_IPADDRESS = 2;
    
    private String value;
    
    @FXML
    private Button zero;
    @FXML
    private Button one;
    @FXML
    private Button two;
    @FXML
    private Button three;
    @FXML
    private Button four;
    @FXML
    private Button five;
    @FXML
    private Button six;
    @FXML
    private Button seven;
    @FXML
    private Button eight;
    @FXML
    private Button nine;
    
    @FXML
    private Button clear;
    @FXML
    private Button enter;
    @FXML
    private Button negative;
    @FXML
    private Button backspace;
    
    @FXML
    private TextField display;
    
    @FXML
    private Label label;
    
    public void initSettings(MainUIController settingsGui, int config) 
    {
        gui = settingsGui;
        this.config = config;
        
        first = true;
        value = "";
        
        switch(config)
        {
            case CONFIG_NUMPAD:
                break;
            case CONFIG_BMS_NUMPAD:
                break;
            case CONFIG_IPADDRESS:
                negative.setText(".");
                break;
            default:
                break;
        }
    }
    
    public void setBMSIndex(int bmsSettingsIndex, FXMLAdvZEVASettingsPage settingsPage)
    {
        this.bmsSettingsIndex = bmsSettingsIndex;
        this.settingsPage = settingsPage;
        this.bmsSettings = SettingsEAICS.getInstance().getEVMSSettings();
        display.setText(display.getText() + bmsSettings.getDisplayUnits(bmsSettingsIndex));
    }
    
    private SettingsEVMS bmsSettings;
    private FXMLAdvZEVASettingsPage settingsPage;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) 
    {
        // TODO
    }
    
    public String getString() 
    {
        return value;
    }
    
    @FXML
    private void handleExit(ActionEvent event)
    {
        Stage stage = (Stage) enter.getScene().getWindow();
        stage.close();
    }

    @FXML
    private void handleButtonAction(ActionEvent event) throws IOException 
    {
        if(first)
        {
            display.setText("");
            first = false;
        }
        
        if (event.getSource() == one) 
        {
            display.setText(display.getText() + "1");
        }
        else if (event.getSource() == two)
        {
            display.setText(display.getText() + "2");
        }
        else if (event.getSource() == three) 
        {
            display.setText(display.getText() + "3");
        }
        else if (event.getSource() == four) 
        {
            display.setText(display.getText() + "4");
        }
        else if (event.getSource() == five) 
        {
            display.setText(display.getText() + "5");
        }
        else if (event.getSource() == six) 
        {
            display.setText(display.getText() + "6");
        }
        else if (event.getSource() == seven) 
        {
            display.setText(display.getText() + "7");
        }
        else if (event.getSource() == eight) 
        {
            display.setText(display.getText() + "8");
        }
        else if (event.getSource() == nine) 
        {
            display.setText(display.getText() + "9");
        }
        else if (event.getSource() == zero) 
        {
            display.setText(display.getText() + "0");
        }
        else if (event.getSource() == negative) 
        {
            if(config == CONFIG_IPADDRESS) 
            {
                display.setText(display.getText() + ".");
            }
            else
            {
                display.setText(display.getText() + "-");
            }
        }
        else if (event.getSource() == clear) 
        {
            display.setText("");
        }
	else if (event.getSource() == backspace) 
        {
            String str = display.getText();
            if(str.length() >= 1)
            {
                str = str.substring(0, str.length() - 1);
            }
	    display.setText(str);
        }
        else if (event.getSource() == enter) 
        {
            if(config == CONFIG_IPADDRESS) 
	    {
                value = display.getText();
                gui.settingsPageController.completeUpdatePixhawk();
            }
            else 
	    {
                this.bmsSettings.setSetting(bmsSettingsIndex,  Integer.parseInt(display.getText()));	
		SettingsEAICS.getInstance().update();
                this.settingsPage.updateLabels();
            }
            Stage stage = (Stage) enter.getScene().getWindow();
            stage.close();
        }
    }
}
