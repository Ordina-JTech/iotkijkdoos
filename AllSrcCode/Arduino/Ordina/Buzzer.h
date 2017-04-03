

class Buzzer  {
 
 public:
  Buzzer(int pin);
  void alarm(Led led1, Led led2);
  void vaderJacob(Led led1, Led led2);
  void customSound();

 private:
  void setLedToPreviousState(Led led1, bool wasOnLed1, Led led2, bool wasOnLed2);
  int _pin;
  
  //Vader Jacob
  int frequence[16] = {523, 587, 659, 523, 659, 699, 784, 784, 880, 784, 699, 659, 523, 523, 659, 523};
  int beginCouplet[4] = {0, 4, 7, 13};
  int endCouplet[4] = {4, 7, 13, 16};
  int secondsRest[4] = {300, 300, 300, 600};
  int nCouplets = (sizeof(endCouplet)/sizeof(int));
  int milliSec = 100;
};





