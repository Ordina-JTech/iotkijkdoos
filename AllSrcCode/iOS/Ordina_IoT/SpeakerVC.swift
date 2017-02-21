//
//  SpeakerVC.swift
//  Ordina_IoT
//
//  Created by Rik Wout on 16-02-17.
//  Copyright © 2017 Wo. All rights reserved.
//

import Foundation


class SpeakerVC: NSObject, TableViewDelegate   {
    
//Properties
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
        
        //Creation
        tableView = UITableView()
        tableView.dataSource = tableViewObj
        tableView.delegate = tableViewObj
        settingView.addSubview(tableView)
        
        //Constraints
        tableView.heightAnchor.constraint(equalToConstant: settingView.frame.height/1.8).isActive = true
        tableView.leftAnchor.constraint(equalTo: settingView.leftAnchor).isActive = true
        tableView.rightAnchor.constraint(equalTo: settingView.rightAnchor).isActive = true
        tableView.bottomAnchor.constraint(equalTo: settingView.bottomAnchor).isActive = true
        
        //Properties
        tableView.translatesAutoresizingMaskIntoConstraints = false
    }
    
    //If user did select a row in table view.
    func userDidSelectRow(indexPath: IndexPath) {
        tableView.deselectRow(at: indexPath, animated: true)
        
        let index = indexPath as NSIndexPath
        blue.sendMessage(string: speakerMessage[index.row])
    }
}
