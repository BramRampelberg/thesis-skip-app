//
//  ReservationService.swift
//  MADiOS
//
//  Created by Bram Rampelberg on 04/01/2025.
//  Copyright © 2025 HOGENT. All rights reserved.
//

import Foundation
import os

final class ReservationService {
    nonisolated(unsafe) static let shared = ReservationService()
    
    private let serviceHelper = APIServiceHelper.shared
    
    let logger = Logger()
    
    func fetchReservations(isPast: Bool, isCanceled: Bool, cursor: Int?, pageSize: Int) async -> Result<ItemsPageDto<ReservationDto>> {
        let queryParams =  [
            "cursor": cursor != nil ? String(cursor!) : nil,
            "isNextPage": String(true),
            "getPast": String(isPast),
            "canceled": String(isCanceled),
            "pageSize": String(pageSize)
        ].compactMapValues{
            $0
        }
        return await serviceHelper.sendRequest(to: "api/Reservation/me", method: HTTPMethod.get, queryParams: queryParams)
    }
    
    func fetchReservationDetails(for reservation: Reservation) async -> Result<ReservationDetailsDto> {
        return await serviceHelper.sendRequest(to: "api/Reservation/\(reservation.id)", method: .get)
    }
    
    func cancelReservation(_ reservation: Reservation) async -> Result<EmptyBody> {
        return await serviceHelper.sendRequest(to: "api/Reservation/cancel/\(reservation.id)", method: .patch)
    }
    
    private init(){}
}
