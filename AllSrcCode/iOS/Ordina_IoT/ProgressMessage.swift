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
    case Scanning
    case NoDevicesDetected
    case Connected
    case Disconnected
    case FailedToConnect
    case PoweredOff
    case NotConnected
    
    func show(view: UIView)   {
        
        MBProgressHUD.hide(for: view, animated: true)
        
        let progressHud = MBProgressHUD.showAdded(to: view, animated: true)
        progressHud.contentColor = UIColor.white
        progressHud.bezelView.color = UIColor.darkGray
        progressHud.label.font = UIFont(name: "Avenir Next", size: 17)
        progressHud.isUserInteractionEnabled = false
        progressHud.hide(animated: true, afterDelay: 3)
        
        switch self   {
            
        case .Scanning:
            progressHud.label.text = "Scanning..."
            
        case .NoDevicesDetected:
            progressHud.mode = MBProgressHUDMode.text
            progressHud.label.text = "No devices detected!"
            progressHud.detailsLabel.text = "Make sure your bluetooth device is powered on."
            
        case .Connected:
            progressHud.mode = MBProgressHUDMode.text
            progressHud.label.text = "Connected to: " + (bluetooth.connectedPeripheral?.name)!
            
        case .Disconnected:
            progressHud.mode = MBProgressHUDMode.text
            progressHud.label.text = "Disconnected!"
            
        case .FailedToConnect:
            progressHud.mode = MBProgressHUDMode.text
            progressHud.label.text = "Failed to connect!"
            progressHud.detailsLabel.text = "Please try again."
            
        case .PoweredOff:
            progressHud.mode = MBProgressHUDMode.text
            progressHud.label.text = "Your bluetooth is turned off!"
            
        case .NotConnected:
            progressHud.mode = MBProgressHUDMode.text
            progressHud.label.text = "Not connected!"
        }
    }
}

