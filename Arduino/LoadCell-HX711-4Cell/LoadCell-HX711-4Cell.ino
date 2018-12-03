/*
 Example using the SparkFun HX711 breakout board with a scale
 By: Nathan Seidle
 SparkFun Electronics
 Date: November 19th, 2014
 License: This code is public domain but you buy me a beer if you use this and we meet someday (Beerware license).
 
 This is the calibration sketch. Use it to determine the calibration_factor that the main example uses. It also
 outputs the zero_factor useful for projects that have a permanent mass on the scale in between power cycles.
 
 Setup your scale and start the sketch WITHOUT a weight on the scale
 Once readings are displayed place the weight on the scale
 Press +/- or a/z to adjust the calibration_factor until the output readings match the known weight
 Use this calibration_factor on the example sketch
 
 This example assumes pounds (lbs). If you prefer kilograms, change the Serial.print(" lbs"); line to kg. The
 calibration factor will be significantly different but it will be linearly related to lbs (1 lbs = 0.453592 kg).
 
 Your calibration factor may be very positive or very negative. It all depends on the setup of your scale system
 and the direction the sensors deflect from zero state

 This example code uses bogde's excellent library: https://github.com/bogde/HX711
 bogde's library is released under a GNU GENERAL PUBLIC LICENSE

 Arduino pin 2 -> HX711 CLK
 3 -> DOUT
 5V -> VCC
 GND -> GND
 
 Most any pin on the Arduino Uno will be compatible with DOUT/CLK.
 
 The HX711 board can be powered from 2.7V to 5V so the Arduino 5V power should be fine.
 
*/

#include "HX711.h"
#include <EEPROM.h>

#define delayTime 10 //in ms

#define DOUT1  3
#define CLK1  2

#define DOUT2  5
#define CLK2  4

#define DOUT3  7
#define CLK3  6

#define DOUT4  9
#define CLK4  8

HX711 scale(DOUT1, CLK1);
HX711 scale2(DOUT2, CLK2);
HX711 scale3(DOUT3, CLK3);
HX711 scale4(DOUT4, CLK4);

int count = 0;
float calibration_factor = 17550; //-7050 worked for my 440lb max scale setup

#define throttlePin A0
int sensorValue = 0;  // variable to store the value coming from the sensor
int throttle = 0;

void setup() {
  //being EEPROM load
  EEPROM.get(0,calibration_factor);
  
  Serial.begin(9600);
  tare();
}

void loop() {
  double weight1, weight2, weight3, weight4;
  double sum;

  weight1 = scale.get_units();
  weight2 = scale2.get_units();
  weight3 = scale3.get_units();
  weight4 = scale4.get_units();

  sum = weight1+weight2+weight3+weight4;

  sensorValue = analogRead(throttlePin); 

  //note this throttle, ranges from 195 - 852
  //eg. output = (sensorValue-min)*1024/(max-min);
  throttle = ((sensorValue-195) * 3)/2;

  if(throttle<0) throttle = 0;
  else if(throttle>1023) throttle = 1023;

  
  Serial.print(count);
  Serial.print(",");
  
  Serial.print(sum, 4);
  Serial.print(",");

  Serial.print(calibration_factor);
  Serial.print(",");

  Serial.print(throttle);
  Serial.print(",");
  
  Serial.print(weight1, 4);
  Serial.print(",");
  
  Serial.print(weight2, 4);
  Serial.print(",");

  Serial.print(weight3, 4);
  Serial.print(",");
  
  Serial.print(weight4, 4);

  Serial.println();
  
  if(Serial.available()) {
    char temp = Serial.read();
    if(temp == '+' || temp == 'a') {
      calibration_factor += 10;
      scale.set_scale(calibration_factor); //Adjust to this calibration factor
      scale2.set_scale(calibration_factor); //Adjust to this calibration factor
      scale3.set_scale(calibration_factor); //Adjust to this calibration factor
      scale4.set_scale(calibration_factor); //Adjust to this calibration factor
    }
    else if(temp == '-' || temp == 'z') {
      calibration_factor -= 10;
        scale.set_scale(calibration_factor); //Adjust to this calibration factor
        scale2.set_scale(calibration_factor); //Adjust to this calibration factor
        scale3.set_scale(calibration_factor); //Adjust to this calibration factor
        scale4.set_scale(calibration_factor); //Adjust to this calibration factor
    }
    else if(temp == '0') {
      tare();
    }
    //storing into EEPROM
    EEPROM.put(0,calibration_factor);
  }

  count++;
  delay(delayTime);
}

void tare() {
  scale.set_scale();
  scale.tare();  //Reset the scale to 0

  scale2.set_scale();
  scale2.tare();  //Reset the scale to 0

  scale3.set_scale();
  scale3.tare();  //Reset the scale to 0

  scale4.set_scale();
  scale4.tare();  //Reset the scale to 0

  scale.set_scale(calibration_factor); //Adjust to this calibration factor
  scale2.set_scale(calibration_factor); //Adjust to this calibration factor
  scale3.set_scale(calibration_factor); //Adjust to this calibration factor
  scale4.set_scale(calibration_factor); //Adjust to this calibration factor
}
