//
//  TableView.swift
//  Ordina_IoT
//
//  Created by Rik Wout on 13-02-17.
//  Copyright © 2017 Wo. All rights reserved.
//

import Foundation
import CoreBluetooth

class TableView: NSObject, UITableViewDelegate, UITableViewDataSource     {
    
    //Array met gescande devices
    var devices: [(peripheral: CBPeripheral, RSSI: Float)] = []
    
    //Het geselecteerde device
    var selectedDevice: CBPeripheral?
    
    
    override init()  {

    }
    
    
    //Aantal rijen in array devices.
    func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        return devices.count
    }
    
    
    //Wat er in de cellen moet komen.
    func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        
        let cell = UITableViewCell()
        cell.textLabel?.text = devices[indexPath.row].peripheral.name
        cell.backgroundColor = UIColor.white
        cell.textLabel?.font = UIFont(name: "Avenir Next", size: 21)
        cell.textLabel?.textColor = UIColor.black
        cell.accessoryType = .disclosureIndicator
                
        return cell
    }
    
    
    //Als er op een specifeke cel wordt geklikt.
    func tableView(_ tableView: UITableView, didSelectRowAt indexPath: IndexPath) {
        
        //Stoppen met scannen.
        blue.manager.stopScan()
        
        //Geselecteerde row deselecten.
        tableView.deselectRow(at: indexPath, animated: true)
        
        //selectedDevice gelijkstellen aan de gekozen device.
        selectedDevice = devices[(indexPath as NSIndexPath).row].peripheral
        
        //Connectie maken met het geselecteerde device.
        blue.manager.connect(selectedDevice!, options: nil)
    }

    
    
    
    
    
    
    
    
    
    
    
}
