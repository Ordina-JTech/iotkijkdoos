//
//  ScanVC.swift
//  Ordina_IoT
//
//  Created by Rik Wout on 10-02-17.
//  Copyright © 2017 Wo. All rights reserved.
//

import UIKit
import CoreBluetooth


class ScanVC: UIViewController, BluetoothConnectionDelegate, TableViewDelegate {
    
//Properties
    @IBOutlet weak var tableView: UITableView!
    @IBOutlet weak var refreshBtn: UIBarButtonItem!
    
    private var tableViewObj: TableView!
    private var refreshControl: UIRefreshControl!
    private var devices: [(peripheral: CBPeripheral, RSSI: Float)] = [] //Array met gescande devices
    private var isFirstChar: Bool = true

//View Did Load
    override func viewDidLoad() {
        super.viewDidLoad()
        
        //Refreshbutton Disable
        refreshBtn.isEnabled = false
        
        //Delegate en DataSource gelijk zetten aan TableView VC.
        tableViewObj = TableView(delegate: self, data: [])
        tableView.delegate = tableViewObj
        tableView.dataSource = tableViewObj
        
        //Add refreshControl
        refreshControl = UIRefreshControl()
        refreshControl.addTarget(self, action: #selector(swipeToRefresh(_:)), for: UIControlEvents.valueChanged)
        tableView.addSubview(refreshControl)
        
        //Bluetooth Connection
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
        if devices.count == 0   {
            BlueProgressMessage.show(state: .noDeviceDetected, currentView: self.view)
        }
    }
    
    //Start scanning again.
    func refreshDevices()  {
        
        //Array met devices leegmaken, tableview reloaden en refreshButton disabelen.
        devices = []
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
        for exisiting in devices {
            if exisiting.peripheral.identifier == peripheral.identifier {
                return
            }
        }
        
        //Op basis van signaalsterkte (RSSI) sorteren.
        let theRSSI = RSSI?.floatValue ?? 0.0
        devices.append(peripheral: peripheral, RSSI: theRSSI)
        devices.sort {$0.RSSI < $1.RSSI }
        
        var deviceNames:[String] = []
        
        for index in 0..<devices.count{
            deviceNames.append(devices[index].peripheral.name!)
        }
        
        //Voeg namen toe op dezelfde volgorde als
        tableViewObj.reloadTableViewData(data: deviceNames)
        
        //Tableview reloaden
        tableView.reloadData()
    }
    
    
    //Als de connectie is gemaakt 'r' zenden om arduino te resetten. Hierdoor blijven er geen componenten actief als de verbinding plotseling is verbroken.
    func blueDidConnect(_ peripheral: CBPeripheral) {
        blue.sendMessage(string: "r")
    }
    
    
    //Als de connectie mislukt.
    func blueDidFailToConnect(_ peripheral: CBPeripheral, error: NSError?) {
        blueDidNotConnect()
        BlueProgressMessage.show(state: .failedToConnect, currentView: self.view)
    }
    
    
    //Als de connectie wordt verbroken.
    func blueDidDisconnect(_ peripheral: CBPeripheral, error: NSError?) {
        blueDidNotConnect()
        BlueProgressMessage.show(state: .disconnected, currentView: self.view)
        
        //Notificate other VC's
        NotificationCenter.default.post(name: Notification.Name("disconnected"), object: nil)
    }
    
    //Central Recieves 'r' from peripheral is resetting was succesfull.
    func blueDidReceiveString(_ message: String) {

        //De ontvangen String scheiden bij /r/n en in array zetten.
        let newLineChars = NSCharacterSet.newlines
        let messageArray = message.components(separatedBy: newLineChars).filter{!$0.isEmpty}
        
        if messageArray[0] == "y" && isFirstChar    {
            performSegue(withIdentifier: "scanToMain", sender: self)
            isFirstChar = false
        }
        else if messageArray[0] == "y"   {
            blue.manager.cancelPeripheralConnection(blue.connectedPeripheral!)
        }
    }
    
    
    //When blue did failed to or disconnected set connectedPeripheral to nil and enable refreshButton
    private func blueDidNotConnect()    {
        blue.connectedPeripheral = nil
        refreshBtn.isEnabled = true
    }
    
    
//Table View Delegate
    
    //Connect to the chosen peripheral.
    func userDidSelectRow(indexPath: IndexPath) {
        
        //Stoppen met scannen.
        blue.manager.stopScan()
        
        //Geselecteerde row deselecten.
        tableView.deselectRow(at: indexPath, animated: true)
        
        //selectedDevice gelijkstellen aan de gekozen device.
        let selectedDevice = devices[(indexPath as NSIndexPath).row].peripheral
        
        //Connectie maken met het geselecteerde device.
        blue.manager.connect(selectedDevice, options: nil)
    }
    
    
//BUTTONS
    
    
    //Restart scanning for peripherals.
    @IBAction func refreshWasTouched(_ sender: Any) {
        startRefreshControl()
        refreshDevices()
    }
}

