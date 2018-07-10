/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eaics.CAN;

/**
 *
 * @author Troy
 */
public class CANRawStringMessages 
{
        private String msg;
	private boolean unread;
        private boolean isTimeToLog;

	public CANRawStringMessages()
	{
		this.msg = "";
		this.unread = false;
                this.isTimeToLog = false;
	}
        
        public void setIsTimeToLog()
        {
            this.isTimeToLog = !this.isTimeToLog;
        }
        
        public boolean isTimeToLog()
        {
            boolean temp;
            
            if(this.isTimeToLog)
            {
                temp = true;
                this.isTimeToLog = false;
            }
            else
            {
                temp = this.isTimeToLog;
            }
            return temp;
        }

	public void setMsg(String inMsg)
	{
		this.msg = inMsg;
		this.unread = true;
	}

	public String getMsg()
	{
		this.unread = false;
		return this.msg;
	}

	public boolean isUnread()
	{
		return this.unread;
	}    
}
