package eaics.FILE;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;

/**
 *
 * @author Troy
 */
public class FileWriterCSV implements FileWriter
{
    //Objects for file reading
    private FileOutputStream fileStrm;
    private OutputStreamWriter writer;
    private BufferedWriter bufWriter;
    
    public FileWriterCSV(String path)
    {	
        try
        {
            fileStrm = new FileOutputStream(path);     //Open the file
            writer = new OutputStreamWriter(fileStrm, "utf-8");                 //Create writer to write stream
            bufWriter = new BufferedWriter(writer);                             //To write the stream one line at a time
        }
        catch(IOException e)
        {
            e.printStackTrace();
        }
    }
    
    @Override
    public void write(String data) 
    {
        try
        {
            bufWriter.write(data);
            bufWriter.flush();	//flush the writer
        }
        catch(IOException e)
        {
            e.printStackTrace();
        }
    }
		
    @Override
    public void close()
    {
        try
        {
            bufWriter.close();	    
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }	
    }
}