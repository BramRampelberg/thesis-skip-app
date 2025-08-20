//
//  Navigation.swift
//  MADiOS
//
//  Created by Bram Rampelberg on 01/01/2025.
//  Copyright © 2025 HOGENT. All rights reserved.
//

package thesis.skip.app

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import skip.lib.*

import skip.ui.*
import skip.foundation.*
import skip.model.*

internal class Navigation: View {
    private var verticalSizeClass: UserInterfaceSizeClass? = null
    private var loginViewModel: LoginViewModel
        get() = _loginViewModel.wrappedValue
        set(newValue) {
            _loginViewModel.wrappedValue = newValue
        }
    private var _loginViewModel: skip.ui.State<LoginViewModel>
    private var reservationsViewModel: ReservationsViewModel
        get() = _reservationsViewModel.wrappedValue
        set(newValue) {
            _reservationsViewModel.wrappedValue = newValue
        }
    private var _reservationsViewModel: skip.ui.State<ReservationsViewModel>
    private var profileViewModel: ProfileViewModel
        get() = _profileViewModel.wrappedValue
        set(newValue) {
            _profileViewModel.wrappedValue = newValue
        }
    private var _profileViewModel: skip.ui.State<ProfileViewModel>
    internal var selectedPage: Page
        get() = _selectedPage.wrappedValue
        set(newValue) {
            _selectedPage.wrappedValue = newValue
        }
    internal var _selectedPage: skip.ui.State<Page>
    private val isLoggedIn: Boolean
        get() = loginViewModel.loginState.isLoggedIn

    override fun body(): View {
        return ComposeBuilder { composectx: ComposeContext ->
            VStack { ->
                ComposeBuilder { composectx: ComposeContext ->
                    if (!isLoggedIn) {
                        LoginPage().environmentObject(loginViewModel).transition(AnyTransition.move(edge = Edge.leading)).Compose(composectx)
                    } else if (verticalSizeClass == UserInterfaceSizeClass.compact) {
                        compactLandscapeNavigation.Compose(composectx)
                    } else {
                        portraitNavigation.Compose(composectx)
                    }
                    ComposeResult.ok
                }
            }
            .onAppear { ->
                if (isLoggedIn) {
                    profileViewModel.getUser()
                    reservationsViewModel.getReservations()
                }
            }
            .onChange(of = loginViewModel.loginState.isLoggedIn) { _, isLoggedIn ->
                if (isLoggedIn) {
                    withAnimation { -> selectedPage = Page.reservations }
                }
            }.Compose(composectx)
        }
    }

    @Composable
    @Suppress("UNCHECKED_CAST")
    override fun Evaluate(context: ComposeContext, options: Int): kotlin.collections.List<Renderable> {
        val rememberedloginViewModel by rememberSaveable(stateSaver = context.stateSaver as Saver<skip.ui.State<LoginViewModel>, Any>) { mutableStateOf(_loginViewModel) }
        _loginViewModel = rememberedloginViewModel

        val rememberedreservationsViewModel by rememberSaveable(stateSaver = context.stateSaver as Saver<skip.ui.State<ReservationsViewModel>, Any>) { mutableStateOf(_reservationsViewModel) }
        _reservationsViewModel = rememberedreservationsViewModel

        val rememberedprofileViewModel by rememberSaveable(stateSaver = context.stateSaver as Saver<skip.ui.State<ProfileViewModel>, Any>) { mutableStateOf(_profileViewModel) }
        _profileViewModel = rememberedprofileViewModel

        val rememberedselectedPage by rememberSaveable(stateSaver = context.stateSaver as Saver<skip.ui.State<Page>, Any>) { mutableStateOf(_selectedPage) }
        _selectedPage = rememberedselectedPage

        this.verticalSizeClass = EnvironmentValues.shared.verticalSizeClass

        return super.Evaluate(context, options)
    }

    private val portraitNavigation: View
        get() {
            return TabView(selection = Binding({ _selectedPage.wrappedValue }, { it -> _selectedPage.wrappedValue = it })) { ->
                ComposeBuilder { composectx: ComposeContext ->
                    ReservationsPage().environmentObject(reservationsViewModel)
                        .tabItem { ->
                            ComposeBuilder { composectx: ComposeContext ->
                                Label(LocalizedStringKey(stringLiteral = "Reservations"), systemImage = "calendar").Compose(composectx)
                                ComposeResult.ok
                            }
                        }.tag(Page.reservations).Compose(composectx)
                    ProfilePage().environmentObject(profileViewModel).environmentObject(loginViewModel).tabItem { ->
                        ComposeBuilder { composectx: ComposeContext ->
                            Label(LocalizedStringKey(stringLiteral = "Profile"), systemImage = "person.crop.circle.fill").Compose(composectx)
                            ComposeResult.ok
                        }
                    }.tag(Page.profile).Compose(composectx)
                    ComposeResult.ok
                }
            }.transition(AnyTransition.move(edge = Edge.trailing))
        }

    private val compactLandscapeNavigation: View
        get() {
            return HStack { ->
                ComposeBuilder { composectx: ComposeContext ->
                    sidebar.Compose(composectx)
                    MaximizedContainer { ->
                        ComposeBuilder { composectx: ComposeContext ->
                            when (selectedPage) {
                                Page.reservations -> ReservationsPage().Compose(composectx)
                                Page.profile -> ProfilePage().environmentObject(loginViewModel).Compose(composectx)
                            }
                            ComposeResult.ok
                        }
                    }.Compose(composectx)
                    ComposeResult.ok
                }
            }.ignoresSafeArea(edges = Edge.Set.leading)
        }

    private val sidebar: View
        get() {
            return List { ->
                ComposeBuilder { composectx: ComposeContext ->
                    SidebarButton(selectedPage = Binding({ _selectedPage.wrappedValue }, { it -> _selectedPage.wrappedValue = it }), page = Page.reservations, label = "Calendar", systemImage = "calendar").Compose(composectx)
                    SidebarButton(selectedPage = Binding({ _selectedPage.wrappedValue }, { it -> _selectedPage.wrappedValue = it }), page = Page.profile, label = "Profile", systemImage = "person.crop.circle.fill").Compose(composectx)
                    ComposeResult.ok
                }
            }
            .frame(maxWidth = Double(225))
        }

    internal class SidebarButton: View {
        internal var selectedPage: Page
            get() = _selectedPage.wrappedValue
            set(newValue) {
                _selectedPage.wrappedValue = newValue
            }
        internal var _selectedPage: Binding<Page>
        internal val page: Page
        internal val label: String
        internal val systemImage: String

        override fun body(): View {
            return ComposeBuilder { composectx: ComposeContext ->
                Button(action = { -> selectedPage = page }, label = { ->
                    ComposeBuilder { composectx: ComposeContext ->
                        Label(label, systemImage = systemImage).Compose(composectx)
                        ComposeResult.ok
                    }
                })
                .buttonStyle(ButtonStyle.plain)
                .listRowBackground(if (selectedPage == page) Color.gray.opacity(0.2) else Color.clear).Compose(composectx)
            }
        }

        constructor(selectedPage: Binding<Page>, page: Page, label: String, systemImage: String) {
            this._selectedPage = selectedPage
            this.page = page
            this.label = label
            this.systemImage = systemImage
        }
    }

    private constructor(loginViewModel: LoginViewModel = LoginViewModel(), reservationsViewModel: ReservationsViewModel = ReservationsViewModel(), profileViewModel: ProfileViewModel = ProfileViewModel(), selectedPage: Page = Page.reservations, privatep: Nothing? = null) {
        this._loginViewModel = skip.ui.State(loginViewModel)
        this._reservationsViewModel = skip.ui.State(reservationsViewModel)
        this._profileViewModel = skip.ui.State(profileViewModel)
        this._selectedPage = skip.ui.State(selectedPage)
    }

    constructor(selectedPage: Page = Page.reservations): this(selectedPage = selectedPage, privatep = null) {
    }
}

internal enum class Page {
    reservations,
    profile;
}

// #Preview omitted
