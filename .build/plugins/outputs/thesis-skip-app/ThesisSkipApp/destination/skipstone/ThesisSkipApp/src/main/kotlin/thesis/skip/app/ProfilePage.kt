//
//  ProfilePage.swift
//  MADiOS
//
//  Created by Bram Rampelberg on 03/01/2025.
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

@Suppress("MUST_BE_INITIALIZED")
internal class ProfilePage: View, MutableStruct {
    internal var profileViewModel: ProfileViewModel
        get() = _profileViewModel.wrappedValue
        set(newValue) {
            _profileViewModel.wrappedValue = newValue
        }
    internal var _profileViewModel = skip.ui.Environment<ProfileViewModel>()
    internal var loginViewModel: LoginViewModel
        get() = _loginViewModel.wrappedValue
        set(newValue) {
            _loginViewModel.wrappedValue = newValue
        }
    internal var _loginViewModel = skip.ui.Environment<LoginViewModel>()
    private var maxWidth: Double
        set(newValue) {
            willmutate()
            field = newValue
            didmutate()
        }

    override fun body(): View {
        return ComposeBuilder { composectx: ComposeContext ->
            MaximizedContainer { ->
                ComposeBuilder { composectx: ComposeContext ->
                    VStack { ->
                        ComposeBuilder { composectx: ComposeContext ->
                            if ((profileViewModel.userState.hasError)) {
                                Text(profileViewModel.userState.errorDescription!!).font(Font.title2).foregroundColor(Colors.red).Compose(composectx)
                            }
                            if ((profileViewModel.userState.isLoading)) {
                                ProgressView().Compose(composectx)
                            }
                            if ((profileViewModel.userState.isSuccess)) {
                                Text(profileViewModel.firstName + " " + profileViewModel.familyName).bold().font(Font.title).Compose(composectx)
                                Text(profileViewModel.email).Compose(composectx)
                            }
                            logoutButton.padding(Edge.Set.top, 24.0).Compose(composectx)
                            ComposeResult.ok
                        }
                    }
                    .padding(24.0).Compose(composectx)
                    ComposeResult.ok
                }
            }.background(Color.from(hex = "F2F2F7")).Compose(composectx)
        }
    }

    @Composable
    override fun Evaluate(context: ComposeContext, options: Int): kotlin.collections.List<Renderable> {
        _profileViewModel.wrappedValue = EnvironmentValues.shared.environmentObject(type = ProfileViewModel::class)!!
        _loginViewModel.wrappedValue = EnvironmentValues.shared.environmentObject(type = LoginViewModel::class)!!

        return super.Evaluate(context, options)
    }

    internal val logoutButton: View
        get() {
            return Button(action = { -> loginViewModel.logout() }) { ->
                ComposeBuilder { composectx: ComposeContext ->
                    HStack { ->
                        ComposeBuilder { composectx: ComposeContext ->
                            Image("rectangle.portrait.and.arrow.right", bundle = Bundle.module).Compose(composectx)
                            Text(LocalizedStringKey(stringLiteral = "Log out")).Compose(composectx)
                            ComposeResult.ok
                        }
                    }.frame(minWidth = 0.0, maxWidth = maxWidth).Compose(composectx)
                    ComposeResult.ok
                }
            }
            .padding()
            .background(RoundedRectangle(cornerRadius = Double.infinity).stroke(Color.gray, lineWidth = 1.0))
            .foregroundColor(Color.black)
        }

    private constructor(maxWidth: Double = 600.0, privatep: Nothing? = null) {
        this.maxWidth = maxWidth
    }

    constructor(): this(privatep = null) {
    }

    override var supdate: ((Any) -> Unit)? = null
    override var smutatingcount = 0
    override fun scopy(): MutableStruct = ProfilePage(maxWidth)
}

// #Preview omitted
