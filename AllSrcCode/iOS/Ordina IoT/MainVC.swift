//
//  MainVC.swift
//  Ordina IoT
//
//  Created by Rik Wout on 24-01-17.
//  Copyright © 2017 Wo. All rights reserved.
//

import UIKit
import CoreBluetooth

class MainVC: UIViewController, BluetoothConnectionDelegate, UIScrollViewDelegate {

    //Properties
    @IBOutlet weak var scrollView: UIScrollView!
    @IBOutlet weak var connectBtn: UIBarButtonItem!
    private var scrollIndicatorObj: ScrollViewIndicator!
    private var pageIndicatorObj: PageIndicator!
    private let pageIndicatorHeight: CGFloat = 35
    
    private var page1: Page1VC!
    private var page2: Page2VC!
    private var page3: Page3VC!
    
    private var shouldDisconnect: Bool = false
    
    
    //ViewDidLoad.
    override func viewDidLoad() {
        super.viewDidLoad()
        
        //bluetooth Connection
        blue = BluetoothConnection(delegate: self)
        
        //Set scrollView delegate
        scrollView.delegate = self
        
        //Add the three xib files
        addViewControllers()
        
        //yOrigin page indicator/ change to 0 if translucent(nav bar) property is false.
        var yOrigin = self.navigationController!.navigationBar.frame.height + UIApplication.shared.statusBarFrame.height
        
        //Add pageIndicator
        pageIndicatorObj = PageIndicator(mainView: self.view, pageIndicatorHeight: pageIndicatorHeight, yOrigin: yOrigin)
        pageIndicatorObj.addPageIndicator()
        
        
        //Add scrollIndicator
        yOrigin = yOrigin + pageIndicatorHeight - 2
        scrollIndicatorObj = ScrollViewIndicator(mainView: self.view, scrollView: scrollView, yOrigin: yOrigin)
        scrollIndicatorObj.addScrollIndicator()
    }
    

    //TODO: Waarom width van de pages groter zijn, maar wel goed showen
    //TODO: change with/height if its in landscape. scrollIndicator performs well.
    //Add the three viewcontrollers.
    private func addViewControllers()   {
        
        //Reference to Page1VC
        page1 = Page1VC(nibName: "Page1VC", bundle: nil)
        self.addChildViewController(page1)
        scrollView.addSubview(page1.view)
        
        //Reference to Page2VC
        page2 = Page2VC(nibName: "Page2VC", bundle: nil)
        self.addChildViewController(page2)
        
        //Change origin of page2 (width page 1)
        var frame = page2.view.frame
        frame.origin.x = self.view.frame.width
        page2.view.frame = frame
        scrollView.addSubview(page2.view)
        
        //Reference to Page3VC
        page3 = Page3VC(nibName: "Page3VC", bundle: nil)
        self.addChildViewController(page3)
        
        //Change origin of page1 (width page 1 * 2)
        var frame2 = page3.view.frame
        frame2.origin.x = self.view.frame.width * 2
        page3.view.frame = frame2
        scrollView.addSubview(page3.view)
        
        //Properties scrollView
        let offset = self.navigationController!.navigationBar.frame.height + UIApplication.shared.statusBarFrame.height
        scrollView.isPagingEnabled = true //hierdoor blijft die niet in het midden haken.
        scrollView.contentSize = CGSize(width: self.view.frame.width * 3 , height: self.view.frame.height - offset)
        scrollView.showsHorizontalScrollIndicator = false
    }
    
    
    //Resets all the views to their begin value.
    private func resetViews()    {
        page1.led1Switch.setOn(false, animated: true)
        page1.led2Switch.setOn(false, animated: true)
        page1.redSlider.setValue(0, animated: true)
        page3.angleSlider.value = 0
        page3.rotateImageView.transform = CGAffineTransform(rotationAngle: 0)
    }
    
    
    //Resets all the Arduino components to their begin value.
    private func resetArduinoComponents()   {
        if blue.isReady {
            blue.sendMessage(string: "r")
        }
    }
    
    
//CB CENTRAL MANAGER DELEGATE METHODS
    
    
    //Als bluetooth state veranderd(poweredOn/Off).
    func blueDidChangeState(_ poweredOn: Bool) {
        
        if !poweredOn   {
            connectBtn.isEnabled = false
            BlueProgressMessage.show(state: .poweredOff, currentView: self.view)
        }
        else    {
            connectBtn.isEnabled = true
        }
    }
    
    //Message geven als verbinding is verbroken.
    func blueDidDisconnect(_ peripheral: CBPeripheral, error: NSError?) {
        BlueProgressMessage.show(state: .disconnected, currentView: self.view)
        connectBtn.title = ""        //Zonder dit krijg je eerst rare puntje..
        connectBtn.title = "Connect"
        
        //Reset all the views
        resetViews()
        
        //set shoudlDisconnect to false
        shouldDisconnect = false
        
    }
    
    //Peripheral only sends "y" if the method resetAllcomponents has called. Now we can disconnect from peripheral.
    func blueDidReceiveString(_ message: String) {
        
        //If user pressed the disconnect button (shouldDisconnect is set to true)
        if shouldDisconnect   {
            blue.manager.cancelPeripheralConnection(blue.connectedPeripheral!)
        }
    }

    
    //Update scrollindicator if user did scroll.
    func scrollViewDidScroll(_ scrollView: UIScrollView) {
        let offset = scrollView.contentOffset.x
        scrollIndicatorObj.updateScrollIndicator(xValue: offset)
        
    }
    
    
    //If Orientation view did change.
    override func viewWillTransition(to size: CGSize, with coordinator: UIViewControllerTransitionCoordinator) {
        
        //Statusbar height + navigationbar height.
        let yOrigin = UIApplication.shared.statusBarFrame.height + navigationController!.navigationBar.frame.height
        
        //Call the orientationChange method based on the current orientation
        if UIDevice.current.orientation.isLandscape   {
            scrollIndicatorObj.orientationChanged(orientation: .landscape, yOrigin: yOrigin/2)
        }
        else if UIDevice.current.orientation.isPortrait    {
            scrollIndicatorObj.orientationChanged(orientation: .portrait, yOrigin: yOrigin*2)
        }
    }
    
    
    //De seque van ScanVC naar MainVC
    @IBAction func unwindToMainViewController(segue: UIStoryboardSegue) {
        blue.delegate = self
        
        if (blue.connectedPeripheral != nil)    {
            connectBtn.title = "Disconnect"
            blue.sendMessage(string: "r")
            //TODO: bericht of label met connected to
        }
        else    {
            connectBtn.title = "Connect"
            //TODO: bericht of label met not connected
        }
    }
    
    
//BUTTONS 
    
    //If connected peripheral is nil -> seque to scan, otherwise cancel connection.
    @IBAction func connectBtn(_ sender: Any) {
        
        if (blue.connectedPeripheral == nil)    {
            performSegue(withIdentifier: "mainToScan", sender: self)
        }
        else    {
            shouldDisconnect = true
            resetArduinoComponents()
            //blue.manager.cancelPeripheralConnection(blue.connectedPeripheral!)

        }
    }
  
    
    
//PREPARE SEQUE
    
    //Will be called befor seque is executed
    override func prepare(for segue: UIStoryboardSegue, sender: Any?) {
        
        if segue.identifier == "mainToScan"    {
            if let scanVC = segue.destination as? ScanVC    {
                scanVC.setDelegate()
            }
        }
    }
}

