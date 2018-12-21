/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eaics.UI;

import eaics.LOGGING.Logging;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
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
    
    public void initData(Logging logging) throws IOException 
    {
        this.logging = logging;
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
    private void handle100msLogging(ActionEvent event)
    {
        this.logging.changeLoggingRate(100);
    }
    
    @FXML
    private void handleOneSecLogging(ActionEvent event)
    {
        this.logging.changeLoggingRate(1000);
    }
    
    @FXML
    private void handleStartLog(ActionEvent event)
    {
        this.logging.startLogging();
    }
    
    @FXML
    private void handleStopLog(ActionEvent event)
    {
        this.logging.stopLogging();
    }  
    
    @FXML
    private void handleExitPressed(ActionEvent event)
    {
        Stage stage = (Stage) buttonExit.getScene().getWindow();
        stage.close();
    }
}
