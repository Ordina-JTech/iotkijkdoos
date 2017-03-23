#include "Led.h"
#include "RgbLed.h"
#include "Buzzer.h"
#include "ServoMotor.h"
#include <SoftwareSerial.h>

//Instance variables with the pin numbers as parameters
SoftwareSerial bluetooth(10, 11);   //RX BLE to TX arduino(11), TX BLE to RX arduino(10)
Led led1(5);
Led led2(6);
RgbLed rgbLed(7, 8, 9);
Buzzer buzzer(4);
ServoMotor servo(2);

char nextChar;
int angle;

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
    
    switch (input)  {
    case 'a':
      nextChar = led1.getLedChar(bluetooth);
      led1.setLed(nextChar);
      break;
    case 'b':
      nextChar = led2.getLedChar(bluetooth);
      led2.setLed(nextChar);
      break;
    case'c':
      nextChar = rgbLed.getColorChar(bluetooth);
      rgbLed.setColor(nextChar);
      break;
    case 'd':
      buzzer.alarm(led1, led2);
      break;
    case 'e':
      buzzer.vaderJacob(led1, led2);
      break;
    //Challenge II
    case 'f':
      buzzer.customSound();
      break;
    case 'g':
      angle = servo.getAngle(bluetooth);
      servo.setAngle(angle, 50);
      break;
    //Challenge III
    case 'h':
      rgbLed.showGradient();
      break;
    //Challenge IV
    case 'i':
      //Add here the call to the method for challenge IV
      break;
    case 'r':
      resetAllComponents();
      break;
    default:
      break;
    }
  }   
}

void resetAllComponents()  {
  led1.reset();
  led2.reset();
  rgbLed.reset();
  servo.reset();
  bluetooth.println("y"); //Central is waiting for "y" before it will disconnect
}









