//
//  ReservationDetailsDto.swift
//  MADiOS
//
//  Created by Bram Rampelberg on 05/01/2025.
//  Copyright © 2025 HOGENT. All rights reserved.
//

package thesis.skip.app

import skip.lib.*

import skip.foundation.*

internal class ReservationDetailsDto: Codable {
    internal val mentorName: String?
    internal val batteryId: Int?
    internal val currentBatteryUserName: String?
    internal val currentBatteryUserId: Int?
    internal val currentHolderPhoneNumber: String?
    internal val currentHolderEmail: String?
    internal val currentHolderStreet: String?
    internal val currentHolderNumber: String?
    internal val currentHolderCity: String?
    internal val currentHolderPostalCode: String?

    constructor(mentorName: String? = null, batteryId: Int? = null, currentBatteryUserName: String? = null, currentBatteryUserId: Int? = null, currentHolderPhoneNumber: String? = null, currentHolderEmail: String? = null, currentHolderStreet: String? = null, currentHolderNumber: String? = null, currentHolderCity: String? = null, currentHolderPostalCode: String? = null) {
        this.mentorName = mentorName
        this.batteryId = batteryId
        this.currentBatteryUserName = currentBatteryUserName
        this.currentBatteryUserId = currentBatteryUserId
        this.currentHolderPhoneNumber = currentHolderPhoneNumber
        this.currentHolderEmail = currentHolderEmail
        this.currentHolderStreet = currentHolderStreet
        this.currentHolderNumber = currentHolderNumber
        this.currentHolderCity = currentHolderCity
        this.currentHolderPostalCode = currentHolderPostalCode
    }

    private enum class CodingKeys(override val rawValue: String, @Suppress("UNUSED_PARAMETER") unusedp: Nothing? = null): CodingKey, RawRepresentable<String> {
        mentorName("mentorName"),
        batteryId("batteryId"),
        currentBatteryUserName("currentBatteryUserName"),
        currentBatteryUserId("currentBatteryUserId"),
        currentHolderPhoneNumber("currentHolderPhoneNumber"),
        currentHolderEmail("currentHolderEmail"),
        currentHolderStreet("currentHolderStreet"),
        currentHolderNumber("currentHolderNumber"),
        currentHolderCity("currentHolderCity"),
        currentHolderPostalCode("currentHolderPostalCode");

        companion object {
            fun init(rawValue: String): CodingKeys? {
                return when (rawValue) {
                    "mentorName" -> CodingKeys.mentorName
                    "batteryId" -> CodingKeys.batteryId
                    "currentBatteryUserName" -> CodingKeys.currentBatteryUserName
                    "currentBatteryUserId" -> CodingKeys.currentBatteryUserId
                    "currentHolderPhoneNumber" -> CodingKeys.currentHolderPhoneNumber
                    "currentHolderEmail" -> CodingKeys.currentHolderEmail
                    "currentHolderStreet" -> CodingKeys.currentHolderStreet
                    "currentHolderNumber" -> CodingKeys.currentHolderNumber
                    "currentHolderCity" -> CodingKeys.currentHolderCity
                    "currentHolderPostalCode" -> CodingKeys.currentHolderPostalCode
                    else -> null
                }
            }
        }
    }

    override fun encode(to: Encoder) {
        val container = to.container(keyedBy = CodingKeys::class)
        container.encodeIfPresent(mentorName, forKey = CodingKeys.mentorName)
        container.encodeIfPresent(batteryId, forKey = CodingKeys.batteryId)
        container.encodeIfPresent(currentBatteryUserName, forKey = CodingKeys.currentBatteryUserName)
        container.encodeIfPresent(currentBatteryUserId, forKey = CodingKeys.currentBatteryUserId)
        container.encodeIfPresent(currentHolderPhoneNumber, forKey = CodingKeys.currentHolderPhoneNumber)
        container.encodeIfPresent(currentHolderEmail, forKey = CodingKeys.currentHolderEmail)
        container.encodeIfPresent(currentHolderStreet, forKey = CodingKeys.currentHolderStreet)
        container.encodeIfPresent(currentHolderNumber, forKey = CodingKeys.currentHolderNumber)
        container.encodeIfPresent(currentHolderCity, forKey = CodingKeys.currentHolderCity)
        container.encodeIfPresent(currentHolderPostalCode, forKey = CodingKeys.currentHolderPostalCode)
    }

    constructor(from: Decoder) {
        val container = from.container(keyedBy = CodingKeys::class)
        this.mentorName = container.decodeIfPresent(String::class, forKey = CodingKeys.mentorName)
        this.batteryId = container.decodeIfPresent(Int::class, forKey = CodingKeys.batteryId)
        this.currentBatteryUserName = container.decodeIfPresent(String::class, forKey = CodingKeys.currentBatteryUserName)
        this.currentBatteryUserId = container.decodeIfPresent(Int::class, forKey = CodingKeys.currentBatteryUserId)
        this.currentHolderPhoneNumber = container.decodeIfPresent(String::class, forKey = CodingKeys.currentHolderPhoneNumber)
        this.currentHolderEmail = container.decodeIfPresent(String::class, forKey = CodingKeys.currentHolderEmail)
        this.currentHolderStreet = container.decodeIfPresent(String::class, forKey = CodingKeys.currentHolderStreet)
        this.currentHolderNumber = container.decodeIfPresent(String::class, forKey = CodingKeys.currentHolderNumber)
        this.currentHolderCity = container.decodeIfPresent(String::class, forKey = CodingKeys.currentHolderCity)
        this.currentHolderPostalCode = container.decodeIfPresent(String::class, forKey = CodingKeys.currentHolderPostalCode)
    }

    companion object: DecodableCompanion<ReservationDetailsDto> {
        override fun init(from: Decoder): ReservationDetailsDto = ReservationDetailsDto(from = from)

        private fun CodingKeys(rawValue: String): CodingKeys? = CodingKeys.init(rawValue = rawValue)
    }
}
