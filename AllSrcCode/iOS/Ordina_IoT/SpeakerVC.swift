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
    private let sounds: [String] = ["Alarm", "Vader Jacob", "Create Your Own"]
    private let speakerMessage: [String] = ["d", "e", "f"]
    
    init(frame: CGRect, title: String)  {
        super.init()
        
        settingView = SettingView(frame: frame, title: title)
        tableViewObj = TableView(delegate: self, data: sounds)
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
            bluetooth.sendMessage(string: speakerMessage[index.row])
        }
    }
}
