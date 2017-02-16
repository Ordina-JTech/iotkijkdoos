//
//  SettingView.swift
//  Ordina_IoT
//
//  Created by Rik Wout on 13-02-17.
//  Copyright © 2017 Wo. All rights reserved.
//

import UIKit

class SettingView: UIView {

//Properties
    
//Constructor
    required init?(coder aDecoder: NSCoder) {
        super.init(coder: aDecoder)
    }
    
    init(frame: CGRect, title: String)  {
        super.init(frame: frame)
        self.backgroundColor = UIColor.white
        addTitleBar(text: title)
    }
    
    
    //Add the title bar to setting view
    private func addTitleBar(text: String)  {
    
    //View
        
        //Creation
        let viewPoint = CGPoint(x: 0, y: 0)
        let viewSize = CGSize(width: self.frame.width, height: self.frame.height/3)
        let viewRect = CGRect(origin: viewPoint, size: viewSize)
        let titleView = UIView(frame: viewRect)
        self.addSubview(titleView)
        
        //Properties
        titleView.backgroundColor = UIColor.orange
        
    //Label
        
        //Creation
        let titleLabel = UILabel()
        titleView.addSubview(titleLabel)
        
        //Constraints
        titleLabel.heightAnchor.constraint(equalToConstant: titleView.frame.height/3).isActive = true
        titleLabel.bottomAnchor.constraint(equalTo: titleView.bottomAnchor).isActive = true
        titleLabel.leftAnchor.constraint(equalTo: titleView.leftAnchor).isActive = true
        
        //Properties
        titleLabel.translatesAutoresizingMaskIntoConstraints = false
        titleLabel.text = text
        titleLabel.textColor = UIColor.black
        titleLabel.font = UIFont(name: "Avenir Next", size: 25.0)
        titleLabel.sizeToFit()
        
    //ImageView
        
        //Creation
        let imageName = "jtech_logo"
        let image = UIImage(named: imageName)
        let imageView = UIImageView(image: image)
        titleView.addSubview(imageView)

        //Constraints
        imageView.rightAnchor.constraint(equalTo: titleView.rightAnchor, constant: 0.0).isActive = true
        imageView.bottomAnchor.constraint(equalTo: titleView.bottomAnchor, constant: titleLabel.frame.height).isActive = true

        let widthAnchor = titleView.frame.width/2.4
        imageView.widthAnchor.constraint(equalToConstant: widthAnchor).isActive = true
        imageView.heightAnchor.constraint(equalToConstant: titleLabel.frame.height*2).isActive = true

        //Properties
        imageView.translatesAutoresizingMaskIntoConstraints = false
        imageView.contentMode = .scaleAspectFit
    }
}
