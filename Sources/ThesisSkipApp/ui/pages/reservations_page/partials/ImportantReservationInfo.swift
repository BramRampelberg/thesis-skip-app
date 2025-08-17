//
//  ImportantReservationInfo.swift
//  MADiOS
//
//  Created by Bram Rampelberg on 02/01/2025.
//  Copyright © 2025 HOGENT. All rights reserved.
//

import SwiftUI

struct ImportantReservationInfo: View {
    let reservation: Reservation

    var body: some View {
        VStack(alignment: .leading) {
            Label(
                reservation.date.formatted(date: .abbreviated, time: .omitted),
                systemImage: "calendar"
            )
            .font(.headline)
            .foregroundColor(.primary)

            Label {
                Text(
                    "\(reservation.start.formatted(date: .omitted, time: .shortened)) - \(reservation.end.formatted(date: .omitted, time: .shortened))"
                )
            } icon: {
                Image("clock", bundle: .module)
                    .resizable()
                    .aspectRatio(contentMode: .fit)
                    .frame(width: 20, height: 20)
            }.font(.subheadline)
                .foregroundColor(.primary)

            Label {
                Text(reservation.boatPersonalName)
            } icon: {
                Image("sailboat.fill", bundle: .module)
                    .resizable()
                    .aspectRatio(contentMode: .fit)
                    .frame(width: 26, height: 26)
            }.font(.subheadline)
                .foregroundColor(.secondary)
        }.padding(.vertical, 5)
    }
}

#Preview {
    ImportantReservationInfo(
        reservation: Reservation(
            start: Date(),
            end: Date(),
            date: Date(),
            boatId: 1,
            boatPersonalName: "test",
            id: 1,
            isDeleted: false
        )
    )
}
