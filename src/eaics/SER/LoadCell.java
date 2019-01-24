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
public class LoadCell {
	private long time;
	private double weight;
        private double calibFactor;
        
        private double weightS;
        private double calibFactorS;
        
        private double loadCells[];
        int numCells;
	
	public LoadCell(int number) {
            this.time = 0;
            this.weight = 0.0;
            this.calibFactor = 0;
            loadCells = new double[number];
            numCells = number;
	}
	
	public void setMsg(String msg) {
            //System.out.println("Serial: "+msg);
            String[] msgArray = msg.split(",");
            
            if(msgArray.length == 10|| msgArray.length == 16) {
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
                
                for(int i = 0; i<numCells; i++) {
                    try {
                        this.loadCells[i] = Double.parseDouble(msgArray[i+4]);
                    }
                    catch (NumberFormatException e) {
                        this.loadCells[i] = 0;
                    }
                }
                    
                    try {
                        this.weightS = Double.parseDouble(msgArray[8]);
                    }
                    catch (NumberFormatException e) {
                        this.weightS = 0;
                    }
                    
                    
                    try {
                        this.calibFactorS = Double.parseDouble(msgArray[9]);
                    }
                    catch (NumberFormatException e) {
                        this.calibFactorS = 0;
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

        public double getWeightS() {
            return weightS;
        }

        public double getCalibFactorS() {
            return calibFactorS;
        }
	
	@Override
	public String toString() {
		return "" + time + " " + weight + "kg" + " " + calibFactor;
	}	
}