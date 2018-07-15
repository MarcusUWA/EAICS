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
    
    @Override
    public void start(Stage stage) throws Exception 
    {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/eaics/UI/MainUI.fxml"));
        
        Scene scene = new Scene(loader.load());
        
        stage.setScene(scene);

        stage.setTitle("ElectroAero Instrumentation and Control System");
        
        stage.setY(36);
        
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
        final CANMessage message = new CANMessage();
        final CANRawStringMessages canMsg = new CANRawStringMessages();
        
        Runnable Logger = new Runnable() 
        {
            @Override
            public void run() 
            {
                canMsg.setIsTimeToLog();
            }
        };

        ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);
        executor.scheduleAtFixedRate(Logger, 0, 1, TimeUnit.SECONDS);   // Run every second
        
        final Process candumpProgram = Runtime.getRuntime().exec("/home/pi/bin/ReadCAN can0 -tz");

        Thread t1 = new Thread(new Runnable()
        {
                public void run()
                {
                        BufferedReader input = new BufferedReader(new InputStreamReader(candumpProgram.getInputStream()));
                        String rawCANmsg = null;

                        try
                        {
                                while((rawCANmsg = input.readLine()) != null)
                                {
                                        System.out.println("Test " + rawCANmsg);
                                        canMsg.setMsg(rawCANmsg);
                                }
                        }
                        catch(IOException e)
                        {
                                e.printStackTrace();
                        }
                }
        });

        t1.start();

        final Process loadCellProgram = Runtime.getRuntime().exec("/home/pi/bin/LoadCell");

        Thread t2 = new Thread(new Runnable()
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

        t2.start();
	
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
                                    //if(canMsg.isUnread())
                                    String temp = canMsg.getMsg();
                                    if(!temp.equals("") && canMsg.isTimeToLog())
                                    {
                                            message.newMessage(temp);
                                            filter.run(message);
                                            //System.out.println(filter.toString() + " " + loadCell.toString());
                                            writer.write(filter.toString() + " " + loadCell.toString() + "\n");
                                            writer.flush();	//flush the writer
                                    }
                                    if(canMsg.isTimeToLog())
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

        //TimeUnit.SECONDS.sleep(1);
	
	// UI stuff here please
        launch(args);
    }
    
    /**
     * Initialises the CAN Connection for reading only!
     * @throws InterruptedException
     * @throws IOException 
     */
    private static void initCAN() throws InterruptedException, IOException
    {
        
    }
    
    /**
     * Initialises the Serial console for reading
     * @throws IOException 
     */
    private static void initSER() throws IOException 
    {
	
    }
    
    private static void writeLogFile() throws FileNotFoundException, UnsupportedEncodingException 
    {
        
    }
}
