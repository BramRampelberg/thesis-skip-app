//
//  ReservationsList.swift
//  MADiOS
//
//  Created by Bram Rampelberg on 02/01/2025.
//  Copyright © 2025 HOGENT. All rights reserved.
//

import SwiftUI

struct ReservationsList: View {
    @EnvironmentObject var reservationsViewModel: ReservationsViewModel
    @State private var selected: Int? = nil

    var body: some View {
        List(reservationsViewModel.reservations) { reservation in
            Button(action: {
                reservationsViewModel.selectedReservation = reservation
            }) {
                ImportantReservationInfo(reservation: reservation)
            }
        }
    }
}

#Preview {
    ReservationsList()
}
