//
//  ReservationService.swift
//  MADiOS
//
//  Created by Bram Rampelberg on 04/01/2025.
//  Copyright © 2025 HOGENT. All rights reserved.
//

package thesis.skip.app

import skip.lib.*

import skip.foundation.*

internal class ReservationService {

    private val serviceHelper = APIServiceHelper.shared

    internal val logger = SkipLogger(subsystem = "be.hogent.ThesisSkipApp", category = "ThesisSkipApp")

    internal suspend fun fetchReservations(isPast: Boolean, isCanceled: Boolean, cursor: Int?, pageSize: Int): Result<ReservationsPageDto> = Async.run l@{
        val queryParams: Dictionary<String, String> = dictionaryOf(
            Tuple2("cursor", if (cursor != null) String(cursor!!) else null),
            Tuple2("isNextPage", String(true)),
            Tuple2("getPast", String(isPast)),
            Tuple2("canceled", String(isCanceled)),
            Tuple2("pageSize", String(pageSize))
        ).compactMapValues { it -> it }
        return@l serviceHelper.sendRequest(to = "api/Reservation/me", method = HTTPMethod.get, queryParams = queryParams)
    }

    internal suspend fun fetchReservationDetails(for_: Reservation): Result<ReservationDetailsDto> = Async.run l@{
        val reservation = for_
        return@l serviceHelper.sendRequest(to = "api/Reservation/${reservation.id}", method = HTTPMethod.get)
    }

    internal suspend fun cancelReservation(reservation: Reservation): Result<EmptyBody> = Async.run l@{
        return@l serviceHelper.sendRequest(to = "api/Reservation/cancel/${reservation.id}", method = HTTPMethod.patch)
    }

    private constructor() {
    }

    companion object {
        internal val shared = ReservationService()
    }
}
