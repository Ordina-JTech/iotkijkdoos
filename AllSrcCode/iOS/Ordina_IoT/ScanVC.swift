//
//  ScanVC.swift
//  Ordina_IoT
//
//  Created by Rik Wout on 10-02-17.
//  Copyright © 2017 Wo. All rights reserved.
//

import UIKit
import CoreBluetooth


class ScanVC: UIViewController, BluetoothConnectionDelegate {
    
    
//Properties
    @IBOutlet weak var tableView: UITableView!
    @IBOutlet weak var refreshBtn: UIBarButtonItem!
    
    private var tableViewObj: TableView!
    private var refreshControl: UIRefreshControl!
    

//View Did Load
    override func viewDidLoad() {
        super.viewDidLoad()
        
        //Refreshbutton Disable
        refreshBtn.isEnabled = false
        
        //Delegate en DataSource gelijk zetten aan deze VC.
        tableViewObj = TableView()
        tableView.delegate = tableViewObj
        tableView.dataSource = tableViewObj
        
        //Add refreshControl
        refreshControl = UIRefreshControl()
        refreshControl.addTarget(self, action: #selector(swipeToRefresh(_:)), for: UIControlEvents.valueChanged)
        tableView.addSubview(refreshControl)
        
        //bluetooth Connection
        blue = BluetoothConnection(delegate: self)
        
        
    }
    
//METHODS ScanVC
    
    //Door naar onder te swipen wordt er opnieuw gescand.
    func swipeToRefresh(_ refreshControl: UIRefreshControl) {
        
        //Als blue manager al aan het scannen is, niet nog een keer gaan scannen.
        if !blue.manager.isScanning{
            refreshDevices()
        }
    }
    
    
    //After 5 seconds of scanning.
    func scanTimeOut()  {
        
        blue.stopScanning()
        refreshControl.endRefreshing()
        refreshBtn.isEnabled = true
        
        //ProgressMessage laten zien.
        if tableViewObj.devices.count == 0   {
            BlueProgressMessage.show(state: .noDeviceDetected, currentView: self.view)
        }
    }
    
    //Start scanning again.
    func refreshDevices()  {
        
        //Array met devices leegmaken, tableview reloaden en refreshButton disabelen.
        tableViewObj.devices = []
        tableView.reloadData()
        refreshBtn.isEnabled = false
        
        //Opnieuw scannen.
        blue.startScanning()
        
        //Na 5 seconden de method scanTimeOut aanroepen en het scanne stoppen.
        Timer.scheduledTimer(timeInterval: 5, target: self, selector: #selector(ScanVC.scanTimeOut), userInfo: nil, repeats: false)
    }
    
    //Start refreshen programmatically: Starting app and pressing refresh button.
    private func startRefreshControl()  {
        tableView.setContentOffset(CGPoint(x: 0, y: -25), animated: true)
        refreshControl.beginRefreshing()
    }
    
    func setPortraitOrientation()   {
        let value = UIInterfaceOrientation.portrait.rawValue
        UIDevice.current.setValue(value, forKey: "orientation")
    }
    
    
//CB CENTRALMANAGER METHODE
    
    
    //Als bluetoothState is veranderd (powerdOn/poweredOff).
    func blueDidChangeState(_ poweredOn: Bool) {
        if poweredOn    {
            //Scannen starten
            blue.startScanning()
            
            //Start refresh programmatically
            startRefreshControl()
            
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
        for exisiting in tableViewObj.devices {
            if exisiting.peripheral.identifier == peripheral.identifier {
                return
            }
        }
        
        //Op basis van signaalsterkte (RSSI) sorteren.
        //TODO: checken waarom dubbele vraagteken en 0.0.
        let theRSSI = RSSI?.floatValue ?? 0.0
        tableViewObj.devices.append(peripheral: peripheral, RSSI: theRSSI)
        tableViewObj.devices.sort {$0.RSSI < $1.RSSI }
        
        //Tableview reloaden, zodat nieuwe data erin wordt gezet.
        tableView.reloadData()
    }
    
    
    //Als de connectie is gemaakt.
    func blueDidConnect(_ peripheral: CBPeripheral) {
        
        //segue naar main uitvoeren.
        performSegue(withIdentifier: "scanToMain", sender: self)
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
        
        NotificationCenter.default.post(name: Notification.Name("disconnected"), object: nil)
    }
    
//BUTTONS
    
    //Refresh Button
    @IBAction func refreshWasTouched(_ sender: Any) {
        startRefreshControl()
        refreshDevices()
    }
}

