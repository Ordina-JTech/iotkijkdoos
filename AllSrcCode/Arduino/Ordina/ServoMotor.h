#include "arduino.h"
#include <SoftwareSerial.h>
#include <Servo.h>

/*
 * Servo angle: 0-179 (180 degrees).
 * Servo will be attached and detached in the setAngle() method, otherwise the servo is moving while changing RGB light.
 */

class ServoMotor  {
  
  public:
    ServoMotor(int pin);
    getAngle(SoftwareSerial &bluetooth);
    void setAngle(int angle, int milliSec);
    void reset();

  private:
    int _pin;
    const int maxAngle = 179;
    Servo servo;
};

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

