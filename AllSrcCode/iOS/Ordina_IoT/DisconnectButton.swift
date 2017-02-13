//
//  DisconnectButton.swift
//  Ordina_IoT
//
//  Created by Rik Wout on 13-02-17.
//  Copyright © 2017 Wo. All rights reserved.
//

import UIKit

class DisconnectButton: UIButton {

    required init?(coder aDecoder: NSCoder) {
        super.init(coder: aDecoder)
        
        self.backgroundColor = UIColor.red.withAlphaComponent(0.75)
        self.layer.cornerRadius = 10
        self.setTitle("Disconnect", for: .normal)
        self.setTitleColor(UIColor.white, for: .normal)
        self.contentEdgeInsets = UIEdgeInsetsMake(8, 8, 8, 8)
        
    }

}
