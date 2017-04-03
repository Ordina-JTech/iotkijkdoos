#import <Arduino.h>
#include <SoftwareSerial.h>
#include "Led.h"
#include "Buzzer.h"

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
        delay(secondsRest[i]);
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

