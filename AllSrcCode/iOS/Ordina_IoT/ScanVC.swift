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
    private var isFirstScan = true
    
    private var isReadyToRefresh: Bool     {
        return !bluetooth.isReady &&
               !isFirstScan &&
               !bluetooth.manager.isScanning &&
               bluetooth.manager.state == .poweredOn
    }

    override func viewDidLoad() {
        super.viewDidLoad()
        tableViewObj = TableView(delegate: self, data: [])
        tableView.delegate = tableViewObj
        tableView.dataSource = tableViewObj
        
        refreshControl = UIRefreshControl()
        refreshControl.addTarget(self, action: #selector(swipeToRefresh(_:)), for: UIControlEvents.valueChanged)
        tableView.addSubview(refreshControl)
        
        bluetooth = BluetoothConnection(delegate: self)
    }
    
    override func viewWillAppear(_ animated: Bool) {
        NotificationCenter.default.addObserver(self, selector: #selector(applicationWillEnterForeground), name: Notification.Name("applicationWillEnterForeground"), object: nil)
    }
    
    override func viewWillDisappear(_ animated: Bool) {
        NotificationCenter.default.removeObserver(self, name: Notification.Name("applicationWillEnterForeground"), object: nil)
    }
    
    
    func swipeToRefresh(_ refreshControl: UIRefreshControl) {
        if bluetooth.manager.state == .poweredOn && !bluetooth.manager.isScanning{
            refreshAndScanDevices()
        }
        else if bluetooth.manager.state == .poweredOff  {
            refreshControl.endRefreshing()
            ProgressMessage.poweredOff.show(view: self.view)
        }
    }
    
    func scanTimeOut()  {
        guard bluetooth.manager.state == .poweredOn else {return}
        bluetooth.stopScanning()
        refreshControl.endRefreshing()
        refreshBtn.isEnabled = true
        
        if scannedDevices.count == 0   {
            ProgressMessage.noDevicesDetected.show(view: self.view)
        }
    }
    
    func refreshAndScanDevices()  {
        scannedDevices = []
        emptyTableView()
        startRefreshControl()
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
    
    func applicationWillEnterForeground()   {
        if bluetooth.manager.state == .poweredOff   {
            ProgressMessage.poweredOff.show(view: self.view)
        }
        guard isReadyToRefresh else {return}
        refreshAndScanDevices()
    }
    
    private func emptyTableView()   {
        tableViewObj.reloadTableViewData(data: [])
        tableView.reloadData()
        
        if refreshControl.isRefreshing  {
            refreshControl.endRefreshing()
        }
    }
    
    
//CB CENTRALMANAGER METHODS
    
    func bluetoothDidChangeState(_ central: CBCentralManager) {
        switch central.state    {
        
        case .unknown:
            ProgressMessage.unknown.show(view: self.view)
        case .resetting:
            ProgressMessage.resetting.show(view: self.view)
        case .unsupported:
            ProgressMessage.unsupported.show(view: self.view)
        case .unauthorized:
            ProgressMessage.unauthorized.show(view: self.view)
        case .poweredOff:
            emptyTableView()
            ProgressMessage.poweredOff.show(view: self.view)
            refreshBtn.isEnabled = false
        case .poweredOn:
            guard !bluetooth.manager.isScanning else {return}
            refreshAndScanDevices()
            isFirstScan = false
        }
    }
    
    func bluetoothDidDiscoverPeripheral(_ peripheral: CBPeripheral, RSSI: NSNumber?) {
        for exisiting in scannedDevices {
            if exisiting.peripheral.identifier == peripheral.identifier {
                return
            }
        }
        let theRSSI = RSSI?.floatValue ?? 0.0
        scannedDevices.append(peripheral: peripheral, RSSI: theRSSI)
        scannedDevices.sort {$0.RSSI < $1.RSSI }
        
        var deviceNames = [String]()
        for index in 0..<scannedDevices.count{
            deviceNames.append(scannedDevices[index].peripheral.name!)
        }
        
        tableViewObj.reloadTableViewData(data: deviceNames)
        tableView.reloadData()
    }
    
    func bluetoothDidConnect(_ peripheral: CBPeripheral) {
        bluetooth.sendMessage(string: PeripheralLetter.reset)
    }
    
    func bluetoothDidFailToConnect(_ peripheral: CBPeripheral, error: NSError?) {
        refreshBtn.isEnabled = true
        ProgressMessage.failedToConnect.show(view: self.view)
    }
    
    func bluetoothDidDisconnect(_ peripheral: CBPeripheral, error: NSError?) {
        refreshBtn.isEnabled = true
        ProgressMessage.disconnected.show(view: self.view)
        NotificationCenter.default.post(name: Notification.Name("disconnected"), object: nil)
    }
    
    func bluetoothDidReceiveString(_ message: String) {
        let newLineChars = NSCharacterSet.newlines
        let messages = message.components(separatedBy: newLineChars).filter{!$0.isEmpty}
        
        if messages[0] == PeripheralLetter.response && isFirstChar    {
            performSegue(withIdentifier: "scanToMain", sender: self)
            isFirstChar = false
        }
        else if messages[0] == PeripheralLetter.response  {
            bluetooth.manager.cancelPeripheralConnection(bluetooth.connectedPeripheral!)
        }
    }
    
    
//TableViewDelegate
    
    func userDidSelectRow(indexPath: IndexPath) {
        tableView.deselectRow(at: indexPath, animated: true)
        bluetooth.stopScanning()
        let selectedDevice = scannedDevices[(indexPath as NSIndexPath).row].peripheral
        bluetooth.manager.connect(selectedDevice, options: nil)
        
        //Prevent user tapping multiple times
        tableView.isUserInteractionEnabled = false
        Timer.scheduledTimer(timeInterval: 2, target: self, selector: #selector(ScanVC.enableTableViewUserInteraction), userInfo: nil, repeats: false)
    }
    
    func enableTableViewUserInteraction()    {
        tableView.isUserInteractionEnabled = true
    }

    
    
//BUTTONS
    
    @IBAction func refreshButtonWasPressed(_ sender: Any) {
        refreshAndScanDevices()
    }
}

