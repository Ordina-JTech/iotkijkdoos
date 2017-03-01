//
//  SettingView.swift
//  Ordina_IoT
//
//  Created by Rik Wout on 13-02-17.
//  Copyright © 2017 Wo. All rights reserved.
//

import UIKit

class SettingView: UIView {

    private var headerText: String!
    
    required init?(coder aDecoder: NSCoder) {
        super.init(coder: aDecoder)
    }
    
    init(frame: CGRect, headerText: String)  {
        super.init(frame: frame)
        self.headerText = headerText
        self.backgroundColor = UIColor.white
    
    }
    

    override func layoutSubviews() {
        addViewComponents()
    }
    
    
    private func addViewComponents()  {
    
        //View
        let viewPoint = CGPoint(x: 0, y: 0)
        let viewSize = CGSize(width: self.frame.width, height: self.frame.height/3)
        let viewRect = CGRect(origin: viewPoint, size: viewSize)
        let titleView = UIView(frame: viewRect)
        titleView.backgroundColor = UIColor.orange
        
        self.addSubview(titleView)
        
        
        //Label
        let titleLabel = UILabel()
        titleLabel.translatesAutoresizingMaskIntoConstraints = false
        titleLabel.text = headerText
        titleLabel.textColor = UIColor.black
        titleLabel.font = UIFont(name: "Avenir Next", size: 25.0)
        titleLabel.sizeToFit()
        
        titleView.addSubview(titleLabel)
        
        titleLabel.heightAnchor.constraint(equalToConstant: titleView.frame.height/3).isActive = true
        titleLabel.bottomAnchor.constraint(equalTo: titleView.bottomAnchor).isActive = true
        titleLabel.leftAnchor.constraint(equalTo: titleView.leftAnchor).isActive = true


        //ImageView
        let imageName = "jtech_logo"
        
        if let image = UIImage(named: imageName)    {
            let imageView = UIImageView(image: image)
            imageView.translatesAutoresizingMaskIntoConstraints = false
            imageView.contentMode = .scaleAspectFit
            
            titleView.addSubview(imageView)

            let widthAnchor = titleView.frame.width/2.4
            imageView.widthAnchor.constraint(equalToConstant: widthAnchor).isActive = true
            imageView.rightAnchor.constraint(equalTo: titleView.rightAnchor, constant: 0.0).isActive = true
            imageView.bottomAnchor.constraint(equalTo: titleView.bottomAnchor, constant: titleLabel.frame.height).isActive = true
            imageView.heightAnchor.constraint(equalToConstant: titleLabel.frame.height*2).isActive = true
        }
        else {
            print("No Image Found")
            return
        }
    }
}
