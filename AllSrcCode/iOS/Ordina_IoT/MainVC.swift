//
//  MainVC.swift
//  Ordina_IoT
//
//  Created by Rik Wout on 10-02-17.
//  Copyright © 2017 Wo. All rights reserved.
//

import UIKit


class MainVC: UIViewController{
    
//Properties
    @IBOutlet weak var disconnectBtn: DisconnectButton!
    @IBOutlet weak var leftLedImage: UIImageView!
    @IBOutlet weak var rightLedImage: UIImageView!
    @IBOutlet weak var tvImage: UIImageView!
    @IBOutlet weak var rgbImage: UIImageView!
    @IBOutlet weak var speakerImage: UIImageView!
    
    private var rgbVC: RgbVC!
    private var leftLedVC: LedVC!
    private var rightLedVC: LedVC!
    private var servoVC: ServoVC!
    private var speakerVC: SpeakerVC!
    
    private var viewIsActive: [Bool] = []
    private var settingViewArray: [UIView]!
    
    
    override open var shouldAutorotate: Bool {
            return true
    }
    
    override open var supportedInterfaceOrientations: UIInterfaceOrientationMask    {
        return .landscape
    }
    
    private final var settingViewFrame: CGRect    {

        let point = CGPoint(x: -self.view.frame.width, y: 0)
        let size = CGSize(width: self.view.frame.width/2, height: self.view.frame.height)
        let rect = CGRect(origin: point, size: size)
        
        return rect
    }
    

    override func viewDidLoad() {
        super.viewDidLoad()
        
        let value = UIInterfaceOrientation.landscapeLeft.rawValue
        UIDevice.current.setValue(value, forKey: "orientation")

        NotificationCenter.default.addObserver(self, selector: #selector(blueDidDisconnect), name: Notification.Name("disconnected"), object: nil)
        addImageTapGestures()
    }
    
    
    override func viewDidAppear(_ animated: Bool) {
        
        leftLedVC = LedVC(frame: settingViewFrame, title: "Control Left Light", blueMessage: "a")
        rightLedVC = LedVC(frame: settingViewFrame, title: "Control Right Light", blueMessage: "b")
        rgbVC = RgbVC(frame: settingViewFrame)
        servoVC = ServoVC(frame: settingViewFrame, title: "Control Television")
        speakerVC = SpeakerVC(frame: settingViewFrame, title: "Control Speaker")

        settingViewArray = [leftLedVC.settingView, rightLedVC.settingView, rgbVC.settingView, servoVC.settingView, speakerVC.settingView]
        
        for index in 0..<settingViewArray.count {
            self.view.addSubview(settingViewArray[index])
            
            let gesture = UISwipeGestureRecognizer(target: self, action: #selector(userSwipedSettingView(tapGestureRecognizer:)))
            gesture.direction = .left
            
            viewIsActive.append(false)
            
            settingViewArray[index].tag = index
            settingViewArray[index].addGestureRecognizer(gesture)
        }
    }
    
//Gestures
    

    private func addImageTapGestures()    {
        
        let imageViewArray = [leftLedImage, rightLedImage, rgbImage, tvImage, speakerImage]
    
        for i in 0..<imageViewArray.count    {
            let tapGesture = UITapGestureRecognizer(target: self, action: #selector(imageWasTapped(tapGestureRecognizer:)))
            imageViewArray[i]?.tag = i
            imageViewArray[i]?.isUserInteractionEnabled = true
            imageViewArray[i]?.addGestureRecognizer(tapGesture)
        }        
    }
    

    func imageWasTapped(tapGestureRecognizer: UITapGestureRecognizer)   {
        
        let index = tapGestureRecognizer.view!.tag

        if !viewIsActive[index] && !viewIsActive.contains(true)    {
            moveSettingView(view: settingViewArray[index], x: 0)
            viewIsActive[index] = true
        }
    }
    
    
    func userSwipedSettingView(tapGestureRecognizer: UITapGestureRecognizer)  {
        
        let index = tapGestureRecognizer.view!.tag

        if viewIsActive[index]    {
            moveSettingView(view: settingViewArray[index], x: -self.view.frame.width/2)
            viewIsActive[index] = false
        }
    }
    

    private func moveSettingView(view: UIView, x: CGFloat)   {
        UIView.animate(withDuration: 0.4, delay: 0.0, options: UIViewAnimationOptions.curveEaseIn, animations: {
            view.frame.origin.x = x
        }, completion: nil)
    }

    
//Bluetooth
    
    func blueDidDisconnect(notificaiton: Notification)    {
        performSegue(withIdentifier: "mainToScan", sender: self)
    }
    
    
//Buttons
    
    
    @IBAction func disconnectWasPressed(_ sender: Any) {
        bluetooth.sendMessage(string: "r")
    }
    

//Prepare Seque
    

    override func prepare(for segue: UIStoryboardSegue, sender: Any?) {
        if segue.identifier == "mainToScan"    {
            if let scanVC = segue.destination as? ScanVC    {
                scanVC.setPortraitOrientation()
            }
        }
    }
}
