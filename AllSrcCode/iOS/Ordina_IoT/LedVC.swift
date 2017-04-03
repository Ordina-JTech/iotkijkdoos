//
//  LedVC.swift
//  Ordina_IoT
//
//  Created by Rik Wout on 16-02-17.
//  Copyright © 2017 Wo. All rights reserved.
//

import Foundation


class LedVC: NSObject   {
    
    enum LedOrientation {
        case left
        case right
    }
    
    
    private enum ImageName  {
        static let leftLed = "lamp-links"
        static let rightLed = "lamp-rechts"
    }
    private enum ButtonState    {
        static let on = "1"
        static let off = "0"
    }
    
    private(set) var settingView: SettingView!
    private var switchBtn: UISwitch!
    private var ledLetter: String!
    private var buttonStateLetter = ButtonState.off

    init(frame: CGRect, headerText: String, ledLetter: String, orientation: LedOrientation) {
        super.init()
        settingView = SettingView(frame: frame, headerText: headerText)
        self.ledLetter = ledLetter
        addLedView(orientation: orientation)
    }
    
    private func addLedView(orientation: LedOrientation)   {
        //ImageView
        var imageName = ""
        
        switch orientation  {
        case .left:
            imageName = ImageName.leftLed
        case .right:
            imageName = ImageName.rightLed
        }
        
        guard let image = UIImage(named: imageName) else    {
            print("Image was not found")
            return
        }
        
        let imageView = UIImageView(image: image)
        imageView.translatesAutoresizingMaskIntoConstraints = false
        imageView.contentMode = .scaleAspectFit
        settingView.addSubview(imageView)
        
        let heightConstant = settingView.frame.width/3.5
        imageView.widthAnchor.constraint(equalToConstant: heightConstant).isActive = true
        imageView.heightAnchor.constraint(equalToConstant: heightConstant).isActive = true
        let yConstant = settingView.frame.height/12.5
        imageView.centerYAnchor.constraint(equalTo: settingView.centerYAnchor, constant: yConstant).isActive = true
        imageView.centerXAnchor.constraint(equalTo: settingView.centerXAnchor).isActive = true

        //Switch Button
        switchBtn = UISwitch()
        switchBtn.translatesAutoresizingMaskIntoConstraints = false
        switchBtn.thumbTintColor = UIColor.lightGray
        switchBtn.tintColor = UIColor.lightGray
        switchBtn.onTintColor = UIColor.orange
        switchBtn.transform = CGAffineTransform(scaleX: 1.15, y: 1.15)
        switchBtn.addTarget(self, action: #selector(switchStateDidChange), for: .valueChanged)
        settingView.addSubview(switchBtn)
        
        let bottomConstant = settingView.frame.height/10
        switchBtn.centerXAnchor.constraint(equalTo: settingView.centerXAnchor).isActive = true
        switchBtn.centerYAnchor.constraint(equalTo: imageView.bottomAnchor, constant: bottomConstant).isActive = true
    }
    
    func switchStateDidChange() {
        if switchBtn.isOn {
            buttonStateLetter = ButtonState.on
        }
        else {
            buttonStateLetter = ButtonState.off
        }
        let msg = ledLetter + buttonStateLetter
        bluetooth.sendMessage(string: msg)
    }
}
