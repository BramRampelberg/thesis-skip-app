//
//  ReservationDetails.swift
//  MADiOS
//
//  Created by Bram Rampelberg on 03/01/2025.
//  Copyright © 2025 HOGENT. All rights reserved.
//

package thesis.skip.app

import skip.lib.*

import skip.foundation.*

internal class ReservationDetails: MutableStruct {
    internal var mentorName: String? = null
        set(newValue) {
            willmutate()
            field = newValue
            didmutate()
        }
    internal var batteryId: Int? = null
        set(newValue) {
            willmutate()
            field = newValue
            didmutate()
        }
    internal var currentBatteryUserName: String? = null
        set(newValue) {
            willmutate()
            field = newValue
            didmutate()
        }
    internal var currentBatteryUserId: Int? = null
        set(newValue) {
            willmutate()
            field = newValue
            didmutate()
        }
    internal var currentHolderPhoneNumber: String? = null
        set(newValue) {
            willmutate()
            field = newValue
            didmutate()
        }
    internal var currentHolderEmail: String? = null
        set(newValue) {
            willmutate()
            field = newValue
            didmutate()
        }
    internal var currentHolderStreet: String? = null
        set(newValue) {
            willmutate()
            field = newValue
            didmutate()
        }
    internal var currentHolderNumber: String? = null
        set(newValue) {
            willmutate()
            field = newValue
            didmutate()
        }
    internal var currentHolderCity: String? = null
        set(newValue) {
            willmutate()
            field = newValue
            didmutate()
        }
    internal var currentHolderPostalCode: String? = null
        set(newValue) {
            willmutate()
            field = newValue
            didmutate()
        }

    internal constructor(mentorName: String?, batteryId: Int?, currentBatteryUserName: String?, currentBatteryUserId: Int?, currentHolderPhoneNumber: String?, currentHolderEmail: String?, currentHolderStreet: String?, currentHolderNumber: String?, currentHolderCity: String?, currentHolderPostalCode: String?) {
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

    internal constructor(fromDto: ReservationDetailsDto) {
        val dto = fromDto
        this.mentorName = dto.mentorName
        this.batteryId = dto.batteryId
        this.currentBatteryUserName = dto.currentBatteryUserName
        this.currentBatteryUserId = dto.currentBatteryUserId
        this.currentHolderPhoneNumber = dto.currentHolderPhoneNumber
        this.currentHolderEmail = dto.currentHolderEmail
        this.currentHolderStreet = dto.currentHolderStreet
        this.currentHolderNumber = dto.currentHolderNumber
        this.currentHolderCity = dto.currentHolderCity
        this.currentHolderPostalCode = dto.currentHolderPostalCode
    }

    private constructor(copy: MutableStruct) {
        @Suppress("NAME_SHADOWING", "UNCHECKED_CAST") val copy = copy as ReservationDetails
        this.mentorName = copy.mentorName
        this.batteryId = copy.batteryId
        this.currentBatteryUserName = copy.currentBatteryUserName
        this.currentBatteryUserId = copy.currentBatteryUserId
        this.currentHolderPhoneNumber = copy.currentHolderPhoneNumber
        this.currentHolderEmail = copy.currentHolderEmail
        this.currentHolderStreet = copy.currentHolderStreet
        this.currentHolderNumber = copy.currentHolderNumber
        this.currentHolderCity = copy.currentHolderCity
        this.currentHolderPostalCode = copy.currentHolderPostalCode
    }

    override var supdate: ((Any) -> Unit)? = null
    override var smutatingcount = 0
    override fun scopy(): MutableStruct = ReservationDetails(this as MutableStruct)
}
