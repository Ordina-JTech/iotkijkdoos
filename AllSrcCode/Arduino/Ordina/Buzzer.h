#include "arduino.h"

class Buzzer  {
 
 public:
  Buzzer(int pin);
  void begin();
  void alarm(Led led1, Led led2);
  void vaderJacob(Led led1, Led led2);
  void customSound();

 private:
  void setLedToPreviousState(Led led1, bool wasOnLed1, Led led2, bool wasOnLed2);
  int _pin;
  char on = '1';
  char off = '0';
  
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

void Buzzer::alarm(Led led1, Led led2)  {
  bool stateLed1 = led1.getState();
  bool stateLed2 = led2.getState();
  
  long startTimer = millis();
  while (millis() - startTimer < 3000) {    //Play alarm for 3 seconds (3000 ms)
      led1.setLed(on);
      led2.setLed(on);
      tone(_pin, 1000, 500);
      delay(500);

      led1.setLed(off);
      led2.setLed(off);
      tone(_pin, 500, 500);
      delay(500);   
  }
  setLedToPreviousState(led1, stateLed1, led2, stateLed2);
}

void Buzzer::vaderJacob(Led led1, Led led2) {
  bool stateLed1 = led1.getState();
  bool stateLed2 = led2.getState();
  
  led1.setLed(off);
  led2.setLed(off);
  
  for(int i = 0; i < nCouplets; i++)  {
    for (int j = 0; j < 2; j++)   {
      for (int k = beginCouplet[i]; k < endCouplet[i]; k++) {
        if (k%2 == 0) {
            led1.setLed(on);
            led2.setLed(off);
        }
        else {
            led1.setLed(off);
            led2.setLed(on);
        }
        tone(_pin, frequence[k], milliSec);
        delay(nSeconds[i]);
      }
      delay(300);
    }
    delay(200);
  }
  setLedToPreviousState(led1, stateLed1, led2, stateLed2);
}

//Challenge II
void Buzzer::customSound()  {
  //Add your code here
}

void Buzzer::setLedToPreviousState(Led led1, bool wasOnLed1, Led led2, bool wasOnLed2) {
  if (wasOnLed1) {
    led1.setLed(on);
  }
  else {
    led1.setLed(off);
  }
  
  if (wasOnLed2) {
    led2.setLed(on);
  }
  else {
    led2.setLed(off);
  }
}




