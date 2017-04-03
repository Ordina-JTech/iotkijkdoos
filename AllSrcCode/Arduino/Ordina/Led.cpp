#import <Arduino.h>
#include <SoftwareSerial.h>
#include "LED.h"

LED::LED(int pin)  {
  _pin = pin;
}

void LED::begin() {
  pinMode(_pin, OUTPUT);
}

char LED::getStateChar(SoftwareSerial &bluetooth) {
  int count = 0;
  char input = '\0';  //Empty Char
  
  while (count == 0) {
    if (bluetooth.available() > 0) {
      input = bluetooth.read();
      count++;
    } 
  }
  return input;
}

void LED::setLED(char input) {
  if (input == '1') {
    digitalWrite(_pin, HIGH);
    isOn = true;
  }
  else if (input == '0')  {
    digitalWrite(_pin, LOW); 
    isOn = false;
  }
}

int LED::getPinNumber() {
  return _pin;
}

bool LED::getState() {
  return isOn;
}

void LED::reset()  {
  digitalWrite(_pin, LOW);
  isOn = false;
}

