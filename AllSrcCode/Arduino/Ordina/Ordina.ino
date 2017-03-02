#include "Led.h"
#include "RgbLed.h"
#include "Buzzer.h"
#include "ServoMotor.h"
#include <SoftwareSerial.h>

//Instance variables
SoftwareSerial bluetooth(10, 11);   //RX BLE to TX arduino(11), TX BLE to RX arduino(10)

const int pinLed1 = 5;
const int pinLed2 = 6;
Led led1(pinLed1);
Led led2(pinLed2);
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
  servo.reset();
}

void loop() {
    //If the app has send data
  if (bluetooth.available() > 0) {
    char input = bluetooth.read(); 
    
    switch (input)  {
    case 'a':
      led1.setLed();
      break;
    case 'b':
      led2.setLed();
      break;
    case'c':
      readCharForColor();
      break;
    case 'd':
      buzzer.alarm(pinLed1, led1.getStatus(), pinLed2, led2.getStatus()); //Parameters: if led is on or off
      break;
    case 'e':
      buzzer.vaderJacob(pinLed1, led1.getStatus(), pinLed2, led2.getStatus());
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

void readCharForColor() {
  int count = 0;
  
  while (count == 0) {
    if (bluetooth.available() > 0) {
      char input = bluetooth.read();
      rgbLed.getAndSetColor(input);
      count++;
    } 
  } 
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









