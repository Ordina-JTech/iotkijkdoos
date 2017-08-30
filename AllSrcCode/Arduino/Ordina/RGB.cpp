#include <Arduino.h>
#include <SoftwareSerial.h>
#define RGB_GLOBALS
#include "RGB.h"
#undef RGB_GLOBALS

RGB::RGB(int redPin, int greenPin, int bluePin) {
  _redPin = redPin;
  _greenPin = greenPin;
  _bluePin = bluePin;

  pinMode(_redPin, OUTPUT);
  pinMode(_greenPin, OUTPUT);
  pinMode(_bluePin, OUTPUT);

}


void RGB::setColor(char input)  { 
  int index = input - '0';
  if (index >= 0 && index < N_COLORS)  {
    writeColor(allRgbColors[index]);
  }
}

void RGB::writeColor(RGB_color rgbValues)  {
  analogWrite(_redPin, rgbValues.red);
  analogWrite(_greenPin, rgbValues.green);
  analogWrite(_bluePin, rgbValues.blue);
}

//Challenge III "Gradient"
void RGB::showGradient()  {
  for (int rgb = 0; rgb <256; rgb++) {
        analogWrite(_redPin, rgb);
        analogWrite(_greenPin, rgb);
        analogWrite(_bluePin, rgb);
        delay(10);
  }
  reset();
}

void RGB::reset() {
  writeColor(OFF);
}




