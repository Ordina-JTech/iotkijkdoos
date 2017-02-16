//
//  LedVC.swift
//  Ordina_IoT
//
//  Created by Rik Wout on 16-02-17.
//  Copyright © 2017 Wo. All rights reserved.
//

import Foundation



class LedVC: NSObject   {
    
    
//Properties.
    var settingView: SettingView!
    private var switchBtn: UISwitch!
    private var message: String!
    
    
//Constructor.
    init(frame: CGRect, title: String, blueMessage: String) {
        super.init()
        
        settingView = SettingView(frame: frame, title: title)
        self.message = blueMessage
        addLedView()
    }
    
    
    private func addLedView()   {
    
    //ImageView
        
        //Creation
        let imageName = "lamp_transparant"
        let image = UIImage(named: imageName)
        let imageView = UIImageView(image: image)
        settingView.addSubview(imageView)
        
        //Constraints
        let heightConstant = settingView.frame.width/2.5
        imageView.widthAnchor.constraint(equalToConstant: heightConstant).isActive = true
        imageView.heightAnchor.constraint(equalToConstant: heightConstant).isActive = true
        
        let yConstant = settingView.frame.height/12.5   //Ratio (30.0 iPhone 7)
        imageView.centerYAnchor.constraint(equalTo: settingView.centerYAnchor, constant: yConstant).isActive = true
        imageView.centerXAnchor.constraint(equalTo: settingView.centerXAnchor).isActive = true

        //Properties
        imageView.translatesAutoresizingMaskIntoConstraints = false
        imageView.contentMode = .scaleAspectFit
        
    //Switch Button
        
        //Creation
        switchBtn = UISwitch()
        settingView.addSubview(switchBtn)
        
        //Constraints
        let bottomConstant = settingView.frame.height/20
        switchBtn.centerXAnchor.constraint(equalTo: settingView.centerXAnchor).isActive = true
        switchBtn.centerYAnchor.constraint(equalTo: imageView.bottomAnchor, constant: bottomConstant).isActive = true
        
        //Properties
        switchBtn.translatesAutoresizingMaskIntoConstraints = false
        switchBtn.thumbTintColor = UIColor.lightGray
        switchBtn.tintColor = UIColor.lightGray
        switchBtn.onTintColor = UIColor.orange
        switchBtn.transform = CGAffineTransform(scaleX: 1.15, y: 1.15)
        switchBtn.addTarget(self, action: #selector(switchStateDidChange), for: .valueChanged)
    }
    
    
    //If user did change the state.
    func switchStateDidChange() {
        if blue.isReady   {
            blue.sendMessage(string: message)
        }
    }
}
