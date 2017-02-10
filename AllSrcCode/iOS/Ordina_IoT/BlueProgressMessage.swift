//
//  BlueProgressMessage.swift
//  PunchPad
//
//  Created by Rik Wout on 16-12-16.
//  Copyright © 2016 Wo. All rights reserved.
//

import Foundation
import UIKit


//TODO: Uit bluetooth delegate gehaald. Even kijken of het zo werkt. Waarschijnlijk wel.
//Progress Message.
 enum EBlueProgressMessage{
     case scanning
     case noDeviceDetected
     case connected
     case disconnected
     case failedToConnect
     case poweredOff
     case notConnected
 }


//TODO: int seconds doorgeven. nu behoorlijke code duplicatie...

//Message for the state of the bluetooth connection.
class BlueProgressMessage{
    
    //Progress Messages geven op basis van input.
    class func show(state: EBlueProgressMessage, currentView: UIView)   {
        
        //Als er nog HUD actief is, eerst weghalen.
        MBProgressHUD.hide(for: currentView, animated: true)
        
        let progressHud = MBProgressHUD.showAdded(to: currentView, animated: true)
        progressHud.contentColor = UIColor.white
        progressHud.bezelView.color = UIColor.darkGray
        progressHud.label.font = UIFont(name: "Avenir Next", size: 17)
        progressHud.isUserInteractionEnabled = false
        
        switch state   {
            
        case .scanning:
            progressHud.label.text = "Scanning..."
            progressHud.hide(animated: true, afterDelay: 5)

        case .noDeviceDetected:
            progressHud.mode = MBProgressHUDMode.text
            progressHud.label.text = "No devices detected!"
            progressHud.detailsLabel.text = "Make sure your bluetooth device is powered on."
            progressHud.hide(animated: true, afterDelay: 3)
            
        case .connected:
            progressHud.mode = MBProgressHUDMode.text
            progressHud.label.text = "Connected to: " + (blue.connectedPeripheral?.name)!
            progressHud.hide(animated: true, afterDelay: 1)
            
        case .disconnected:
            progressHud.mode = MBProgressHUDMode.text
            progressHud.label.text = "Disconnected!"
            progressHud.hide(animated: true, afterDelay: 2)
            
        case .failedToConnect:
            progressHud.mode = MBProgressHUDMode.text
            progressHud.label.text = "Failed to connect!"
            progressHud.detailsLabel.text = "Please try again."
            progressHud.hide(animated: true, afterDelay: 2)
            
        case .poweredOff:
            progressHud.mode = MBProgressHUDMode.text
            progressHud.label.text = "Your bluetooth is turned off!"
            progressHud.hide(animated: true, afterDelay: 2)
            
        case .notConnected:
            progressHud.mode = MBProgressHUDMode.text
            progressHud.label.text = "Not connected!"
            progressHud.hide(animated: true, afterDelay: 2)
        }
    }
}
