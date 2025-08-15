//
//  ReservationsPage.swift
//  MADiOS
//
//  Created by Bram Rampelberg on 01/01/2025.
//  Copyright © 2025 HOGENT. All rights reserved.
//

import SwiftUI

struct ReservationsPage: View {
    @EnvironmentObject var reservationsViewModel: ReservationsViewModel

    private var reservationDetailsState: ReservationDetailsState {
        reservationsViewModel.reservationDetailsState
    }

    var body: some View {
        VStack {
            MaximizedContainer {
                VStack(spacing: 0) {
                    ReservationTypePicker()
                    errorMessage
                    ReservationsList()
                }
                .sheet(
                    isPresented: $reservationsViewModel.isReservationSelected
                ) {
                    if reservationDetailsState.isLoading {
                        ProgressView().presentationDetents([.medium, .large])
                    } else if reservationDetailsState.hasError {
                        Text(reservationDetailsState.errorDescription!).font(
                            .title2
                        ).foregroundColor(Colors.red).presentationDetents([
                            .medium, .large,
                        ])
                    } else {
                        ReservationDetail(
                            reservation: reservationsViewModel
                                .selectedReservation!,
                            reservationDetails: reservationDetailsState
                                .reservationDetails,
                            isReservationCancelable: reservationsViewModel
                                .isReservationCancelable
                        )
                        #if !SKIP
                            .presentationContentInteraction(.scrolls)
                        #endif
                    }
                }.environmentObject(reservationsViewModel)
            }
        }
    }

    var errorMessage: some View {
        VStack {
            if reservationsViewModel.hasReservationsError {
                Text(reservationsViewModel.reservationsErrorMessage!)
                    .font(.title)
                    .bold()
                    .frame(minWidth: 0, maxWidth: .infinity)
                    .padding()
                    .foregroundColor(.white)
                    .background((Rectangle().fill(Colors.red)))
                    .multilineTextAlignment(.center)
            }
        }
    }
}

#Preview {
    @Previewable @StateObject var reservationsViewModel =
        ReservationsViewModel()

    ReservationsPage().environmentObject(reservationsViewModel)
}
