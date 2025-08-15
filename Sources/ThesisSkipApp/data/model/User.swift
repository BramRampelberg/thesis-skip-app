//
//  User.swift
//  MADiOS
//
//  Created by Bram Rampelberg on 12/08/2025.
//  Copyright © 2025 HOGENT. All rights reserved.
//

import Foundation

struct User {
    let firstName: String
    let email: String
    let familyName: String
    
    init(firstName: String, email: String, familyName: String, id: Int) {
        self.firstName = firstName
        self.email = email
        self.familyName = familyName
    }
    
    init(fromDto dto: UserDetailsDto) {
        self.firstName = dto.firstName
        self.email = dto.email
        self.familyName = dto.familyName
    }
}
