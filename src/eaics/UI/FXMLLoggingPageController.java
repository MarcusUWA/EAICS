/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eaics.UI;

import eaics.LOGGING.Logging;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author Troy Burgess
 */
public class FXMLLoggingPageController implements Initializable 
{
    private Logging logging;
    
    @FXML
    private javafx.scene.control.Button buttonExit;
    
    @FXML
    private ChoiceBox rateChoiceBox;
    
    @FXML
    private Button loggingState;
    
    public void initData(Logging logging) throws IOException 
    {
        this.logging = logging;
        
        List<Integer> loggingRates = new ArrayList<>();
        
        loggingRates.add(10);
        loggingRates.add(20);
        loggingRates.add(50);
        loggingRates.add(100);
        loggingRates.add(200);
        loggingRates.add(500);
        loggingRates.add(1000);
        loggingRates.add(2000);
        loggingRates.add(5000);
        
        rateChoiceBox.setItems((ObservableList) FXCollections.observableArrayList(loggingRates));
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
    private void handleChangeRate(ActionEvent event) {
        this.logging.changeLoggingRate(Integer.parseInt(rateChoiceBox.getValue().toString()));
        System.out.println("Rate(ms)"+Integer.parseInt(rateChoiceBox.getValue().toString()));
    }
    
    @FXML
    private void handleState(ActionEvent event)
    {       
        if(this.logging.getLogging())
        {
            this.logging.stopLogging();
            loggingState.setText("Start Logging");
        }
        else
        {
            this.logging.startLogging();
            loggingState.setText("Stop Logging");
        }
    }
    
    @FXML
    private void handleExitPressed(ActionEvent event)
    {
        Stage stage = (Stage) buttonExit.getScene().getWindow();
        stage.close();
    }
}
