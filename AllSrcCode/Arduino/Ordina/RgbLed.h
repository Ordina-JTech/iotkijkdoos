#include "arduino.h"
#include <SoftwareSerial.h>

class RgbLed {
  
  public:
    RgbLed(int redPin, int greenPin, int bluePin);
    void begin();
    char getColorChar(SoftwareSerial &bluetooth);
    void setColor(char input);
    void fadeColor();
    void reset();

  private:
     int _redPin;
     int _greenPin;
     int _bluePin; 
     void setColor(int redValue, int greenValue, int blueValue);
};

RgbLed::RgbLed(int redPin, int greenPin, int bluePin) {
  _redPin = redPin;
  _greenPin = greenPin;
  _bluePin = bluePin;
}

void RgbLed::begin() {
  pinMode(_redPin, OUTPUT);
  pinMode(_greenPin, OUTPUT);
  pinMode(_bluePin, OUTPUT);
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
  switch (input)  { 
  
  case '0':
    setColor(0,0,0);    //Off
    break;
  case '1':
  setColor(255,0,0);    //Red
  break;
  case '2':
  setColor(255,255,0);  //Yellow
  break;
  case '3':
  setColor(0,255,0);    //Green
  break;
  case '4':
  setColor(0,255,255);  //Aqua
  break;
  case '5':
  setColor(0,0,255);    //Blue
  break;
  case '6':
  setColor(255,0,255);  //Purple
  break;
  }
}

void RgbLed::setColor(int redValue, int greenValue, int blueValue)  {
  analogWrite(_redPin, redValue);
  analogWrite(_greenPin, greenValue);
  analogWrite(_bluePin, blueValue);
}

//Challenge III
void RgbLed::fadeColor()  {
  //Add your code here
}

void RgbLed::reset() {
  setColor(0, 0, 0);
}



