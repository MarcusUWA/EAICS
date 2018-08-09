/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eaics.UI;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import eaics.BMSSettings;

/**
 * FXML Controller class
 *
 * @author Troy
 */
public class FXMLNumpadController implements Initializable 
{
    MainUIController gui;
    private BMSSettings bmsSettings;
    private int index;
    private boolean first;
    private FXMLGeneralSettingsPage settingsPage;
    
    public void initSettings(MainUIController settingsGui, int index, FXMLGeneralSettingsPage settingsPage) 
    {
        gui = settingsGui;
	this.settingsPage = settingsPage;
        this.bmsSettings = settingsPage.getBMSSettings();
        this.index = index;
        display.setText(display.getText() + bmsSettings.getUnit(index));
        first = true;
    }

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) 
    {
        // TODO
    }
    
    @FXML
    private Button nine;

    @FXML
    private Button six;

    @FXML
    private Button one;

    @FXML
    private TextField display;

    @FXML
    private Button clear;

    @FXML
    private Button seven;

    @FXML
    private Label label;

    @FXML
    private Button two;

    @FXML
    private Button three;

    @FXML
    private Button eight;

    @FXML
    private Button zero;

    @FXML
    private Button enter;

    @FXML
    private Button four;

    @FXML
    private Button decimal;

    @FXML
    private Button five;
    
    @FXML
    private Button backspace;

    @FXML
    void handleButtonAction(ActionEvent event) 
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
        else if (event.getSource() == decimal) 
        {
            display.setText(display.getText() + ".");
        }
        else if (event.getSource() == clear) 
        {
            display.setText("");
        }
	else if (event.getSource() == backspace) 
        {
            String str = display.getText();
	    str = str.substring(0, str.length() - 1);
	    display.setText(str);
        }
        else if (event.getSource() == enter) 
        {
            this.bmsSettings.setSetting(index, Integer.parseInt(display.getText()));
            this.bmsSettings.update();
	    this.settingsPage.updateLabels();
            Stage stage = (Stage) enter.getScene().getWindow();
            stage.close();
        }
    }
}
