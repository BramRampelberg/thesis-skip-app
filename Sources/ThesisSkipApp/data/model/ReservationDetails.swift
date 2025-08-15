//
//  ReservationDetails.swift
//  MADiOS
//
//  Created by Bram Rampelberg on 03/01/2025.
//  Copyright © 2025 HOGENT. All rights reserved.
//

import Foundation

struct ReservationDetails {
    var mentorName: String?
    var batteryId: Int?
    var currentBatteryUserName: String?
    var currentBatteryUserId: Int?
    var currentHolderPhoneNumber: String?
    var currentHolderEmail: String?
    var currentHolderStreet: String?
    var currentHolderNumber: String?
    var currentHolderCity: String?
    var currentHolderPostalCode: String?
    
    init(
        mentorName: String?,
        batteryId: Int?,
        currentBatteryUserName: String?,
        currentBatteryUserId: Int?,
        currentHolderPhoneNumber: String?,
        currentHolderEmail: String?,
        currentHolderStreet: String?,
        currentHolderNumber: String?,
        currentHolderCity: String?,
        currentHolderPostalCode: String?
    ) {
        self.mentorName = mentorName
        self.batteryId = batteryId
        self.currentBatteryUserName = currentBatteryUserName
        self.currentBatteryUserId = currentBatteryUserId
        self.currentHolderPhoneNumber = currentHolderPhoneNumber
        self.currentHolderEmail = currentHolderEmail
        self.currentHolderStreet = currentHolderStreet
        self.currentHolderNumber = currentHolderNumber
        self.currentHolderCity = currentHolderCity
        self.currentHolderPostalCode = currentHolderPostalCode
    }
    
    init(fromDto dto: ReservationDetailsDto) {
        self.mentorName = dto.mentorName
        self.batteryId = dto.batteryId
        self.currentBatteryUserName = dto.currentBatteryUserName
        self.currentBatteryUserId = dto.currentBatteryUserId
        self.currentHolderPhoneNumber = dto.currentHolderPhoneNumber
        self.currentHolderEmail = dto.currentHolderEmail
        self.currentHolderStreet = dto.currentHolderStreet
        self.currentHolderNumber = dto.currentHolderNumber
        self.currentHolderCity = dto.currentHolderCity
        self.currentHolderPostalCode = dto.currentHolderPostalCode
    }
}
