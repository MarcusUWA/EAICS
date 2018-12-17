/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eaics.UI.Trike;

import eaics.CAN.CANFilter;
import eaics.SER.Throttle;
import eaics.UI.MainUIController;
import static eaics.UI.MainUIController.refreshFrequency;
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
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.util.Duration;

/**
 * FXML Controller class
 * CSV Profile loader
 * Format of csv: Throttle(Constant) | ThrottlePos (0-1024) | Time (s)  
 * @author Markcuz
 */
public class FXMLLoadProfileController implements Initializable 
{
    List<int[]> lines = new ArrayList<>();
    MainUIController gui;
    
    boolean status;
    
    @FXML
    TextField path;
    
    @FXML
    Button exit;
    
    private Throttle throttle;
                
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) 
    {
        // TODO
        status = true;
        isRunning = false;
    } 
    
    public void initData(MainUIController gui, Throttle throttle) 
    {
        this.gui = gui;
        gui.setOverrideStatus(true);
        this.throttle = throttle;
    }
    
    public void loadFile(String path) throws FileNotFoundException, IOException 
    {
        path = "/home/pi/LoadProfiles/testLP.csv";
        status = true;
        //clear list
        lines.clear();
        
        try 
        {
            BufferedReader br = new BufferedReader(new FileReader(path));
            
            String line = null;

            while ((line = br.readLine()) != null) 
            {
                String[] values = line.split(",");
                if(values.length == 2) 
                {
                    int row[] = new int[2];
                    //throttle value
                    try 
                    {
                        if(Integer.parseInt(values[0])>1024) 
                        {
                            row[0] = 1024;
                        }
                        else if(Integer.parseInt(values[0])<0) 
                        {
                            row[0] = 0;
                        }
                        else {
                            row[0] = Integer.parseInt(values[0]);  
                        }
                    }
                    catch (NumberFormatException e) {
                        row[0] = 0;
                        row[1] = 0;
                    }
                    
                    try 
                    {
                        int time = Integer.parseInt(values[1].trim());
                        
                        if(time < 0) 
                        {
                            row[1] = 0;
                        }
                        else 
                        {
                            row[1] = time; 
                        }
                        
                    }
                    catch (NumberFormatException e) 
                    {
                        row[1] = 0;
                    }
                    
                    lines.add(row);
                }
            }
            br.close();
        }
        catch(FileNotFoundException e) 
        {
            System.out.println("File not found...");
        }
    }
    
    public void showProfile() 
    {
        //todo: show the profile of the load profile file
    }
    
    @FXML
    private void handleLoadFile(ActionEvent event) throws IOException 
    {
        loadFile(path.getText());
        showProfile();
    }
    
    @FXML
    private void handleStop(ActionEvent event) throws IOException 
    {
        System.out.println("STOP BUTTON PRESSED!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
        stopLoadProfile();
    }
    
    private ScheduledExecutorService executor;
    private boolean isRunning;
    
    @FXML
    private void handleRun(ActionEvent event) throws IOException
    {
        if(!isRunning)
        {
            isRunning = true;
            throttle.setIsUsingManualThrottle(false);
            //ensure that array is not empty
            if(!lines.isEmpty()&&status) 
            {
                Runnable ThrottleCommandSender = new Runnable() 
                {
                    int ii = 0;
                    long startTime;
                    long endTime = System.currentTimeMillis();

                    @Override
                    public void run() 
                    {
                        if(System.currentTimeMillis() >= endTime && ii < lines.size())
                        {
                            //send this throttle value for time length : lines.get(i)[1] in seconds
                            //handleThrottle(lines.get(i)[1]);

                            System.out.println("Throttle: " + lines.get(ii)[0] + " Time: " + lines.get(ii)[1]);

                            throttle.setThrottleSetting(lines.get(ii)[0]);
                            startTime = System.currentTimeMillis();
                            endTime = startTime+(lines.get(ii)[1] * 1000);

                            ii++;

                            if(ii >= lines.size())
                            {
                                stopLoadProfile();
                            }
                        }
                    }
                };

                System.out.println("start executor");
                executor = Executors.newScheduledThreadPool(1);
                executor.scheduleAtFixedRate(ThrottleCommandSender, 0, 1, TimeUnit.SECONDS); 
                System.out.println("end start executor");
            }
        }
    }
    
    private void stopLoadProfile()
    {
        if(status)
        {
            this.executor.shutdown();
        }
        throttle.setIsUsingManualThrottle(true);
        isRunning = false;
        status = false;
    }
    
    @FXML
    private void handleExit(ActionEvent event) throws IOException 
    {
        Stage stage = (Stage) exit.getScene().getWindow();
        stage.close();
    }
    
}
