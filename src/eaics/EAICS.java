/*
 * ElectroAero Instrumentation and Control System
 */
package eaics;

import eaics.CAN.CANFilter;
import eaics.SER.SERMessage;
import eaics.CAN.CANMessage;
import eaics.CAN.CANRawStringMessages;
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
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
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
	
	/*
	Thread t3 = new Thread(new Runnable()
        {
                @Override
                public void run()
                {
                    Writer writer = null;
                    int count = 0;

                    try
                    {
                            writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream("log.txt"), "utf-8"));	

                            while(true)
                            {
                                    //if(canRawStringMessage.isUnread())
                                    //String temp = canRawStringMessage.getMsg();
                                    //if(!temp.equals("") && canRawStringMessage.isUnread())
                                    //{
                                            //message.newMessage(temp);
                                            //filter.run(canMessage);
                                            //System.out.println(filter.toString() + " " + loadCell.toString());
					    
					    //if(canRawStringMessage.isTimeToLog())
					    //{
					//	writer.write(filter.toString() + " " + loadCell.toString() + "\n");
					//	writer.flush();	//flush the writer
					   // }
                                   // }
				    
                                    if(canRawStringMessage.isTimeToLog())
                                    {
                                        System.out.println("test: " + count++);
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

        t3.start();
	*/

        //TimeUnit.SECONDS.sleep(1);
	
	
	// Launch the User Interface (UI) --------------------------------------
        launch(args);
    }
}
