#include <Arduino.h>
#include <SoftwareSerial.h>
#include "LED.h"
#include "Buzzer.h"

Buzzer::Buzzer(int pin) {
  _pin = pin;
  pinMode(_pin, OUTPUT);
}

void Buzzer::alarm(LED led1, LED led2)  {
  bool stateLED1 = led1.getState();
  bool stateLED2 = led2.getState();
  
  long startTimer = millis();
  while (millis() - startTimer < 3000) {    //Play alarm for 3 seconds (3000 ms)
      led1.setLed(false);
      led2.setLed(true);
      tone(_pin, 1000, 500);
      delay(500);

      led1.setLed(true);
      led2.setLed(false);
      tone(_pin, 500, 500);
      delay(500);   
  }
  setLEDToPreviousState(led1, stateLED1, led2, stateLED2);
}

void Buzzer::vaderJacob(LED led1, LED led2) {
  bool stateLED1 = led1.getState();
  bool stateLED2 = led2.getState();
  
  led1.setLed(false);
  led2.setLed(false);
  
//Play first section
  firstSection(led1, led2);
 
  //Play second section
  secondSection(led1, led2);
 
  //Variant 1
  beep(NOTE_F4, 250, led1, led2);  
  beep(NOTE_GS4, 500, led1, led2);  
  beep(NOTE_F4, 350, led1, led2);  
  beep(NOTE_A4, 125, led1, led2);
  beep(NOTE_C5, 500, led1, led2);
  beep(NOTE_A4, 375, led1, led2);  
  beep(NOTE_C5, 125, led1, led2);
  beep(NOTE_E5, 650, led1, led2);
 
  delay(500);
 
  //Repeat second section
  secondSection(led1, led2);
 
  //Variant 2
  beep(NOTE_F4, 250, led1, led2);  
  beep(NOTE_GS4, 500, led1, led2);  
  beep(NOTE_F4, 375, led1, led2);  
  beep(NOTE_C5, 125, led1, led2);
  beep(NOTE_A4, 500, led1, led2);  
  beep(NOTE_F4, 375, led1, led2);  
  beep(NOTE_C5, 125, led1, led2);
  beep(NOTE_A4, 650, led1, led2);  
 
  delay(650);
  
  setLEDToPreviousState(led1, stateLED1, led2, stateLED2);
}

void Buzzer::beep(int note, int duration, LED led1, LED led2)
{
  //Play tone on buzzerPin
  tone(_pin, note, duration);
 
  //Play different LED depending on value of 'counter'
  if(counter % 2 == 0)
  {
    led1.setLed(true);
    delay(duration);
    led1.setLed(false);
  }else
  {
    led2.setLed(true);
    delay(duration);
    led2.setLed(true);
  }
 
  //Stop tone on buzzerPin
  noTone(_pin);
 
  delay(50);
 
  //Increment counter
  counter++;
}

void Buzzer::firstSection(LED led1, LED led2)
{
  beep(NOTE_A4, 500, led1, led2);
  beep(NOTE_A4, 500, led1, led2);    
  beep(NOTE_A4, 500, led1, led2);
  beep(NOTE_F4, 350, led1, led2);
  beep(NOTE_C5, 150, led1, led2);  
  beep(NOTE_A4, 500, led1, led2);
  beep(NOTE_F4, 350, led1, led2);
  beep(NOTE_C5, 150, led1, led2);
  beep(NOTE_A4, 650, led1, led2);
 
  delay(500);
 
  beep(NOTE_E5, 500, led1, led2);
  beep(NOTE_E5, 500, led1, led2);
  beep(NOTE_E5, 500, led1, led2);  
  beep(NOTE_F5, 350, led1, led2);
  beep(NOTE_C5, 150, led1, led2);
  beep(NOTE_GS4,500, led1, led2);
  beep(NOTE_F4, 350, led1, led2);
  beep(NOTE_C5, 150, led1, led2);
  beep(NOTE_A4, 650, led1, led2);
 
  delay(500);
}
 
void Buzzer::secondSection(LED led1, LED led2)
{
  beep(NOTE_A5, 500, led1, led2);
  beep(NOTE_A4, 300, led1, led2);
  beep(NOTE_A4, 150, led1, led2);
  beep(NOTE_A5, 500, led1, led2);
  beep(NOTE_GS5,325, led1, led2);
  beep(NOTE_G5, 175, led1, led2);
  beep(NOTE_FS5,125, led1, led2);
  beep(NOTE_F5, 125, led1, led2);    
  beep(NOTE_FS5,250, led1, led2);
 
  delay(325);
 
  beep(NOTE_AS4, 250, led1, led2);
  beep(NOTE_DS5, 500, led1, led2);
  beep(NOTE_D5,  325, led1, led2);  
  beep(NOTE_CS5, 175, led1, led2);  
  beep(NOTE_C5,  125, led1, led2);  
  beep(NOTE_AS4,  125, led1, led2);  
  beep(NOTE_C5,  250, led1, led2);  
 
  delay(350);
}
//Challenge II
void Buzzer::customSound(LED led1, LED led2)  {
  bool stateLED1 = led1.getState();
  bool stateLED2 = led2.getState();
  
  led1.setLed(false);
  led2.setLed(false);
  
  singMarioTheme(led1, led2);
  singMarioTheme(led1, led2);
  singUnderworldTheme(led1, led2);

  setLEDToPreviousState(led1, stateLED1, led2, stateLED2);
}

void Buzzer::setLEDToPreviousState(LED led1, bool wasOnLed1, LED led2, bool wasOnLed2) {
    led1.setLed(wasOnLed1);
    led2.setLed(wasOnLed2);
}




void Buzzer::singUnderworldTheme(LED led1, LED led2) {
  //Underworld melody
  int melody[56] = {
    NOTE_C4, NOTE_C5, NOTE_A3, NOTE_A4,
    NOTE_AS3, NOTE_AS4, 0,
    0,
    NOTE_C4, NOTE_C5, NOTE_A3, NOTE_A4,
    NOTE_AS3, NOTE_AS4, 0,
    0,
    NOTE_F3, NOTE_F4, NOTE_D3, NOTE_D4,
    NOTE_DS3, NOTE_DS4, 0,
    0,
    NOTE_F3, NOTE_F4, NOTE_D3, NOTE_D4,
    NOTE_DS3, NOTE_DS4, 0,
    0, NOTE_DS4, NOTE_CS4, NOTE_D4,
    NOTE_CS4, NOTE_DS4,
    NOTE_DS4, NOTE_GS3,
    NOTE_G3, NOTE_CS4,
    NOTE_C4, NOTE_FS4, NOTE_F4, NOTE_E3, NOTE_AS4, NOTE_A4,
    NOTE_GS4, NOTE_DS4, NOTE_B3,
    NOTE_AS3, NOTE_A3, NOTE_GS3,
    0, 0, 0
  };
  //Underwolrd tempo
  int tempo[56] = {
    12, 12, 12, 12,
    12, 12, 6,  3,
    12, 12, 12, 12,
    12, 12, 6,  3,
    12, 12, 12, 12,
    12, 12, 6,  3,
    12, 12, 12, 12,
    12, 12, 6,  6, 
    18, 18, 18, 6, 
    6,  6,  6,  6, 
    6,  18, 18, 18, 
    18, 18, 18, 10, 
    10, 10, 10, 10, 
    10,  3, 3, 3
  };
  
  Serial.println(" 'Underworld Theme'");
  
  int size = sizeof(melody) / sizeof(int);
  for (int thisNote = 0; thisNote < size; thisNote++) {

    // to calculate the note duration, take one second
    // divided by the note type.
    //e.g. quarter note = 1000 / 4, eighth note = 1000/8, etc.
    int noteDuration = 1000 / tempo[thisNote];

    buzz(_pin, melody[thisNote], noteDuration, led1, led2);

    // to distinguish the notes, set a minimum time between them.
    // the note's duration + 30% seems to work well:
    int pauseBetweenNotes = noteDuration * 1.30;
    delay(pauseBetweenNotes);

    // stop the tone playing:
    buzz(_pin, 0, noteDuration, led1, led2);

  }

}
void Buzzer::singMarioTheme(LED led1, LED led2) {
  //Mario main theme melody
  int melody[78] = {
    NOTE_E7, NOTE_E7, 0, NOTE_E7,
    0, NOTE_C7, NOTE_E7, 0,
    NOTE_G7, 0, 0,  0,
    NOTE_G6, 0, 0, 0,
  
    NOTE_C7, 0, 0, NOTE_G6,
    0, 0, NOTE_E6, 0,
    0, NOTE_A6, 0, NOTE_B6,
    0, NOTE_AS6, NOTE_A6, 0,
  
    NOTE_G6, NOTE_E7, NOTE_G7,
    NOTE_A7, 0, NOTE_F7, NOTE_G7,
    0, NOTE_E7, 0, NOTE_C7,
    NOTE_D7, NOTE_B6, 0, 0,
  
    NOTE_C7, 0, 0, NOTE_G6,
    0, 0, NOTE_E6, 0,
    0, NOTE_A6, 0, NOTE_B6,
    0, NOTE_AS6, NOTE_A6, 0,
  
    NOTE_G6, NOTE_E7, NOTE_G7,
    NOTE_A7, 0, NOTE_F7, NOTE_G7,
    0, NOTE_E7, 0, NOTE_C7,
    NOTE_D7, NOTE_B6, 0, 0
  };
  //Mario main them tempo
  int tempo[78] = {
    12, 12, 12, 12,
    12, 12, 12, 12,
    12, 12, 12, 12,
    12, 12, 12, 12,
  
    12, 12, 12, 12,
    12, 12, 12, 12,
    12, 12, 12, 12,
    12, 12, 12, 12, 
  
    9, 9, 9,
    12, 12, 12, 12,
    12, 12, 12, 12,
    12, 12, 12, 12, 
  
    12, 12, 12, 12,
    12, 12, 12, 12,
    12, 12, 12, 12,
    12, 12, 12, 12, 
  
    9, 9, 9,
    12, 12, 12, 12,
    12, 12, 12, 12,
    12, 12, 12, 12, 
  };

  Serial.println(" 'Mario Theme'");
  int size = sizeof(melody) / sizeof(int);
  for (int thisNote = 0; thisNote < size; thisNote++) {

    // to calculate the note duration, take one second
    // divided by the note type.
    //e.g. quarter note = 1000 / 4, eighth note = 1000/8, etc.
    int noteDuration = 1000 / tempo[thisNote];

    buzz(_pin, melody[thisNote], noteDuration, led1, led2);

    // to distinguish the notes, set a minimum time between them.
    // the note's duration + 30% seems to work well:
    int pauseBetweenNotes = noteDuration * 1.30;
    delay(pauseBetweenNotes);

    // stop the tone playing:
    buzz(_pin, 0, noteDuration, led1, led2);

  }
}



void Buzzer::buzz(int targetPin, long frequency, long length, LED led1, LED led2) {
  led1.setLed(true);
  led2.setLed(false);
  long delayValue = 1000000 / frequency / 2; // calculate the delay value between transitions
  //// 1 second's worth of microseconds, divided by the frequency, then split in half since
  //// there are two phases to each cycle
  long numCycles = frequency * length / 1000; // calculate the number of cycles for proper timing
  //// multiply frequency, which is really cycles per second, by the number of seconds to
  //// get the total number of cycles to produce
  for (long i = 0; i < numCycles; i++) { // for the calculated length of time...
    digitalWrite(targetPin, HIGH); // write the buzzer pin high to push out the diaphram
    delayMicroseconds(delayValue); // wait for the calculated delay value
    digitalWrite(targetPin, LOW); // write the buzzer pin low to pull back the diaphram
    delayMicroseconds(delayValue); // wait again or the calculated delay value
  }
  led1.setLed(false);
  led2.setLed(true);

}

