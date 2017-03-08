//
//  MainVC.swift
//  Ordina_IoT
//
//  Created by Rik Wout on 10-02-17.
//  Copyright © 2017 Wo. All rights reserved.
//

import UIKit


class MainVC: UIViewController{
    
    private enum HeaderText {
        static let led1 = "Control Left Light"
        static let led2 = "Control Right Light"
        static let rgb  = "Control Rgb light"
        static let servo = "Control Television"
        static let buzzer = "Control Buzzer"
        static let challenge = "Control Challenge"
    }
    
    @IBOutlet weak var disconnectBtn: DisconnectButton!
    @IBOutlet weak var leftLedImage: UIImageView!
    @IBOutlet weak var rightLedImage: UIImageView!
    @IBOutlet weak var tvImage: UIImageView!
    @IBOutlet weak var rgbImage: UIImageView!
    @IBOutlet weak var speakerImage: UIImageView!
    @IBOutlet weak var challengeImage: UIImageView!
    
   
    private var transparantView: UIView!
    private var rgbVC: RgbVC!
    private var leftLedVC: LedVC!
    private var rightLedVC: LedVC!
    private var servoVC: ServoVC!
    private var speakerVC: SpeakerVC!
    private var challengeVC: ChallengeVC!
    private var viewIsActive = [Bool]()
    private var settingViewArray: [UIView]!
    
    private var settingViewFrame: CGRect    {
        let point = CGPoint(x: -self.view.frame.width, y: 0)
        let size = CGSize(width: self.view.frame.width/2, height: self.view.frame.height)
        let rect = CGRect(origin: point, size: size)
        
        return rect
    }
    
    override var shouldAutorotate: Bool {
            return true
    }
    
    override var supportedInterfaceOrientations: UIInterfaceOrientationMask    {
        return .landscape
    }
    
    override var prefersStatusBarHidden: Bool {
        return true
    }
    
    override func viewDidLoad() {
        super.viewDidLoad()
        let value = UIInterfaceOrientation.landscapeLeft.rawValue
        UIDevice.current.setValue(value, forKey: "orientation")
        NotificationCenter.default.addObserver(self, selector: #selector(blueDidDisconnect), name: Notification.Name("disconnected"), object: nil)
        addImageTapGestures()
    }
    
    override func viewDidAppear(_ animated: Bool) {
        addTransparantView()
        createAndAddSettingViews()
    }
    
    private func createAndAddSettingViews()   {
        leftLedVC = LedVC(frame: settingViewFrame, headerText: HeaderText.led1, ledLetter: PeripheralLetter.led1)
        rightLedVC = LedVC(frame: settingViewFrame, headerText: HeaderText.led2, ledLetter: PeripheralLetter.led2)
        rgbVC = RgbVC(frame: settingViewFrame, headerText: HeaderText.rgb)
        servoVC = ServoVC(frame: settingViewFrame, headerText: HeaderText.servo)
        speakerVC = SpeakerVC(frame: settingViewFrame, headerText: HeaderText.buzzer)
        challengeVC = ChallengeVC(frame: settingViewFrame, headerText: HeaderText.challenge)
        
        settingViewArray = [leftLedVC.settingView, rightLedVC.settingView, rgbVC.settingView, servoVC.settingView, speakerVC.settingView, challengeVC.settingView]
        
        for index in 0..<settingViewArray.count {
            self.view.addSubview(settingViewArray[index])
            let gesture = UISwipeGestureRecognizer(target: self, action: #selector(userSwipedSettingView(tapGestureRecognizer:)))
            gesture.direction = .left
            
            viewIsActive.append(false)
            settingViewArray[index].tag = index
            settingViewArray[index].addGestureRecognizer(gesture)
        }
    }
    
    private func addTransparantView()   {
        let viewPoint = CGPoint(x: 0, y: 0)
        let viewSize = CGSize(width: self.view.frame.width, height: self.view.frame.height)
        let viewRect = CGRect(origin: viewPoint, size: viewSize)
        transparantView = UIView(frame: viewRect)
        transparantView.backgroundColor = UIColor.black
        transparantView.alpha = 0.0
        
        let tapGesture = UITapGestureRecognizer(target: self, action: #selector(userTappedTransparantView(tapGestureRecognizer:)))
        transparantView.addGestureRecognizer(tapGesture)
        self.view.addSubview(transparantView)
    }
    
    private func addImageTapGestures()    {
        let imageViewArray = [leftLedImage, rightLedImage, rgbImage, tvImage, speakerImage, challengeImage]
    
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
            moveSettingView(view: settingViewArray[index], x: 0, alpha: 0.6)
            viewIsActive[index] = true
        }
    }
    
    func userSwipedSettingView(tapGestureRecognizer: UITapGestureRecognizer)  {
        let index = tapGestureRecognizer.view!.tag

        if viewIsActive[index]    {
            moveSettingView(view: settingViewArray[index], x: -self.view.frame.width/2, alpha: 0.0)
            viewIsActive[index] = false
        }
    }
    
    func userTappedTransparantView(tapGestureRecognizer: UITapGestureRecognizer)    {
        for index in 0..<settingViewArray.count  {
            if viewIsActive[index] == true{
                moveSettingView(view: settingViewArray[index], x: -self.view.frame.width/2, alpha: 0.0)
                viewIsActive[index] = false
                break
            }
        }
    }
    
    private func moveSettingView(view: UIView, x: CGFloat, alpha: CGFloat)   {
        UIView.animate(withDuration: 0.4, delay: 0.0, options: UIViewAnimationOptions.curveEaseIn, animations: {
            view.frame.origin.x = x
            self.transparantView.alpha = alpha
        }, completion: nil)
    }

    
//Bluetooth
    
    func blueDidDisconnect(notificaiton: Notification)    {
        performSegue(withIdentifier: "mainToScan", sender: self)
    }
    
    
//Button
    
    @IBAction func disconnectWasPressed(_ sender: Any) {
        bluetooth.sendMessage(string: PeripheralLetter.reset)
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
