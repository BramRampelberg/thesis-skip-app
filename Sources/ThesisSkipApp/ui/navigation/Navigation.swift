//
//  Navigation.swift
//  MADiOS
//
//  Created by Bram Rampelberg on 01/01/2025.
//  Copyright © 2025 HOGENT. All rights reserved.
//

import SwiftUI

struct Navigation: View {
    @Environment(\.verticalSizeClass) private var verticalSizeClass
    @StateObject private var loginViewModel = LoginViewModel()
    @StateObject private var reservationsViewModel = ReservationsViewModel()
    @StateObject private var profileViewModel = ProfileViewModel()
    @State var selectedPage = Page.reservations
    @State private var isLoggedIn = false

    var body: some View {
        VStack {
            if !isLoggedIn {
                LoginPage().environmentObject(loginViewModel).transition(
                    .move(edge: .leading)
                )
            } else {
                if verticalSizeClass == .compact {
                    compactLandscapeNavigation
                } else {
                    portraitNavigation
                }
            }
        }
        .onAppear {
            isLoggedIn = loginViewModel.loginState.isLoggedIn
        }
        .onChange(of: loginViewModel.loginState.isLoggedIn) { _, newState in
            withAnimation {
                self.isLoggedIn = newState
                selectedPage = .reservations
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
            .onChange(of: selectedPage) { _, newValue in
                if newValue == .reservations {
                    //Force refresh of reservations
                    reservationsViewModel.selectedReservationType =
                        reservationsViewModel.selectedReservationType
                } else if newValue == .profile {
                    profileViewModel.refreshUserData()
                }
            }
    }

    private var compactLandscapeNavigation: some View {
        HStack {
            sidebar
            MaximizedContainer {
                switch selectedPage {
                case .reservations:
                    ReservationsPage()
                case .profile:
                    ProfilePage().environmentObject(loginViewModel)
                }
            }
        }.ignoresSafeArea(edges: .leading)
    }

    private var sidebar: some View {
        List {
            SidebarButton(
                selectedPage: $selectedPage,
                page: .reservations,
                label: "Calendar",
                systemImage: "calendar"
            )
            SidebarButton(
                selectedPage: $selectedPage,
                page: .profile,
                label: "Profile",
                systemImage: "person.crop.circle.fill"
            )
        }
        .frame(maxWidth: CGFloat(225))
    }

    struct SidebarButton: View {
        @Binding var selectedPage: Page
        let page: Page
        let label: String
        let systemImage: String

        var body: some View {
            Button {
                selectedPage = page
            } label: {
                Label(label, systemImage: systemImage)
            }
            .buttonStyle(.plain)
            .listRowBackground(
                selectedPage == page ? Color.gray.opacity(0.2) : Color.clear
            )
        }
    }
}

enum Page: Hashable {
    case reservations
    case profile
}

#Preview {
    Navigation()
}
