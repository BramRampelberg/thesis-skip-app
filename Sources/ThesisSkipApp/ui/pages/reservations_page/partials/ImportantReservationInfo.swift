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
        VStack(alignment: .leading){
            Label(reservation.date.formatted(date: .abbreviated, time: .omitted), systemImage: "calendar")
                .font(.headline)
                .foregroundColor(.primary)
            Label("\(reservation.start.formatted(date: .omitted, time: .shortened)) - \(reservation.end.formatted(date: .omitted, time: .shortened))", systemImage: "clock")
                .font(.subheadline)
                .foregroundColor(.primary)
            Label(reservation.boatPersonalName, systemImage: "sailboat.fill")
                .font(.subheadline)
                .foregroundColor(.secondary)
        }.padding(.vertical, 5)
    }
}

#Preview {
    ImportantReservationInfo(reservation: Reservation(start: Date(), end: Date(), date: Date(), boatId: 1, boatPersonalName: "test", id: 1, isDeleted: false))
}
