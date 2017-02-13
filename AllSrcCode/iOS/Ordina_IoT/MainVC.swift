//
//  MainVC.swift
//  Ordina_IoT
//
//  Created by Rik Wout on 10-02-17.
//  Copyright © 2017 Wo. All rights reserved.
//

import UIKit


class MainVC: UIViewController {
    
//Properties
    @IBOutlet weak var disconnectBtn: DisconnectButton!
    @IBOutlet weak var leftLedImage: UIImageView!
    @IBOutlet weak var rightLedImage: UIImageView!
    @IBOutlet weak var tvImage: UIImageView!
    
    var rgbVC: RgbVC!

    
    //Prevent changing to portrait
    override var shouldAutorotate: Bool {
            return false
    }
    
    private var settingViewFrame: CGRect    {
        let point = CGPoint(x: self.view.frame.width/4, y: -self.view.frame.height)
        let size = CGSize(width: self.view.frame.width/2, height: self.view.frame.height - 50)
        let rect = CGRect(origin: point, size: size)
        
        return rect
        
    }
    
    //View Did Load
    override func viewDidLoad() {
        super.viewDidLoad()
        
        //Set VC in landscape mode
        let value = UIInterfaceOrientation.landscapeLeft.rawValue
        UIDevice.current.setValue(value, forKey: "orientation")
        
        //Add notifcation listener
        NotificationCenter.default.addObserver(self, selector: #selector(blueDidDisconnect), name: Notification.Name("disconnected"), object: nil)
        
        //Add tap gesture for every image view
        addTapGestures()
        
        
    }
    
    //Adding SettingViews to mainView. Has to bed in view did appear, because in the view did load the frame size is incorrect.
    override func viewDidAppear(_ animated: Bool) {
        
        rgbVC = RgbVC(frame: settingViewFrame)
        self.view.addSubview(rgbVC.rgbSettingView)
    }
    
    
    //Adding tap gesture for every image view.
    private func addTapGestures()    {
        
        let imageViewArray = [leftLedImage, tvImage, rightLedImage]
        
        for i in 0..<imageViewArray.count    {
            
            let tapGesture = UITapGestureRecognizer(target: self, action: #selector(imageWasTapped(tapGestureRecognizer:)))
            imageViewArray[i]?.tag = i
            imageViewArray[i]?.isUserInteractionEnabled = true
            imageViewArray[i]?.addGestureRecognizer(tapGesture)
        }        
    }
    
    
    //If a image is tapped, show the SettingView
    func imageWasTapped(tapGestureRecognizer: UITapGestureRecognizer)   {
        
        let tappedImage = tapGestureRecognizer.view as! UIImageView
        
        switch tappedImage.tag  {
         
        //Left Led Image
        case 0:
            animateSettingView(view: rgbVC.rgbSettingView, y: 25)
        //Tv Image
        case 1:
            break
        //Right Led Image
        case 2:
            animateSettingView(view: rgbVC.rgbSettingView, y: -rgbVC.rgbSettingView.frame.height)
        default:
            break
        }
    }
    
    
    private func animateSettingView(view: UIView, y: CGFloat)   {
        
        UIView.animate(withDuration: 0.4, delay: 0.0, options: UIViewAnimationOptions.curveEaseIn, animations: {
            
            view.frame.origin.y = y
            
        }, completion: nil)
    }
    
    
    
    //If bluetooth disconnected.
    func blueDidDisconnect(notificaiton: Notification)    {
        performSegue(withIdentifier: "mainToScan", sender: self)
        
    }   
    
    //Disconnect button was pressed.
    @IBAction func disconnectWasPressed(_ sender: Any) {
        blue.manager.cancelPeripheralConnection(blue.connectedPeripheral!)
    }
    
    
    //Set scanVC in portrait pefore seque.
    override func prepare(for segue: UIStoryboardSegue, sender: Any?) {
        if segue.identifier == "mainToScan"    {
            if let scanVC = segue.destination as? ScanVC    {
                scanVC.setPortraitOrientation()
            }
        }
    }
    
    


}
