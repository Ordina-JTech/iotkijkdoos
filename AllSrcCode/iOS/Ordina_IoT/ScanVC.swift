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
    
    @IBOutlet weak var tableView: UITableView!
    @IBOutlet weak var refreshBtn: UIBarButtonItem!

    private var tableViewObj: TableView!
    private var refreshControl: UIRefreshControl!
    private var scannedDevices = [(peripheral: CBPeripheral, RSSI: Float)]()
    private var isFirstChar = true

    override func viewDidLoad() {
        super.viewDidLoad()
        
        refreshBtn.isEnabled = false
        
        tableViewObj = TableView(delegate: self, data: [])
        tableView.delegate = tableViewObj
        tableView.dataSource = tableViewObj
        
        refreshControl = UIRefreshControl()
        refreshControl.addTarget(self, action: #selector(swipeToRefresh(_:)), for: UIControlEvents.valueChanged)
        tableView.addSubview(refreshControl)
        
        bluetooth = BluetoothConnection(delegate: self)
    }
    
    
//METHODS ScanVC
    
    
    func swipeToRefresh(_ refreshControl: UIRefreshControl) {
        if !bluetooth.manager.isScanning{
            refreshDevices()
        }
    }
    
    
    func scanTimeOut()  {
        bluetooth.stopScanning()
        refreshControl.endRefreshing()
        refreshBtn.isEnabled = true
        
        if scannedDevices.count == 0   {
            ProgressMessage.NoDevicesDetected.show(view: self.view)
        }
    }
    
    
    func refreshDevices()  {
        scannedDevices = []
        tableView.reloadData()
        refreshBtn.isEnabled = false
        bluetooth.startScanning()
        Timer.scheduledTimer(timeInterval: 5, target: self, selector: #selector(ScanVC.scanTimeOut), userInfo: nil, repeats: false)
    }
    
    
    private func startRefreshControl()  {
        tableView.setContentOffset(CGPoint(x: 0, y: -25), animated: true)
        refreshControl.beginRefreshing()
    }
    

    func setPortraitOrientation()   {
        let value = UIInterfaceOrientation.portrait.rawValue
        UIDevice.current.setValue(value, forKey: "orientation")
    }
    
    
    
//CB CENTRALMANAGER METHODE
    
    
    
    func blueDidChangeState(_ poweredOn: Bool) {
        if poweredOn    {
            bluetooth.startScanning()
            startRefreshControl()
            refreshBtn.isEnabled = false
            Timer.scheduledTimer(timeInterval: 5, target: self, selector: #selector(scanTimeOut), userInfo: nil, repeats: false)
        }
        else{
            ProgressMessage.PoweredOff.show(view: self.view)
            refreshBtn.isEnabled = false
        }
    }
    
    
    func blueDidDiscoverPeripheral(_ peripheral: CBPeripheral, RSSI: NSNumber?) {
        
        for exisiting in scannedDevices {
            if exisiting.peripheral.identifier == peripheral.identifier {
                return
            }
        }
        
        let theRSSI = RSSI?.floatValue ?? 0.0
        scannedDevices.append(peripheral: peripheral, RSSI: theRSSI)
        scannedDevices.sort {$0.RSSI < $1.RSSI }
        
        var deviceNames:[String] = []
        
        for index in 0..<scannedDevices.count{
            deviceNames.append(scannedDevices[index].peripheral.name!)
        }
        
        tableViewObj.reloadTableViewData(data: deviceNames)
        tableView.reloadData()
    }
    
    
    func blueDidConnect(_ peripheral: CBPeripheral) {
        bluetooth.sendMessage(string: Letter.Reset.rawValue)
    }
    
    
    func blueDidFailToConnect(_ peripheral: CBPeripheral, error: NSError?) {
        refreshBtn.isEnabled = true
        ProgressMessage.FailedToConnect.show(view: self.view)
    }
    
    
    func blueDidDisconnect(_ peripheral: CBPeripheral, error: NSError?) {
        refreshBtn.isEnabled = true
        ProgressMessage.Disconnected.show(view: self.view)
        NotificationCenter.default.post(name: Notification.Name("disconnected"), object: nil)
    }
    
    
    func blueDidReceiveString(_ message: String) {
        let newLineChars = NSCharacterSet.newlines
        let messageArray = message.components(separatedBy: newLineChars).filter{!$0.isEmpty}
        
        if messageArray[0] == Letter.Resetted.rawValue && isFirstChar    {
            performSegue(withIdentifier: "scanToMain", sender: self)
            isFirstChar = false
        }
        else if messageArray[0] == Letter.Resetted.rawValue   {
            bluetooth.manager.cancelPeripheralConnection(bluetooth.connectedPeripheral!)
        }
    }
    
    
//TableViewDelegate
    
    
    func userDidSelectRow(indexPath: IndexPath) {
        bluetooth.manager.stopScan()
        tableView.deselectRow(at: indexPath, animated: true)
        let selectedDevice = scannedDevices[(indexPath as NSIndexPath).row].peripheral
        bluetooth.manager.connect(selectedDevice, options: nil)
    }
    
    
//BUTTONS
    
    
    @IBAction func refreshButtonWasPressed(_ sender: Any) {
        startRefreshControl()
        refreshDevices()
    }
    
}

