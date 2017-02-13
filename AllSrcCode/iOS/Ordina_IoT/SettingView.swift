//
//  SettingView.swift
//  Ordina_IoT
//
//  Created by Rik Wout on 13-02-17.
//  Copyright © 2017 Wo. All rights reserved.
//

import UIKit

class SettingView: UIView {

    private var mainView: CGRect!
    var rgbSlider: UISlider!
    var rgbLabel: UILabel!
    var okBtn: UIButton!
    
    required init?(coder aDecoder: NSCoder) {
        super.init(coder: aDecoder)
    }
    
    override init(frame: CGRect)  {
        super.init(frame: frame)
        
        mainView = frame
        
        self.backgroundColor = UIColor.black.withAlphaComponent(0.85)
        self.layer.cornerRadius = 10
        //self.isHidden = true
    }
    
    
    
    func addRgbView()    {
        
        //Label
        var x = (mainView.width/2) - (mainView.width/8)
        var y  = ((mainView.height/2) - (mainView.height/4))/2
        var point = CGPoint(x: x, y: y)
        var size = CGSize(width: mainView.width/4, height: mainView.height/4)
        var rect = CGRect(origin: point, size: size)
        
        rgbLabel = UILabel(frame: rect)
        rgbLabel.layer.masksToBounds = true
        rgbLabel.layer.cornerRadius = 15
        rgbLabel.backgroundColor = UIColor.white
        
        self.addSubview(rgbLabel)
        
        //Slider
        point = CGPoint(x: 25, y: mainView.height/2)
        size = CGSize(width: mainView.width - 50, height: 20)
        rect = CGRect(origin: point, size: size)
        
        rgbSlider = UISlider(frame: rect)
        rgbSlider.value = 0
        rgbSlider.minimumValue = 0
        rgbSlider.maximumValue = 300
        
        self.addSubview(rgbSlider)
        
        //Button
        /*x = (mainView.width/2) - (mainView.width/8)
        y  = ((mainView.height/2) + (mainView.height/4))
        
        
        point = CGPoint(x: x, y: y)
        size = CGSize(width: rgbLabel.frame.width*2, height: 40)
        rect = CGRect(origin: point, size: size)
        
        okBtn = UIButton(frame: rect)
        okBtn.setTitle("Done", for: .normal)
        okBtn.setTitleColor(UIColor.white, for: .normal)
        okBtn.layer.cornerRadius = 10
        okBtn.backgroundColor = UIColor.orange
        
        self.addSubview(okBtn)*/
        
        
        
        
        
        
        
    }

}
