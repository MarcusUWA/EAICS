/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eaics.CAN.Battery;

import eaics.CAN.CANFilter;
import eaics.CAN.MiscCAN.CANMessage;
import eaics.Settings.SettingsEAICS;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 *
 * @author Markcuz
 */
public class CANMiniDAQ {
    
    final int CANaddress = 0x40;
    
    CANFilter filter;
    int[] input1;
    int[] input2;
    int[] input3;
    
    int[] output1;
    int[] output2;

    int refreshRate = 100; //in ms
    
    private ScheduledExecutorService displayExecutor;

    public CANMiniDAQ(CANFilter filter) {
	this.filter = filter;
        this.input1 = new int[64];
        this.input2 = new int[64];
        this.input3 = new int[64];

        this.output1 = new int[64];
        this.output2 = new int[64];
        
        
       // sendMessages();
    }
    
    public void setAll(CANMessage message){
        int prechargeNum = message.getByte(0);
	input1[prechargeNum] = message.getByte(1);
        input2[prechargeNum] = message.getByte(2);
        input3[prechargeNum] = message.getByte(3);
    }
    
    public void setOutput(int num, int outputNum, int value){
        switch(outputNum) {
            case 1:
                output1[num] = value;
                break;
            case 2:
                output2[num] = value;
                break;
            default:
                break;
        }
    }
    
    private void sendMessages() {
        displayExecutor = null;
        Runnable Id = new Runnable() { 

            int count = 0;

            @Override
            public void run() {
                filter.getCANHandler(SettingsEAICS.getInstance().getCanSettings().getMinidaqCAN()).writeMessage(CANaddress, new int[]{0, 1, 0, 0, 0, 0, 0, 0 });
            }
        };
        displayExecutor = Executors.newScheduledThreadPool(1);
        displayExecutor.scheduleAtFixedRate(Id, 0, 200, TimeUnit.MILLISECONDS);
    }

    public int[] getInput1() {
        return input1;
    }

    public int[] getInput2() {
        return input2;
    }

    public int[] getInput3() {
        return input3;
    }

    public int[] getOutput1() {
        return output1;
    }

    public int[] getOutput2() {
        return output2;
    }
    
}
