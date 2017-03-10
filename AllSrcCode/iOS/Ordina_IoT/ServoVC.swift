//
//  ServoVC.swift
//  Ordina_IoT
//
//  Created by Rik Wout on 16-02-17.
//  Copyright © 2017 Wo. All rights reserved.
//

import Foundation

class ServoVC: NSObject {
    
    private enum ImageName  {
        static let tv = "Tv"
    }
    
    private(set) var settingView: SettingView!
    private var slider: UISlider!
    private var previousValue: Int = 0
    private var angleLabel: UILabel!
    private var imageView: UIImageView!
    
    init(frame: CGRect, headerText: String) {
        super.init()
        settingView = SettingView(frame: frame, headerText: headerText)
        addServoView()
    }
    
    private func addServoView() {
        //TV Image
        guard let image = UIImage(named: ImageName.tv) else    {
            print("Image was not found")
            return
        }
        
        imageView = UIImageView(image: image)
        imageView.translatesAutoresizingMaskIntoConstraints = false
        imageView.contentMode = .scaleAspectFit
        settingView.addSubview(imageView)
        
        let heightConstant = settingView.frame.width/2.5
        let yConstant = settingView.frame.height/12.5
        imageView.widthAnchor.constraint(equalToConstant: heightConstant).isActive = true
        imageView.heightAnchor.constraint(equalToConstant: heightConstant).isActive = true
        imageView.centerYAnchor.constraint(equalTo: settingView.centerYAnchor, constant: yConstant).isActive = true
        imageView.centerXAnchor.constraint(equalTo: settingView.centerXAnchor).isActive = true
        
        //Angle Slider
        slider = UISlider()
        slider.translatesAutoresizingMaskIntoConstraints = false
        slider.value = 0
        slider.minimumValue = 0
        slider.maximumValue = 180
        slider.thumbTintColor = UIColor.lightGray
        slider.minimumTrackTintColor = UIColor.orange
        slider.addTarget(self, action: #selector(sliderValueChanged), for: UIControlEvents.valueChanged)
        settingView.addSubview(slider)
        
        let topConstant = settingView.frame.height/12.5
        let leftRightConstant = settingView.frame.height/15.0
        slider.topAnchor.constraint(equalTo: imageView.bottomAnchor, constant: topConstant).isActive = true
        slider.leftAnchor.constraint(equalTo: settingView.leftAnchor, constant: leftRightConstant).isActive = true
        slider.rightAnchor.constraint(equalTo: settingView.rightAnchor, constant: -leftRightConstant).isActive = true
        
        //Angle Label
        angleLabel = UILabel()
        angleLabel.translatesAutoresizingMaskIntoConstraints = false
        angleLabel.text = String(Int(slider.value)) + "°"
        angleLabel.font = UIFont.avenirNext(size: 20)
        angleLabel.textColor = UIColor.black
        angleLabel.sizeToFit()
        settingView.addSubview(angleLabel)
        
        angleLabel.rightAnchor.constraint(equalTo: slider.rightAnchor).isActive = true
        angleLabel.bottomAnchor.constraint(equalTo: slider.topAnchor, constant: -5.0).isActive = true
    }
    
    func sliderValueChanged()   {
        var sliderValue = Int(slider.value)
        
        if sliderValue % 5 == 0 && sliderValue != previousValue || sliderValue % 8 == 0 && sliderValue != previousValue  {
            previousValue = sliderValue
            rotateImage(value: slider.value)
            angleLabel.text = "\(sliderValue)°"
            
            if sliderValue == 180   {
                sliderValue -= 1
            }
            let message = PeripheralLetter.servo + String(sliderValue) + "\n"
            bluetooth.sendMessage(string: message)
        }
    }
    
    private func rotateImage(value: Float)  {
        let radianAngle = value * .pi / 180
        imageView.transform = CGAffineTransform(rotationAngle: CGFloat(radianAngle))
    }
}
