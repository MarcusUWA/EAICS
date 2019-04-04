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
import eaics.Settings.TYPEVehicle;
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
    
    private static Logging logging;
    
    public static void main(String[] args) throws InterruptedException, IOException  {
        
        SettingsEAICS settings = SettingsEAICS.getInstance();
        settings.loadSettings();
       
        CANFilter filter = CANFilter.getInstance();    //Start the CANHandler and create all objects.
        
        //need to improve upon this....
        Serial comms = Serial.getInstance();
        
        if((settings.getGeneralSettings().getVeh()==TYPEVehicle.WAVEFLYER)||settings.getGeneralSettings().getVeh() == TYPEVehicle.TRIFAN) {
            // Pix Hawk Code ------------------------------------------------------
            String ipAddressString = settings.getPixHawkSettings().getIpAddress();
            final Process pixHawkProgram = Runtime.getRuntime().exec("sudo mavproxy.py --master=/dev/ttyACM0 --baudrate 57600 --out " + ipAddressString + ":14550 --aircraft MyCopter");   //start the pixHawkProgram
        }
            
            // Logging to a CSV File Code ------------------------------------------
            
            logging = new Logging();
            
            // Launch the User Interface (UI) --------------------------------------
            
            launch(args);
    }
    
    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader loader;
        SettingsEAICS settings = SettingsEAICS.getInstance();
            
        switch (settings.getGeneralSettings().getVeh())  {
            case TRIKE_Prototype:
                loader = new FXMLLoader(getClass().getResource("/eaics/UI/Trike/FXMLTrikeMainUI.fxml"));
                break;
            case TRIFAN:
                loader = new FXMLLoader(getClass().getResource("/eaics/UI/Trifan/FXMLTrifanMainUI.fxml"));
                break;
            case WAVEFLYER:
                loader = new FXMLLoader(getClass().getResource("/eaics/UI/FXMLWaveFlyer/FXMLWaveFlyerMainUI.fxml"));
                break;
            case VERTICAL_TESTRIG:
                loader = new FXMLLoader(getClass().getResource("/eaics/UI/FXMLWaveFlyer/FXMLTrikeMainUI.fxml"));
                break;
            default:
                loader = new FXMLLoader(getClass().getResource("/eaics/UI/Trifan/FXMLTrikeMainUI.fxml"));
                break;
        }
        
        Scene scene = new Scene(loader.load());
        stage.setScene(scene);
        
        stage.setTitle("ElectroAero Instrumentation and Control System");
        
        //initialising main UI controller
        
        MainUIController mainUIcontroller = loader.getController();
        
        Serial.getInstance().connect();
        
        mainUIcontroller.initData(logging);
        stage.show();
    }
}