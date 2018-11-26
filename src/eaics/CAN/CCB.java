/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eaics.CAN;

/**
 *
 * @author Markcuz
 */
public class CCB 
{
    private int size;
    private boolean[] ccb;

    public CCB(int size) 
    {
	this.size = size;
        ccb = new boolean[size];
    }
    
    public void setAll(int ID, CANMessage message)
    {
        switch (ID) 
	{
            case 81:
                ccb[0] = ((message.getByte(0)&0x01) == 1);
                break;
            case 83:
                ccb[1] = ((message.getByte(0)&0x01) == 1);
                break;
            case 85:
                ccb[2] = ((message.getByte(0)&0x01) == 1);
                break;
            default:
                break;
        } 
    }
    
    public boolean isCCB_On(int index)
    {
	boolean isCCB_On = false;
	
	if(index >= 0 && index < size)
	{
	    isCCB_On = ccb[index];
	}
	else
	{
	    throw new IllegalArgumentException("There are only 3 CCBs");
	}
	
	return isCCB_On;
    }
}
