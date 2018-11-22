/*
 * ElectroAero Instrumentation and Control System
 */
package eaics;

import eaics.CAN.BMS;
import eaics.CAN.CANFilter;
import eaics.CAN.CANMessage;
import eaics.CAN.CurrentSensor;
import eaics.CAN.ESC;
import eaics.CAN.EVMS_v3;
import eaics.FILE.FileWriterCSV;
import eaics.SER.LoadCell;
import eaics.UI.MainUIController;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
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
    private static final boolean DEBUG = false;
    
    static CANFilter filter = new CANFilter();
    static LoadCell loadCell = new LoadCell();
    static IPAddress ipAddress = new IPAddress();
    
    //Serial comms = new Serial("/dev/ttyUSB0");
    
    @Override
    public void start(Stage stage) throws Exception 
    {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/eaics/UI/MainUI.fxml"));
        Scene scene = new Scene(loader.load());
        stage.setScene(scene);
        
        stage.setTitle("ElectroAero Instrumentation and Control System");
        
        //initialising main UI controller
        MainUIController controller = loader.getController();
        
        /*
        startSerComms();
        controller.initData(filter, comms);
        */
        
        controller.initData(filter, loadCell);
        stage.show();
    }

    /**
     * @param args the command line arguments
     * @throws java.lang.InterruptedException
     * @throws java.io.IOException
     */
    public static void main(String[] args) throws InterruptedException, IOException 
    {
       
	// Pix Hawk Code ------------------------------------------------------
        Settings settings = new Settings();
        String ipAddressString = settings.getPixHawkSettings().getIpAddress();
	final Process pixHawkProgram = Runtime.getRuntime().exec("sudo mavproxy.py --master=/dev/ttyACM0 --baudrate 57600 --out " + ipAddressString + ":14550 --aircraft MyCopter");   //start the pixHawkProgram
	//final Process pixHawkProgram = Runtime.getRuntime().exec("sudo mavproxy.py --master=/dev/ttyACM0 --baudrate 57600 --out " + ipAddressString + "--aircraft MyCopter");
	
	// Read the CAN 0 interface (CAN B on the Hardware) --------------------
	final CANMessage canMessageCAN0 = new CANMessage();
        final Process candumpProgramCAN0 = Runtime.getRuntime().exec("/home/pi/bin/ReadCAN can0 -tz");

        Thread threadReadCAN0 = new Thread(new Runnable()
        {
	    public void run()
	    {
		BufferedReader input = new BufferedReader(new InputStreamReader(candumpProgramCAN0.getInputStream()));
		String rawCANmsg = null;

		try
		{
		    while((rawCANmsg = input.readLine()) != null)
		    {
			canMessageCAN0.newMessage(rawCANmsg);
                        if(DEBUG) {
                            System.out.println(rawCANmsg);
                        }
			filter.run(canMessageCAN0);
		    }
		}
		catch(IOException e)
		{
		    e.printStackTrace();
		}
	    }
        });
	threadReadCAN0.start();
	
	// Read the CAN 1 interface (CAN A on the Hardware) --------------------
        
	final CANMessage canMessageCAN1 = new CANMessage();
        final Process candumpProgramCAN1 = Runtime.getRuntime().exec("/home/pi/bin/ReadCAN can1 -tz");

        Thread threadReadCAN1 = new Thread(new Runnable()
        {
	    public void run()
	    {
		BufferedReader input = new BufferedReader(new InputStreamReader(candumpProgramCAN1.getInputStream()));
		String rawCANmsg = null;

		try
		{
		    while((rawCANmsg = input.readLine()) != null)
		    {
                        canMessageCAN1.newMessage(rawCANmsg);
                        if(DEBUG) {
                            System.out.println(rawCANmsg);
                        }
			filter.run(canMessageCAN1);
		    }
		}
		catch(IOException e)
		{
		    e.printStackTrace();
		}
	    }
        });

        threadReadCAN1.start();
	
	
	// Load Cell Code ------------------------------------------------------

        final Process loadCellProgram = Runtime.getRuntime().exec("/home/pi/bin/LoadCell");

        Thread threadLoadCell = new Thread(new Runnable()
        {
	    public void run()
	    {
		BufferedReader input = new BufferedReader(new InputStreamReader(loadCellProgram.getInputStream()));
		String loadCellmsg = null;

		try
		{
		    boolean isFirst = true;
		    while((loadCellmsg = input.readLine()) != null && isFirst)
		    {
			isFirst = false;
		    }

		    while((loadCellmsg = input.readLine()) != null)
		    {
			if(loadCellmsg != null && !loadCellmsg.equals(""))
			{
			    loadCell.setMsg(loadCellmsg);
			}
		    }
		}
		catch(IOException e)
		{
		    e.printStackTrace();
		}
	    }
        });
        threadLoadCell.start();
	
	
	// Logging to a CSV File Code ------------------------------------------
	
	SimpleDateFormat formatterDate = new SimpleDateFormat("yyyy/MM/dd");
	SimpleDateFormat formatterTime = new SimpleDateFormat("HH:mm:ss");
	String filename = new SimpleDateFormat("yyyy-MM-dd hh-mm-ss'.csv'").format(new Date());
	
	String rpmFile = new SimpleDateFormat("yyyy-MM-dd hh-mm-ss'.csv'").format(new Date());
	rpmFile = "rpm-" + rpmFile;
	
	FileWriterCSV fileWriterCSV = new FileWriterCSV(filename, 0);
	FileWriterCSV rpmFileWriterCSV = new FileWriterCSV(rpmFile, 1);
	    
	Runnable Logger = new Runnable() 
	{
	    @Override
	    public void run() 
	    {
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
		columnData += loadCell.getWeight() + "\n";

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
	filter.setLoggingExecutor(executor);
	    
	// Launch the User Interface (UI) --------------------------------------
        launch(args);
    }
    
    /*
    
    private void startSerComms() {
        comms.connect();
        if (comms.getConnected() == true) {
            if (comms.initIOStream() == true) {
                comms.initListener();
            }
        }
    }
        
    private void stopSerComms() {
        comms.disconnect();
    }
*/
    
    
    
}