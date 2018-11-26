/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eaics.Settings;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Collections;
import java.util.Enumeration;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author Markcuz
 */
public class IPAddress  {
    
    private static final boolean DEBUG = false;
    
    private String LANIP;
    private String WifiIP;
    private String WifiSSID;
    
    public IPAddress() {
        this.LANIP = "";
        this.WifiIP = "";
        this.WifiSSID = "";
    }
    
    public String getLANIP() {
        return this.LANIP;
    }
    public String getWifiIP() {
        return this.WifiIP;
    }
    public String getWifiSSID() {
        return this.WifiSSID;
    }
    
    public void updateIPAddress() throws SocketException, IOException {
        Enumeration<NetworkInterface> nets = NetworkInterface.getNetworkInterfaces();
        
        Pattern ethPattern = Pattern.compile("eth");   // the pattern to search for
        Pattern wifiPattern = Pattern.compile("wlan");   // the pattern to search for
        
        for (NetworkInterface netint : Collections.list(nets)) {
            
            if(DEBUG) {
                System.out.println("Display name: "+ netint.getDisplayName());
                System.out.println("Name: "+ netint.getName());
            }
          
            Matcher ethMatch = ethPattern.matcher(netint.getDisplayName());
            Matcher wifiMatch = wifiPattern.matcher(netint.getDisplayName());
            
            if (ethMatch.find()) {
                if(DEBUG) {
                    System.out.println("Found ETH match");
                }
                Enumeration<InetAddress> inetAddresses = netint.getInetAddresses();
                for (InetAddress inetAddress : Collections.list(inetAddresses)) {
                    if (!inetAddress.isLoopbackAddress() && !inetAddress.isLinkLocalAddress()) {
                        if(DEBUG) {
                            System.out.println("InetAddress: " + inetAddress);
                        }
                        LANIP = inetAddress.toString().substring(1);
                    }
                }
            }
            
            if (wifiMatch.find()) {
                if(DEBUG) {
                    System.out.println("Found WLAN match");
                }
                Enumeration<InetAddress> inetAddresses = netint.getInetAddresses();
                for (InetAddress inetAddress : Collections.list(inetAddresses)) {
                    if (!inetAddress.isLoopbackAddress() && !inetAddress.isLinkLocalAddress()) {
                        if(DEBUG) {
                            System.out.println("InetAddress: " + inetAddress);
                        }
                        WifiIP = inetAddress.toString().substring(1);
                    }
                }
                
                Runtime rt = Runtime.getRuntime();
                Process proc = rt.exec("sudo iwgetid");
           
                BufferedReader stdInput = new BufferedReader(new InputStreamReader(proc.getInputStream()));
        
                String s = null;
                while (((s = stdInput.readLine()) != null)) {
                    if(s.contains("ESSID")) {
                        s = s.trim();
                        s = s.substring(16);
                        WifiSSID = s;
                    }
                }
            }
        }
    }
}
