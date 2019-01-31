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

#define OUTS 11
#define CLKS 10

HX711 scale(DOUT1, CLK1);
HX711 scale2(DOUT2, CLK2);
HX711 scale3(DOUT3, CLK3);
HX711 scale4(DOUT4, CLK4);

HX711 scaleS(OUTS, CLKS);

float calibration_factor = 17550; //-7050 worked for my 440lb max scale setup

float calibration_factorS = 17550;

#define throttlePin A0
float throttle = 0;

int pressurePin = A1;   
int pressureValue = 0; 
float pressureVout=0;
float pressureP=0;

// which analog pin to connect
#define THERM2 A2  
#define THERM3 A3 
#define THERM4 A4 
#define THERM5 A5 
#define THERM6 A6 
#define THERM7 A7 

// resistance at 25 degrees C
#define THERMISTORNOMINAL 10000      
// temp. for nominal resistance (almost always 25 C)
#define TEMPERATURENOMINAL 25   

// how many samples to take and average, more takes longer
// but is more 'smooth'
#define NUMSAMPLES 5
// The beta coefficient of the thermistor (usually 3000-4000)
#define BCOEFFICIENT 3950

// the value of the 'other' resistor
#define SERIESRESISTOR 10000    
 
uint16_t samples[NUMSAMPLES][6];

uint16_t thrSamples[NUMSAMPLES];

#define inline true

void setup() {
  //being EEPROM load
  EEPROM.get(0,calibration_factor);

  if(inline) {
    EEPROM.get(sizeof(float), calibration_factorS);
  }
  
  Serial.begin(9600);

    analogReference(EXTERNAL);

  pinMode(throttlePin, INPUT);
  pinMode(THERM2, INPUT);
  pinMode(THERM3, INPUT);
  pinMode(THERM4, INPUT);
  pinMode(THERM5, INPUT);
  pinMode(THERM6, INPUT);
  pinMode(THERM7, INPUT);
  
  tare();
}

void loop() {
  double weight1, weight2, weight3, weight4, weightS;
  double sum;

  weight1 = scale.get_units();
  weight2 = scale2.get_units();
  weight3 = scale3.get_units();
  weight4 = scale4.get_units();

  if(inline) {
    weightS = scaleS.get_units();
  }

  sum = weight1+weight2+weight3+weight4;

  for (int i=0; i< NUMSAMPLES; i++) {

          thrSamples[i] = analogRead(throttlePin);
          delay(10);
          samples[i][0] = analogRead(THERM2);
          delay(10);
          samples[i][1] = analogRead(THERM3);
          delay(10);
          samples[i][2] = analogRead(THERM4);
          delay(10);
          samples[i][3] = analogRead(THERM5);
          delay(10);
          samples[i][4] = analogRead(THERM6);
          delay(10);
          samples[i][5] = analogRead(THERM7);
          delay(10);
    }

    float thrAv = 0;
    for(int i = 0; i<NUMSAMPLES; i++) {
      thrAv += thrSamples[i];
    }

    thrAv = thrAv/NUMSAMPLES;
      //note this throttle, ranges from 195 - 852
  //eg. output = (sensorValue-min)*1024/(max-min);

  //for throttle one...
  //throttle = ((sensorValue-195) * 1024.0/(852-195));
  // throttle two
  throttle = ((thrAv-280) * 1024.0/(775-280));

  if(throttle<0) throttle = 0;
  else if(throttle>1023) throttle = 1023;

  Serial.print("EA");
  Serial.print(",");
  
  Serial.print(sum, 4);
  Serial.print(",");

  Serial.print(calibration_factor);
  Serial.print(",");

  Serial.print((int)round(throttle));
  Serial.print(",");
  
  Serial.print(weight1, 4);
  Serial.print(",");
  
  Serial.print(weight2, 4);
  Serial.print(",");

  Serial.print(weight3, 4);
  Serial.print(",");
  
  Serial.print(weight4, 4);
 
  if(inline) {
    Serial.print(",");
    Serial.print(weightS, 4);
    Serial.print(",");
  
    Serial.print(calibration_factorS);
  }
  
  uint8_t i;
  float average[6];

   for(int j=0; j<6; j++) {
      average[j] = 0;
      for (i=0; i< NUMSAMPLES; i++) {
       average[j] += samples[i][j];
      }
    average[j] /= NUMSAMPLES;

    // convert the value to resistance
    average[j] = 1023 / average[j] - 1;
    average[j] = SERIESRESISTOR / average[j];

    float steinhart;
    steinhart = average[j] / THERMISTORNOMINAL;     // (R/Ro)
    steinhart = log(steinhart);                  // ln(R/Ro)
    steinhart /= BCOEFFICIENT;                   // 1/B * ln(R/Ro)
    steinhart += 1.0 / (TEMPERATURENOMINAL + 273.15); // + (1/To)
    steinhart = 1.0 / steinhart;                 // Invert
    steinhart -= 273.15;                         // convert to C

    Serial.print(","); 
    Serial.print(steinhart);
  }

  
    
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

    else if(temp == 's') {
      calibration_factorS += 10;
      scaleS.set_scale(calibration_factorS); //Adjust to this calibration factor
    }
    else if(temp == 'x') {
      calibration_factorS -= 10;
      scaleS.set_scale(calibration_factorS); //Adjust to this calibration factor
    }
    else if(temp == '0') {
      tare();
    }

    //storing into EEPROM
    EEPROM.put(0,calibration_factor);
    EEPROM.put(sizeof(float),calibration_factorS);
  }

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

  if(inline) {
    scaleS.set_scale();
    scaleS.tare();  //Reset the scale to 0
    scaleS.set_scale(calibration_factorS); //Adjust to this calibration factor
  }
}
