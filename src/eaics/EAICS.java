/*
 * ElectroAero Instrumentation and Control System
 */
package eaics;

import eaics.Settings.IPAddress;
import eaics.Settings.EAICS_Settings;
import eaics.CAN.BMS;
import eaics.CAN.CANFilter;
import eaics.CAN.CurrentSensor;
import eaics.CAN.ESC;
import eaics.CAN.EVMS_v3;
import eaics.FILE.FileWriterCSV;
import eaics.MiscCAN.CANHandler;
import eaics.SER.LoadCell;
import eaics.SER.Serial;
import eaics.SER.Throttle;
import eaics.UI.MainUIController;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class EAICS extends Application 
{    
    public static final String TRIFAN = "xti trifan 600";
    public static final String TRIKE = "ABM4-Y1";
    
    public static String currentAircraft = TRIKE;
    
    static LoadCell loadCell = new LoadCell();
    static Throttle throttle = new Throttle();
    static IPAddress ipAddress = new IPAddress();
    
    Serial comms = new Serial("/dev/ttyUSB0", loadCell, throttle);
  
    /**
     * @param args the command line arguments
     * @throws java.lang.InterruptedException
     * @throws java.io.IOException
     */
    public static void main(String[] args) throws InterruptedException, IOException 
    {
        //final Process vncServerProgram = Runtime.getRuntime().exec("sudo dispmanx_vncserver rfcbport 5900");
  
        CANFilter.getInstance();    //Start the CANHandler and create all objects
        
        EAICS_Settings settings = EAICS_Settings.getInstance();
        settings.loadSettings();
        
	// Pix Hawk Code ------------------------------------------------------
        String ipAddressString = settings.getPixHawkSettings().getIpAddress();
	final Process pixHawkProgram = Runtime.getRuntime().exec("sudo mavproxy.py --master=/dev/ttyACM0 --baudrate 57600 --out " + ipAddressString + ":14550 --aircraft MyCopter");   //start the pixHawkProgram
	
        
        //CANFilter.getInstance().getCharger().startSendHandshake();
	
	// Logging to a CSV File Code ------------------------------------------
	
	startLogging();
	    
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
        
        mainUIcontroller.initData(loadCell, comms, throttle);
        stage.show();
    }
    
    public static void startLogging()
    {
        SimpleDateFormat formatterDate = new SimpleDateFormat("yyyy/MM/dd");
	SimpleDateFormat formatterTime = new SimpleDateFormat("HH:mm:ss");
	String filename = new SimpleDateFormat("yyyy-MM-dd hh-mm-ss'.csv'").format(new Date());
	
	String rpmFile = new SimpleDateFormat("yyyy-MM-dd hh-mm-ss'.csv'").format(new Date());
	rpmFile = "rpm-" + rpmFile;
	
	FileWriterCSV fileWriterCSV = new FileWriterCSV(filename, 0);
	FileWriterCSV rpmFileWriterCSV = new FileWriterCSV(rpmFile, 1);
	    
	Runnable Logger = new Runnable() 
	{
	    private Date date = new Date();
	    
	    @Override
	    public void run() 
	    {
		CANFilter filter = CANFilter.getInstance();
		
		if(!filter.hasCANBus0TimedOut())    //CAN Bus 0 hasn't timed out yet so check if it has
		{
		    if((date.getTime() - filter.getLastPacketRecievedCANbus0()) > 3)	//CAN Bus 0 timed out?
		    {
			filter.can0Timeout();	//If yes then set CAN Bus 0 has timed out
		    }
		}
		
		if(!filter.hasCANBus1TimedOut())    //CAN Bus 1 hasn't timed out yet so check if it has
		{
		    if((date.getTime() - filter.getLastPacketRecievedCANbus1()) > 3)	//CAN Bus 1 timed out?
		    {
			filter.can1Timeout();	//If yes then set CAN Bus 1 has timed out
		    }
		}
		
		String columnData = "";
		Date date = new Date();
		columnData += formatterDate.format(date) + " " + formatterTime.format(date) + ", ";

		//Current Sensor
		CurrentSensor currentSensor = filter.getCurrentSensor();
		columnData += currentSensor.getCurrent();
		columnData += ", ";

		//EVMS
		EVMS_v3 evms = (EVMS_v3)filter.getEVMS_v3();
		columnData += evms.getLoggingString();

		//BMS
		BMS bms[] = filter.getBMS();
		for(int ii = 0; ii < bms.length; ii++)
		{
		    columnData += bms[ii].getVoltagesString();
		    columnData += bms[ii].getTemperatureString();
		}

		//ESC
		ESC esc[] = filter.getESC();
		for(int ii = 0; ii < esc.length; ii++)
		{
		    columnData += esc[ii].getLoggingString();
		}

		//Load Cell
		columnData += loadCell.getWeight()+",";
                
                columnData += loadCell.getCalibration()+",";
                
                for(int i = 0; i<4; i++) {
                    columnData += loadCell.getLoadCells(i)+",";
                }

                columnData +="\n";
                
		// -------------------------------------------------

		fileWriterCSV.write(columnData);
		
		String rpmColumnData = "";

		rpmColumnData += formatterDate.format(date) + " " + formatterTime.format(date) + ", ";

		//Current Sensor
		rpmColumnData += currentSensor.getCurrent();
		rpmColumnData += ", ";

		//EVMS
		rpmColumnData += evms.getLoggingString();

		//ESC
		rpmColumnData += esc[0].getLoggingString();
		
		//Power
		rpmColumnData += (evms.getVoltage() * (currentSensor.getCurrent() / 1000)) / 1000 + "\n";

		// -------------------------------------------------

		rpmFileWriterCSV.write(rpmColumnData);
	    }
	};

	ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);
	executor.scheduleAtFixedRate(Logger, 0, 1, TimeUnit.SECONDS);   // Run every second
	CANFilter.getInstance().setLoggingExecutor(executor);
    }
}