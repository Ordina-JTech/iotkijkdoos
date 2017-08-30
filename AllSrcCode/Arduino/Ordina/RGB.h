struct RGB_color {
  int red;
  int green;
  int blue;  
};




class RGB {
  
  public:
    RGB(int redPin, int greenPin, int bluePin);
    void setColor(char input);
    void showGradient();
    void reset();

  private:
     int _redPin;
     int _greenPin;
     int _bluePin;
     void writeColor(RGB_color rgbValues);    
};


#ifdef RGB_GLOBALS
     RGB_color allRgbColors[7] = { {0,0,0}, {255,0,0}, {255,255,0}, {0,255,0}, {0,255,255}, {0,0,255}, {255,0,255}};
#endif
#define OFF allRgbColors[0]
#define N_COLORS sizeof(allRgbColors) / sizeof(RGB_color)
