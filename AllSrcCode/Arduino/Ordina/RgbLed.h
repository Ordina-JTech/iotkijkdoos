struct RGB {
  byte r;
  byte g;
  byte b;
};

class RgbLed {
  
  public:
    RgbLed(int redPin, int greenPin, int bluePin);
    void setColor(char input);
    void showGradient();
    void reset();

  private:
     int _redPin;
     int _greenPin;
     int _bluePin;

     const RGB off = {0, 0, 0};
     const RGB red = {255, 0, 0};
     const RGB yellow = {255, 255, 0};
     const RGB green = {0, 255, 0}; 
     const RGB aqua = {0, 255, 255}; 
     const RGB blue = {0, 0, 255}; 
     const RGB purple = {255, 0, 255}; 
     const RGB *allColors[6];
     
     void writeColor(const RGB *rgbValues);
};


