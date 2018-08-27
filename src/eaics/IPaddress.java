/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eaics;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Collections;
import java.util.Enumeration;

/**
 *
 * @author Troy
 */
public class IPaddress 
{
    private String ipAddress;
    
    public IPaddress()
    {
        this.ipAddress = "";
    }
    
    public IPaddress(String ipaddress)
    {
        this.ipAddress = ipaddress;
    }
    
    public String getIPaddress()
    {
        try
        {
            this.upDateIPaddress();
        }
        catch(SocketException e)
        {
            e.printStackTrace();
        }
        
        return this.ipAddress;
    }
    
    public void upDateIPaddress() throws SocketException
    {
        Enumeration<NetworkInterface> nets = NetworkInterface.getNetworkInterfaces();
        for (NetworkInterface netint : Collections.list(nets))
        {
            updateInterfaceInformation(netint);
        }
    }

    private void updateInterfaceInformation(NetworkInterface netint) throws SocketException 
    {
        //out.printf("Display name: %s\n", netint.getDisplayName());
        //out.printf("Name: %s\n", netint.getName());
        Enumeration<InetAddress> inetAddresses = netint.getInetAddresses();
        for (InetAddress inetAddress : Collections.list(inetAddresses)) 
        {
            if (!inetAddress.isLoopbackAddress() && !inetAddress.isLinkLocalAddress()) 
            {
                setIPaddress(inetAddress.toString().substring(1));
            }
        }
     }
    
    public void setIPaddress(String ipaddress)
    {
        this.ipAddress = ipaddress;
    }
}
