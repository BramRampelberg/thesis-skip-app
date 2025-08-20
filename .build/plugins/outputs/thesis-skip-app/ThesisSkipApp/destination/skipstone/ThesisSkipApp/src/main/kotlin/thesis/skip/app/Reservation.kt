//
//  ReservationModel.swift
//  MADiOS
//
//  Created by Bram Rampelberg on 02/01/2025.
//  Copyright © 2025 HOGENT. All rights reserved.
//

package thesis.skip.app

import skip.lib.*


import skip.foundation.*


@Suppress("MUST_BE_INITIALIZED")
internal class Reservation: Identifiable<Int>, MutableStruct {
    internal var start: Date
        get() = field.sref({ this.start = it })
        set(newValue) {
            @Suppress("NAME_SHADOWING") val newValue = newValue.sref()
            willmutate()
            field = newValue
            didmutate()
        }
    internal var end: Date
        get() = field.sref({ this.end = it })
        set(newValue) {
            @Suppress("NAME_SHADOWING") val newValue = newValue.sref()
            willmutate()
            field = newValue
            didmutate()
        }
    internal var date: Date
        get() = field.sref({ this.date = it })
        set(newValue) {
            @Suppress("NAME_SHADOWING") val newValue = newValue.sref()
            willmutate()
            field = newValue
            didmutate()
        }
    internal var boatId: Int
        set(newValue) {
            willmutate()
            field = newValue
            didmutate()
        }
    internal var boatPersonalName: String
        set(newValue) {
            willmutate()
            field = newValue
            didmutate()
        }
    override var id: Int
        set(newValue) {
            willmutate()
            field = newValue
            didmutate()
        }
    internal var isDeleted: Boolean
        set(newValue) {
            willmutate()
            field = newValue
            didmutate()
        }

    internal constructor(start: Date, end: Date, date: Date, boatId: Int, boatPersonalName: String, id: Int, isDeleted: Boolean) {
        this.start = start
        this.end = end
        this.date = date
        this.boatId = boatId
        this.boatPersonalName = boatPersonalName
        this.id = id
        this.isDeleted = isDeleted
    }

    internal constructor(fromDto: ReservationDto) {
        val dto = fromDto
        start = dto.start
        end = dto.end
        date = dto.date
        boatId = dto.boatId
        boatPersonalName = dto.boatPersonalName
        id = dto.id
        isDeleted = dto.isDeleted
    }

    internal val identifier: Int
        get() = id

    private constructor(copy: MutableStruct) {
        @Suppress("NAME_SHADOWING", "UNCHECKED_CAST") val copy = copy as Reservation
        this.start = copy.start
        this.end = copy.end
        this.date = copy.date
        this.boatId = copy.boatId
        this.boatPersonalName = copy.boatPersonalName
        this.id = copy.id
        this.isDeleted = copy.isDeleted
    }

    override var supdate: ((Any) -> Unit)? = null
    override var smutatingcount = 0
    override fun scopy(): MutableStruct = Reservation(this as MutableStruct)

    override fun equals(other: Any?): Boolean {
        if (other !is Reservation) return false
        return start == other.start && end == other.end && date == other.date && boatId == other.boatId && boatPersonalName == other.boatPersonalName && id == other.id && isDeleted == other.isDeleted
    }

    override fun hashCode(): Int {
        var result = 1
        result = Hasher.combine(result, start)
        result = Hasher.combine(result, end)
        result = Hasher.combine(result, date)
        result = Hasher.combine(result, boatId)
        result = Hasher.combine(result, boatPersonalName)
        result = Hasher.combine(result, id)
        result = Hasher.combine(result, isDeleted)
        return result
    }
}
