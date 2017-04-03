#include <Arduino.h>
#include <SoftwareSerial.h>
#include <Servo.h>
#include "ServoMotor.h"

ServoMotor::ServoMotor(int pin) {
  _pin = pin;
}

int ServoMotor::getAngle(SoftwareSerial &bluetooth) {
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
  int angle = maxAngle - angleStr.toInt(); //'maxAngle-angle' = counter clockwise.
  return angle;
}

void ServoMotor::setAngle(int angle, int milliSec)  {
  servo.attach(_pin); 
  servo.write(angle);
  delay(milliSec);
  servo.detach();
}

void ServoMotor::reset()  {
  setAngle(maxAngle, 400);
}

