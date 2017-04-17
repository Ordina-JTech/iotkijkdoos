#include <Arduino.h>
#include <SoftwareSerial.h>

#include "RGB.h"

RGB::RGB(int redPin, int greenPin, int bluePin) {
  _redPin = redPin;
  _greenPin = greenPin;
  _bluePin = bluePin;

  pinMode(_redPin, OUTPUT);
  pinMode(_greenPin, OUTPUT);
  pinMode(_bluePin, OUTPUT);

  discoStates[0] = off;
  discoStates[1] = red; 
  discoStates[2] = yellow; 
  discoStates[3] = green; 
  discoStates[4] = aqua; 
  discoStates[5] = blue; 
  discoStates[6] = purple; 

  nStates = sizeof(discoStates) / sizeof(int);
}

void RGB::setColor(char input)  { 
  int index = input - '0';

  if (index >= 0 && index < nStates)  {
    writeColor(discoStates[index]);
  }
}

void RGB::writeColor(int rgbValues[3])  {
  analogWrite(_redPin, rgbValues[0]);
  analogWrite(_greenPin, rgbValues[1]);
  analogWrite(_bluePin, rgbValues[2]);
}

//Challenge II "Gradient"
void RGB::showGradient()  {
  //Add your code here
}

void RGB::reset() {
  writeColor(off);
}




