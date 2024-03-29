/*
 * ElectroAero Instrumentation and Control System
 * @author Marcus Pham
 * @version 3.6.7.0
 */
package eaics;

import eaics.Settings.SettingsEAICS;
import eaics.CAN.CANFilter;
import eaics.LOGGING.Logging;
import eaics.SER.Serial;
import eaics.UI.MainUIController;
import java.io.IOException;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * Main Application Loader for EAICS
 * @author Markcuz
 */
public class EAICS extends Application { 
    
    //Strings to define different operating conditions
    public static final String TRIFAN = "xti trifan 600";
    public static final String TRIKE = "ABM4-Y1";
    public static final String AEROSKI = "ElectroNautic Jetski";
    
    public static String currentAircraft;
    
    private static Serial comms1;
    private static Serial comms0;
    
    private static Logging logging;
    
    public static void main(String[] args) throws InterruptedException, IOException 
    {
        currentAircraft = TRIKE;
        
        //need to improve upon this....
        comms0 = new Serial("/dev/ttyUSB0");
        comms1 = new Serial("/dev/ttyUSB1");
        
        SettingsEAICS settings = SettingsEAICS.getInstance();
        settings.loadSettings();
  
        CANFilter.getInstance();    //Start the CANHandler and create all objects.
        
	// Pix Hawk Code ------------------------------------------------------
        String ipAddressString = settings.getPixHawkSettings().getIpAddress();
	final Process pixHawkProgram = Runtime.getRuntime().exec("sudo mavproxy.py --master=/dev/ttyACM0 --baudrate 57600 --out " + ipAddressString + ":14550 --aircraft MyCopter");   //start the pixHawkProgram
	
	// Logging to a CSV File Code ------------------------------------------
	
        logging = new Logging(comms0, comms1);
	    
	// Launch the User Interface (UI) --------------------------------------

        launch(args);
    }
    
    @Override
    public void start(Stage stage) throws Exception 
    {
        FXMLLoader loader;
                
        switch (currentAircraft) 
        {
            case TRIKE:
                loader = new FXMLLoader(getClass().getResource("/eaics/UI/Trike/FXMLTrikeMainUI.fxml"));
                break;
            case TRIFAN:
                loader = new FXMLLoader(getClass().getResource("/eaics/UI/Trifan/FXMLTrifanMainUI.fxml"));
                break;
            default:
                loader = new FXMLLoader(getClass().getResource("/eaics/UI/Trifan/FXMLTrifanMainUI.fxml"));
                break;
        }
        
        Scene scene = new Scene(loader.load());
        stage.setScene(scene);
        
        stage.setTitle("ElectroAero Instrumentation and Control System");
        
        //initialising main UI controller
        
        MainUIController mainUIcontroller = loader.getController();
        
        comms0.connect();
        comms1.connect();
        
        mainUIcontroller.initData(logging,comms0,comms1);
        stage.show();
    }
}