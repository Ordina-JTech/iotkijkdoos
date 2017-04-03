
#include <Arduino.h>
#include <SoftwareSerial.h>
#include <Servo.h>

#include "LED.h"
#include "RGB.h"
#include "Buzzer.h"
#include "ServoMotor.h"

//Instance variables with the pin numbers as parameters
SoftwareSerial bluetooth(10, 11);   //RX BLE to TX arduino(11), TX BLE to RX arduino(10)

ServoMotor servo(3);
Buzzer buzzer(4);
RGB rgbLed(9, 6, 5);
LED led1(8);
LED led2(12);

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
      nextChar = led1.getStateChar(bluetooth);
      led1.setLED(nextChar);
      break;
    case 'b':
      nextChar = led2.getStateChar(bluetooth);
      led2.setLED(nextChar);
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
    case 'f':
      buzzer.customSound();
      break;
    case 'g':
      angle = servo.getAngle(bluetooth);
      servo.setAngle(angle, 50);
      break;
    case 'h':
      rgbLed.showGradient();
      break;
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









