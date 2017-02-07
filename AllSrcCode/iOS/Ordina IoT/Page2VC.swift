//
//  Page2VC.swift
//  Ordina IoT
//
//  Created by Rik Wout on 24-01-17.
//  Copyright © 2017 Wo. All rights reserved.
//

import UIKit

class Page2VC: UIViewController {

    //Properties
    @IBOutlet weak var speakerImageView: UIImageView!
    
    @IBOutlet weak var alarmBtn: UIButton!
    @IBOutlet weak var tone1Btn: UIButton!
    @IBOutlet weak var customToneBtn: UIButton!
    
    override func viewDidLoad() {
        super.viewDidLoad()
        
        
    }



    //Did press alarm button.
    @IBAction func didPressAlarmBtn(_ sender: Any) {
        
        if blue.isReady {
            blue.sendMessage(string: "d")
        }
        else    {
            print("Cannot send messag")
        }
    }

    //Did press tone 1 button.
    @IBAction func didPressTone1Btn(_ sender: Any) {
        
        if blue.isReady {
            blue.sendMessage(string: "e")
        }
        else    {
            print("Cannot send messag")
        }
    }
    
    //Did press custom tone button.
    @IBAction func didPressCustomToneBtn(_ sender: Any) {
        
        if blue.isReady {
            blue.sendMessage(string: "f")
            print("pressed")
        }
        else    {
            print("Cannot send messag")
        }
    }

    
}
