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
        
        private double loadCells[];
	
	public LoadCell() {
		this.time = 0;
		this.weight = 0.0;
		this.calibFactor = 0;
                loadCells = new double[4];
	}
	
	public void setMsg(String msg) {
            String[] msgArray = msg.split(",");
            
            if(msgArray.length == 8 ) {
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
                
                try {
                    this.loadCells[0] = Double.parseDouble(msgArray[4]);
                }
                catch (NumberFormatException e) {
                    this.loadCells[0] = 0;
                }
                
                try {
                    this.loadCells[1] = Double.parseDouble(msgArray[5]);
                }
                catch (NumberFormatException e) {
                    this.loadCells[1] = 0;
                }
                
                try {
                    this.loadCells[2] = Double.parseDouble(msgArray[6]);
                }
                catch (NumberFormatException e) {
                    this.loadCells[2] = 0;
                }
                
                try {
                    this.loadCells[3] = Double.parseDouble(msgArray[7]);
                }
                catch (NumberFormatException e) {
                    this.loadCells[3] = 0;
                }
            }
            
            else {
                System.out.println("Invalid Serial Length: "+msgArray.length);
            }
	}
        
        
	public String getMsg() {
		String outString = "";
		outString = toString();
		return outString;
	}
        
        public double getWeight()  {
            return weight;
        }
        
        public double getCalibration()  {
            return calibFactor;
        }
        
        public double getLoadCells(int pos) {
            return loadCells[pos];
        }
	
	@Override
	public String toString() {
		return "" + time + " " + weight + "kg" + " " + calibFactor;
	}	
}