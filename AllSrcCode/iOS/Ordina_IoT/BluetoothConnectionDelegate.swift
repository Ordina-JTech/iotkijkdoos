//
//  DelegateTest.swift
//  BLE_punchpad
//
//  Created by Rik Wout on 22-10-16.
//  Copyright © 2016 Wo. All rights reserved.
//

import UIKit
import CoreBluetooth


//Global variable
var blue: BluetoothConnection!


//Protocol BluetoothConnectionDelegate.
protocol BluetoothConnectionDelegate    {
    
    func blueDidChangeState(_ poweredOn: Bool)
    func blueDidDiscoverPeripheral(_ peripheral: CBPeripheral, RSSI: NSNumber?)
    func blueDidConnect(_ peripheral: CBPeripheral)
    func blueDidDisconnect(_ peripheral: CBPeripheral, error: NSError?)
    func blueDidFailToConnect(_ peripheral: CBPeripheral, error: NSError?)
    func blueDidReceiveString(_ message: String)
}

//Sommig methods optional maken.
extension BluetoothConnectionDelegate   {
    
    func blueDidDiscoverPeripheral(_ peripheral: CBPeripheral, RSSI: NSNumber?){}
    func blueDidConnect(_ peripheral: CBPeripheral){}
    func blueDidDisconnect(_ peripheral: CBPeripheral, error: NSError?){}
    func blueDidFailToConnect(_ peripheral: CBPeripheral, error: NSError?){}
    func blueDidReceiveString(_ message: String){}
}

//Sturen naar Arduino.
enum Message {
    case quit
    case start
    case pause
    case result
}


final class BluetoothConnection: NSObject, CBCentralManagerDelegate, CBPeripheralDelegate   {
    
//Properties 
    
    //Delegate variabele voor bluetoothConnectionDelegate
    var delegate: BluetoothConnectionDelegate?
    
    //Variabele voor CBCentralManager
    var manager: CBCentralManager!
    
    //Variabele voor de connected device(optional)
    private(set) var connectedPeripheral: CBPeripheral?
    
    //Write characteristic als CBcharacteristic gevonden is
    weak var writeCharacteristic: CBCharacteristic?
    
    //Variabele voor de manier van schrijven: without response
    private var writeType: CBCharacteristicWriteType = .withoutResponse
    
    //Als isReady true is kan er iets verzonden worden naar het device
    var isReady: Bool {
        get {
            return manager.state == .poweredOn &&
                connectedPeripheral != nil &&
                writeCharacteristic != nil
        }
    }
    
    //UUID bluetooth module
    private let serviceUUID: CBUUID = CBUUID(string: "FFE0")
    private let characteristicUUID: CBUUID = CBUUID(string: "FFE1")
    
    
//Constructor
    init(delegate: BluetoothConnectionDelegate) {
        super.init()
        self.delegate = delegate
        manager = CBCentralManager(delegate: self, queue: nil)
    }
    
    
//CENTRALMANAGER DELEGATE METHODS
    
    
    //Als bluetooth veranderd (aan/uit).
    func centralManagerDidUpdateState(_ central: CBCentralManager) {
        if central.state == .poweredOn {
            delegate?.blueDidChangeState(true)
        }
        else if central.state == .poweredOff    {
            delegate?.blueDidChangeState(false)
        }
    }
    
    //Zoeken van peripherals.
    func centralManager(_ central: CBCentralManager, didDiscover peripheral: CBPeripheral, advertisementData: [String : Any], rssi RSSI: NSNumber) {
        delegate?.blueDidDiscoverPeripheral(peripheral, RSSI: RSSI)
        
    }
    
    //Als connectie met device is gemaakt.
    func centralManager(_ central: CBCentralManager, didConnect peripheral: CBPeripheral) {
        
        //Strong reference houden om de connectie in stand te houden.
        peripheral.delegate = self
        
        //connectedPeripheral gelijkstellen aan de peripheral.
        connectedPeripheral = peripheral
        
        //Kijken of er services zijn: "FFE0".
        peripheral.discoverServices([serviceUUID])
    }
    
    //Als het niet gelukt is om te connecten.
    func centralManager(_ central: CBCentralManager, didFailToConnect peripheral: CBPeripheral, error: Error?) {
        connectedPeripheral = nil
        delegate?.blueDidFailToConnect(peripheral, error: error as NSError?)
    }
    
    //Als de connectie verbroken is.
    func centralManager(_ central: CBCentralManager, didDisconnectPeripheral peripheral: CBPeripheral, error: Error?) {
        connectedPeripheral = nil
        delegate?.blueDidDisconnect(peripheral, error: error as NSError?)
    }
    
    
//PERIPHERAL DELEGATE METHODS
    
    //Als er services zijn ontdekt.
    func peripheral(_ peripheral: CBPeripheral, didDiscoverServices error: Error?) {
        peripheral.discoverCharacteristics([characteristicUUID], for: peripheral.services![0])
    }
    
    
    func peripheral(_ peripheral: CBPeripheral, didDiscoverCharacteristicsFor service: CBService, error: Error?) {
        
        //Wordt gekeken of alle characteristic "FFE1" zijn. Vervolgens wordt notify value op true gezet.
        for characteristic in service.characteristics!   {
            if characteristic.uuid == characteristicUUID    {
                peripheral.setNotifyValue(true, for: characteristic)
            }
            
            //Reference houden. Als writecharacteristic != nil kan je data zenden.
            writeCharacteristic = characteristic
            
            //Connectie naar de delegate sturen.
            delegate?.blueDidConnect(peripheral)
            
        }
    }
    
    //Als er data wordt ontvangen.
    func peripheral(_ peripheral: CBPeripheral, didUpdateValueFor characteristic: CBCharacteristic, error: Error?) {
        
        //Data ophalen.
        let data = characteristic.value
        
        //Checken of data niet nil is
        guard data != nil else {return}

        //Data converten naar unicode en delegate updaten
        if let message = String(data: data!, encoding: String.Encoding.utf8) {
            delegate?.blueDidReceiveString(message)
        }
    }
  
    
//METHODS BLUETOOTH CONNECTION
    
    
    //Start scanning for peripherals.
    func startScanning()    {

        guard manager.state == .poweredOn else {return}
        
        let uuid = serviceUUID
        manager.scanForPeripherals(withServices: [uuid], options: nil)
    }
    
    //Stop scanning for peripherals.
    func stopScanning() {
        manager.stopScan()
    }
    
    
    //Message sturen naar peripheral
    func sendMessage(string: String)  {
        
        //Check is string is not empty and conenction is ready to send messages.
        if string != "" && blue.isReady {
            let data = string.data(using: String.Encoding.utf8)
            connectedPeripheral!.writeValue(data!, for: blue.writeCharacteristic!, type: blue.writeType)
        }
    }
}








