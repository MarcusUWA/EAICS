/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eaics.UI;

import eaics.Settings.VirtualKeyboard;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author Markcuz
 */
public class FXMLSendLogsController implements Initializable {

    @FXML
    TextField path;
    
    @FXML
    TextField ip;
    
    @FXML
    Button closeButton;
    
    @FXML
    Button deleteLogsButton;
    
    @FXML
    Button downloadLogsButton;
    
    @FXML 
    VBox root;
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    
    
    @FXML
    private void closeButtonAction(ActionEvent event) {
        Stage stage = (Stage) closeButton.getScene().getWindow();
        stage.close();
    }
    
    public void setupKeyboard() {
        VirtualKeyboard vkb = new VirtualKeyboard();
        vkb.view().setStyle("-fx-border-color: darkblue; -fx-border-radius: 5;");
        root.getChildren().addAll(vkb.view());
    }
    @FXML
    private void deleteLogsButtonAction(ActionEvent event) throws IOException {
        Runtime.getRuntime().exec("rm /home/pi/Logging/*");
    }
    
    @FXML
    private void sendLogsAction(ActionEvent event) {
        String command = "scp -r /home/pi/Logging "+ip.getText()+":"+path.getText();
        
        System.out.println("IP: "+ ip.getText() +"Path: " + path.getText());
        //Runtime.getRuntime().exec(command);
        
    }
    
    
    
    
}
