# Naming a Bluetooth module HM-10 bt4.0 BLE (BTM) with USB connector

## Setup
- Connect Arduino to your computer with an USB connector
- Open Arduino IDE, choose Menu-Tools-Board=Arduino/Genuino UNO
- Choose Menu-Tools-Port=USBModem....
- Upload an empty sketch to your Arduino 
- Connect the BTM with wires to the Arduino, see below

BTM
- Yellow to 3.3 V
- Orange to GND
- Brown to RX
- Red to TX

Arduino/Genuino UNO
- Red to TX
- Brown to RX

## Naming a BTM
- Open Arduino IDE, choose Serial monitor from the menu, choose Baudrate=9600
- Type ‘AT+NAME?’ and give Enter for asking the current name of the BTM

Follow next steps for naming more then 1 BTM
1. Close Serial monitor window
2. Power Off Arduino and connect a new BTM
3. Open Serial monitor window
4. AT+NAMEKijkdoos2 + send (or enter)
5. Close Serial monitor window
6. Power Off/On Arduino
7. Check if BTM has new name
8. For naming next BTM goto 1

## URL's where this is partly based on: 
http://fab.cba.mit.edu/classes/863.15/doc/tutorials/programming/bluetooth.html
see Step 1: Setup With FTDI + Arduino Serial Monitor + AT Command

http://www.instructables.com/id/How-to-Use-Bluetooth-40-HM10/
see Step 8 en 9
