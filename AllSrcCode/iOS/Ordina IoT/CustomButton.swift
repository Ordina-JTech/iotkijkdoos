//
//  CustomButton.swift
//  Ordina IoT
//
//  Created by Rik Wout on 26-01-17.
//  Copyright © 2017 Wo. All rights reserved.
//

import Foundation
import UIKit


class CustomButton: UIButton    {
    
    //Can use it in storyboard
    required init?(coder aDecoder: NSCoder) {
        super.init(coder: aDecoder)
        
        self.layer.cornerRadius = 5
        self.backgroundColor = UIColor.lightGray
    }
    
    
    

}

class ButtonPointer: UIButton    {
    
    //Can use it in storyboard
    required init?(coder aDecoder: NSCoder) {
        super.init(coder: aDecoder)
        
        self.layer.cornerRadius = 20
        self.backgroundColor = UIColor.black
    }
}


    
    


