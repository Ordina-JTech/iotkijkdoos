//
//  ProgressMessage.swift
//  PunchPad
//
//  Created by Rik Wout on 16-12-16.
//  Copyright © 2016 Wo. All rights reserved.
//

import Foundation
import UIKit


enum ProgressMessage{
    case scanning
    case noDevicesDetected
    case connected
    case disconnected
    case failedToConnect
    case poweredOff
    case notConnected
    
    func show(view: UIView)   {
        
        MBProgressHUD.hide(for: view, animated: true)
        
        let progressHud = MBProgressHUD.showAdded(to: view, animated: true)
        progressHud.contentColor = UIColor.white
        progressHud.bezelView.color = UIColor.darkGray
        progressHud.label.font = UIFont(name: "Avenir Next", size: 17)
        progressHud.isUserInteractionEnabled = false
        progressHud.hide(animated: true, afterDelay: 3)
        
        switch self   {
            
        case .scanning:
            progressHud.label.text = "Scanning..."
            
        case .noDevicesDetected:
            progressHud.mode = MBProgressHUDMode.text
            progressHud.label.text = "No devices detected!"
            progressHud.detailsLabel.text = "Make sure your bluetooth device is powered on."
            
        case .connected:
            progressHud.mode = MBProgressHUDMode.text
            progressHud.label.text = "Connected to: " + (bluetooth.connectedPeripheral?.name)!
            
        case .disconnected:
            progressHud.mode = MBProgressHUDMode.text
            progressHud.label.text = "Disconnected!"
            
        case .failedToConnect:
            progressHud.mode = MBProgressHUDMode.text
            progressHud.label.text = "Failed to connect!"
            progressHud.detailsLabel.text = "Please try again."
            
        case .poweredOff:
            progressHud.mode = MBProgressHUDMode.text
            progressHud.label.text = "Your bluetooth is turned off!"
            
        case .notConnected:
            progressHud.mode = MBProgressHUDMode.text
            progressHud.label.text = "Not connected!"
        }
    }
}

