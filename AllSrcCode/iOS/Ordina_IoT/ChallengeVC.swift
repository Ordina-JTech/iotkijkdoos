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
        static let colorFade = "Fade Color"
        static let challenge4 = "Special Effect"
    }
    
    private enum ImageName  {
        static let challenge = "challenge"
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
        
        //Challenge III button
        let colorFadingButton = UIButton()
        addButtonProperties(button: colorFadingButton, buttonTitle: ChallengeName.colorFade)
        colorFadingButton.addTarget(self, action: #selector(colorFadingButtonWasPressed(sender:)), for: .touchUpInside)
        settingView.addSubview(colorFadingButton)
        
        colorFadingButton.topAnchor.constraint(equalTo: challengeImageView.bottomAnchor, constant: 5.0).isActive = true
        colorFadingButton.centerXAnchor.constraint(equalTo: settingView.centerXAnchor).isActive = true
        
        //Challenge IV button
        let challenge4Button = UIButton()
        addButtonProperties(button: challenge4Button, buttonTitle: ChallengeName.challenge4)
        challenge4Button.addTarget(self, action: #selector(challenge4ButtonWasPressed(sender:)), for: .touchUpInside)
        settingView.addSubview(challenge4Button)
        
        challenge4Button.topAnchor.constraint(equalTo: colorFadingButton.bottomAnchor, constant: 2).isActive = true
        challenge4Button.centerXAnchor.constraint(equalTo: settingView.centerXAnchor).isActive = true
    }
    
    private func addButtonProperties(button: UIButton, buttonTitle: String)  {
        button.translatesAutoresizingMaskIntoConstraints = false
        button.setTitle(buttonTitle, for: .normal)
        button.setTitleColor(UIColor.defaultButtonColor, for: .normal)
        button.setTitleColor(UIColor.defaultButtonColor.withAlphaComponent(0.25), for: .highlighted)
        button.titleLabel?.font = UIFont.avenirNext(size: 22)
        button.sizeToFit()
    }
    
    func colorFadingButtonWasPressed(sender: UIButton)    {
        bluetooth.sendMessage(string: PeripheralLetter.fadeColor)
    }
    
    func challenge4ButtonWasPressed(sender: UIButton)    {
        bluetooth.sendMessage(string: PeripheralLetter.challenge4)
    }
}
