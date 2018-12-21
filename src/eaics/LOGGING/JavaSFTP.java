/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eaics.LOGGING;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import com.jcraft.jsch.SftpException;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Markcuz
 */
public class JavaSFTP {
    
    JSch jsch;
    Session session; 
    
    Channel channel;
    ChannelSftp sftpChannel;
    
    JavaZip zip;
            
    public JavaSFTP() {
        jsch = new JSch();
        session = null;
        channel = null;
        sftpChannel = null;
        zip = new JavaZip();
    }
    
    public void openTunnel(String user, String address, String pass) {
        try {
            session = jsch.getSession(user, address, 22);           
            session.setConfig("StrictHostKeyChecking", "no");
            session.setPassword(pass);
            session.connect();
            
            channel = session.openChannel("sftp");
            channel.connect();
            sftpChannel = (ChannelSftp) channel;
            
        } catch (JSchException ex) {
            Logger.getLogger(JavaSFTP.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void closeTunnel() {
            sftpChannel.exit();
            session.disconnect();
    }
    
    public void downloadFile(String sourcePath, String destPath) {   
        try {  
            sftpChannel.get(sourcePath, destPath);
        } catch (SftpException ex) {
            Logger.getLogger(JavaSFTP.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void sendFile(String sourcePath, String destPath) {
        try {
            sftpChannel.put(sourcePath, destPath);   
        } catch (SftpException e) {
            e.printStackTrace();
        }
    }   
    
    public void sendFolder(String sourcePath, String destPath, String compName) {
        zip.compress(sourcePath, "/home/pi/"+compName+".zip");
        
        try { 
            sftpChannel.put("/home/pi/"+compName+".zip", destPath);
        } catch (SftpException ex) {
            Logger.getLogger(JavaSFTP.class.getName()).log(Level.SEVERE, null, ex);
        }

        try {
            Runtime.getRuntime().exec("rm /home/pi/"+compName+".zip");
        } catch (IOException ex) {
            Logger.getLogger(JavaSFTP.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
