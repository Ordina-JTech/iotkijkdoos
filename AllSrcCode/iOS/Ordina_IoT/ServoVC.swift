//
//  ServoVC.swift
//  Ordina_IoT
//
//  Created by Rik Wout on 16-02-17.
//  Copyright © 2017 Wo. All rights reserved.
//

import Foundation


class ServoVC: NSObject {
    

//Properties
    var settingView: SettingView!
    private var slider: UISlider!
    private var previousValue: Float = 0
    private var angleLabel: UILabel!
    private var imageView: UIImageView!
    
    
//Constructor.
    init(frame: CGRect, title: String) {
        super.init()
        
        settingView = SettingView(frame: frame, title: title)
        addServoView()
    }
    
    
    //Add the servo view
    private func addServoView() {
    
    //ImageView
        
        //Creation
        let imageName = "Tv"
        let image = UIImage(named: imageName)
        imageView = UIImageView(image: image)
        
        settingView.addSubview(imageView)
        
        //Constraints
        let heightConstant = settingView.frame.width/2.5
        imageView.widthAnchor.constraint(equalToConstant: heightConstant).isActive = true
        imageView.heightAnchor.constraint(equalToConstant: heightConstant).isActive = true
        
        let yConstant = settingView.frame.height/12.5   //Ratio (30.0 iPhone 7)
        imageView.centerYAnchor.constraint(equalTo: settingView.centerYAnchor, constant: yConstant).isActive = true
        imageView.centerXAnchor.constraint(equalTo: settingView.centerXAnchor).isActive = true
        
        //Properties
        imageView.translatesAutoresizingMaskIntoConstraints = false
        imageView.contentMode = .scaleAspectFit

    //Slider

        //Creation
        slider = UISlider()
        settingView.addSubview(slider)
        
        //Constraints
        let topConstant = settingView.frame.height/12.5 //Ratio (30.0 iPhone 7)
        slider.topAnchor.constraint(equalTo: imageView.bottomAnchor, constant: topConstant).isActive = true
        
        let leftRightConstant = settingView.frame.height/15.0 //Ratio (25.0 iPhone 7)
        slider.leftAnchor.constraint(equalTo: settingView.leftAnchor, constant: leftRightConstant).isActive = true
        slider.rightAnchor.constraint(equalTo: settingView.rightAnchor, constant: -leftRightConstant).isActive = true
        
        //Properties
        slider.translatesAutoresizingMaskIntoConstraints = false
        slider.value = 0
        slider.minimumValue = 0
        slider.maximumValue = 180
        slider.addTarget(self, action: #selector(sliderValueChanged), for: UIControlEvents.valueChanged)
        
    //Label
        
        //Creation
        angleLabel = UILabel()
        settingView.addSubview(angleLabel)
        
        //Constraints
        angleLabel.rightAnchor.constraint(equalTo: slider.rightAnchor).isActive = true
        angleLabel.bottomAnchor.constraint(equalTo: slider.topAnchor, constant: -5.0).isActive = true
        
        //Properties
        angleLabel.translatesAutoresizingMaskIntoConstraints = false
        angleLabel.text = "0°"
        angleLabel.font = UIFont(name: "Avenir Next", size: 20.0)
        angleLabel.textColor = UIColor.black
        angleLabel.sizeToFit()
    }
    
    
    //If user did change slider value.
    func sliderValueChanged()   {
        
        //Change servo angle every 5 degree if the previous slider value is not equal to the current value.
        if blue.isReady{
            if (Int(slider.value) % 5 == 0) && (Int(slider.value) != Int(previousValue))  {
                
                //Convert slider value to radians and make transformation
                let sliderValue = slider.value
                let radianAngle = sliderValue * .pi / 180
                imageView.transform = CGAffineTransform(rotationAngle: CGFloat(radianAngle))
                blue.sendMessage(string: "g\(Int(sliderValue))\n")
                angleLabel.text = "\(String((Int(sliderValue))))°"
                previousValue = slider.value
            }
        }
    }
}
