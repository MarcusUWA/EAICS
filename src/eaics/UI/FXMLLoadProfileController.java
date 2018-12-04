/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eaics.UI;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;

        
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

/**
 * FXML Controller class
 * CSV Profile loader
 * Format of csv: Throttle(Constant) | ThrottlePos (0-1024) | Time (s)  
 * @author Markcuz
 */
public class FXMLLoadProfileController implements Initializable {
    List<int[]> lines = new ArrayList<>();
    MainUIController gui;
    
    Boolean status = true;
    
    @FXML
    TextField path;
    
    @FXML
    Button exit;
                
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    } 
    
    public void init(MainUIController gui) {
        this.gui = gui;
        gui.setOverrideStatus(true);
    }
    
    public void loadFile(String path) throws FileNotFoundException, IOException {
        //clear list
        lines.clear();
        
        try {
            BufferedReader br = new BufferedReader(new FileReader(path));
            
            String line = null;

            while ((line = br.readLine()) != null) {
                String[] values = line.split(",");
                
                if(values.length == 3) {
                    int row[] = new int[2];
                    //throttle value
                    try {
                        if(Integer.parseInt(values[1])>1024) {
                            row[0] = 1024;
                        }
                        else if(Integer.parseInt(values[1])<0) {
                            row[0] = 0;
                        }
                        else {
                            row[0] = Integer.parseInt(values[1]);  
                        }
                    }
                    catch (NumberFormatException e) {
                        row[0] = 0;
                        row[1] = 0;
                    }
                    
                    try {
                        if(Integer.parseInt(values[2])<0) {
                            row[1] = 0;
                        }
                        else {
                            row[1] = Integer.parseInt(values[2]); 
                        }
                        
                    }
                    catch (NumberFormatException e) {
                        row[1] = 0;
                    }
                }
            }
            br.close();
        }
        catch(FileNotFoundException e) {
            System.out.println("File not found...");
        }
    }
    
    public void showProfile() {
        //todo: show the profile of the load profile file
    }
    
    @FXML
    private void handleLoadFile(ActionEvent event) throws IOException {
        loadFile(path.getText());
        showProfile();
    }
    
    @FXML
    private void handleStop(ActionEvent event) throws IOException {
        handleThrottle(0); 
        status = false;
    }
    
    @FXML
    private void handleRun(ActionEvent event) throws IOException {
        //ensure that array is not empty
        if(!lines.isEmpty()&&status) {
            //TODO:
            for(int i = 0; i<lines.size(); i++) {
                
                //send this throttle value for time length : lines.get(i)[1] in seconds
                handleThrottle(lines.get(i)[1]);
            }
        }
    }
    
    private void handleThrottle(int value) {
        
    }
    
    @FXML
    private void handleExit(ActionEvent event) throws IOException {
        Stage stage = (Stage) exit.getScene().getWindow();
        stage.close();
    }
    
}
