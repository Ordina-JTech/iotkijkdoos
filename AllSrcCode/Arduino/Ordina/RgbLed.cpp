#include <Arduino.h>
#include <SoftwareSerial.h>
#include "RgbLed.h"

RgbLed::RgbLed(int redPin, int greenPin, int bluePin) {
  _redPin = redPin;
  _greenPin = greenPin;
  _bluePin = bluePin;

  pinMode(_redPin, OUTPUT);
  pinMode(_greenPin, OUTPUT);
  pinMode(_bluePin, OUTPUT);

    allColors[0] = &red;
    allColors[1] = &yellow;
    allColors[2] = &green;
    allColors[3] = &aqua;
    allColors[4] = &blue;
    allColors[5] = &purple;
}



void RgbLed::setColor(char input)  { 
  switch (input)  { 
  case '0':
    writeColor(&off);    
    break;
  case '1':
    writeColor(&red);    
    break;
  case '2':
    writeColor(&yellow);  
    break;
  case '3':
    writeColor(&green);    
    break;
  case '4':
    writeColor(&aqua);  
    break;
  case '5':
    writeColor(&blue);    
    break;
  case '6':
    writeColor(&purple);  
    break;    
  }
}

void RgbLed::writeColor(const RGB *rgbValue)  {
  analogWrite(_redPin, rgbValue->r);
  analogWrite(_greenPin, rgbValue->g);
  analogWrite(_bluePin, rgbValue->b);
}

//Challenge III "Gradient"
void RgbLed::showGradient()  {
  int nColors = (sizeof(allColors)/3) / sizeof(int);
  //Add your code here
  for (int i = 0; i <= 255; i++) {
    analogWrite(_redPin, i);
    analogWrite(_greenPin, i);
    analogWrite(_bluePin, i);
      delay(50);
  }
  delay(500);
  for (int h=0; h <= 6; h++) {
    writeColor(allColors[h]);
      delay(500);
  }
  writeColor(&off);
}

void RgbLed::reset() {
  writeColor(&off);
}




