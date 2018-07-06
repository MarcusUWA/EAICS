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
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class EAICS extends Application 
{
    static CANFilter filter = new CANFilter();
    static LoadCell loadCell = new LoadCell();
    
    //static CANFilter filter = new CANFilter();
    //static LoadCell loadCell = new LoadCell();
    
    //static Thread t1;
    //static Thread t2;
    
    @Override
    public void start(Stage stage) throws Exception 
    {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/eaics/UI/MainUI.fxml"));
        
        Scene scene = new Scene(loader.load());
        
        stage.setScene(scene);
        
        stage.setTitle("ElectroAero Instrumentation and Control System");
        
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
        //final CANFilter filter = new CANFilter();
        
        //CANMessage message = new CANMessage();
       // CANFilter filter = new CANFilter();

        final Process candumpProgram = Runtime.getRuntime().exec("/home/pi/bin/ReadCAN can0 -tz");

        final CANRawStringMessages canMsg = new CANRawStringMessages();

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

        //final LoadCell loadCell = new LoadCell();
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
                public void run()
                {
                        String temp = "";
			int count = 0;

			Writer writer = null;

			try
			{
				writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream("log.txt"), "utf-8"));	

				while(true)
				{
					if(canMsg.isUnread())
					{
						message.newMessage(canMsg.getMsg());
						System.out.println(filter.run(message) + " " + loadCell.toString());
						writer.write(filter.run(message) + " " + loadCell.toString() + "\n");
						writer.flush();	//flush the writer
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
