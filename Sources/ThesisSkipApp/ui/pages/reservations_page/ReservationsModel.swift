//
//  ReservationsModel.swift
//  MADiOS
//
//  Created by Bram Rampelberg on 02/01/2025.
//  Copyright © 2025 HOGENT. All rights reserved.
//

import Foundation

struct ReservationsModel {
    private(set) var reservations: [Reservation]
    private(set) var reservationsErrorMessage: String?
    private(set) var selectedReservation: Reservation?
    private(set) var selectedReservationType: ReservationType
    private(set) var reservationDetailsState: ReservationDetailsState = .unselected
    
    mutating func setReservations(to reservations: [Reservation]){
        self.reservations = reservations
    }
    
    mutating func setReservationsErrorMessage(to message: String?){
        self.reservationsErrorMessage = message
    }
    
    mutating func changeReservationDetailsState(to state: ReservationDetailsState){
        reservationDetailsState = state
    }
    
    mutating func changeSelectedReservation(to reservation: Reservation?){
        selectedReservation = reservation
    }
    
    mutating func changeSelectedReservationType(to type: ReservationType){
        selectedReservationType = type
    }
}

enum ReservationType {
    case upcoming
    case past
    case canceled
}

enum ReservationDetailsState {
    case unselected
    case loading
    case selected(ReservationDetails)
    case error(String)
    
    var isLoading: Bool {
        switch self {
        case .loading: return true
        default: return false
        }
    }
    
    var isSelected: Bool {
        switch self {
        case .selected: return true
        default: return false
        }
    }
    
    var reservationDetails: ReservationDetails? {
        switch self {
        case.selected(let details): return details
        default: return nil
        }
    }
    
    var hasError: Bool {
        switch self {
        case .error: return true
        default: return false
        }
    }
    
    var errorDescription: String? {
        switch self {
        case .error(let description): return description
        default: return nil
        }
    }
}
