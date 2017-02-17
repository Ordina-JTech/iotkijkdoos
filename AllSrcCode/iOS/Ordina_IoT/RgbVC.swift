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
    
    private let white = UIColor.white
    private let red = UIColor.red.withAlphaComponent(0.75)
    private let yellow = UIColor.yellow.withAlphaComponent(0.75)
    private let green = UIColor.green.withAlphaComponent(0.75)
    private let aqua = UIColor(red: 153/255, green: 241/255, blue: 250/255, alpha: 1.0)
    private let blueColor = UIColor.blue.withAlphaComponent(0.75)
    private let purple = UIColor.purple.withAlphaComponent(0.75)
    
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
                blue.sendMessage(string: "c0")
                setRgbColor(color: white)
            }
        //Red
        case 1..<51:
            if currentRgbColor != .red  {
                currentRgbColor = .red
                blue.sendMessage(string: "c1")
                setRgbColor(color: red)
            }
        //Yellow
        case 51..<101:
            if currentRgbColor != .yellow   {
                currentRgbColor = .yellow
                blue.sendMessage(string: "c2")
                setRgbColor(color: yellow)
            }
        //Green
        case 101..<151:
            if currentRgbColor != .green    {
                currentRgbColor = .green
                blue.sendMessage(string: "c3")
                setRgbColor(color: green)
            }
        //Aqua
        case 151..<201:
            if currentRgbColor != .aqua {
                currentRgbColor = .aqua
                blue.sendMessage(string: "c4")
                setRgbColor(color: aqua)
            }
        //Blue
        case 201..<251:
            if currentRgbColor != .blue {
                currentRgbColor = .blue
                blue.sendMessage(string: "c5")
                setRgbColor(color: blueColor)
            }
        //Purple
        case 251..<301:
            if currentRgbColor != .purple   {
                currentRgbColor = .purple
                blue.sendMessage(string: "c6")
                setRgbColor(color: purple)
     
            }
        default:
            break
        }
    }
    
    //Set the rgbLabel's background color en rgb slider's track tint color.
    private func setRgbColor(color: UIColor) {
        rgbLabel.backgroundColor = color
        rgbSlider.minimumTrackTintColor = color
        
    }
}
