

class LED{
  
 public:
  LED(int pin); //constructor
  void begin();
  char getStateChar(SoftwareSerial &bluetooth);
  void setLED(char input);
  bool getState();
  int getPinNumber();
  void reset();
  
 private:
  int _pin; 
  bool isOn = false;
};


