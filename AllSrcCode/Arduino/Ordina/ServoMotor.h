#include "arduino.h"
#include <Servo.h>

class ServoMotor  {

  public:
    ServoMotor(int pin);
    void setAngle(int angle, int milliSec);
    void reset();

  private:
    int _pin;
    Servo servo;
  
};

ServoMotor::ServoMotor(int pin) {
  _pin = pin;
}

void ServoMotor::setAngle(int angle, int milliSec)  {
  
  //Attach and detach servo here, otherwise the servo is moving while changing RGB light
  servo.attach(_pin);

  //Write the angle to the servo
  servo.write(angle);
  
  //Give servo time to get at the given angle
  delay(milliSec);

  //Detach the servo for unwanted movement
  servo.detach();
}

void ServoMotor::reset()  {
  setAngle(179, 400);
}

