//
//  Navigation.swift
//  MADiOS
//
//  Created by Bram Rampelberg on 01/01/2025.
//  Copyright © 2025 HOGENT. All rights reserved.
//

import SwiftUI

struct Navigation: View {
    @StateObject private var loginViewModel = LoginViewModel()
    @StateObject private var reservationsViewModel = ReservationsViewModel()
    @StateObject private var profileViewModel = ProfileViewModel()
    @State var selectedPage = Page.reservations
    private var isLoggedIn: Bool { loginViewModel.loginState.isLoggedIn }

    var body: some View {
        VStack {
            if !isLoggedIn {
                LoginPage().environmentObject(loginViewModel).transition(
                    .move(edge: .leading)
                )
            } else {
                portraitNavigation
            }
        }
        .onAppear {
            if isLoggedIn {
                profileViewModel.getUser()
                reservationsViewModel.getReservations()
            }
        }
        .onChange(of: loginViewModel.loginState.isLoggedIn) { _, isLoggedIn in
            if isLoggedIn {
                withAnimation {
                    selectedPage = .reservations
                }
            }
        }
    }

    private var portraitNavigation: some View {
        TabView(selection: $selectedPage) {
            ReservationsPage().environmentObject(reservationsViewModel)
                .tabItem {
                    Label("Reservations", systemImage: "calendar")
                }.tag(Page.reservations)
            ProfilePage().environmentObject(profileViewModel).environmentObject(
                loginViewModel
            ).tabItem {
                Label("Profile", systemImage: "person.crop.circle.fill")
            }.tag(Page.profile)
        }.transition(.move(edge: .trailing))
    }
}

enum Page: Hashable {
    case reservations
    case profile
}

#Preview {
    Navigation()
}
