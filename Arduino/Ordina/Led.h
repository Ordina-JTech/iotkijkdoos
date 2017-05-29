

class LED{
  
 public:
  LED(int pin); 
  void powerLED(bool state);
  bool getState();
  int getPinNumber();
  void reset();
  
 private:
  int _pin; 
  bool isOn = false;
};


