#include <Arduino.h>
#include <SoftwareSerial.h>
#include "Led.h"

Led::Led(int pin)  {
  _pin = pin;
  pinMode(_pin, OUTPUT);
}


void Led::setLed(bool state) {
  isOn = state;
  digitalWrite(_pin, isOn ? HIGH : LOW);
}

void Led::flip() {
  isOn = !isOn;
  digitalWrite(_pin, isOn ? HIGH : LOW);
}

int Led::getPinNumber() {
  return _pin;
}

bool Led::getState() {
  return isOn;
}

void Led::reset()  {
  digitalWrite(_pin, LOW);
  isOn = false;
}

