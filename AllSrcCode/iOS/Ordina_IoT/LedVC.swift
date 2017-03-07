//
//  LedVC.swift
//  Ordina_IoT
//
//  Created by Rik Wout on 16-02-17.
//  Copyright © 2017 Wo. All rights reserved.
//

import Foundation


class LedVC: NSObject   {
    
    var settingView: SettingView!
    private var switchBtn: UISwitch!
    private var ledLetter: String!

    init(frame: CGRect, headerText: String, ledLetter: String) {
        super.init()
        settingView = SettingView(frame: frame, headerText: headerText)
        self.ledLetter = ledLetter
        addLedView()
    }
    
    private func addLedView()   {
        //ImageView
        let imageName = "lamp_transparant"
        
        guard let image = UIImage(named: imageName) else    {
            print("Image was not found")
            return
        }
        
        let imageView = UIImageView(image: image)
        imageView.translatesAutoresizingMaskIntoConstraints = false
        imageView.contentMode = .scaleAspectFit
        settingView.addSubview(imageView)
        
        let heightConstant = settingView.frame.width/2.5
        imageView.widthAnchor.constraint(equalToConstant: heightConstant).isActive = true
        imageView.heightAnchor.constraint(equalToConstant: heightConstant).isActive = true
        let yConstant = settingView.frame.height/12.5
        imageView.centerYAnchor.constraint(equalTo: settingView.centerYAnchor, constant: yConstant).isActive = true
        imageView.centerXAnchor.constraint(equalTo: settingView.centerXAnchor).isActive = true

        //Switch
        switchBtn = UISwitch()
        switchBtn.translatesAutoresizingMaskIntoConstraints = false
        switchBtn.thumbTintColor = UIColor.lightGray
        switchBtn.tintColor = UIColor.lightGray
        switchBtn.onTintColor = UIColor.orange
        switchBtn.transform = CGAffineTransform(scaleX: 1.15, y: 1.15)
        switchBtn.addTarget(self, action: #selector(switchStateDidChange), for: .valueChanged)
        settingView.addSubview(switchBtn)
        
        let bottomConstant = settingView.frame.height/20
        switchBtn.centerXAnchor.constraint(equalTo: settingView.centerXAnchor).isActive = true
        switchBtn.centerYAnchor.constraint(equalTo: imageView.bottomAnchor, constant: bottomConstant).isActive = true
    }
    
    func switchStateDidChange() {
        var status = ""
        
        if switchBtn.isOn {
            status = "1"
        }
        else {
            status = "0"
        }
        
        if bluetooth.isReady   {
            let msg = ledLetter + status
            bluetooth.sendMessage(string: msg)
        }
    }
}
