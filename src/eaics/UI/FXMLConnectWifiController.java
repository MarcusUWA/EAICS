/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eaics.UI;

import eaics.VirtualKeyboard;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author Markcuz
 */
public class FXMLConnectWifiController implements Initializable
{

    @FXML
    TextField textField;
    
    @FXML 
    Button okButton;

    @FXML 
    HBox buttons;
    
    @FXML 
    VBox root;
    
    @FXML
    HBox ssidBox;
    
    @FXML
    Button closeButton;
    
    ChoiceBox choiceBox;
    
    List<String> ssidList = new ArrayList<String>();
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }
    
    //TODO: remove keyboard?
    public void test() throws IOException 
    {
	textField.setPromptText("Enter WiFi password");
        
        VirtualKeyboard vkb = new VirtualKeyboard();
    
        vkb.view().setStyle("-fx-border-color: darkblue; -fx-border-radius: 5;");
        
        root.getChildren().addAll(vkb.view());
        
        Runtime rt = Runtime.getRuntime();
        Process proc = rt.exec("sudo iwlist wlan0 scan");// | grep -w 'ESSID'");//| cut -d ':' -f2");
           
        BufferedReader stdInput = new BufferedReader(new InputStreamReader(proc.getInputStream()));
        
        
        String s = null;
        while (((s = stdInput.readLine()) != null)) {
            if(s.contains("ESSID")) {
                s = s.trim();
                s = s.substring(6);
    
                System.out.println(s);
                ssidList.add(s);
            }
        }
        
        choiceBox = new ChoiceBox((ObservableList) FXCollections.observableArrayList(ssidList));
        
        ssidBox.getChildren().add(choiceBox);
    }
    
    @FXML
    private void closeButtonAction(ActionEvent event)
    {
        Stage stage = (Stage) closeButton.getScene().getWindow();
        stage.close();
    }
    
    //TODO: instead of overwriting wifi file, append if not already there, rpelace if already there
    @FXML
    private void okButtonAction(ActionEvent event) throws FileNotFoundException, IOException
    {
        String currentSSID = choiceBox.getValue().toString();
        String currentPass = textField.getText();
        
        try {
            BufferedReader file = new BufferedReader(new FileReader("/home/pi/opt/wpa_supplicant-empty"));
            String line;
            StringBuffer inputBuffer = new StringBuffer();
            int lineNumber = 1;

            while ((line = file.readLine()) != null) {
                
                if(lineNumber == 5) {
                    inputBuffer.append("        ssid="+currentSSID);
                }
                else if (lineNumber == 6) {
                    inputBuffer.append("        psk=\""+currentPass+"\"");
                }
                else {
                    inputBuffer.append(line);
                }
                inputBuffer.append('\n');
                lineNumber++;
            }
            
            String inputStr = inputBuffer.toString();

            file.close();
            
            FileOutputStream fileOut = new FileOutputStream("/home/pi/opt/wpa_supplicant-new");
            fileOut.write(inputStr.getBytes());
            fileOut.close();
        }
            
        catch (Exception e) {
            System.out.println("Problem reading file.");
        }
        
        Runtime.getRuntime().exec("sudo cp /home/pi/opt/wpa_supplicant-new /etc/wpa_supplicant/wpa_supplicant.conf");
        /*
        Runtime.getRuntime().exec("sudo ifconfig wlan0 down");
        Runtime.getRuntime().exec("sudo service dhcpcd restart");
        Runtime.getRuntime().exec("sudo service hostapd restart");
        Runtime.getRuntime().exec("sudo ifconfig wlan0 up");
        */
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setHeaderText("WiFi Update");
        alert.setResizable(false);
        alert.setContentText("Select okay to restart");

        Optional<ButtonType> result = alert.showAndWait();
        ButtonType button = result.orElse(ButtonType.CANCEL);

        if (button == ButtonType.OK) 
        {
            Runtime.getRuntime().exec("sudo reboot");
        } 
        else
        {
            
        }
        
        //alert.show();
        //Stage stage = (Stage) closeButton.getScene().getWindow();
        //stage.close();
    }
}
