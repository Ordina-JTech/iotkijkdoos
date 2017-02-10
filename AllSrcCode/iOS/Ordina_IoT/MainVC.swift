//
//  MainVC.swift
//  Ordina_IoT
//
//  Created by Rik Wout on 10-02-17.
//  Copyright © 2017 Wo. All rights reserved.
//

import UIKit


class MainVC: UIViewController {
    
    //Properties
    
    
    //Prevent changing to portrait
    override var shouldAutorotate: Bool {
            return false
    }
    
    
    
    //View Did Load
    override func viewDidLoad() {
        super.viewDidLoad()
        
        let value = UIInterfaceOrientation.landscapeLeft.rawValue
        UIDevice.current.setValue(value, forKey: "orientation")
        


        
        
    }
    
    


}
