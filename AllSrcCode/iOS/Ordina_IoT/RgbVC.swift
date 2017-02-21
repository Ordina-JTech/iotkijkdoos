//
//  RgbVC.swift
//  Ordina_IoT
//
//  Created by Rik Wout on 13-02-17.
//  Copyright © 2017 Wo. All rights reserved.
//

import Foundation


class RgbVC: NSObject    {
    
//Properties.
    var settingView: SettingView!
    private var rgbLabel: UILabel!
    private var rgbSlider: UISlider!
    
    private let white = UIColor.white
    private let red = UIColor.red.withAlphaComponent(0.75)
    private let yellow = UIColor.yellow.withAlphaComponent(0.75)
    private let green = UIColor.green.withAlphaComponent(0.75)
    private let aqua = UIColor(red: 153/255, green: 241/255, blue: 250/255, alpha: 1.0)
    private let blueColor = UIColor.blue.withAlphaComponent(0.75)
    private let purple = UIColor.purple.withAlphaComponent(0.75)
    
    private var colorArray: [UIColor] = []
    private let messageArray = ["c0", "c1", "c2", "c3", "c4", "c5", "c6"]
    private var valueArray: [Int] = [0, 50, 100, 150, 200, 250, 300]
    private var currentColor: UIColor = UIColor.white


//Constructor.
    init(frame: CGRect) {
        super.init()
        settingView = SettingView(frame: frame, title: "Control Rgb light")
        addRgbView()
        
        //Add all the colors to the colorArray
        colorArray = [white, red, yellow, green, aqua, blueColor, purple]
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

        let sliderValue = Int(sender.value)
        
        for index in 0..<valueArray.count   {
            if sliderValue <= valueArray[index]  {
                
                if currentColor != colorArray[index] {
                    currentColor = colorArray[index]
                    blue.sendMessage(string: messageArray[index])
                    setRgbColor(color: colorArray[index])
                }
                
                break
            }
        }
    }
    
    //Set the rgbLabel's background color en rgb slider's track tint color.
    private func setRgbColor(color: UIColor) {
        rgbLabel.backgroundColor = color
        rgbSlider.minimumTrackTintColor = color
    }
}
