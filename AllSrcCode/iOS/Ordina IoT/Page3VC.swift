//
//  Page3VC.swift
//  Ordina IoT
//
//  Created by Rik Wout on 24-01-17.
//  Copyright © 2017 Wo. All rights reserved.
//

import UIKit


class Page3VC: UIViewController {

    //Properties
    @IBOutlet weak var rotateImageView: UIImageView!
    @IBOutlet weak var angleSlider: UISlider!
    private var previousValue: Float = 0

    
    //View did Load
    override func viewDidLoad() {
        super.viewDidLoad()
        
    }
    
    
    //If user changed the slider value
    @IBAction func sliderValueChanged(_ sender: Any) {
        
        //Change servo angle every 5 degree if the previous slider value is not equal to the current value.
        //If bluetooth connec
        if blue.isReady{
            if (Int(angleSlider.value) % 5 == 0) && (Int(angleSlider.value) != Int(previousValue))  {
            
                //Convert slider value to radians and make transformation
                let sliderValue = angleSlider.value
                let radianAngle = sliderValue * .pi / 180
                rotateImageView.transform = CGAffineTransform(rotationAngle: CGFloat(radianAngle))
                blue.sendMessage(string: "g\(Int(sliderValue))\n")
                print(Int(sliderValue))
                previousValue = angleSlider.value
            }
        }
        //Set value to previous value (=0)
        else {
            angleSlider.setValue(previousValue, animated: false)
        }
    }
}
