//
//  ReservationsViewModel.swift
//  MADiOS
//
//  Created by Bram Rampelberg on 02/01/2025.
//  Copyright © 2025 HOGENT. All rights reserved.
//

package thesis.skip.app

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import skip.lib.*
import skip.lib.Array

import skip.foundation.*
import skip.ui.*
import skip.model.*


@Stable
internal open class ReservationsViewModel: ObservableObject {
    override val objectWillChange = ObservableObjectPublisher()
    private val reservationRepo = ReservationRepository.shared

    internal constructor() {
        this._reservationsModel = skip.model.Published(ReservationsModel(reservationsState = ReservationsState.loading, selectedReservationType = ReservationType.upcoming))
    }

    private var reservationsModel: ReservationsModel
        get() = _reservationsModel.wrappedValue.sref({ this.reservationsModel = it })
        set(newValue) {
            objectWillChange.send()
            _reservationsModel.wrappedValue = newValue.sref()
        }
    private var _reservationsModel: skip.model.Published<ReservationsModel>

    private var cancelReservationState: ReservationsViewModel.CancelReservationState
        get() = _cancelReservationState.wrappedValue
        set(newValue) {
            objectWillChange.send()
            _cancelReservationState.wrappedValue = newValue
        }
    private var _cancelReservationState: skip.model.Published<ReservationsViewModel.CancelReservationState> = skip.model.Published(ReservationsViewModel.CancelReservationState.resting)

    internal open val hasCancelError: Boolean
        get() = cancelReservationState.hasError

    internal open val cancelErrorDescription: String?
        get() = cancelReservationState.errorDescription

    internal open val isCancelationPending: Boolean
        get() = cancelReservationState.isLoading

    internal open val hasReservationsError: Boolean
        get() = reservationsModel.reservationsState.hasError

    internal open val reservationsErrorMessage: String?
        get() = reservationsModel.reservationsState.errorDescription

    internal open val reservations: Array<Reservation>
        get() = reservationsModel.reservationsState.reservations

    internal open val areReservationsLoading: Boolean
        get() = reservationsModel.reservationsState.isLoading

    internal open var selectedReservationType: ReservationType
        get() = reservationsModel.selectedReservationType
        set(newValue) {
            reservationsModel.changeSelectedReservationType(to = newValue)
            getReservations()
        }

    internal open val reservationDetailsState: ReservationDetailsState
        get() = reservationsModel.reservationDetailsState

    internal open var selectedReservation: Reservation?
        get() = reservationsModel.selectedReservation.sref({ this.selectedReservation = it })
        set(newValue) {
            @Suppress("NAME_SHADOWING") val newValue = newValue.sref()
            if (newValue == null) {
                deselectReservation()
            } else {
                reservationsModel.changeSelectedReservation(to = newValue)
                changeReservationDetailsSate(for_ = newValue)
            }
        }

    internal open val isReservationCancelable: Boolean
        get() {
            Calendar.current.date(byAdding = Calendar.Component.day, value = 2, to = Date())?.let { currentDatePlusTwoDays ->
                return reservationsModel.selectedReservation != null && reservationsModel.selectedReservation?.isDeleted == false && reservationsModel.selectedReservation!!.date > currentDatePlusTwoDays && (cancelReservationState.isResting || cancelReservationState.hasError)
            }
            return false
        }

    internal open var isReservationSelected: Boolean
        get() = reservationsModel.selectedReservation != null
        set(newValue) {
            if (!newValue) {
                reservationsModel.changeSelectedReservation(to = null)
                reservationsModel.changeReservationDetailsState(to = ReservationDetailsState.unselected)
            }
        }

    internal open fun changeReservationDetailsSate(for_: Reservation?) {
        val reservation = for_
        if (reservation == null) {
            reservationsModel.changeReservationDetailsState(to = ReservationDetailsState.unselected)
        } else {
            reservationsModel.changeReservationDetailsState(to = ReservationDetailsState.loading)
            Task(isMainActor = true) { ->
                val result = reservationRepo.getReservationDetails(for_ = reservation!!)
                if (result.isSuccess) {
                    reservationsModel.changeReservationDetailsState(to = ReservationDetailsState.selected(result.data!!))
                } else {
                    reservationsModel.changeReservationDetailsState(to = ReservationDetailsState.error(result.failureCause!!))
                }
            }
        }
    }

    internal open fun cancelReservation() {
        if (isReservationCancelable) {
            cancelReservationState = ReservationsViewModel.CancelReservationState.loading
            Task(isMainActor = true) { ->
                val result = reservationRepo.cancelReservation(reservationsModel.selectedReservation!!)
                if (result.isSuccess) {
                    cancelReservationState = ReservationsViewModel.CancelReservationState.resting
                    deselectReservation()
                    getReservations()
                } else {
                    cancelReservationState = ReservationsViewModel.CancelReservationState.error(result.failureCause!!)
                }
            }
        }
    }

    private fun deselectReservation() {
        reservationsModel.changeSelectedReservation(to = null)
        changeReservationDetailsSate(for_ = null)
    }

    internal open fun getReservations() {
        val type = reservationsModel.selectedReservationType
        val isPast = type == ReservationType.past
        val isCanceled = type == ReservationType.canceled
        reservationsModel.setReservationsState(to = ReservationsState.loading)
        Task(isMainActor = true) { ->
            val result = reservationRepo.getReservations(isPast = isPast, isCanceled = isCanceled)
            if (result.isSuccess) {
                reservationsModel.setReservationsState(to = ReservationsState.resting(result.data!!))
            } else {
                reservationsModel.setReservationsState(to = ReservationsState.error("Data is not up to date:\n${if (result.failureCause!!.isEmpty) "Network error!" else result.failureCause!!}"))
            }
        }
    }

    internal sealed class CancelReservationState {
        class RestingCase: CancelReservationState() {
        }
        class LoadingCase: CancelReservationState() {
        }
        class ErrorCase(val associated0: String): CancelReservationState() {
        }

        internal val isResting: Boolean
            get() {
                when (this) {
                    is ReservationsViewModel.CancelReservationState.RestingCase -> return true
                    else -> return false
                }
            }

        internal val isLoading: Boolean
            get() {
                when (this) {
                    is ReservationsViewModel.CancelReservationState.LoadingCase -> return true
                    else -> return false
                }
            }

        internal val hasError: Boolean
            get() {
                when (this) {
                    is ReservationsViewModel.CancelReservationState.ErrorCase -> return true
                    else -> return false
                }
            }

        internal val errorDescription: String?
            get() {
                when (this) {
                    is ReservationsViewModel.CancelReservationState.ErrorCase -> {
                        val description = this.associated0
                        return description
                    }
                    else -> return null
                }
            }

        companion object {
            val resting: CancelReservationState = RestingCase()
            val loading: CancelReservationState = LoadingCase()
            fun error(associated0: String): CancelReservationState = ErrorCase(associated0)
        }
    }

}
