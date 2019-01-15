/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eaics.UI.Deprecated;

import eaics.CAN.Battery.BMS.BMS12v3;
import eaics.CAN.CANFilter;
import eaics.CAN.Battery.CurrentSensor;
import eaics.CAN.ESC.ESC;
import eaics.CAN.Battery.EVMS;
import eaics.LOGGING.Logging;
import eaics.SER.LoadCell;
import eaics.SER.Serial;
import eaics.SER.Throttle;
import static eaics.UI.MainUIController.refreshFrequency;
import eaics.UI.Trifan.TrifanMainUIController;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Alert;
import javafx.scene.image.Image;
import javafx.util.Duration;

/**
 * FXML Controller class
 *
 * @author Troy Burgess
 */
public class FXMLBatteryGraphController implements Initializable 
{
    @FXML
    BarChart<String, Number> batteryBarGraph;
    
    final static String BMS1 = "BMS 1";
    final static String BMS2 = "BMS 2";
    final static String BMS3 = "BMS 3";
    final static String BMS4 = "BMS 4";
    final static String BMS5 = "BMS 5";
    final static String BMS6 = "BMS 6";
    final static String BMS7 = "BMS 7";
    final static String BMS8 = "BMS 8";
    
    final static String BMS9 = "BMS 9";
    final static String BMS10 = "BMS 10";
    final static String BMS11 = "BMS 11";
    final static String BMS12 = "BMS 12";
    final static String BMS13 = "BMS 13";
    final static String BMS14 = "BMS 14";
    final static String BMS15 = "BMS 15";
    final static String BMS16 = "BMS 16";
    
    final static String BMS17 = "BMS 17";
    final static String BMS18 = "BMS 18";
    final static String BMS19 = "BMS 19";
    final static String BMS20 = "BMS 20";
    final static String BMS21 = "BMS 21";
    final static String BMS22 = "BMS 22";
    final static String BMS23 = "BMS 23";
    final static String BMS24 = "BMS 24";

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) 
    {
	// TODO
    }
    
    public void initData() throws IOException 
    {
	final CategoryAxis xAxis = new CategoryAxis();
        final NumberAxis yAxis = new NumberAxis();
        batteryBarGraph = new BarChart<>(xAxis,yAxis);
        batteryBarGraph.setTitle("Battery Summary");
        xAxis.setLabel("Battery Cell");       
        yAxis.setLabel("Voltage");
 
        XYChart.Series series1 = new XYChart.Series();
        series1.setName("Battery Box 1");       
        series1.getData().add(new XYChart.Data(BMS1, 3.1));
        series1.getData().add(new XYChart.Data(BMS2, 3.2));
        series1.getData().add(new XYChart.Data(BMS3, 3.3));
        series1.getData().add(new XYChart.Data(BMS4, 3.4));
        series1.getData().add(new XYChart.Data(BMS5, 3.5));
	series1.getData().add(new XYChart.Data(BMS6, 3.6));
	series1.getData().add(new XYChart.Data(BMS7, 3.7));
	series1.getData().add(new XYChart.Data(BMS8, 3.8));
        
        XYChart.Series series2 = new XYChart.Series();
        series2.setName("Battery Box 2");       
        series2.getData().add(new XYChart.Data(BMS9, 4.1));
        series2.getData().add(new XYChart.Data(BMS10, 4.2));
        series2.getData().add(new XYChart.Data(BMS11, 4.3));
        series2.getData().add(new XYChart.Data(BMS12, 4.4));
        series2.getData().add(new XYChart.Data(BMS13, 4.5));
	series2.getData().add(new XYChart.Data(BMS14, 4.6));
	series2.getData().add(new XYChart.Data(BMS15, 4.7));
	series2.getData().add(new XYChart.Data(BMS16, 4.8));
        
        XYChart.Series series3 = new XYChart.Series();
        series3.setName("Battery Box 3");       
        series3.getData().add(new XYChart.Data(BMS17, 0.1));
        series3.getData().add(new XYChart.Data(BMS18, 0.2));
        series3.getData().add(new XYChart.Data(BMS19, 0.3));
        series3.getData().add(new XYChart.Data(BMS20, 0.4));
        series3.getData().add(new XYChart.Data(BMS21, 0.5));
	series3.getData().add(new XYChart.Data(BMS22, 0.6));
	series3.getData().add(new XYChart.Data(BMS23, 0.7));
	series3.getData().add(new XYChart.Data(BMS24, 0.8));
        
        batteryBarGraph.getData().addAll(series1, series2, series3);       
        
        Timeline refreshUI;
        refreshUI = new Timeline(new KeyFrame(Duration.millis(refreshFrequency), new EventHandler<ActionEvent>() 
	{	    
            @Override
            public void handle(ActionEvent event) 
	    {
                
            }
            
        }));
        refreshUI.setCycleCount(Timeline.INDEFINITE);
        refreshUI.play();
       	
    }
}
