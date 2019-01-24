/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eaics.Settings;

/**
 *
 * @author Troy Burgess
 */
public interface Settings {
    public String getSettingsFileString();
    public void setSettings(String fileString);
    public void update();
}
