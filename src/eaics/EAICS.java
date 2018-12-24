/*
 * ElectroAero Instrumentation and Control System
 */
package eaics;

import eaics.Settings.IPAddress;
import eaics.Settings.EAICS_Settings;
import eaics.CAN.CANFilter;
import eaics.LOGGING.Logging;
import eaics.SER.LoadCell;
import eaics.SER.Serial;
import eaics.SER.Throttle;
import eaics.UI.MainUIController;
import java.io.IOException;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class EAICS extends Application 
{    
    public static final String TRIFAN = "xti trifan 600";
    public static final String TRIKE = "ABM4-Y1";
    
    public static String currentAircraft;
    
    private static Serial comms;
    private static LoadCell loadCell;
    private static Throttle throttle;
    
    private static Logging logging;
    
    public static void main(String[] args) throws InterruptedException, IOException 
    {
        currentAircraft = TRIKE;
        
        loadCell = new LoadCell();
        throttle = new Throttle();
        IPAddress ipAddress = new IPAddress();
        
        comms = new Serial("/dev/ttyUSB0", loadCell, throttle);
        
        //final Process vncServerProgram = Runtime.getRuntime().exec("sudo dispmanx_vncserver rfcbport 5900");
  
        CANFilter.getInstance();    //Start the CANHandler and create all objects.
        
        EAICS_Settings settings = EAICS_Settings.getInstance();
        settings.loadSettings();
        
	// Pix Hawk Code ------------------------------------------------------
        String ipAddressString = settings.getPixHawkSettings().getIpAddress();
	final Process pixHawkProgram = Runtime.getRuntime().exec("sudo mavproxy.py --master=/dev/ttyACM0 --baudrate 57600 --out " + ipAddressString + ":14550 --aircraft MyCopter");   //start the pixHawkProgram
	
	// Logging to a CSV File Code ------------------------------------------
	
        logging = new Logging(loadCell, throttle);
	    
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
        
        comms.connect();
        
        mainUIcontroller.initData(logging, loadCell, comms, throttle);
        stage.show();
    }
}