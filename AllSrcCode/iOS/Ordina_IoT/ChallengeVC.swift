//
//  ChallengeVC.swift
//  Ordina_IoT
//
//  Created by Wout, Rik on 08/03/2017.
//  Copyright © 2017 Wo. All rights reserved.
//

import Foundation

class ChallengeVC: NSObject {
    
    private enum ChallengeName  {
        static let gradient = "Gradient"
        static let specialEffect = "Special Effect"
    }
    
    private enum ImageName  {
        static let challenge = "hamer-jtech1"
    }
    
    private(set) var settingView: SettingView!
    
    init(frame: CGRect, headerText: String) {
        super.init()
        settingView = SettingView(frame: frame, headerText: headerText)
        addChallengeView()
    }
    
    private func addChallengeView() {
        //Challenge ImageView
        guard let challengeImage = UIImage(named: ImageName.challenge) else   {
            print("Image was not found")
            return
        }
        
        let challengeImageView = UIImageView(image: challengeImage)
        challengeImageView.translatesAutoresizingMaskIntoConstraints = false
        challengeImageView.contentMode = .scaleAspectFit
        settingView.addSubview(challengeImageView)
        
        let heightConstant = settingView.frame.width/3.5
        let yConstant = settingView.frame.height/25
        challengeImageView.widthAnchor.constraint(equalToConstant: heightConstant).isActive = true
        challengeImageView.heightAnchor.constraint(equalToConstant: heightConstant).isActive = true
        challengeImageView.centerYAnchor.constraint(equalTo: settingView.centerYAnchor, constant: yConstant).isActive = true
        challengeImageView.centerXAnchor.constraint(equalTo: settingView.centerXAnchor).isActive = true
        
        //Gradient challenge button
        let gradientButton = UIButton()
        addButtonProperties(button: gradientButton, buttonTitle: ChallengeName.gradient)
        gradientButton.addTarget(self, action: #selector(gradientButtonWasPressed(sender:)), for: .touchUpInside)
        settingView.addSubview(gradientButton)
        
        gradientButton.topAnchor.constraint(equalTo: challengeImageView.bottomAnchor, constant: 10.0).isActive = true
        gradientButton.centerXAnchor.constraint(equalTo: settingView.centerXAnchor).isActive = true
        
        //Special effect challenge button
        let specialEffectButton = UIButton()
        addButtonProperties(button: specialEffectButton, buttonTitle: ChallengeName.specialEffect)
        specialEffectButton.addTarget(self, action: #selector(specialEffectButtonWasPressed(sender:)), for: .touchUpInside)
        settingView.addSubview(specialEffectButton)
        specialEffectButton.topAnchor.constraint(equalTo: gradientButton.bottomAnchor, constant: 10).isActive = true
        specialEffectButton.centerXAnchor.constraint(equalTo: settingView.centerXAnchor).isActive = true
        
        if UI_USER_INTERFACE_IDIOM() == UIUserInterfaceIdiom.pad {
            specialEffectButton.widthAnchor.constraint(equalTo: challengeImageView.widthAnchor, multiplier: 1).isActive = true
        }
        
        gradientButton.widthAnchor.constraint(equalTo: specialEffectButton.widthAnchor, multiplier: 1).isActive = true
    }
    
    private func addButtonProperties(button: UIButton, buttonTitle: String)  {
        button.translatesAutoresizingMaskIntoConstraints = false
        button.setTitle(buttonTitle, for: .normal)
        button.setTitleColor(UIColor.darkGray, for: .normal)
        button.setTitleColor(UIColor.darkGray.withAlphaComponent(0.25), for: .highlighted)
        button.titleLabel?.font = UIFont.avenirNext(size: 18)
        button.layer.cornerRadius = 10
        button.layer.borderWidth = 1
        button.layer.borderColor = UIColor.darkGray.cgColor
        button.contentEdgeInsets = UIEdgeInsetsMake(8, 8, 8, 8)
        button.sizeToFit()
    }
    
    func gradientButtonWasPressed(sender: UIButton)    {
        bluetooth.sendMessage(string: PeripheralLetter.gradient)
    }
    
    func specialEffectButtonWasPressed(sender: UIButton)    {
        bluetooth.sendMessage(string: PeripheralLetter.specialEffect)
    }
}
