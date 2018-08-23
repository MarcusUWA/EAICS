/*
 * ElectroAero Instrumentation and Control System
 */
package eaics;

import eaics.CAN.BMS;
import eaics.CAN.CANFilter;
import eaics.SER.SERMessage;
import eaics.CAN.CANMessage;
import eaics.CAN.CANRawStringMessages;
import eaics.CAN.ESC;
import eaics.CAN.EVMS;
import eaics.CAN.EVMS_v3;
import eaics.SER.LoadCell;
import eaics.UI.MainUIController;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class EAICS extends Application 
{
    static CANFilter filter = new CANFilter();
    static LoadCell loadCell = new LoadCell();
    static IPaddress ipAddress = new IPaddress();
    
    @Override
    public void start(Stage stage) throws Exception 
    {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/eaics/UI/MainUI.fxml"));
        
        Scene scene = new Scene(loader.load());
        
        stage.setScene(scene);

        stage.setTitle("ElectroAero Instrumentation and Control System");
        
        //stage.setY(36);
        
        MainUIController controller = loader.getController();
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
	// Pix Hawk Code -------------------------------------------------------
	
	String ipAddressString = "192.168.1.6";
	final Process pixHawkProgram = Runtime.getRuntime().exec("sudo mavproxy.py --master=/dev/ttyACM0 --baudrate 57600 --out " + ipAddressString + ":14550 --aircraft MyCopter");   //start the pixHawkProgram
	
	
        // Logging Every 1 Second Code -----------------------------------------	
        
        final CANRawStringMessages canRawStringMessage = new CANRawStringMessages();
        
        Runnable Logger = new Runnable() 
        {
            @Override
            public void run() 
            {
                canRawStringMessage.setIsTimeToLog();
            }
        };

        ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);
        executor.scheduleAtFixedRate(Logger, 0, 1, TimeUnit.SECONDS);   // Run every second
	
	
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
			//System.out.println("Raw CAN Msg: " + rawCANmsg);
			//canRawStringMessage.setMsg(rawCANmsg);

			canMessageCAN0.newMessage(rawCANmsg);
			
			if(canMessageCAN0.getFrameID() != 461 && canMessageCAN0.getFrameID() != 462 && canMessageCAN0.getFrameID() != 463 && canMessageCAN0.getFrameID() != 464)
			{
			    filter.run(canMessageCAN0);
			}
			else
			{
			    //System.out.println("Raw CAN Msg: " + rawCANmsg + " >> " + canMessageCAN0.getFrameID());
			}
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
			//if(canMessageCAN1.getFrameID() != 461 && canMessageCAN1.getFrameID() != 462 && canMessageCAN1.getFrameID() != 463 && canMessageCAN1.getFrameID() != 464)
			//{
			    filter.run(canMessageCAN1);
			//}
			//else
			//{
			    //System.out.println("Raw CAN Msg: " + rawCANmsg + " >> " + canMessageCAN1.getFrameID());
			//}
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
                                        //System.out.println("Test " + loadCellmsg);
                                        if(loadCellmsg != null && !loadCellmsg.equals(""))
                                        {
					    loadCell.setMsg(loadCellmsg);
					    //System.out.println(loadCell.toString());
					    //System.out.println(loadCell.getWeight());					    
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
	
	
	Thread threadLoggingCSV = new Thread(new Runnable()
        {
	    @Override
	    public void run()
	    {
		SimpleDateFormat formatterDate = new SimpleDateFormat("yyyy/MM/dd");
		SimpleDateFormat formatterTime = new SimpleDateFormat("HH:mm:ss");
		Writer writer = null;
		//String filename = new SimpleDateFormat("yyyy-MM-dd hh-mm-ss'.csv'").format(new Date());
		String filename = "testCSV.csv";
		
		int countLines = 0;

		try
		{
		    writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(filename), "utf-8"));

		    String columnNames = "";
		    columnNames += "Date, Time, ";
		    columnNames += EVMS.getLoggingHeadings();
		    columnNames += EVMS_v3.getLoggingHeadings();
		    for(int ii = 1; ii <= 24; ii++)
		    {
			for(int jj = 1; jj <= 12; jj++)
			{
			    columnNames += "BMS " + ii + " Cell " + jj + " Voltage, ";			    
			}
			columnNames += "BMS " + ii + " Temp 1, ";
			columnNames += "BMS " + ii + " Temp 2, ";
		    }
		    for(int ii = 1; ii <= 4; ii++)
		    {
			columnNames += "ESC " + ii + " Battery Voltage, ";
			columnNames += "ESC " + ii + " Battery Current, ";
			columnNames += "ESC " + ii + " RPM, ";
			columnNames += "ESC " + ii + " Odometer, ";
			columnNames += "ESC " + ii + " Controller Temperature, ";
			columnNames += "ESC " + ii + " Motor Temperature, ";
			columnNames += "ESC " + ii + " Battery Temperature, ";
			columnNames += "ESC " + ii + " Requested Output PWM, ";
			columnNames += "ESC " + ii + " Real Output PWM, ";
			columnNames += "ESC " + ii + " Warnings, ";
			columnNames += "ESC " + ii + " Failures, ";
			columnNames += "ESC " + ii + " Remaining Battery Capacity, ";
			columnNames += "ESC " + ii + " Throttle Command, ";			
		    }
		    columnNames += "Load Cell\n";
		    writer.write(columnNames);

		    while(true)
		    {
			if(canRawStringMessage.isTimeToLog() && countLines < 100)
			{
			    String columnData = "";
			    Date date = new Date();
			    columnData += formatterDate.format(date) + ", " + formatterTime.format(date) + ", ";
			    
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
			    
			    //EVMS
			    ESC esc[] = filter.getESC();
			    for(int ii = 0; ii < esc.length; ii++)
			    {
				columnData += esc[ii].getLoggingString();
			    }
			    
			    //Load Cell
			    columnData += loadCell.getWeight() + "\n";
			    
			    // -------------------------------------------------
			    
			    writer.write(columnData);
			    writer.flush();	//flush the writer
			    
			    
			    countLines++;   //Delete this after testing
			}
		    }
		}
		catch(IOException e)
		{
			e.printStackTrace();
		}
		finally
		{
			try
			{
				writer.close();
			}
			catch(Exception e)
			{
			}
		}
	    }
        });

        threadLoggingCSV.start();	
	
	// Launch the User Interface (UI) --------------------------------------
        launch(args);
    }
}
