/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eaics.UI;

import eaics.CAN.CANFilter;
import eaics.SER.LoadCell;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.util.Duration;

public class LabelListController implements Initializable
{
    @FXML
    private List<Label> labelList ;

    public void initialize() 
    {
        int count = 1 ;
        for (Label label : labelList) 
	{
            label.setText("Message " + (count++) );
        }
    }
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) 
    {
	// TODO
    }
    
    public void initData() 
    {
	int timeToRefresh = 1000;
	
        CANFilter filter = CANFilter.getInstance();
	
	initialize();
                
        Timeline refreshUI;
        refreshUI = new Timeline(new KeyFrame(Duration.millis(timeToRefresh), new EventHandler<ActionEvent>()
	{
            @Override
            public void handle(ActionEvent event) 
	    {
		initialize();
            }
        }));
        refreshUI.setCycleCount(Timeline.INDEFINITE);
        refreshUI.play();
    }
}