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
    private let fontSize: CGFloat = 22.0
    
    enum ChallengeName  {
        static let colorGradient = "Fade Color"
        static let challenge4 = "Special Effect"
    }
    
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
        let colorGradientButton = UIButton()
        addButtonProperties(button: colorGradientButton, buttonTitle: ChallengeName.colorGradient)
        colorGradientButton.addTarget(self, action: #selector(challenge3ButtonWasPressed(sender:)), for: .touchUpInside)
        settingView.addSubview(colorGradientButton)
        
        colorGradientButton.topAnchor.constraint(equalTo: challengeImageView.bottomAnchor, constant: 5.0).isActive = true
        colorGradientButton.centerXAnchor.constraint(equalTo: settingView.centerXAnchor).isActive = true
        
        //Challenge IV button
        let challenge4Button = UIButton()
        addButtonProperties(button: challenge4Button, buttonTitle: ChallengeName.challenge4)
        challenge4Button.addTarget(self, action: #selector(challenge4ButtonWasPressed(sender:)), for: .touchUpInside)
        settingView.addSubview(challenge4Button)
        
        challenge4Button.topAnchor.constraint(equalTo: colorGradientButton.bottomAnchor, constant: 2).isActive = true
        challenge4Button.centerXAnchor.constraint(equalTo: settingView.centerXAnchor).isActive = true
    }
    
    private func addButtonProperties(button: UIButton, buttonTitle: String)  {
        button.translatesAutoresizingMaskIntoConstraints = false
        button.setTitle(buttonTitle, for: .normal)
        button.setTitleColor(UIColor.defaultButtonColor, for: .normal)
        button.setTitleColor(UIColor.defaultButtonColor.withAlphaComponent(0.25), for: .highlighted)
        button.titleLabel?.font = UIFont(name: "Avenir Next", size: fontSize)!
        button.sizeToFit()
    }
    
    func challenge3ButtonWasPressed(sender: UIButton)    {
        bluetooth.sendMessage(string: PeripheralLetter.fadeColor)
    }
    
    func challenge4ButtonWasPressed(sender: UIButton)    {
        bluetooth.sendMessage(string: PeripheralLetter.challenge4)
    }
}
