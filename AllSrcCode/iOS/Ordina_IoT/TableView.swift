//
//  TableView.swift
//  Ordina_IoT
//
//  Created by Rik Wout on 13-02-17.
//  Copyright © 2017 Wo. All rights reserved.
//

import UIKit
import CoreBluetooth

//Delegate table view protocol
protocol TableViewDelegate  {
    func userDidSelectRow(indexPath: IndexPath)
}


class TableView: NSObject, UITableViewDelegate, UITableViewDataSource     {
    
//Properties
    private var tableViewData: [String] = []
    private var delegate: TableViewDelegate?
 
    
//Constructor
    init(delegate: TableViewDelegate, data: [String])  {
        super.init()
        self.delegate = delegate
        self.tableViewData = data
    }
    
    //Als er nieuwe data.
    func reloadTableViewData(data: [String])  {
        tableViewData = data
    }
    

    //Aantal rijen in array devices.
    func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        return tableViewData.count
    }
    
    
    //Wat er in de cellen moet komen.
    func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        
        let cell = UITableViewCell()
        cell.textLabel?.text = tableViewData[indexPath.row]
        cell.backgroundColor = UIColor.white
        cell.textLabel?.font = UIFont(name: "Avenir Next", size: 21)
        cell.textLabel?.textColor = UIColor.black
        cell.accessoryType = .disclosureIndicator
                
        return cell
    }
    
    
    //Als er op een specifeke cel wordt geklikt.
    func tableView(_ tableView: UITableView, didSelectRowAt indexPath: IndexPath) {
        delegate?.userDidSelectRow(indexPath: indexPath)
    }
}
