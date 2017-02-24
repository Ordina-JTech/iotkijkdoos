//
//  RgbVC.swift
//  Ordina_IoT
//
//  Created by Rik Wout on 13-02-17.
//  Copyright © 2017 Wo. All rights reserved.
//

import Foundation

enum Color {
    static let White = UIColor.white
    static let Red = UIColor.red.withAlphaComponent(0.75)
    static let Yellow = UIColor.yellow.withAlphaComponent(0.75)
    static let Green = UIColor.green.withAlphaComponent(0.75)
    static var Aqua = UIColor(red: 153/255, green: 241/255, blue: 250/255, alpha: 1.0)
    static let Blue = UIColor.blue.withAlphaComponent(0.75)
    static let Purple = UIColor.purple.withAlphaComponent(0.75)
    
    static let allColors = [White, Red, Yellow, Green, Aqua, Blue, Purple]
}

enum Slider {
    static let Val0 = 0
    static let Val1 = 50
    static let Val2 = 100
    static let Val3 = 150
    static let Val4 = 200
    static let Val5 = 250
    static let Val6 = 300
    
    static let allValues = [Val0, Val1, Val2, Val3, Val4, Val5, Val6]
}


class RgbVC: NSObject    {
    
    var settingView: SettingView!
    private var rgbLabel: UILabel!
    private var rgbSlider: UISlider!
    private var rgbLetter: String!
    private var previousIndex = 0


    init(frame: CGRect, headerText: String, rgbLetter: String) {
        super.init()
        
        settingView = SettingView(frame: frame, headerText: headerText)
        self.rgbLetter = rgbLetter
        addRgbView()
    }
    
    
    private func addRgbView()    {
        
        //Label
        rgbLabel = UILabel()
        rgbLabel.translatesAutoresizingMaskIntoConstraints = false
        rgbLabel.layer.masksToBounds = true
        rgbLabel.layer.borderWidth = 2
        rgbLabel.layer.borderColor = UIColor.lightGray.cgColor
        rgbLabel.backgroundColor = UIColor.white
        
        settingView.addSubview(rgbLabel)
        
        let heightConstant = settingView.frame.width/4
        rgbLabel.heightAnchor.constraint(equalToConstant: heightConstant).isActive = true
        rgbLabel.widthAnchor.constraint(equalToConstant: heightConstant).isActive = true
        rgbLabel.centerXAnchor.constraint(equalTo: settingView.centerXAnchor).isActive = true
        let yConstant = settingView.frame.height/12.5
        rgbLabel.centerYAnchor.constraint(equalTo: settingView.centerYAnchor, constant: yConstant).isActive = true
        
        rgbLabel.layer.cornerRadius = heightConstant/2
        
        //Slider
        rgbSlider = UISlider()
        rgbSlider.translatesAutoresizingMaskIntoConstraints = false
        rgbSlider.value = 0
        rgbSlider.minimumValue = 0
        rgbSlider.maximumValue = 300
        rgbSlider.addTarget(self, action: #selector(sliderValueChanged), for: UIControlEvents.valueChanged)
        
        settingView.addSubview(rgbSlider)
        
        let topConstant = settingView.frame.height/12.5
        rgbSlider.topAnchor.constraint(equalTo: rgbLabel.bottomAnchor, constant: topConstant).isActive = true
        let leftRightConstant = settingView.frame.height/15.0
        rgbSlider.leftAnchor.constraint(equalTo: settingView.leftAnchor, constant: leftRightConstant).isActive = true
        rgbSlider.rightAnchor.constraint(equalTo: settingView.rightAnchor, constant: -leftRightConstant).isActive = true
    }

    
    func sliderValueChanged(sender: UISlider) {

        let sliderValue = Int(sender.value)
        
        //TODO: kijken if init(rawValue werkt) geen kabel bij.
        for index in 0..<Slider.allValues.count   {

            if sliderValue <= Slider.allValues[index]  {
                
                if previousIndex != index {
                    previousIndex = index
                    let message = rgbLetter + "\(index)"    //Index (0-6) -> white, red, yellow, green, aqua, blue, pruple
                    bluetooth.sendMessage(string: message)
                    setComponentsColor(color: Color.allColors[index])
                }
                
                break
            }
        }
    }
    

    private func setComponentsColor(color: UIColor) {
        rgbLabel.backgroundColor = color
        rgbSlider.minimumTrackTintColor = color
    }
}
