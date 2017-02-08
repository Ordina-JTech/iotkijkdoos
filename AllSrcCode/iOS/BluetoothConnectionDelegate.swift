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


//TODO: kijken welke variabelen private/final kunnen zijn
final class BluetoothConnection: NSObject, CBCentralManagerDelegate, CBPeripheralDelegate   {
    
//Properties 
    
    //Delegate variabele voor bluetoothConnectionDelegate.
    var delegate: BluetoothConnectionDelegate?
    
    //Variabele voor CBCentralManager.
    var manager: CBCentralManager!
    
    //Variabele voor de connected device(optional).
    var connectedPeripheral: CBPeripheral?
    
    //Variabele voor
    weak var writeCharacteristic: CBCharacteristic?
    
    //Variabele voor de manier van schrijven: without response
    private var writeType: CBCharacteristicWriteType = .withoutResponse
    
    //Als isReady true is kan er iets verzonden worden naar het device.
    var isReady: Bool {
        get {
            return manager.state == .poweredOn &&
                connectedPeripheral != nil &&
                writeCharacteristic != nil
        }
    }
    
    //UUID
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
        print(advertisementData)
        delegate?.blueDidDiscoverPeripheral(peripheral, RSSI: RSSI)
        
    }
    
    //Als connectie met device is gemaakt.
    func centralManager(_ central: CBCentralManager, didConnect peripheral: CBPeripheral) {
        
        //Strong reference houden om de connectie in stand te houden.
        peripheral.delegate = self
        
        //connectedPeripheral gelijkstellen aan de peripheral.
        connectedPeripheral = peripheral
        
        //Kijken of er services zijn "FFE0".
        peripheral.discoverServices([serviceUUID])
    }
    
    //Als het niet gelukt is om te connecten.
    func centralManager(_ central: CBCentralManager, didFailToConnect peripheral: CBPeripheral, error: Error?) {
        delegate?.blueDidFailToConnect(peripheral, error: error as NSError?)
    }
    
    //Als de connectie verbroken is.
    func centralManager(_ central: CBCentralManager, didDisconnectPeripheral peripheral: CBPeripheral, error: Error?) {
        connectedPeripheral = nil
        delegate?.blueDidDisconnect(peripheral, error: error as NSError?)
    }
    
    
//PERIPHERAL DELEGATE METHODS
    
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
    
    
    func peripheral(_ peripheral: CBPeripheral, didUpdateValueFor characteristic: CBCharacteristic, error: Error?) {
        
        //Data ophalen.
        let data = characteristic.value
        
        //Checken of data niet niets is
        guard data != nil else {return}

        
        //Data converten van chars naar string
        if let message = String(data: data!, encoding: String.Encoding.utf8) {
            
            //Uit de ontvangen string /r/n halen.
            //let message = str.replacingOccurrences(of: "\r\n", with: "", options: .regularExpression, range: nil)
            
            delegate?.blueDidReceiveString(message)
        }
        /*
        var bytes = [UInt8](repeating: 0, count: data!.count / MemoryLayout<UInt8>.size)
        (data! as NSData).getBytes(&bytes, length: data!.count)
        print(bytes) */
    }
  
    
    
    
//METHODS BLUETOOTH CONNECTION
    
    
    //Start scanning for peripherals.
    func startScanning()    {
        //Zeker zijn dat bluetooth aanstaat.
        guard manager.state == .poweredOn else {return}
        
        let uuid = serviceUUID
        manager.scanForPeripherals(withServices: [uuid], options: nil)
        
        //Na 5 seconden de method scanTimeOut aanroepen en het scanne stoppen.
        //Timer.scheduledTimer(timeInterval: 5, target: self, selector: #selector(scanTimeOut), userInfo: nil, repeats: false)
    }
    
    //Stop scanning for peripherals.
    func stopScanning() {
        manager.stopScan()
    }
    
    //TODO: Change name method. To vague right now.
    //Message sturen naar PunchPad.
    func sendMessage(string: String)  {
        
        //Check is string is not empty and conenction is ready to send messages.
        if string != "" && blue.isReady {
            let data = string.data(using: String.Encoding.utf8)
            blue.connectedPeripheral!.writeValue(data!, for: blue.writeCharacteristic!, type: blue.writeType)
        }
    }
}








