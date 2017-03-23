#include "arduino.h"
#include <SoftwareSerial.h>

class RgbLed {
  
  public:
    RgbLed(int redPin, int greenPin, int bluePin);
    void begin();
    char getColorChar(SoftwareSerial &bluetooth);
    void setColor(char input);
    void showGradient();
    void reset();

  private:
     int _redPin;
     int _greenPin;
     int _bluePin;

     int off[3] = {0, 0, 0};
     int red[3] = {255, 0, 0};
     int yellow[3] = {255, 255, 0};
     int green[3] = {0, 255, 0}; 
     int aqua[3] = {0, 255, 255}; 
     int blue[3] = {0, 0, 255}; 
     int purple[3] = {255, 0, 255}; 
     
     int allColors[6][3] = {{red}, {yellow}, {green}, {aqua}, {blue}, {purple}};
     void writeColor(int rgbValues[3]);
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
    writeColor(off);    
    break;
  case '1':
    writeColor(red);    
    break;
  case '2':
    writeColor(yellow);  
    break;
  case '3':
    writeColor(green);    
    break;
  case '4':
    writeColor(aqua);  
    break;
  case '5':
    writeColor(blue);    
    break;
  case '6':
    writeColor(purple);  
    break;    
  }

  int index = input - '0';

  for (int i = 0; i < 3; i++) {
    Serial.println(allColors[index][i]);
  }
}

void RgbLed::writeColor(int rgbValue[3])  {
  analogWrite(_redPin, rgbValue[0]);
  analogWrite(_greenPin, rgbValue[1]);
  analogWrite(_bluePin, rgbValue[2]);
}

//Challenge III
void RgbLed::showGradient()  {
  //Add your code here
}

void RgbLed::reset() {
  writeColor(off);
}



