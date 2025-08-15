//
//  ReservationDetailsDto.swift
//  MADiOS
//
//  Created by Bram Rampelberg on 05/01/2025.
//  Copyright © 2025 HOGENT. All rights reserved.
//

import Foundation

struct ReservationDetailsDto: Codable, Sendable {
    let mentorName: String?
    let batteryId: Int?
    let currentBatteryUserName: String?
    let currentBatteryUserId: Int?
    let currentHolderPhoneNumber: String?
    let currentHolderEmail: String?
    let currentHolderStreet: String?
    let currentHolderNumber: String?
    let currentHolderCity: String?
    let currentHolderPostalCode: String?
}
