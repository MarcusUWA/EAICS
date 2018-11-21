/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eaics;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

/**
 *
 * @author Markcuz
 */
public class Config {
    
    
    public void LoadConfig() throws FileNotFoundException, IOException { 
    // pass the path to the file as a parameter 
    FileReader fr = 
      new FileReader("\\home\\pi\\bin\\eaics.conf"); 
  
    int i; 
    while ((i=fr.read()) != -1) {
        
    }
      
  } 
}
