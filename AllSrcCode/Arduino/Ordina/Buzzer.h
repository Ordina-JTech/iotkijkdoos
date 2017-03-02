#include "arduino.h"

class Buzzer  {

 public:
  Buzzer(int pin);
  void begin();
  void alarm(int pinLed1, bool isOnLed1, int pinLed2, bool isOnled2);
  void vaderJacob(int pinLed1, bool isOnLed1, int pinLed2, bool isOnLed2);
  void yourCustomSound();

 private:
  void checkLedStatus(int pinLed1, bool isOnLed1, int pinLed2, bool isOnLed2);
  int _pin;
  //Vader Jacob
  int frequence[16] = {523, 587, 659, 523, 659, 699, 784, 784, 880, 784, 699, 659, 523, 523, 659, 523};
  int beginCouplet[4] = {0, 4, 7, 13};
  int endCouplet[4] = {4, 7, 13, 16};
  int nSeconds[4] = {300, 300, 300, 600};
  int nCouplets = (sizeof(endCouplet)/sizeof(int));
  int milliSec = 100;

};

Buzzer::Buzzer(int pin) {
  _pin = pin;
}

void Buzzer::begin()  {
  pinMode(_pin, OUTPUT);
}


void Buzzer::alarm(int pinLed1, bool isOnLed1, int pinLed2, bool isOnLed2)  {
  long startTimer = millis();

  while (millis() - startTimer < 3000) {    //Play alarm for 3 seconds (3000 ms)
      digitalWrite(pinLed1, HIGH);
      digitalWrite(pinLed2, HIGH);
      tone(_pin, 1000, 500);
      delay(500);

      digitalWrite(pinLed1, LOW);
      digitalWrite(pinLed2, LOW);
      tone(_pin, 500, 500);
      delay(500);   
  }

  checkLedStatus(pinLed1, isOnLed1, pinLed2, isOnLed2);
}

void Buzzer::vaderJacob(int pinLed1, bool isOnLed1, int pinLed2, bool isOnLed2) {
  digitalWrite(pinLed1, LOW);
  digitalWrite(pinLed2, LOW);
  
  for(int i = 0; i < nCouplets; i++)  {
    for (int j = 0; j < 2; j++)   {
      for (int k = beginCouplet[i]; k < endCouplet[i]; k++) {
        if (k%2 == 0) {
            digitalWrite(pinLed1, HIGH);
            digitalWrite(pinLed2, LOW);
        }
        else {
            digitalWrite(pinLed1, LOW);
            digitalWrite(pinLed2, HIGH);
        }
        tone(_pin, frequence[k], milliSec);
        delay(nSeconds[i]);
      }
      delay(300);
    }
    delay(200);
  }
  
  checkLedStatus(pinLed1, isOnLed1, pinLed2, isOnLed2);
}



//CHALLENGE: CREATE YOUR CUSTOM SOUND HERE

void Buzzer::yourCustomSound()  {
   //char names[] =     { 'c', 'd', 'e', 'f', '#', 'g', 'a', 'b', 'C', 'D', 'E', 'F', 'G', 'A', 'B' };
  //int frequencies[] = {262, 294, 330, 349, 370, 392, 440, 494, 523, 587, 659, 699, 784, 880, 989};
}



  
void Buzzer::checkLedStatus(int pinLed1, bool isOnLed1, int pinLed2, bool isOnLed2) {
  if (isOnLed1) {
    digitalWrite(pinLed1, HIGH);
  }
  else {
    digitalWrite(pinLed1, LOW);
  }
  
  if (isOnLed2) {
    digitalWrite(pinLed2, HIGH);
  }
  else {
    digitalWrite(pinLed2, LOW);
  }
}




