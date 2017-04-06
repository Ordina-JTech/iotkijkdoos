#include <Arduino.h>
#include <SoftwareSerial.h>

#include "LED.h"
#include "Buzzer.h"

Buzzer::Buzzer(int pin) {
  _pin = pin;
  pinMode(_pin, OUTPUT);
}

void Buzzer::playAlarm(LED led1, LED led2)  {
  bool stateLED1 = led1.getState();
  bool stateLED2 = led2.getState();
  
  long startTimer = millis();
  while (millis() - startTimer < 3000) {    //Play alarm for 3 seconds (3000 ms)
      led1.powerLED(true);
      led2.powerLED(true);
      tone(_pin, 1000, 500);
      delay(500);

      led1.powerLED(false);
      led2.powerLED(false);
      tone(_pin, 500, 500);
      delay(500);   
  }
  setLEDToPreviousState(led1, stateLED1, led2, stateLED2);
}

void Buzzer::playVaderJacob(LED led1, LED led2) {
  bool stateLED1 = led1.getState();
  bool stateLED2 = led2.getState();
  
  led1.powerLED(false);
  led2.powerLED(false);
  
  for(int i = 0; i < nCouplets; i++)  {
    for (int j = 0; j < 2; j++)   {
      for (int k = beginCouplet[i]; k < endCouplet[i]; k++) {
        if (k%2 == 0) {
            led1.powerLED(true);
            led2.powerLED(false);
        }
        else {
            led1.powerLED(false);
            led2.powerLED(true);
        }
        tone(_pin, frequences[k], milliSec);
        delay(secondsRest[i]);
      }
      delay(300);
    }
    delay(200);
  }
  setLEDToPreviousState(led1, stateLED1, led2, stateLED2);
}

//Challenge II
void Buzzer::playCustomSound()  {
  //Add your code here
}

void Buzzer::setLEDToPreviousState(LED led1, bool wasOnLED1, LED led2, bool wasOnLED2) {
  led1.powerLED(wasOnLED1 ? true : false); 
  led2.powerLED(wasOnLED2 ? true : false);
}

