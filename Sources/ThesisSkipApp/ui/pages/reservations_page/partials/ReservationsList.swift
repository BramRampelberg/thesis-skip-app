//
//  ReservationsList.swift
//  MADiOS
//
//  Created by Bram Rampelberg on 02/01/2025.
//  Copyright © 2025 HOGENT. All rights reserved.
//

import SwiftUI

struct ReservationsList: View {
    @Environment(\.horizontalSizeClass) var horizontalSizeClass
    @EnvironmentObject var reservationsViewModel: ReservationsViewModel
    @State private var selected: Int? = nil

    var isHorizontalCompact: Bool {
        horizontalSizeClass == .compact
    }

    var body: some View {
        if isHorizontalCompact {
            listView
        } else {
            tableView
        }
    }

    private var listView: some View {
        List(reservationsViewModel.reservations) { reservation in
            Button(action: {
                reservationsViewModel.selectedReservation = reservation
            }) {
                ImportantReservationInfo(reservation: reservation)
            }
        }
    }

    private var tableView: some View {
        Table(reservationsViewModel.reservations, selection: $selected) {
            TableColumn(
                "Date",
                content: { (reservation: Reservation) in
                    Text(
                        "Date: \(reservation.date.formatted(date: .numeric, time: .omitted))"
                    )
                }
            )

            TableColumn(
                "Time",
                content: { (reservation: Reservation) in
                    Text(
                        "\(reservation.start.formatted(date: .omitted, time: .shortened)) - \(reservation.end.formatted(date: .omitted, time: .shortened))"
                    )
                }
            )

            TableColumn(
                "Boat",
                content: { (reservation: Reservation) in
                    Text("Boat: \(reservation.boatPersonalName)")
                }
            )
        }.onChange(of: selected) { oldId, newId in
            let reservation = reservationsViewModel.reservations.first {
                reservation in
                reservation.identifier == newId
            }
            reservationsViewModel.selectedReservation = reservation
        }
    }
}

#Preview {
    ReservationsList()
}
