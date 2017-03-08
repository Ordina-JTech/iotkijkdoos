//
//  SpeakerVC.swift
//  Ordina_IoT
//
//  Created by Rik Wout on 16-02-17.
//  Copyright © 2017 Wo. All rights reserved.
//

import Foundation

class SpeakerVC: TableViewDelegate   {
    
    private enum Sound  {
        static let alarm = "Alarm"
        static let vaderJacob = "Vader Jacob"
        static let challenge2 = "Challenge II"
        static let names = [alarm, vaderJacob, challenge2]
        static let letters = [PeripheralLetter.alarm, PeripheralLetter.vaderJacob, PeripheralLetter.challenge2]
    }
    
    var settingView: SettingView!
    private var tableView: UITableView!
    private var tableViewObj: TableView!

    init(frame: CGRect, headerText: String)  {
        settingView = SettingView(frame: frame, headerText: headerText)
        tableViewObj = TableView(delegate: self, data: Sound.names)
        addServoView()
    }
    
    private func addServoView() {
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
            bluetooth.sendMessage(string: Sound.letters[index.row])
        }
    }
}
