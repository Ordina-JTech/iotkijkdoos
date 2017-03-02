#include "arduino.h"

class Led{
  
 public:
  Led(int pin); //constructor
  void begin();
  void setLed(char input);
  bool getStatus();
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

bool Led::getStatus() {
  return isOn;
}

void Led::reset()  {
  digitalWrite(_pin, LOW);
  isOn = false;
}

