//
//  ProgressMessage.swift
//  PunchPad
//
//  Created by Rik Wout on 16-12-16.
//  Copyright © 2016 Wo. All rights reserved.
//

import Foundation

enum ProgressMessage{
    case scanning
    case noDevicesDetected
    case connected
    case disconnected
    case failedToConnect
    case poweredOff
    case notConnected
    case unsupported
    case resetting
    case unknown
    case unauthorized
    
    
    static func hideActiveMessage(view: UIView) {
        MBProgressHUD.hide(for: view, animated: true)
    }

    func show(view: UIView)   {
        MBProgressHUD.hide(for: view, animated: true)
        
        let progressHud = MBProgressHUD.showAdded(to: view, animated: true)
        progressHud.contentColor = UIColor.white
        progressHud.bezelView.color = UIColor.darkGray
        progressHud.label.font = UIFont.avenirNext(size: 17)
        progressHud.isUserInteractionEnabled = false
        progressHud.mode = MBProgressHUDMode.text
        progressHud.hide(animated: true, afterDelay: 3)
        
        switch self   {
            
        case .scanning:
            progressHud.mode = MBProgressHUDMode.indeterminate
            progressHud.label.text = "Scanning..."
        case .noDevicesDetected:
            progressHud.label.text = "No devices detected!"
            progressHud.detailsLabel.text = "Make sure your bluetooth device is powered on"
        case .connected:
            progressHud.label.text = "Connected to: " + (bluetooth.connectedPeripheral?.name)!
        case .disconnected:
            progressHud.label.text = "Disconnected!"
        case .failedToConnect:
            progressHud.label.text = "Connection failed!"
            progressHud.detailsLabel.text = "Please try again"
        case .poweredOff:
            progressHud.label.text = "Your bluetooth is turned off!"
        case .notConnected:
            progressHud.label.text = "Not connected!"
        case .unsupported:
            progressHud.label.text = "Bluetooth is not supported"
        case .resetting:
            progressHud.label.text = "Bluetooth is resetting"
        case .unknown:
            progressHud.label.text = "An unknown error occurred"
        case .unauthorized:
            progressHud.label.text = "No permission to use Bluetooth"
        }
    }
}

