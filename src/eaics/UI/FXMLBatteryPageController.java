/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eaics.UI;

import eaics.CAN.CANFilter;
import eaics.SER.LoadCell;
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
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author Troy
 */
public class FXMLBatteryPageController implements Initializable 
{
    MainUIController gui;
    
    private CANFilter filter;
    private LoadCell loadCell;
    
    FXMLBatteryCellPage1Controller cellPage1;
    FXMLBatteryCellPage2Controller cellPage2;
    FXMLBatteryCellPage3Controller cellPage3;
    
    @FXML
    Button buttonCellPage1;
    
    @FXML
    Button buttonCellPage2;
    
    @FXML
    Button buttonCellPage3;
    
    @FXML
    private javafx.scene.control.Button closeButton;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) 
    {
	// TODO
    }
    
    public void initData(CANFilter fil, LoadCell cell) 
    {
        this.filter = fil;
        this.loadCell = cell;
    }

    public void initSettings(MainUIController mainGui) 
    {
        gui = mainGui;
    }
    
    @FXML
    private void closeButtonAction(ActionEvent event)
    {
        Stage stage = (Stage) closeButton.getScene().getWindow();
        stage.close();
    }
    
    @FXML
    private void handleCellPage1(ActionEvent event) throws IOException
    {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("FXMLBatteryCellPage1.fxml"));
        
        try 
        {
            Pane pane = loader.load();

            cellPage1 = loader.getController();
            cellPage1.initSettings(gui);
	    cellPage1.initData(filter, loadCell);
        
            Stage stage = new Stage();
        
            stage.initModality(Modality.WINDOW_MODAL);
            stage.initOwner(buttonCellPage1.getScene().getWindow());
        
            Scene scene = new Scene(pane);
        
            stage.setScene(scene);
            stage.setTitle("Battery Cell Page 1");
            
            stage.show();
        }
        
        catch (Exception e) 
        {
            System.out.println("Failed to open Battery Cell Page 1");
            e.printStackTrace();
        }
    }
    
    @FXML
    private void handleCellPage2(ActionEvent event) throws IOException
    {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("FXMLBatteryCellPage2.fxml"));
        
        try 
        {
            Pane pane = loader.load();

            cellPage2 = loader.getController();
            cellPage2.initSettings(gui);
	    cellPage2.initData(filter, loadCell);
        
            Stage stage = new Stage();
        
            stage.initModality(Modality.WINDOW_MODAL);
            stage.initOwner(buttonCellPage2.getScene().getWindow());
        
            Scene scene = new Scene(pane);
        
            stage.setScene(scene);
            stage.setTitle("Battery Cell Page 2");
            
            stage.show();
        }
        
        catch (Exception e) 
        {
            System.out.println("Failed to open Battery Cell Page 2");
            e.printStackTrace();
        }
    }
    
    @FXML
    private void handleCellPage3(ActionEvent event) throws IOException
    {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("FXMLBatteryCellPage3.fxml"));
        
        try 
        {
            Pane pane = loader.load();
	    
            cellPage3 = loader.getController();
            cellPage3.initSettings(gui);
	    cellPage3.initData(filter, loadCell);
        
            Stage stage = new Stage();
        
            stage.initModality(Modality.WINDOW_MODAL);
            stage.initOwner(buttonCellPage3.getScene().getWindow());
        
            Scene scene = new Scene(pane);
        
            stage.setScene(scene);
            stage.setTitle("Battery Cell Page 3");
            
            stage.show();
        }
        
        catch (Exception e) 
        {
            System.out.println("Failed to open Battery Cell Page 3");
            e.printStackTrace();
        }
    }
    
}
