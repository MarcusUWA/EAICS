/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eaics.SER;

/**
 *
 * @author Markcuz
 */
public class LoadCell
{
	private long time;
	private double weight;
        private double calibFactor;
        
	private boolean isUnread;
	
	public LoadCell() {
		this.time = 0;
		this.weight = 0.0;
		this.calibFactor = 0;
		this.isUnread = false;
	}
	
	public void setMsg(String msg) {
		String[] msgArray = msg.split(",");
                
                if(msgArray.length > 2) {
                    try {
                        this.time =  Long.parseLong(msgArray[0]);
                    }
                    catch (NumberFormatException e) {
                        this.time = 0;
                    }
                    
                    try {
                        this.weight = Double.parseDouble(msgArray[1]);
                    }
                    catch (NumberFormatException e) {
                        this.weight = 0;
                    }
                    
                    try {
                        this.calibFactor = Double.parseDouble(msgArray[2]);
                    }
                    catch (NumberFormatException e) {
                        this.calibFactor = 0;
                    }
                    isUnread = true;
                }
	}

	public boolean isUnread(){
		return this.isUnread;
	}

	public String getMsg() {
		String outString = "";
		if(isUnread == true) {
			isUnread = false;
			outString = toString();
                }
		return outString;
	}
        
        public double getWeight()  {
            return weight;
        }
        
        public double getCalibration()  {
            return calibFactor;
        }
	
	@Override
	public String toString()
	{
		return "" + time + " " + weight + "kg" + " " + calibFactor;
	}	
}