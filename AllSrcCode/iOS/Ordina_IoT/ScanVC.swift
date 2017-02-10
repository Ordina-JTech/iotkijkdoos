//
//  ScanVC.swift
//  Ordina_IoT
//
//  Created by Rik Wout on 10-02-17.
//  Copyright © 2017 Wo. All rights reserved.
//

import UIKit
import CoreBluetooth


class ScanVC: UIViewController, UITableViewDelegate, UITableViewDataSource, BluetoothConnectionDelegate {
    
    
    //Properties
    @IBOutlet weak var tableView: UITableView!
    @IBOutlet weak var refreshBtn: UIBarButtonItem!


    //Swipe to refresh
    lazy var refreshControl: UIRefreshControl = {
        
        let refreshControl = UIRefreshControl()
        refreshControl.addTarget(self, action: #selector(swipeToRefresh(_:)), for: UIControlEvents.valueChanged)
        
        return refreshControl
    }()
    
    //Array met gescande devices
    var devices: [(peripheral: CBPeripheral, RSSI: Float)] = []
    
    //Het geselecteerde device
    var selectedDevice: CBPeripheral?
    
    
    override func viewDidLoad() {
        super.viewDidLoad()
        
        
        //Refreshbutton Disable
        refreshBtn.isEnabled = false
        
        //Delegate en DataSource gelijk zetten aan deze VC.
        tableView.delegate = self
        tableView.dataSource = self
        tableView.addSubview(refreshControl)
        
        //bluetooth Connection
        blue = BluetoothConnection(delegate: self)
        
        //Checken of bluetooth aan staat
        if (blue.manager.state == .poweredOn)   {
            blue.startScanning()
            
            //Enable refresh button while scanning
            refreshBtn.isEnabled = false
            
        
            //Na 5 seconden de method scanTimeOut aanroepen en het scanne stoppen.
            Timer.scheduledTimer(timeInterval: 5, target: self, selector: #selector(scanTimeOut), userInfo: nil, repeats: false)
            
        }
    }
    
    
//METHODS ScanVC
    
    //Door naar onder te swipen wordt er opnieuw gescand.
    func swipeToRefresh(_ refreshControl: UIRefreshControl) {
        
        //Als blue manager al aan het scannen is, niet nog een keer gaan scannen.
        if !blue.manager.isScanning{
            refreshDevices()
        }
    }
    
    
    //Als er 5 seconden is gescand.
    func scanTimeOut()  {
        
        blue.stopScanning()
        refreshControl.endRefreshing()
        refreshBtn.isEnabled = true
        
        //ProgressMessage laten zien.
        if devices.count == 0   {
            BlueProgressMessage.show(state: .noDeviceDetected, currentView: self.view)
        }
    }
    
    
    func refreshDevices()  {
        
        //Array met devices leegmaken, tableview reloaden en refreshButton disabelen.
        devices = []
        tableView.reloadData()
        refreshBtn.isEnabled = false
        
        //Opnieuw scannen.
        blue.startScanning()
        BlueProgressMessage.show(state: .scanning, currentView: self.view)
        
        //Na 5 seconden de method scanTimeOut aanroepen en het scanne stoppen.
        Timer.scheduledTimer(timeInterval: 5, target: self, selector: #selector(scanTimeOut), userInfo: nil, repeats: false)
    }
    
    
    //CB CENTRALMANAGER METHODE
    
    
    //Als bluetoothState is veranderd (powerdOn/poweredOff).
    func blueDidChangeState(_ poweredOn: Bool) {
        if poweredOn    {
            //Scannen starten
            blue.startScanning()
            refreshBtn.isEnabled = false
            
            //Na 5 seconden de method scanTimeOut aanroepen en het scanne stoppen.
            Timer.scheduledTimer(timeInterval: 5, target: self, selector: #selector(scanTimeOut), userInfo: nil, repeats: false)
        }
        else{
            blue.connectedPeripheral = nil
            BlueProgressMessage.show(state: .poweredOff, currentView: self.view)
            refreshBtn.isEnabled = false
        }
    }
    
    
    //Als devices zijn gevonden in array peripherals zetten.
    func blueDidDiscoverPeripheral(_ peripheral: CBPeripheral, RSSI: NSNumber?) {
        
        //checken of de gevonden al in perpheral staan, zo ja return.
        for exisiting in devices {
            if exisiting.peripheral.identifier == peripheral.identifier { return }
        }
        
        //Op basis van signaalsterkte (RSSI) sorteren.
        //TODO: checken waarom dubbele vraagteken en 0.0.
        let theRSSI = RSSI?.floatValue ?? 0.0
        devices.append(peripheral: peripheral, RSSI: theRSSI)
        devices.sort {$0.RSSI < $1.RSSI }
        
        //Tableview reloaden, zodat nieuwe data erin wordt gezet.
        tableView.reloadData()
    }
    
    
    //Als de connectie is gemaakt.
    func blueDidConnect(_ peripheral: CBPeripheral) {
        
        //Unwind segue uitvoeren.
        performSegue(withIdentifier: "scanToMain", sender: nil)
    }
    
    
    //Als de connectie mislukt.
    func blueDidFailToConnect(_ peripheral: CBPeripheral, error: NSError?) {
        
        blue.connectedPeripheral = nil
        refreshBtn.isEnabled = true
        BlueProgressMessage.show(state: .failedToConnect, currentView: self.view)
    }
    
    
    //Als de connectie wordt verbroken.
    func blueDidDisconnect(_ peripheral: CBPeripheral, error: NSError?) {
        
        blue.connectedPeripheral = nil
        refreshBtn.isEnabled = true
        BlueProgressMessage.show(state: .disconnected, currentView: self.view)
        
    }
    
    
    //TABLEVIEW DELEGATE/DATASOURCE METHODS.
    
    
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
    
    
    //BUTTONS
    
    //Refresh Button
    @IBAction func refreshWasTouched(_ sender: Any) {
        refreshDevices()
    }



}

