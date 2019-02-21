/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eaics.UI;

import eaics.CAN.CANFilter;
import eaics.CAN.MGL.MGLDisplay;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author Markcuz
 */
public class FXMLMGMTestController implements Initializable {

    
    CANFilter filter;
    
    MGLDisplay mgl;
    
    @FXML
    private Label ff1pc;
    @FXML
    private Slider ff1pcSlider;
    
    @FXML
    private Label ff1pr;
    @FXML
    private Slider ff1prSlider;
    
    @FXML
    private Label ff2pc;
    @FXML
    private Slider ff2pcSlider;
    
    @FXML
    private Label ff2pr;
    @FXML
    private Slider ff2prSlider;
    
    @FXML
    private Label aux1;
    @FXML
    private Slider aux1Slider;
    
    @FXML
    private Label aux2;
    @FXML
    private Slider aux2Slider;
    
    @FXML
    private Label fuelPressure;
    @FXML
    private Slider fuelPressureSlider;
    
    @FXML
    private Label coolant;
    @FXML
    private Slider coolantSlider;
    
    @FXML
    private Label fuelLevel1;
    @FXML
    private Slider fuelLevel1Slider;
    
    @FXML
    private Label fuelLevel2;
    @FXML
    private Slider fuelLevel2Slider;
    
    @FXML
    private Label temp;
    @FXML
    private Slider tempSlider;
    
    @FXML
    private Label voltage;
    @FXML
    private Slider voltageSlider;
    
    @FXML
    private Label map;
    @FXML
    private Slider mapSlider;
    
    @FXML
    private Label current;
    @FXML
    private Slider currentSlider;
    
    @FXML
    private Label oilTemp;
    @FXML
    private Slider oilTempSlider;
    
    @FXML
    private Label oilPress;
    @FXML
    private Slider oilPressSlider;
    
    @FXML
    private Button backButton;
    
    @FXML 
    private ToggleButton startStop;
    
    @FXML 
    private TextField text;
    
    
    public void initData()  {
        this.filter = CANFilter.getInstance();
        this.mgl = CANFilter.getInstance().getMgl();
        setValues(0);
    }
    
    private void setValues(int start) {
        ff1pcSlider.setValue(start);
        ff2pcSlider.setValue(start);
        ff1prSlider.setValue(start);
        ff1prSlider.setValue(start);
        aux1Slider.setValue(start);
        aux2Slider.setValue(start);
        fuelPressureSlider.setValue(start);
        coolantSlider.setValue(start);
        fuelLevel1Slider.setValue(start);
        fuelLevel2Slider.setValue(start);
        tempSlider.setValue(start);
        voltageSlider.setValue(start);
        mapSlider.setValue(start);
        currentSlider.setValue(start);
        oilTempSlider.setValue(start);
        oilPressSlider.setValue(start);
    }
    
    @FXML
    private void updateScreen() {
        
        try{
            setValues(Integer.parseInt(text.getText()));
        }
        catch(NumberFormatException e){
            System.out.println("invalid value");
        }
        
        ff1pc.setText(""+ff1pcSlider.getValue());
        ff2pc.setText(""+ff2pcSlider.getValue());
        ff1pr.setText(""+ff1prSlider.getValue());
        ff2pr.setText(""+ff1prSlider.getValue());
        aux1.setText(""+aux1Slider.getValue());
        aux2.setText(""+aux2Slider.getValue());
        fuelPressure.setText(""+fuelPressureSlider.getValue());
        coolant.setText(""+coolantSlider.getValue());
        fuelLevel1.setText(""+fuelLevel1Slider.getValue());
        fuelLevel2.setText(""+fuelLevel2Slider.getValue());
        temp.setText(""+tempSlider.getValue());
        voltage.setText(""+voltageSlider.getValue());
        map.setText(""+mapSlider.getValue());
        current.setText(""+currentSlider.getValue());
        oilTemp.setText(""+oilTempSlider.getValue());
        oilPress.setText(""+oilPressSlider.getValue());
        
        mgl.setFf1pc((int) ff1pcSlider.getValue());
        mgl.setFf2pc((int) ff2pcSlider.getValue());
        mgl.setFf1pr((int) ff1prSlider.getValue());
        mgl.setFf2pr((int) ff2prSlider.getValue());
        mgl.setAux1((int) aux1Slider.getValue());
        mgl.setAux2((int) aux2Slider.getValue());
        mgl.setFuelPressure((int) fuelPressureSlider.getValue());
        mgl.setCoolant((int) coolantSlider.getValue());
        mgl.setFuelLevel1((int) fuelLevel1Slider.getValue());
        mgl.setFuelLevel2((int) fuelLevel2Slider.getValue());
        mgl.setTemp((int) tempSlider.getValue());
        mgl.setVoltage((int) voltageSlider.getValue());
        mgl.setMap((int) mapSlider.getValue());
        mgl.setCurrent((int) currentSlider.getValue());
        mgl.setOilTemp((int) oilTempSlider.getValue());
        mgl.setOilPress((int) oilPressSlider.getValue());
    }
        
    @FXML
    private void closeButtonAction(ActionEvent event) {
        Stage stage = (Stage) backButton.getScene().getWindow();
        stage.close();
    }
        
    @FXML
    private void startStopAction(ActionEvent event) {
        if(startStop.isSelected()) {
             startStop.setText("Stop MGL");
             mgl.startDisplay();
        } 
        else {
            startStop.setText("Start MGL");
            mgl.stopDisplay();
        }
    }
    
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    
    
    
    
}
