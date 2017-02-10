//
//  Page1VC.swift
//  Ordina IoT
//
//  Created by Rik Wout on 24-01-17.
//  Copyright © 2017 Wo. All rights reserved.
//

import UIKit


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



class Page1VC: UIViewController {

    //Properties
    @IBOutlet weak var led1Switch: UISwitch!
    @IBOutlet weak var led2Switch: UISwitch!
    @IBOutlet weak var led1ImageView: UIImageView!
    @IBOutlet weak var led2ImageView: UIImageView!
    @IBOutlet weak var rgbSlider: UISlider!
    @IBOutlet weak var rgbLabel: UILabel!
    
    var currentRgbColor: RgbColor!
    

    

    //View did load
    override func viewDidLoad() {
        super.viewDidLoad()
        

        //Rotate slider
        //rgbSlider.transform = CGAffineTransform(rotationAngle: CGFloat(-M_PI_2))
        
        
        
        
        //Rgb label
        rgbLabel.backgroundColor = UIColor.white
        currentRgbColor = .white
        rgbLabel.layer.masksToBounds = true //has to be true to change corner radius label
        rgbLabel.layer.cornerRadius = 20
        
        
        
    }
    
    
    //TODO: naam veranderen, nu niet duideijk welke het is...s
    //State changed switch led 1.
    @IBAction func led1Switch(_ sender: Any) {
    
        if blue.isReady {
            blue.sendMessage(string: "a")
        }
        else {
            print("not connected")
        }
    }
    
    //State changed switch led 2.
    @IBAction func led2Switch(_ sender: Any) {
        print("Led 2 is on: \(led2Switch.isOn)")
        
        if blue.isReady {
            blue.sendMessage(string: "b")
        }
        else    {
            print("Not connected")
        }
    }
    

    //If slider value changed and blue is ready, call changecolor otherwise set back to 0.
    @IBAction func sliderChangedValue(_ sender: Any) {
        
        if blue.isReady {
            changeColor(sliderValue: rgbSlider.value)
        }
        else {
            rgbSlider.setValue(0, animated: false)
        }
    }
    
    
    //If user touches the slider -> show progress message .notConnected
    @IBAction func rgbSliderWasTouched(_ sender: Any) {
        if !blue.isReady    {
            BlueProgressMessage.show(state: .notConnected, currentView: self.view)
        }
    }
    
    
    //TODO: checken of enum een goede oplossing is
    
    //Sending the RGB values based on sliders value.
    private func changeColor(sliderValue: Float)  {
        
        switch sliderValue    {
         
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
        
        
/*
        //White
        if value == 0 && currentRgbColor != .white {
            currentRgbColor = .white
            blue.sendMessage(string: "c000\n000\n000")
            rgbLabel.backgroundColor = UIColor.white
        }
        //Red
        else if value > 0 && value <= 50 && currentRgbColor != .red {
            currentRgbColor = .red
            blue.sendMessage(string: "c255\n000\n000")
            rgbLabel.backgroundColor = UIColor.red.withAlphaComponent(0.75)
        }
        //Yellow
        else if value > 50 && value <= 100 && currentRgbColor != .yellow  {
            currentRgbColor = .yellow
            blue.sendMessage(string: "c255\n255\n000")
            rgbLabel.backgroundColor = UIColor.yellow.withAlphaComponent(0.75)
        }
        //Green
        else if value > 100 && value <= 150 && currentRgbColor != .green{
            currentRgbColor = .green
            blue.sendMessage(string: "c000\n255\n000")
            rgbLabel.backgroundColor = UIColor.green.withAlphaComponent(0.75)
        }
        //Aqua
        else if value > 150 && value <= 200 && currentRgbColor != .aqua {
            currentRgbColor = .aqua
            blue.sendMessage(string: "c000\n255\n255")
            rgbLabel.backgroundColor = UIColor(red: 153/255, green: 241/255, blue: 250/255, alpha: 1.0)
        }
        //Blue
        else if value > 200 && value <= 250 && currentRgbColor != .blue {
            currentRgbColor = .blue
            blue.sendMessage(string: "c000\n000\n255")
            rgbLabel.backgroundColor = UIColor.blue.withAlphaComponent(0.75)
        }
        //Purple
        else if value > 250 && value <= 300 && currentRgbColor != .purple    {
            currentRgbColor = .purple
            blue.sendMessage(string: "c255\n000\n255")
            rgbLabel.backgroundColor = UIColor.purple.withAlphaComponent(0.75)
        }*/
    }
}

