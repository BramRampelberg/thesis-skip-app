//
//  UserDetailsDto.swift
//  MADiOS
//
//  Created by Bram Rampelberg on 12/08/2025.
//  Copyright © 2025 HOGENT. All rights reserved.
//

import Foundation

struct UserDetailsDto: Codable, Sendable {
    let firstName: String
    let email: String
    let familyName: String
}
