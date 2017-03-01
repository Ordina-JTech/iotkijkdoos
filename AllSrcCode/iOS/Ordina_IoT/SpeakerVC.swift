//
//  SpeakerVC.swift
//  Ordina_IoT
//
//  Created by Rik Wout on 16-02-17.
//  Copyright © 2017 Wo. All rights reserved.
//

import Foundation

class SpeakerVC: NSObject, TableViewDelegate   {
    
    var settingView: SettingView!
    private var tableView: UITableView!
    private var tableViewObj: TableView!
    private let soundName = ["Alarm", "Vader Jacob", "Create Your Own"]
    private var speakerLetter = [String]()
    
    init(frame: CGRect, headerText: String, speakerLetter: [String])  {
        super.init()
        
        settingView = SettingView(frame: frame, headerText: headerText)
        self.speakerLetter = speakerLetter
        tableViewObj = TableView(delegate: self, data: soundName)
        addServoView()
    }
    
    
    private func addServoView() {
     
        //TableView
        tableView = UITableView()
        tableView.dataSource = tableViewObj
        tableView.delegate = tableViewObj
        tableView.translatesAutoresizingMaskIntoConstraints = false
        
        settingView.addSubview(tableView)
        
        tableView.heightAnchor.constraint(equalToConstant: settingView.frame.height/1.8).isActive = true
        tableView.leftAnchor.constraint(equalTo: settingView.leftAnchor).isActive = true
        tableView.rightAnchor.constraint(equalTo: settingView.rightAnchor).isActive = true
        tableView.bottomAnchor.constraint(equalTo: settingView.bottomAnchor).isActive = true
    }
    

    func userDidSelectRow(indexPath: IndexPath) {
        tableView.deselectRow(at: indexPath, animated: true)
        
        if bluetooth.isReady    {
            let index = indexPath as NSIndexPath
            bluetooth.sendMessage(string: speakerLetter[index.row])
        }
    }
}
