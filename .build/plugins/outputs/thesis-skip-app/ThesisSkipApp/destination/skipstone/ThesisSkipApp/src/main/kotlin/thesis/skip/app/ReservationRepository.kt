//
//  OfflineFirstReservationRepository.swift
//  MADiOS
//
//  Created by Bram Rampelberg on 02/01/2025.
//  Copyright © 2025 HOGENT. All rights reserved.
//

package thesis.skip.app

import skip.lib.*
import skip.lib.Array

import skip.foundation.*

internal class ReservationRepository {

    private val reservationService = ReservationService.shared

    private val pageSize = 20

    internal suspend fun getReservations(isPast: Boolean, isCanceled: Boolean): Result<Array<Reservation>> = Async.run l@{
        var currentCursor: Int? = null
        var hasMorePages = true
        var reservations = Array<ReservationDto>()

        while (hasMorePages) {

            val result = reservationService.fetchReservations(isPast = isPast, isCanceled = isCanceled, cursor = currentCursor, pageSize = pageSize)

            if (result.isFailure) {
                hasMorePages = false
                return@l Result.failure(cause = result.failureCause!!, failureError = result.error)
            }

            val data = result.data!!

            reservations.append(contentsOf = data.data)
            currentCursor = data.nextId
            hasMorePages = data.nextId != null && !data.data.isEmpty
        }

        return@l Result.success(resultData = reservations.map({ ReservationDto -> Reservation(fromDto = ReservationDto) }))
    }

    internal suspend fun getReservationDetails(for_: Reservation): Result<ReservationDetails> = Async.run l@{
        val reservation = for_
        val result = reservationService.fetchReservationDetails(for_ = reservation)
        return@l if (result.isSuccess) Result.success(resultData = ReservationDetails(fromDto = result.data!!)) else Result.failure(cause = "Failed to get reservation details.", failureError = result.error)
    }

    internal suspend fun cancelReservation(reservation: Reservation): Result<Unit> = Async.run l@{
        val result = reservationService.cancelReservation(reservation)
        return@l if (result.isSuccess) Result.success(resultData = Unit) else Result.failure(cause = result.failureCause!!, failureError = result.error)
    }

    private constructor() {
    }

    companion object {
        internal val shared = ReservationRepository()
    }
}
