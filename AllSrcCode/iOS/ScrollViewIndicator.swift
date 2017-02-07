//
//  ScrollViewIndicator.swift
//  Ordina IoT
//
//  Created by Rik Wout on 24-01-17.
//  Copyright © 2017 Wo. All rights reserved.
//

import Foundation

enum Orientation{
    case landscape
    case portrait
}


class ScrollViewIndicator   {

    private var mainView: UIView
    private var scrollView: UIScrollView
    private var scrollIndicator: UILabel!
    private var widthScale: CGFloat!
    private var yOrigin: CGFloat!
    private var scrollIndicatorWidth: CGFloat!
    private let scrollIndicatorHeight: CGFloat = 2
    private var currentOrientation = Orientation.portrait
    
    
    init(mainView: UIView, scrollView: UIScrollView, yOrigin: CGFloat)  {
        
        self.mainView = mainView
        self.scrollView = scrollView
        self.yOrigin = yOrigin 

    }
    
    //ScrollIndicator Toevoegen.
    func addScrollIndicator()   {
        
        //Breedte scrollView.
        let widthVC = mainView.frame.width
        
        //De verhouding tussen wat te zien is en er nog gescrolld kan worden.
        widthScale = widthVC / scrollView.contentSize.width
        
        //Breedte scrollIndicator
        scrollIndicatorWidth = widthVC * widthScale
        
        //Afmetingen van de scrollIndicator. Net onder statusBar beginnen
        let point = CGPoint(x: 0, y: yOrigin)
        let size = CGSize(width: scrollIndicatorWidth, height: scrollIndicatorHeight)
        let rect = CGRect(origin: point , size: size)
        
        self.scrollIndicator = UILabel(frame: rect)
        self.scrollIndicator.backgroundColor = UIColor.black
        //ScrollIndicator toevoegen aan scrollView.
        mainView.addSubview(scrollIndicator)
    }
    
    
    //De ScrollIndicator updaten als er gescrolld wordt.
    func updateScrollIndicator(xValue: CGFloat)    {
        
        //ScrollIndicator verbergen.
        scrollIndicator.isHidden = true
        
        //De x waarde updaten en indicator zichtbaar maken.
        var frame = scrollIndicator.frame
        
        //Check the curren orientation
        switch (currentOrientation) {
        case .landscape:
            frame.origin.x = xValue * (widthScale*3)
        case .portrait:
            frame.origin.x = xValue * widthScale
        }
        
        //add the new frame
        scrollIndicator.frame = frame
        scrollIndicator.isHidden = false
        
    }
    
    
    //If the orientation has changed,
    func orientationChanged(orientation: Orientation, yOrigin: CGFloat) {
        
        //ScrollIndicator verbergen.
        scrollIndicator.isHidden = true
        
        //Create new point with yOrigin
        let point = CGPoint(x: scrollIndicator.frame.origin.x, y: yOrigin)
        
        //Create size variable
        let size: CGSize
                
        //Check the orientation, change indicator width and create new size
        switch (orientation)    {
        case .landscape:
            currentOrientation = .landscape
            let newWidth = mainView.frame.width * (widthScale * 2)
            size = CGSize(width: newWidth, height: scrollIndicatorHeight)

        case .portrait:
            currentOrientation = .portrait
            size = CGSize(width: scrollIndicatorWidth, height: scrollIndicatorHeight)
        }
        
        //Create new frame and add it to the scrollindicator
        let rect = CGRect(origin: point, size: size)
        scrollIndicator.frame = rect

        //Make scrollIndicator visible
        scrollIndicator.isHidden = false
        
    }
    
    
    
    
    
    
    
    
    
}
