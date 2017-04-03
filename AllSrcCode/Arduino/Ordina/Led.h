

class Led{
  
 public:
  Led(int pin); //constructor
  void begin();
  char getStateChar(SoftwareSerial &bluetooth);
  void setLed(char input);
  bool getState();
  int getPinNumber();
  void reset();
  
 private:
  int _pin; 
  bool isOn = false;
};


