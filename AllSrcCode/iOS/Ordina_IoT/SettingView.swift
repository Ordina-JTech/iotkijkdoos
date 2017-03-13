//
//  SettingView.swift
//  Ordina_IoT
//
//  Created by Rik Wout on 13-02-17.
//  Copyright © 2017 Wo. All rights reserved.
//

import UIKit

class SettingView: UIView {

    private enum ImageName {
        static let ordina = "ordinaLogo"
    }
    
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
        addHeaderComponents()
    }
    
    private func addHeaderComponents()  {
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
        titleLabel.font = UIFont.avenirNext(size: 25)
        titleLabel.sizeToFit()
        titleView.addSubview(titleLabel)
        
        titleLabel.bottomAnchor.constraint(equalTo: titleView.bottomAnchor).isActive = true
        titleLabel.leftAnchor.constraint(equalTo: titleView.leftAnchor, constant: 5.0).isActive = true

        //ImageView
        guard let ordinaLogo = UIImage(named: ImageName.ordina)    else {
            print("Image was not found")
            return
        }
        
        let ordinaLogoView = UIImageView(image: ordinaLogo)
        ordinaLogoView.translatesAutoresizingMaskIntoConstraints = false
        ordinaLogoView.contentMode = .scaleAspectFit
        titleView.addSubview(ordinaLogoView)

        let widthAnchor = titleView.frame.width/2.4
        ordinaLogoView.widthAnchor.constraint(equalToConstant: widthAnchor).isActive = true
        ordinaLogoView.rightAnchor.constraint(equalTo: titleView.rightAnchor).isActive = true
        ordinaLogoView.bottomAnchor.constraint(equalTo: titleView.bottomAnchor, constant: titleLabel.frame.height).isActive = true
        ordinaLogoView.heightAnchor.constraint(equalToConstant: titleLabel.frame.height*2).isActive = true
    }
}
