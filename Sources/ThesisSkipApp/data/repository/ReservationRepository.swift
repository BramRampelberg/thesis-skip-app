//
//  OfflineFirstReservationRepository.swift
//  MADiOS
//
//  Created by Bram Rampelberg on 02/01/2025.
//  Copyright © 2025 HOGENT. All rights reserved.
//

import Foundation

final class ReservationRepository {
    nonisolated(unsafe) static let shared = ReservationRepository()

    private let reservationService = ReservationService.shared

    private let pageSize = 20

    func getReservations(isPast: Bool, isCanceled: Bool) async -> Result<
        [Reservation]
    > {
        var currentCursor: Int? = nil
        var hasMorePages = true
        var reservations = [ReservationDto]()

        while hasMorePages {

            let result = await reservationService.fetchReservations(
                isPast: isPast,
                isCanceled: isCanceled,
                cursor: currentCursor,
                pageSize: pageSize
            )

            if result.isFailure {
                hasMorePages = false
                return .failure(
                    cause: result.failureCause!,
                    failureError: result.error
                )
            }

            let data = result.data!

            reservations.append(contentsOf: data.data)
            currentCursor = data.nextId
            hasMorePages = data.nextId != nil && !data.data.isEmpty
        }
        
        return .success(
            resultData: reservations.map({ ReservationDto in
                Reservation(fromDto: ReservationDto)
            })
        )
    }

    func getReservationDetails(for reservation: Reservation) async -> Result<
        ReservationDetails
    > {
        let result = await reservationService.fetchReservationDetails(
            for: reservation
        )
        return result.isSuccess
            ? .success(resultData: ReservationDetails(fromDto: result.data!))
            : .failure(
                cause: "Failed to get reservation details.",
                failureError: result.error
            )
    }

    func cancelReservation(_ reservation: Reservation) async -> Result<Void> {
        let result = await reservationService.cancelReservation(reservation)
        #if SKIP
            return result.isSuccess
                ? .success(resultData: Void)
                : .failure(cause: result.failureCause!, failureError: result.error)
        #else
            return result.isSuccess
                ? .success(resultData: Void())
                : .failure(cause: result.failureCause!, failureError: result.error)
        #endif
    }

    private init() {}
}
