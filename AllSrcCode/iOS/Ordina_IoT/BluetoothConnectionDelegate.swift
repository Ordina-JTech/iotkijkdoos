//
//  DelegateTest.swift
//  BLE_punchpad
//
//  Created by Rik Wout on 22-10-16.
//  Copyright © 2016 Wo. All rights reserved.
//

import UIKit
import CoreBluetooth

var bluetooth: BluetoothConnection!

protocol BluetoothConnectionDelegate    {
    func blueDidChangeState(_ poweredOn: Bool)
    func blueDidDiscoverPeripheral(_ peripheral: CBPeripheral, RSSI: NSNumber?)
    func blueDidConnect(_ peripheral: CBPeripheral)
    func blueDidDisconnect(_ peripheral: CBPeripheral, error: NSError?)
    func blueDidFailToConnect(_ peripheral: CBPeripheral, error: NSError?)
    func blueDidReceiveString(_ message: String)
}

extension BluetoothConnectionDelegate   {
    func blueDidDiscoverPeripheral(_ peripheral: CBPeripheral, RSSI: NSNumber?){}
    func blueDidConnect(_ peripheral: CBPeripheral){}
    func blueDidDisconnect(_ peripheral: CBPeripheral, error: NSError?){}
    func blueDidFailToConnect(_ peripheral: CBPeripheral, error: NSError?){}
    func blueDidReceiveString(_ message: String){}
}


final class BluetoothConnection: NSObject, CBCentralManagerDelegate, CBPeripheralDelegate   {
    
    var delegate: BluetoothConnectionDelegate?
    var manager: CBCentralManager!
    private(set) var connectedPeripheral: CBPeripheral?
    weak var writeCharacteristic: CBCharacteristic?
    private var writeType: CBCharacteristicWriteType = .withoutResponse

    private enum UUID   {
        static let Service = CBUUID(string: "FFE0")
        static let Characteristic = CBUUID(string: "FFE1")
    }
    
    var isReady: Bool {
        get {
            return manager.state == .poweredOn &&
                connectedPeripheral != nil &&
                writeCharacteristic != nil
        }
    }
    

    init(delegate: BluetoothConnectionDelegate) {
        super.init()
        self.delegate = delegate
        manager = CBCentralManager(delegate: self, queue: nil)
    }
    
    
//Central Manager
    
    
    func centralManagerDidUpdateState(_ central: CBCentralManager) {
        if central.state == .poweredOn {
            delegate?.blueDidChangeState(true)
        }
        else if central.state == .poweredOff    {
            delegate?.blueDidChangeState(false)
        }
    }
    

    func centralManager(_ central: CBCentralManager, didDiscover peripheral: CBPeripheral, advertisementData: [String : Any], rssi RSSI: NSNumber) {
        delegate?.blueDidDiscoverPeripheral(peripheral, RSSI: RSSI)
    }
    

    func centralManager(_ central: CBCentralManager, didConnect peripheral: CBPeripheral) {
        peripheral.delegate = self      //Strong reference to keep connected
        connectedPeripheral = peripheral
        peripheral.discoverServices([UUID.Service])
    }
    

    func centralManager(_ central: CBCentralManager, didFailToConnect peripheral: CBPeripheral, error: Error?) {
        connectedPeripheral = nil
        delegate?.blueDidFailToConnect(peripheral, error: error as NSError?)
    }
    
    func centralManager(_ central: CBCentralManager, didDisconnectPeripheral peripheral: CBPeripheral, error: Error?) {
        connectedPeripheral = nil
        delegate?.blueDidDisconnect(peripheral, error: error as NSError?)
    }
    
    
//Peripheral
    
    func peripheral(_ peripheral: CBPeripheral, didDiscoverServices error: Error?) {
        peripheral.discoverCharacteristics([UUID.Characteristic], for: peripheral.services![0])
    }
    
    
    func peripheral(_ peripheral: CBPeripheral, didDiscoverCharacteristicsFor service: CBService, error: Error?) {
        for characteristic in service.characteristics!   {
            if characteristic.uuid == UUID.Characteristic    {
                peripheral.setNotifyValue(true, for: characteristic)
            }
            writeCharacteristic = characteristic
            delegate?.blueDidConnect(peripheral)
        }
    }
    

    func peripheral(_ peripheral: CBPeripheral, didUpdateValueFor characteristic: CBCharacteristic, error: Error?) {
        let data = characteristic.value
        guard data != nil else {return}
        if let message = String(data: data!, encoding: String.Encoding.utf8) {
            delegate?.blueDidReceiveString(message)
        }
    }
  

    func startScanning()    {
        guard manager.state == .poweredOn else {return}
        let uuid = UUID.Service
        manager.scanForPeripherals(withServices: [uuid], options: nil)
    }
    

    func stopScanning() {
        manager.stopScan()
    }
    
    
    func sendMessage(string: String)  {
        if string != "" && isReady {
            let data = string.data(using: String.Encoding.utf8)
            connectedPeripheral!.writeValue(data!, for: writeCharacteristic!, type: writeType)
        }
    }
}








