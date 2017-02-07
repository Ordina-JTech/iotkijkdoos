//
//  Page1VC.swift
//  Ordina IoT
//
//  Created by Rik Wout on 24-01-17.
//  Copyright © 2017 Wo. All rights reserved.
//

import UIKit



class Page1VC: UIViewController {

    
    @IBOutlet weak var led1Switch: UISwitch!
    @IBOutlet weak var led2Switch: UISwitch!
    @IBOutlet weak var led1ImageView: UIImageView!
    @IBOutlet weak var led2ImageView: UIImageView!
    @IBOutlet weak var redSlider: UISlider!

    
    
    //View did load
    override func viewDidLoad() {
        super.viewDidLoad()

        //Resize switch buttons
        led1Switch.transform = CGAffineTransform(scaleX: 1.25, y: 1.25)
        led2Switch.transform = CGAffineTransform(scaleX: 1.25, y: 1.25)
        redSlider.transform = CGAffineTransform(rotationAngle: CGFloat(-M_PI_2))
        print(redSlider.frame.origin.x)
        
        
    }
    
    
    //TODO: naam veranderen, nu niet duideijk welke het is...s
    //State changed switch led 1
    @IBAction func led1Switch(_ sender: Any) {
    
        if blue.isReady {
            blue.sendMessage(string: "a")
        }
        else {
            print("not connected")
        }
    }
    
    //State changed switch led 2
    @IBAction func led2Switch(_ sender: Any) {
        print("Led 2 is on: \(led2Switch.isOn)")
        
        if blue.isReady {
            blue.sendMessage(string: "b")
        }
        else    {
            print("Not connected")
        }
    }
    

    
    @IBAction func sliderChangedValue(_ sender: Any) {
        
        if blue.isReady {
            changeColor(value: redSlider.value)
        }
        else {
            redSlider.setValue(0, animated: false)
        }
    }
    
    
    
    
    
    
    @IBAction func sliderTouchedUpInside(_ sender: Any) {
        //changeColor(value: redSlider.value)
        
    }

    @IBAction func sliderTouchUpOutside(_ sender: Any) {
        //changeColor(value: redSlider.value)
    }

    private func changeColor(value: Float)  {
        
        if value == 0   {
            blue.sendMessage(string: "c000\n000\n000")
        }
        else if value > 1 && value <= 50  {
            blue.sendMessage(string: "c255\n000\n000")
            
        }
        else if value > 50 && value <= 100  {
            blue.sendMessage(string: "c255\n255\n000")
            
        }
        else if value > 100 && value <= 185 {
            blue.sendMessage(string: "c000\n255\n000")
        }
        else    {
            blue.sendMessage(string: "c000\n000\n255")
        }
    }
    
}
