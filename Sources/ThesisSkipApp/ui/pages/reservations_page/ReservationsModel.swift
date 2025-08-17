//
//  ReservationsModel.swift
//  MADiOS
//
//  Created by Bram Rampelberg on 02/01/2025.
//  Copyright © 2025 HOGENT. All rights reserved.
//

import Foundation

struct ReservationsModel {
    private(set) var reservationsState: ReservationsState = .loading
    private(set) var selectedReservation: Reservation?
    private(set) var selectedReservationType: ReservationType
    private(set) var reservationDetailsState: ReservationDetailsState = .unselected
    
    mutating func setReservationsState(to reservationsState: ReservationsState){
        self.reservationsState = reservationsState
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

enum ReservationsState{
    case resting([Reservation])
    case loading
    case error(String)
    
    var reservations: [Reservation] {
        switch self {
        case.resting(let reservations): return reservations
        default: return []
        }
    }
    
    var isLoading: Bool {
        switch self {
        case .loading: return true
        default: return false
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
