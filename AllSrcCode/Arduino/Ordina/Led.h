#include "arduino.h"
#include <SoftwareSerial.h>

class Led{
  
 public:
  Led(int pin); //constructor
  void begin();
  char getStateChar(SoftwareSerial &bluetooth);
  void setLed(char input);
  bool getState();
  int getPinNumber();
  void reset();
  
 private:
  int _pin; 
  bool isOn = false;
};

Led::Led(int pin)  {
  _pin = pin;
}

void Led::begin() {
  pinMode(_pin, OUTPUT);
}

char Led::getStateChar(SoftwareSerial &bluetooth) {
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

void Led::setLed(char input) {
  if (input == '1') {
    digitalWrite(_pin, HIGH);
    isOn = true;
  }
  else if (input == '0')  {
    digitalWrite(_pin, LOW); 
    isOn = false;
  }
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

