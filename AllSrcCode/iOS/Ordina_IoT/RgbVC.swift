//
//  RgbVC.swift
//  Ordina_IoT
//
//  Created by Rik Wout on 13-02-17.
//  Copyright © 2017 Wo. All rights reserved.
//

import Foundation


//TODO:     - Value did change refactoren


//Enum to check the current color of rgb led
enum RgbColor   {
    case white
    case red
    case yellow
    case green
    case aqua
    case blue
    case purple
}


class RgbVC: NSObject    {
    
//Properties.
    var settingView: SettingView!
    private var rgbLabel: UILabel!
    private var currentRgbColor: RgbColor = RgbColor.white
    private var rgbSlider: UISlider!
    
    
//Constructor.
    init(frame: CGRect) {
        super.init()
        settingView = SettingView(frame: frame, title: "Control Rgb light")
        addRgbView()
    }
    
    
    //Add the view for rgb light.
    private func addRgbView()    {
        
    //Label
        
        //Creation
        rgbLabel = UILabel()
        settingView.addSubview(rgbLabel)
        
        //Constraints
        let heightConstant = settingView.frame.width/4
        rgbLabel.heightAnchor.constraint(equalToConstant: heightConstant).isActive = true
        rgbLabel.widthAnchor.constraint(equalToConstant: heightConstant).isActive = true
        rgbLabel.centerXAnchor.constraint(equalTo: settingView.centerXAnchor).isActive = true
        
        let yConstant = settingView.frame.height/12.5   //Ratio (30.0 iPhone 7)
        rgbLabel.centerYAnchor.constraint(equalTo: settingView.centerYAnchor, constant: yConstant).isActive = true
        
        //Properties
        rgbLabel.translatesAutoresizingMaskIntoConstraints = false
        rgbLabel.layer.masksToBounds = true
        rgbLabel.layer.cornerRadius = heightConstant/2
        rgbLabel.layer.borderWidth = 2
        rgbLabel.layer.borderColor = UIColor.lightGray.cgColor
        rgbLabel.backgroundColor = UIColor.white
        
    //Slider
        
        //Creation
        rgbSlider = UISlider()
        settingView.addSubview(rgbSlider)
        
        //Constraints
        let topConstant = settingView.frame.height/12.5 //Ratio (30.0 iPhone 7)
        rgbSlider.topAnchor.constraint(equalTo: rgbLabel.bottomAnchor, constant: topConstant).isActive = true
        
        let leftRightConstant = settingView.frame.height/15.0 //Ratio (25.0 iPhone 7)
        rgbSlider.leftAnchor.constraint(equalTo: settingView.leftAnchor, constant: leftRightConstant).isActive = true
        rgbSlider.rightAnchor.constraint(equalTo: settingView.rightAnchor, constant: -leftRightConstant).isActive = true
        
        //Properties
        rgbSlider.translatesAutoresizingMaskIntoConstraints = false
        rgbSlider.value = 0
        rgbSlider.minimumValue = 0
        rgbSlider.maximumValue = 300
        rgbSlider.addTarget(self, action: #selector(sliderValueChanged), for: UIControlEvents.valueChanged)
    }

    
    //Sending the RGB values based on sliders value.
    func sliderValueChanged(sender: UISlider) {

        switch sender.value    {
            
        //White
        case 0:
            if currentRgbColor != .white    {
                currentRgbColor = .white
                blue.sendMessage(string: "c000\n000\n000")
                rgbLabel.backgroundColor = UIColor.white
            }
        //Red
        case 1..<51:
            if currentRgbColor != .red  {
                currentRgbColor = .red
                blue.sendMessage(string: "c255\n000\n000")
                rgbLabel.backgroundColor = UIColor.red.withAlphaComponent(0.75)
                rgbSlider.minimumTrackTintColor = UIColor.red.withAlphaComponent(0.75)
            }
        //Yellow
        case 51..<101:
            if currentRgbColor != .yellow   {
                currentRgbColor = .yellow
                blue.sendMessage(string: "c255\n255\n000")
                rgbLabel.backgroundColor = UIColor.yellow.withAlphaComponent(0.75)
                rgbSlider.minimumTrackTintColor = UIColor.yellow.withAlphaComponent(0.75)
            }
        //Green
        case 101..<151:
            if currentRgbColor != .green    {
                currentRgbColor = .green
                blue.sendMessage(string: "c000\n255\n000")
                rgbLabel.backgroundColor = UIColor.green.withAlphaComponent(0.75)
                rgbSlider.minimumTrackTintColor = UIColor.green.withAlphaComponent(0.75)
            }
        //Aqua
        case 151..<201:
            if currentRgbColor != .aqua {
                currentRgbColor = .aqua
                blue.sendMessage(string: "c000\n255\n255")
                rgbLabel.backgroundColor = UIColor(red: 153/255, green: 241/255, blue: 250/255, alpha: 1.0)
                rgbSlider.minimumTrackTintColor = UIColor(red: 153/255, green: 241/255, blue: 250/255, alpha: 1.0)
            }
        //Blue
        case 201..<251:
            if currentRgbColor != .blue {
                currentRgbColor = .blue
                blue.sendMessage(string: "c000\n000\n255")
                rgbLabel.backgroundColor = UIColor.blue.withAlphaComponent(0.75)
                rgbSlider.minimumTrackTintColor = UIColor.blue.withAlphaComponent(0.75)
            }
        //Purple
        case 251..<301:
            if currentRgbColor != .purple   {
                currentRgbColor = .purple
                blue.sendMessage(string: "c255\n000\n255")
                rgbLabel.backgroundColor = UIColor.purple.withAlphaComponent(0.75)
                rgbSlider.minimumTrackTintColor = UIColor.purple.withAlphaComponent(0.75)
            }
        default:
            break
        }
    }
}
