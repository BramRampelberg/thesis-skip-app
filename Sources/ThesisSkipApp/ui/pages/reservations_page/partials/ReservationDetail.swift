//
//  ReservationDetail.swift
//  MADiOS
//
//  Created by Bram Rampelberg on 02/01/2025.
//  Copyright © 2025 HOGENT. All rights reserved.
//

import SwiftUI

struct ReservationDetail: View {
    @Environment(\.verticalSizeClass) private var verticalSizeClass
    @EnvironmentObject var reservationViewModel: ReservationsViewModel
    let reservation: Reservation
    let reservationDetails: ReservationDetails?
    let isReservationCancelable: Bool
    
    @State private var showCancelErrorAlert = false
    
    var body: some View {
        Form {
            Section(header: Text("Reservation Details").font(.title2)) {
                ImportantReservationInfo(reservation: reservation)
            }
            
            if reservationDetailsAreValid() {
                Section(header: Text("Acceptee Info")) {
                    infoRow(label: "Name", value: reservationDetails!.currentBatteryUserName!)
                    infoRow(label: "Phone", value: reservationDetails!.currentHolderPhoneNumber!)
                    infoRow(label: "E-mail", value: reservationDetails!.currentHolderEmail!)
                }
                
                Section(header: Text("Address")) {
                    infoRow(label: "Street", value: "\(reservationDetails!.currentHolderStreet!) \(reservationDetails!.currentHolderNumber!)")
                    infoRow(label: "City", value: "\(reservationDetails!.currentHolderPostalCode!) \(reservationDetails!.currentHolderCity!)")
                }
                
                if let mentorName = reservationDetails?.mentorName, !mentorName.isEmpty {
                    Section(header: Text("Mentor")) {
                        Text(mentorName)
                    }
                }
            } else {
                Section {
                    detailsNotAvailable
                }
            }
            
            if reservationViewModel.isCancelationPending {
                Section {
                    HStack {
                        Spacer()
                        ProgressView().padding()
                        Spacer()
                    }
                }
            }
            
            if isReservationCancelable {
                Section {
                    cancelButton
                }
            }
        }
//        .presentationDetents([
//            reservationDetailsAreValid() ?
//            UIDevice.current.userInterfaceIdiom == .pad ? .fraction(0.77) : .fraction(0.7)
//            :
//                UIDevice.current.userInterfaceIdiom == .pad ? .medium : .fraction(0.5),
//            .large
//        ])
    }
    
    func infoRow(label: String, value: String) -> some View {
        HStack {
            Text(label)
                .foregroundColor(.secondary)
            Spacer()
            Text(value)
                .multilineTextAlignment(.trailing)
        }
    }
    
    var detailsNotAvailable: some View {
        VStack {
            Image(systemName: "info.circle.fill")
                .resizable()
                .frame(width: 48, height: 48)
                .foregroundColor(.blue)
                .padding(.top, 20)
            
            Text("No pickup information available")
                .font(.body)
                .multilineTextAlignment(.center)
                .padding(.top, 8)
        }
        .frame(maxWidth: .infinity)
    }
    
    var cancelButton: some View {
        Button(role: .destructive, action: {reservationViewModel.cancelReservation()}) {
            Label("Cancel Reservation", systemImage: "xmark.circle.fill")
                .frame(maxWidth: .infinity)
                .padding()
                .foregroundColor(.red)
        }
    }
    
    func reservationDetailsAreValid() -> Bool {
        return !(
            reservationDetails?.currentBatteryUserName?.isEmpty ?? true ||
            reservationDetails?.currentHolderPhoneNumber?.isEmpty ?? true ||
            reservationDetails?.currentHolderEmail?.isEmpty ?? true ||
            reservationDetails?.currentHolderStreet?.isEmpty ?? true ||
            reservationDetails?.currentHolderNumber?.isEmpty ?? true ||
            reservationDetails?.currentHolderCity?.isEmpty ?? true ||
            reservationDetails?.currentHolderPostalCode?.isEmpty ?? true
        )
    }
}

#Preview {
    @Previewable var reservationViewModel = ReservationsViewModel()
    
    ReservationDetail(
        reservation: Reservation(start: Date(), end: Date(), date: Date(), boatId: 1, boatPersonalName: "boatName", id: 0, isDeleted: false),
        reservationDetails: ReservationDetails(mentorName: "mentor", batteryId: 1, currentBatteryUserName: "username", currentBatteryUserId: 1, currentHolderPhoneNumber: "phonenumber", currentHolderEmail: "email", currentHolderStreet: "street", currentHolderNumber: "number", currentHolderCity: "city", currentHolderPostalCode: "postalCode"),
        isReservationCancelable: true
    ).environmentObject(reservationViewModel)
}


