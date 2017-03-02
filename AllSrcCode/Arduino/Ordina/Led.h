#include "arduino.h"

class Led{
  
 public:
  Led(int pin); //constructor
  void begin();
  void setLed();
  bool getStatus();
  void reset();
  
 private:
  bool isOn = false;
  int _pin;  
};

//Constructor
Led::Led(int pin)  {
  _pin = pin;
}

//Output
void Led::begin() {
  pinMode(_pin, OUTPUT);
}

//Set led on or off, depending on the bool 'isOn'.
void Led::setLed() {
  if (!isOn) {
    isOn = true;
    digitalWrite(_pin, HIGH);
  }
  else  {
    isOn = false;
    digitalWrite(_pin, LOW); 
  }
}

bool Led::getStatus() {
  return isOn;
}


void Led::reset()  {
  digitalWrite(_pin, LOW);
  isOn = false;
}

