#include <Servo.h>
#include <SoftwareSerial.h>

//Software Serial for communicating with the Bluetooth
SoftwareSerial bleSerial(10, 11);   //RX BLE to TX arduino(11), TX BLE to RX arduino(10)

//Servo
const int SERVO = 2;
Servo servo;

//Piezo Speaker
const int SPEAKER = 4;

//Yellow LED's
const int LED1 = 5;
const int LED2 = 6;

//RGB LED
const int RED = 7;
const int GREEN = 8;
const int BLUE = 9;

//Set led on or off (TODO: duidelijker)
bool isOnLed1 = false;
bool isOnLed2 = false;


//Setup runs once if the Arduino is powered on.
void setup() {

  //Start Serial for debugging (if needed)
  Serial.begin(9600);
  
  //Start communication with the bluetooth module
  bleSerial.begin(9600);

//Declare the digtial pins as output  

  //Yellow led1 & led2
  pinMode(LED1, OUTPUT);
  pinMode(LED2, OUTPUT);

  //RGB led
  pinMode(RED, OUTPUT);
  pinMode(GREEN, OUTPUT);
  pinMode(BLUE, OUTPUT);

  //Piëzo Speaker
  pinMode(SPEAKER, OUTPUT);

  //Start servo at 180 degrees (for counter clockwise)
  setServoAngle(179);
  
}


//Main loop 
void loop() {
     
  //If the app has send data
  if (bleSerial.available() > 0) {
    
    //Read char from central device
    char input = bleSerial.read(); 
    
    //Check the input and call the right method
    switch (input)  {
      case 'a':
        setLed(1);
        break;

      case 'b':
        setLed(2);
        break;

      case'c':
        getRgbValues();
        break;

      case 'd':
        alarm(); 
        break;
      
      case 'e':
        vaderJacob();
        break;

      //Create your own challenge
      case 'f':
        //Add call to method
        break;

      case 'g':
        getServoAngle();
        break;

      case 'r':
        resetAllComponents();
        break;

      default:
        break;
    }
  }
}

//Get the rgb values from the central device.
void getRgbValues() {
  
  //Array for the RGB values, the charCount and the index for accessing the array
  int rgbValues[3];
  int charCount = 0;
  int index = 0;
  
  //Combine the 9 chars together in one String
  String rgbString = "";

  //Loop untill the 9 chars are received
  while (charCount < 9)  {

    //If data is available
    if (bleSerial.available() > 0)  {

      //Read the char
      char input = bleSerial.read();

      //If the input is not equal to a newline char, add input to rgbString
      if (input != '\n') {
        
        //Add input to the rgbString
        rgbString += input;
        charCount++;

        //If rgbString length is equal to 3, the first value is received
        if (rgbString.length() == 3)  {

           //Add to int value to the array, add 1 to index and 
           rgbValues[index] = rgbString.toInt();
           Serial.println(rgbValues[index]);
           index++;
           rgbString = "";    
        }
      }
    }
  }

  //Set the RGB color with the received values.
  setRGBColor(rgbValues[0], rgbValues[1], rgbValues[2]);
  
}

//Read the serial input, translate it to the angle and call setServoAngle method.
void getServoAngle() {

  //Create empty string and char
  String angleStr = "";
  char input = '\0';

  while (input != '\n') {

    if (bleSerial.available() > 0)  {

      //Read input
      input = bleSerial.read();

      //If input is not equal to '\n', add the char to the string
      if (input != '\n')  {
        angleStr += input;     
      }
    }   
  }

  //Convert var angleStr an int. To get the servo moving counter clockwise -> 180-
  int angle = 180 - angleStr.toInt();

  //Set the servo angle with received angle
  setServoAngle(angle);
  
}

//Set led ON/OFF.
void setLed(int number) {
  
  if (number == 1)  {
    if (!isOnLed1)  {
      Serial.println("powerLed 1 = true");
      isOnLed1 = true;
      digitalWrite(LED1, HIGH);
    }
    else  {
      isOnLed1 = false;
      digitalWrite(LED1, LOW);
    }
  }
  else if (number == 2) {
    if (!isOnLed2)  {
      isOnLed2 = true;
      digitalWrite(LED2, HIGH);
    }
    else {
      isOnLed2 = false;
      digitalWrite(LED2, LOW);
    }
  }
}

//The method for setting the color of the RGB.
void setRGBColor(int red, int green, int blue)  {
  analogWrite(RED, red);
  analogWrite(GREEN, green);
  analogWrite(BLUE, blue);
}

//Play alarm with blinking LED's.
void alarm()  {

  //Start timer
  long startTime = millis();

  //Play alarm for 3 seconds (3000 ms)
  while (millis() - startTime < 3000) {
      digitalWrite(LED1, HIGH);
      digitalWrite(LED2, HIGH);
      tone(SPEAKER, 1000, 500);
    
      delay(500);
      
      digitalWrite(LED1, LOW);
      digitalWrite(LED2, LOW);
      tone(SPEAKER, 500, 500);
    
      delay(500);
  }

  //TODO: LED aan of uitzetten naar het alarm. anders kloppen de buttons in app niet meer.
   
}

//Play vader Jacob.
void vaderJacob() {

  for (int i = 0; i < 2; i++) {
    tone(SPEAKER, 523, 100);
    delay(300);
    tone(SPEAKER, 587, 100);
    delay(300);
    tone(SPEAKER, 659, 100);
    delay(300);
    tone(SPEAKER, 523, 100);
    delay(500); 
  }

  for (int i = 0; i < 2; i++) {
    tone(SPEAKER, 659, 100);
    delay(300);
    tone(SPEAKER, 699, 100);
    delay(300);
    tone(SPEAKER, 784, 100);
    delay(500);
  }
  //char names[] =     { 'c', 'd', 'e', 'f', '#', 'g', 'a', 'b', 'C', 'D', 'E', 'F', 'G', 'A', 'B' };
  //int frequencies[] = {262, 294, 330, 349, 370, 392, 440, 494, 523, 587, 659, 699, 784, 880, 989};
}

//Set the angle of the servo. The input has to be in the range of 0 - 180.
void setServoAngle(int angle)  {
  
  //Attach and detach servo here, otherwise the servo is moving while changing RGB led
  servo.attach(SERVO);
  servo.write(angle);
  
  //Give servo time to get at the given angle
  delay(50);

  //Detach the servo for unwanted movement
  servo.detach();
}

//Reset all the components to their start value
void resetAllComponents() {
  digitalWrite(LED1, LOW);
  digitalWrite(LED2, LOW);
  isOnLed1 = false;
  isOnLed2 = false;
  setRGBColor(0, 0, 0);
  setServoAngle(180);

  //Send 'y' to central device. If received central can disconnect from peripheral
  bleSerial.println("y");
}




