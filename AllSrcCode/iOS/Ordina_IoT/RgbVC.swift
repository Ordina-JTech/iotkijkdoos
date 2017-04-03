//
//  RgbVC.swift
//  Ordina_IoT
//
//  Created by Rik Wout on 13-02-17.
//  Copyright © 2017 Wo. All rights reserved.
//

import Foundation


class RgbVC: NSObject    {
    
    private enum ImageName {
        static let discobal = "discobal-1"
        static let slider = "rgbBalk"
    }
    private enum ColorMessage{
        static let rgbLetter = PeripheralLetter.rgbLed
        static let off =    rgbLetter + "0"
        static let red =    rgbLetter + "1"
        static let yellow = rgbLetter + "2"
        static let green =  rgbLetter + "3"
        static let aqua =   rgbLetter + "4"
        static let blue =   rgbLetter + "5"
        static let purple = rgbLetter + "6"
        
        static let colors = [red, yellow, green, aqua, blue, purple]
    }
    private enum Slider {
        static let beginValue: Float = 0
        static let endValue: Float = 300
        static let redVal = 60
        static let yellowVal = 100
        static let greenVal = 170
        static let aquaVal = 210
        static let blueVal = 285
        static let purpleVal = 300
        
        static let values = [redVal, yellowVal, greenVal, aquaVal, blueVal, purpleVal]
    }
    private enum SwitchOnTintColor {
        static let aqua = UIColor.init(red: 0, green: 255, blue: 255, alpha: 1)
        static let all = [UIColor.red, UIColor.yellow, UIColor.green, SwitchOnTintColor.aqua, UIColor.blue, UIColor.purple]
    }
    
    
    private enum StateBtnText{
        static let on = "Turn Light On"
        static let off = "Turn Light Off"
    }
    
    private(set) var settingView: SettingView!
    private var rgbLabel: UILabel!
    private var rgbSlider: UISlider!
    private var imageView: UIImageView!
    private var switchBtn: UISwitch!
    private var colorMessage = [String]()
    private var previousIndex = 0

    init(frame: CGRect, headerText: String) {
        super.init()
        settingView = SettingView(frame: frame, headerText: headerText)
        addRgbView()
    }
    
    private func addRgbView()    {
        //Discobal ImageView
        guard let discoImage = UIImage(named: ImageName.discobal) else   {
            print("Image was not found")
            return
        }
        
        let discoImageView = UIImageView(image: discoImage)
        discoImageView.translatesAutoresizingMaskIntoConstraints = false
        discoImageView.contentMode = .scaleAspectFit
        settingView.addSubview(discoImageView)
        
        var heightConstant = settingView.frame.width/3.5
        let yConstant = settingView.frame.height/12.5
        discoImageView.widthAnchor.constraint(equalToConstant: heightConstant).isActive = true
        discoImageView.heightAnchor.constraint(equalToConstant: heightConstant).isActive = true
        discoImageView.centerYAnchor.constraint(equalTo: settingView.centerYAnchor, constant: yConstant).isActive = true
        discoImageView.centerXAnchor.constraint(equalTo: settingView.centerXAnchor).isActive = true
        
        //RGB Led ImageView
        guard let rgbImage = UIImage(named: ImageName.slider) else  {
            print("Image was not found")
            return
        }
        
        let rgbImageView = UIImageView(image: rgbImage)
        rgbImageView.translatesAutoresizingMaskIntoConstraints = false
        rgbImageView.clipsToBounds = true
        rgbImageView.layer.cornerRadius = 5
        settingView.addSubview(rgbImageView)
    
        heightConstant = 30
        let topConstant = settingView.frame.height/18
        let leftRightConstant: CGFloat = settingView.frame.height/15
        rgbImageView.topAnchor.constraint(equalTo: discoImageView.bottomAnchor, constant: topConstant).isActive = true
        rgbImageView.leftAnchor.constraint(equalTo: settingView.leftAnchor, constant: leftRightConstant).isActive = true
        rgbImageView.rightAnchor.constraint(equalTo: settingView.rightAnchor, constant: -leftRightConstant).isActive = true
        rgbImageView.heightAnchor.constraint(equalToConstant: heightConstant).isActive = true
        
        //Slider
        rgbSlider = UISlider()
        rgbSlider.translatesAutoresizingMaskIntoConstraints = false
        rgbSlider.value = 0
        rgbSlider.minimumValue = Slider.beginValue
        rgbSlider.maximumValue = Slider.endValue
        rgbSlider.minimumTrackTintColor = UIColor.clear
        rgbSlider.maximumTrackTintColor = UIColor.clear
        rgbSlider.thumbTintColor = UIColor.black.withAlphaComponent(0.7)
        rgbSlider.addTarget(self, action: #selector(sliderValueChanged), for: UIControlEvents.valueChanged)
        settingView.addSubview(rgbSlider)
        
        rgbSlider.topAnchor.constraint(equalTo: rgbImageView.topAnchor).isActive = true
        rgbSlider.leftAnchor.constraint(equalTo: settingView.leftAnchor, constant: leftRightConstant).isActive = true
        rgbSlider.rightAnchor.constraint(equalTo: settingView.rightAnchor, constant: -leftRightConstant).isActive = true
        
        //Switch Button
        switchBtn = UISwitch()
        switchBtn.translatesAutoresizingMaskIntoConstraints = false
        switchBtn.thumbTintColor = UIColor.lightGray
        switchBtn.tintColor = UIColor.lightGray
        switchBtn.onTintColor = UIColor.orange
        switchBtn.addTarget(self, action: #selector(switchStateDidChange), for: .valueChanged)
        settingView.addSubview(switchBtn)
        
        switchBtn.topAnchor.constraint(equalTo: rgbImageView.bottomAnchor, constant: 12.5).isActive = true
        switchBtn.centerXAnchor.constraint(equalTo: settingView.centerXAnchor).isActive = true
    }
    
    func switchStateDidChange() {
        if switchBtn.isOn {
            getAndSendColorMessage(isButtonCall: true)
        }
        else {
            bluetooth.sendMessage(string: ColorMessage.off)
        }
    }
    
    func sliderValueChanged(sender: UISlider) {
        getAndSendColorMessage()
    }
    
    private func getAndSendColorMessage(isButtonCall: Bool = false)   {
        if switchBtn.isOn  {
            let sliderValue = Int(rgbSlider.value)
            for index in 0..<Slider.values.count   {
                if sliderValue <= Slider.values[index]  {
                    if previousIndex != index || isButtonCall {
                        previousIndex = index
                        setSwitchOntTinColor(index: index)
                        let message = ColorMessage.colors[index]
                        bluetooth.sendMessage(string: message)
                    }
                    break
                }
            }
        }
    }
    
    private func setSwitchOntTinColor(index: Int) {
        switchBtn.onTintColor = SwitchOnTintColor.all[index].withAlphaComponent(0.8)
        
    }
}
