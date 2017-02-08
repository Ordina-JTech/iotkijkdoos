//
//  PageIndicator.swift
//  Ordina IoT
//
//  Created by Rik Wout on 25-01-17.
//  Copyright © 2017 Wo. All rights reserved.
//

import Foundation


class PageIndicator {
    
    //Properties
    private var mainView: UIView!
    private var pageIndicator: UILabel!
    private var labelHeight: CGFloat!
    private var yOrigin: CGFloat!
    private let labelText: [String] = ["Light", "Sound", "Movement"]
    private let nLabels = 3
    
    
    //Constructor
    init(mainView: UIView, pageIndicatorHeight: CGFloat, yOrigin: CGFloat)  {
        self.mainView = mainView
        self.labelHeight = pageIndicatorHeight
        self.yOrigin = yOrigin
    }
    
    //This method creates and adds the page indicator to the view
    func addPageIndicator() {
        
        //MainView width
        let mainWidth = mainView.frame.width
        
        //Create and Add the n labels to mainView/navigation controller View
        for i in 0..<nLabels    {
            
            let xOrigin = (mainWidth/3) * CGFloat(i)

            /* - Afmetingen van de pageIndicator. Net op statusBar beginnen, zodat de rand van de statusbar net wegvalt
               - Width + 1 is omdat bij de iphone 5 het aantal pixels achter de komma gaat
               Er wordt 1 pixel overgeslagen, waardoor er een witte streep is te zien
             */
        
            let point = CGPoint(x: xOrigin, y: yOrigin)
            let size = CGSize(width: mainWidth/3 + 1, height: labelHeight)
            let rect = CGRect(origin: point , size: size)
            
            //Properties
            pageIndicator = UILabel(frame: rect)
            pageIndicator.backgroundColor = UIColor(red: 75/255, green: 150/255, blue: 200/255, alpha: 1.0)
            pageIndicator.font = UIFont(name: "Avenir Next", size: 14.0)
            pageIndicator.text = labelText[i]
            pageIndicator.textAlignment = .center
            pageIndicator.textColor = UIColor.white
            
            //Add to main view
            mainView.addSubview(pageIndicator)
        }
    }
}
