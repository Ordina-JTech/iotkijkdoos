//
//  ChallengeVC.swift
//  Ordina_IoT
//
//  Created by Wout, Rik on 08/03/2017.
//  Copyright © 2017 Wo. All rights reserved.
//

import Foundation



class ChallengeVC: NSObject {
    
    var settingView: SettingView!
    
    init(frame: CGRect, headerText: String) {
        super.init()
        settingView = SettingView(frame: frame, headerText: headerText)
        addChallengeView()
    }
    
    
    private func addChallengeView() {
        //Work in Progress ImageView
        let imageName = "workInProgress"
        guard let workImage = UIImage(named: imageName) else   {
            print("Image was not found")
            return
        }
        
        let challengeImageView = UIImageView(image: workImage)
        challengeImageView.translatesAutoresizingMaskIntoConstraints = false
        challengeImageView.contentMode = .scaleAspectFit
        settingView.addSubview(challengeImageView)
        
        let heightConstant = settingView.frame.width/3.5
        let yConstant = settingView.frame.height/25
        challengeImageView.widthAnchor.constraint(equalToConstant: heightConstant).isActive = true
        challengeImageView.heightAnchor.constraint(equalToConstant: heightConstant).isActive = true
        challengeImageView.centerYAnchor.constraint(equalTo: settingView.centerYAnchor, constant: yConstant).isActive = true
        challengeImageView.centerXAnchor.constraint(equalTo: settingView.centerXAnchor).isActive = true
        
        //Challenge III button
        let challenge3Button = UIButton()
        challenge3Button.translatesAutoresizingMaskIntoConstraints = false
        challenge3Button.setTitle("Challenge III", for: .normal)
        challenge3Button.setTitleColor(UIColor.defaultButtonColor, for: .normal)
        challenge3Button.setTitleColor(UIColor.defaultButtonColor.withAlphaComponent(0.25), for: .highlighted)
        challenge3Button.titleLabel?.font = UIFont(name: "Avenir Next", size: 25.0)!
                challenge3Button.sizeToFit()
        challenge3Button.addTarget(self, action: #selector(challenge3ButtonWasPressed(sender:)), for: .touchUpInside)
        settingView.addSubview(challenge3Button)
        
        challenge3Button.topAnchor.constraint(equalTo: challengeImageView.bottomAnchor, constant: 5.0).isActive = true
        challenge3Button.centerXAnchor.constraint(equalTo: settingView.centerXAnchor).isActive = true
        
        //Challenge IV button
        let challenge4Button = UIButton()
        challenge4Button.translatesAutoresizingMaskIntoConstraints = false
        challenge4Button.setTitle("Challenge IV", for: .normal)
        challenge4Button.setTitleColor(UIColor.defaultButtonColor, for: .normal)
        challenge4Button.setTitleColor(UIColor.defaultButtonColor.withAlphaComponent(0.25), for: .highlighted)
        challenge4Button.titleLabel?.font = UIFont(name: "Avenir Next", size: 25.0)!
        challenge4Button.sizeToFit()
        challenge4Button.addTarget(self, action: #selector(challenge4ButtonWasPressed(sender:)), for: .touchUpInside)
        settingView.addSubview(challenge4Button)
        
        challenge4Button.topAnchor.constraint(equalTo: challenge3Button.bottomAnchor, constant: 2).isActive = true
        challenge4Button.centerXAnchor.constraint(equalTo: settingView.centerXAnchor).isActive = true
        
        
    }
    
    func challenge3ButtonWasPressed(sender: UIButton)    {
        print("challenge3")
    }
    
    func challenge4ButtonWasPressed(sender: UIButton)    {
        print("challenge4")
    }
    
    
    
}
