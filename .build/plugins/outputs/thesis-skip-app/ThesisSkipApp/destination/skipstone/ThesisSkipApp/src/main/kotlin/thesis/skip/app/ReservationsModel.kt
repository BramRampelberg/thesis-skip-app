//
//  ReservationsModel.swift
//  MADiOS
//
//  Created by Bram Rampelberg on 02/01/2025.
//  Copyright © 2025 HOGENT. All rights reserved.
//

package thesis.skip.app

import skip.lib.*
import skip.lib.Array

import skip.foundation.*

@Suppress("MUST_BE_INITIALIZED")
internal class ReservationsModel: MutableStruct {
    internal var reservationsState: ReservationsState
        private set(newValue) {
            willmutate()
            field = newValue
            didmutate()
        }
    internal var selectedReservation: Reservation? = null
        get() = field.sref({ this.selectedReservation = it })
        private set(newValue) {
            @Suppress("NAME_SHADOWING") val newValue = newValue.sref()
            willmutate()
            field = newValue
            didmutate()
        }
    internal var selectedReservationType: ReservationType
        private set(newValue) {
            willmutate()
            field = newValue
            didmutate()
        }
    internal var reservationDetailsState: ReservationDetailsState
        private set(newValue) {
            willmutate()
            field = newValue
            didmutate()
        }

    internal fun setReservationsState(to: ReservationsState) {
        val reservationsState = to
        willmutate()
        try {
            this.reservationsState = reservationsState
        } finally {
            didmutate()
        }
    }

    internal fun changeReservationDetailsState(to: ReservationDetailsState) {
        val state = to
        willmutate()
        try {
            reservationDetailsState = state
        } finally {
            didmutate()
        }
    }

    internal fun changeSelectedReservation(to: Reservation?) {
        val reservation = to
        willmutate()
        try {
            selectedReservation = reservation
        } finally {
            didmutate()
        }
    }

    internal fun changeSelectedReservationType(to: ReservationType) {
        val type = to
        willmutate()
        try {
            selectedReservationType = type
        } finally {
            didmutate()
        }
    }

    constructor(reservationsState: ReservationsState = ReservationsState.loading, selectedReservation: Reservation? = null, selectedReservationType: ReservationType, reservationDetailsState: ReservationDetailsState = ReservationDetailsState.unselected) {
        this.reservationsState = reservationsState
        this.selectedReservation = selectedReservation
        this.selectedReservationType = selectedReservationType
        this.reservationDetailsState = reservationDetailsState
    }

    override var supdate: ((Any) -> Unit)? = null
    override var smutatingcount = 0
    override fun scopy(): MutableStruct = ReservationsModel(reservationsState, selectedReservation, selectedReservationType, reservationDetailsState)
}

internal sealed class ReservationsState {
    class RestingCase(val associated0: Array<Reservation>): ReservationsState() {
    }
    class LoadingCase: ReservationsState() {
    }
    class ErrorCase(val associated0: String): ReservationsState() {
    }

    internal val reservations: Array<Reservation>
        get() {
            when (this) {
                is ReservationsState.RestingCase -> {
                    val reservations = this.associated0
                    return reservations
                }
                else -> return arrayOf()
            }
        }

    internal val isLoading: Boolean
        get() {
            when (this) {
                is ReservationsState.LoadingCase -> return true
                else -> return false
            }
        }

    internal val hasError: Boolean
        get() {
            when (this) {
                is ReservationsState.ErrorCase -> return true
                else -> return false
            }
        }

    internal val errorDescription: String?
        get() {
            when (this) {
                is ReservationsState.ErrorCase -> {
                    val description = this.associated0
                    return description
                }
                else -> return null
            }
        }

    companion object {
        fun resting(associated0: Array<Reservation>): ReservationsState = RestingCase(associated0)
        val loading: ReservationsState = LoadingCase()
        fun error(associated0: String): ReservationsState = ErrorCase(associated0)
    }
}

internal enum class ReservationType {
    upcoming,
    past,
    canceled;
}

internal sealed class ReservationDetailsState {
    class UnselectedCase: ReservationDetailsState() {
    }
    class LoadingCase: ReservationDetailsState() {
    }
    class SelectedCase(val associated0: ReservationDetails): ReservationDetailsState() {
    }
    class ErrorCase(val associated0: String): ReservationDetailsState() {
    }

    internal val isLoading: Boolean
        get() {
            when (this) {
                is ReservationDetailsState.LoadingCase -> return true
                else -> return false
            }
        }

    internal val isSelected: Boolean
        get() {
            when (this) {
                is ReservationDetailsState.SelectedCase -> return true
                else -> return false
            }
        }

    internal val reservationDetails: ReservationDetails?
        get() {
            when (this) {
                is ReservationDetailsState.SelectedCase -> {
                    val details = this.associated0
                    return details
                }
                else -> return null
            }
        }

    internal val hasError: Boolean
        get() {
            when (this) {
                is ReservationDetailsState.ErrorCase -> return true
                else -> return false
            }
        }

    internal val errorDescription: String?
        get() {
            when (this) {
                is ReservationDetailsState.ErrorCase -> {
                    val description = this.associated0
                    return description
                }
                else -> return null
            }
        }

    companion object {
        val unselected: ReservationDetailsState = UnselectedCase()
        val loading: ReservationDetailsState = LoadingCase()
        fun selected(associated0: ReservationDetails): ReservationDetailsState = SelectedCase(associated0)
        fun error(associated0: String): ReservationDetailsState = ErrorCase(associated0)
    }
}
