#include <Arduino.h>
#include <SoftwareSerial.h>
#include "RgbLed.h"

RgbLed::RgbLed(int redPin, int greenPin, int bluePin) {
  _redPin = redPin;
  _greenPin = greenPin;
  _bluePin = bluePin;
}

void RgbLed::begin() {
  pinMode(_redPin, OUTPUT);
  pinMode(_greenPin, OUTPUT);
  pinMode(_bluePin, OUTPUT);

  //Create one array with all colors 
  byte arraySize = 3 * sizeof(int);
  memcpy(allColors[0], off, arraySize);
  memcpy(allColors[1], red, arraySize);
  memcpy(allColors[2], yellow, arraySize);
  memcpy(allColors[3], green, arraySize);
  memcpy(allColors[4], aqua, arraySize);
  memcpy(allColors[5], blue, arraySize);
  memcpy(allColors[6], purple, arraySize);
}

char RgbLed::getColorChar(SoftwareSerial &bluetooth) {
  int count = 0;
  char input = '\0';
  
  while (count == 0) {
    if (bluetooth.available() > 0) {
      input = bluetooth.read();
      count++;
    } 
  }
  return input;
}

void RgbLed::setColor(char input)  { 
  int index = input - '0';
  int nColors = (sizeof(allColors)/3) / sizeof(int);
  
  if (index >= 0 && index < nColors)  {
    writeColor(allColors[index]);
  }
}

void RgbLed::writeColor(int rgbValue[3])  {
  analogWrite(_redPin, rgbValue[0]);
  analogWrite(_greenPin, rgbValue[1]);
  analogWrite(_bluePin, rgbValue[2]);
}

//Challenge III "Gradient"
void RgbLed::showGradient()  {
  int nColors = (sizeof(allColors)/3) / sizeof(int);
  //Add your code here
}

void RgbLed::reset() {
  writeColor(off);
}




