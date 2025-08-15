//
//  ReservationTypePicker.swift
//  MADiOS
//
//  Created by Bram Rampelberg on 02/01/2025.
//  Copyright © 2025 HOGENT. All rights reserved.
//

import SwiftUI

struct ReservationTypePicker: View {
    @EnvironmentObject var reservationsViewModel: ReservationsViewModel
    
    var body: some View {
        VStack(spacing: 0) {
            HStack {
                Text("Type:")
                    .font(.headline)
                Picker("Type", selection: $reservationsViewModel.selectedReservationType) {
                    Text("Upcoming").tag(ReservationType.upcoming)
                    Text("Past").tag(ReservationType.past)
                    Text("Canceled").tag(ReservationType.canceled)
                }
                Spacer()
            }
            .padding(.horizontal, 20)
            .padding(.bottom, 16)
            
            Divider()
                .background(Color.gray)
                .frame(height: 1)
        }
    }
}

#Preview {
    ReservationTypePicker()
}
