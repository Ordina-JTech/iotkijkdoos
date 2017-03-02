#include "Led.h"
#include "RgbLed.h"
#include "Buzzer.h"
#include "ServoMotor.h"
#include <SoftwareSerial.h>

//Instance variables
SoftwareSerial bluetooth(10, 11);   //RX BLE to TX arduino(11), TX BLE to RX arduino(10)
Led led1(5);
Led led2(6);
RgbLed rgbLed(7, 8, 9);
Buzzer buzzer(4);
ServoMotor servo(2);

void setup()  {
  Serial.begin(9600);
  bluetooth.begin(9600);
  led1.begin();
  led2.begin();
  rgbLed.begin();
  buzzer.begin();
}

void loop() {
  
  if (bluetooth.available() > 0) {
    char input = bluetooth.read(); 
    char nextChar;
    
    switch (input)  {
    case 'a':
      nextChar = readNextChar();
      led1.setLed(nextChar);
      break;
    case 'b':
      nextChar = readNextChar();
      led2.setLed(nextChar);
      break;
    case'c':
      nextChar = readNextChar();
      rgbLed.getAndSetColor(nextChar);
      break;
    case 'd':
      buzzer.alarm(led1, led2); //Parameters: if led is on or off
      break;
    case 'e':
      buzzer.vaderJacob(led1, led2);
      break;
    case 'f':
      buzzer.yourCustomSound();
      break;
    case 'g':
      getAndSetServoAngle();
      break;
    case 'r':
      resetAllComponents();
      break;
    default:
      break;
    }
  }   
}

char readNextChar() {
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

void getAndSetServoAngle() {
  String angleStr = "";
  char input = '\0';

  while (input != '\n') {
    if (bluetooth.available() > 0)  {
      input = bluetooth.read();
      
      if (input != '\n')  {
        angleStr += input;     
      }
    }   
  }
  int angle = 179 - angleStr.toInt(); //'179-' is because we need counter clockwise.
  servo.setAngle(angle, 50); 
}

void resetAllComponents()  {
  led1.reset();
  led2.reset();
  rgbLed.reset();
  servo.reset();
  bluetooth.println("y"); //Central is waiting for "y" before it will disconnect
}









