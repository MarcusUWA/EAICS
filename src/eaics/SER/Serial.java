/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package eaics.SER;

import com.fazecast.jSerialComm.SerialPort;
import com.fazecast.jSerialComm.SerialPortDataListener;
import com.fazecast.jSerialComm.SerialPortEvent;

/**
 * Serial class to handle serial communications
 * Requires usage of JSerialComm library, obtainable from https://github.com/Fazecast/jSerialComm/wiki
 * Currently using version 2.3.0
 * Expecting to see string of format:
 * Count, Thrust, Calibration Factor, Throttle, Load1, Load2, Load3, Load4
 * @author Markcuz
 */
public class Serial {
    
    private String path;
    private LoadCell cell;
    private SerialPort sp = null;
    private Throttle throttle;
    
    private SerialPort[] commPorts = null;

    final static int SPACE_ASCII = 32;
    final static int DASH_ASCII = 45;
    final static int NEW_LINE_ASCII = 10;
    
    public Serial(String path, LoadCell cell, Throttle throttle) {
        this.path = path;
        this.cell = cell;
        this.throttle = throttle;
        
        searchForPorts();
    }
    
    //search for all the serial ports
    //pre: none
    //post: adds all the found ports to a combo box on the GUI
    public void searchForPorts() {
        commPorts = SerialPort.getCommPorts();
    }
    
    //connect to the selected port in the combo box
    //pre: ports are already found by using the searchForPorts method
    //post: the connected comm port is stored in commPort, otherwise,
    //an exception is generated
    public void connect() {
        
        sp = SerialPort.getCommPort(path);
        
        sp.setComPortParameters(9600, 8, 1, 0); // default connection settings for Arduino
        sp.setComPortTimeouts(SerialPort.TIMEOUT_NONBLOCKING, 0, 0); // block until bytes can be written
    
        sp.openPort();
        
        StringBuffer buffer = new StringBuffer();
        
        sp.addDataListener(new SerialPortDataListener() {
            
            @Override
            public int getListeningEvents() { 
                return SerialPort.LISTENING_EVENT_DATA_AVAILABLE; 
            }
        
            @Override
            public void serialEvent(SerialPortEvent event) {
                
                
                if (event.getEventType() != SerialPort.LISTENING_EVENT_DATA_AVAILABLE) {
                    return;
                }
                
                byte[] newData = new byte[sp.bytesAvailable()];
                int numRead = sp.readBytes(newData, newData.length);
               
                String s = new String(newData);
                
                for(int i = 0; i<numRead; i++) {
                    if (s.charAt(i) != NEW_LINE_ASCII) {
                        buffer.append(s.charAt(i));
                    }
                    else {
                        cell.setMsg(buffer.toString());
                        throttle.setMsg(buffer.toString());
                        buffer.setLength(0);
                    } 
                }
                
            }
        });
    }

    //disconnect the serial port
    //pre: an open serial port
    //post: clsoed serial port
    public void disconnect() {
        sp.closePort();
    }

    //method that can be called to send data
    //pre: open serial port
    //post: data sent to the other device
    public void writeData(String data) {
        try { 
            sp.writeBytes(data.getBytes(), data.length());
        }
        catch (Exception e){
           System.out.println("Failed to write data. (" + e.toString() + ")");
        }
    }
}
