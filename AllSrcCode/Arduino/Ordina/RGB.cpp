#include <Arduino.h>
#include <SoftwareSerial.h>
#include "RGB.h"

RGB::RGB(int redPin, int greenPin, int bluePin) {
  _redPin = redPin;
  _greenPin = greenPin;
  _bluePin = bluePin;
}

void RGB::begin() {
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

char RGB::getColorChar(SoftwareSerial &bluetooth) {
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

void RGB::setColor(char input)  { 
  int index = input - '0';
  int nColors = (sizeof(allColors)/3) / sizeof(int);
  
  if (index >= 0 && index < nColors)  {
    writeColor(allColors[index]);
  }
}

void RGB::writeColor(int rgbValues[3])  {
  analogWrite(_redPin, rgbValues[0]);
  analogWrite(_greenPin, rgbValues[1]);
  analogWrite(_bluePin, rgbValues[2]);
}

//Challenge III "Gradient"
void RGB::showGradient()  {
  int nColors = (sizeof(allColors)/3) / sizeof(int);
  //Add your code here
}

void RGB::reset() {
  writeColor(off);
}




