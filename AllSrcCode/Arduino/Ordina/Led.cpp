#include <Arduino.h>
#include <SoftwareSerial.h>

#include "LED.h"

LED::LED(int pin)  {
  _pin = pin;
  pinMode(_pin, OUTPUT);
}

void LED::powerLED(bool state) {
  isOn = state;
  digitalWrite(_pin, state ? HIGH : LOW);
}

int LED::getPinNumber() {
  return _pin;
}

bool LED::getState() {
  return isOn;
}

void LED::reset()  {
  isOn = false;
  digitalWrite(_pin, LOW); 
}

