#import <Arduino.h>
#include <SoftwareSerial.h>
#include "LED.h"
#include "Buzzer.h"

Buzzer::Buzzer(int pin) {
  _pin = pin;
}

void Buzzer::begin()  {
  pinMode(_pin, OUTPUT);
}

void Buzzer::alarm(LED led1, LED led2)  {
  bool stateLED1 = led1.getState();
  bool stateLED2 = led2.getState();
  
  long startTimer = millis();
  while (millis() - startTimer < 3000) {    //Play alarm for 3 seconds (3000 ms)
      led1.setLED(on);
      led2.setLED(on);
      tone(_pin, 1000, 500);
      delay(500);

      led1.setLED(off);
      led2.setLED(off);
      tone(_pin, 500, 500);
      delay(500);   
  }
  setLEDToPreviousState(led1, stateLED1, led2, stateLED2);
}

void Buzzer::vaderJacob(LED led1, LED led2) {
  bool stateLED1 = led1.getState();
  bool stateLED2 = led2.getState();
  
  led1.setLED(off);
  led2.setLED(off);
  
  for(int i = 0; i < nCouplets; i++)  {
    for (int j = 0; j < 2; j++)   {
      for (int k = beginCouplet[i]; k < endCouplet[i]; k++) {
        if (k%2 == 0) {
            led1.setLED(on);
            led2.setLED(off);
        }
        else {
            led1.setLED(off);
            led2.setLED(on);
        }
        tone(_pin, frequence[k], milliSec);
        delay(secondsRest[i]);
      }
      delay(300);
    }
    delay(200);
  }
  setLEDToPreviousState(led1, stateLED1, led2, stateLED2);
}

//Challenge II
void Buzzer::customSound()  {
  //Add your code here
}

void Buzzer::setLEDToPreviousState(LED led1, bool wasOnLED1, LED led2, bool wasOnLED2) {
  if (wasOnLED1) {
    led1.setLED(on);
  }
  else {
    led1.setLED(off);
  }
  
  if (wasOnLED2) {
    led2.setLED(on);
  }
  else {
    led2.setLED(off);
  }
}

