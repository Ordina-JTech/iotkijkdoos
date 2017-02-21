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

//Bool for led 1/2 ot know if their powered on 
bool isOnLed1 = false;
bool isOnLed2 = false;


//Setup runs once if the Arduino is powered on.
void setup() {

  //Start Serial for debugging (if needed)
  Serial.begin(9600);
  
  //Start communication with the bluetooth module
  bleSerial.begin(9600);

//Declare the digital pins as output  

  //Yellow led1 & led2
  pinMode(LED1, OUTPUT);
  pinMode(LED2, OUTPUT);

  //RGB led
  pinMode(RED, OUTPUT);
  pinMode(GREEN, OUTPUT);
  pinMode(BLUE, OUTPUT);

  //Piëzo Speaker
  pinMode(SPEAKER, OUTPUT);

  //Start servo at 180 degrees for counter clockwise and pause 400 milliseconds to get to the angle.
  setServoAngle(179, 400);
  
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
        getRGBValues();
        break;

      case 'd':
        alarm(); 
        break;
      
      case 'e':
        vaderJacob();
        break;

      //Create your own sound
      case 'f':
        yourCustomSound();
        break;

      case 'g':
        getServoAngle();
        break;

      case 'r':
        resetComponents();
        break;

      default:
        break;
    }
  }
}


//LED 1 & 2

//Set led ON/OFF.
void setLed(int number) {
  
  if (number == 1)  {
    if (!isOnLed1)  {
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


//RGB Light

//Read the char and set the RGB value.
void getRGBValues() {
  
  int count = 0;

  while (count == 0) {
    if (bleSerial.available() > 0) {
      
      char input = bleSerial.read();
    
      switch (input)  {
        
        //White
        case '0':
          setRGBColor(0,0,0);
          break;
        
        //Red
        case '1':
        setRGBColor(255,0,0);
        break;
       
        //Yellow
        case '2':
        setRGBColor(255,255,0);
        break;
        
        //Green
        case '3':
        setRGBColor(0,255,0);
        break;
        
        //Aqua
        case '4':
        setRGBColor(0,255,255);
        break;
        
        //Blue
        case '5':
        setRGBColor(0,0,255);
        break;
        
        //Purple
        case '6':
        setRGBColor(255,0,255);
        break;
      }
      count++;
    }
  }
}

//The method for setting the color of the RGB.
void setRGBColor(int red, int green, int blue)  {
  analogWrite(RED, red);
  analogWrite(GREEN, green);
  analogWrite(BLUE, blue);
}


//BUZZER

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

  //Leds are off after alarm. Check weither they were on before alarm started
  if (isOnLed1) {
    digitalWrite(LED1, HIGH);
  }
  if(isOnLed2)  {
    digitalWrite(LED2, HIGH);
  }    
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

//Create Your Custom Sound!
void yourCustomSound()  {
  
}


//SERVO 

//Read the serial input, convert it to the angle and call setServoAngle method.
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

  //Convert var angleStr to an int. To get the servo moving counter clockwise reduce 180 with received angle
  int angle = 180 - angleStr.toInt();

  //Set the servo angle with received angle in 50 milliseconds
  setServoAngle(angle, 50);
  
}

//Set the angle of the servo. The input has to be in the range of 0 - 180.
void setServoAngle(int angle, int milliSec)  {
  
  //Attach and detach servo here, otherwise the servo is moving while changing RGB light
  servo.attach(SERVO);

  //Write the angle to the servo
  servo.write(angle);
  
  //Give servo time to get at the given angle
  delay(milliSec);

  //Detach the servo for unwanted movement
  servo.detach();
}


//RESET

//Reset all the components to their begin value
void resetComponents() {
  digitalWrite(LED1, LOW);
  digitalWrite(LED2, LOW);
  isOnLed1 = false;
  isOnLed2 = false;
  setRGBColor(0, 0, 0);
  setServoAngle(179, 400);

  /*Send 'y' to central device. If received, central can disconnect from peripheral. 
  This is to make sure all the components are off after disconnecting with central device.*/
  bleSerial.println("y");
}




