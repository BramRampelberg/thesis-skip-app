//
//  LoginPage.swift
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

import android.content.Context
import skip.foundation.*
import skip.model.*

internal class LoginPage: View {
    internal var loginViewModel: LoginViewModel
        get() = _loginViewModel.wrappedValue
        set(newValue) {
            _loginViewModel.wrappedValue = newValue
        }
    internal var _loginViewModel = skip.ui.Environment<LoginViewModel>()
    private var isPasswordVisible: Boolean
        get() = _isPasswordVisible.wrappedValue
        set(newValue) {
            _isPasswordVisible.wrappedValue = newValue
        }
    private var _isPasswordVisible: skip.ui.State<Boolean>

    private val baseLogoSize: Double = 100.0
    private val maxWidth: Double = 600.0

    internal val scaledLogoSize: Double
        get() {
            val context = ProcessInfo.processInfo.androidContext.sref()
            val scale = context.resources.configuration.fontScale.sref()
            return baseLogoSize * Double(scale)

        }

    override fun body(): View {
        return ComposeBuilder { composectx: ComposeContext ->
            VStack { ->
                ComposeBuilder { composectx: ComposeContext ->
                    Spacer().Compose(composectx)
                    loginForm.Compose(composectx)
                    if (loginViewModel.loginState.isLoading) {
                        ProgressView().Compose(composectx)
                    }
                    if (loginViewModel.loginState.hasError) {
                        Text(loginViewModel.loginState.errorDescription!!).padding(Edge.Set.horizontal, 20.0).foregroundColor(Colors.red).Compose(composectx)
                    }
                    loginButton.Compose(composectx)
                    Spacer().Compose(composectx)
                    ComposeResult.ok
                }
            }
            .frame(minWidth = 0.0, maxWidth = Double.infinity)
            .background(Colors.primary)
            .ignoresSafeArea().Compose(composectx)
        }
    }

    @Composable
    @Suppress("UNCHECKED_CAST")
    override fun Evaluate(context: ComposeContext, options: Int): kotlin.collections.List<Renderable> {
        val rememberedisPasswordVisible by rememberSaveable(stateSaver = context.stateSaver as Saver<skip.ui.State<Boolean>, Any>) { mutableStateOf(_isPasswordVisible) }
        _isPasswordVisible = rememberedisPasswordVisible

        _loginViewModel.wrappedValue = EnvironmentValues.shared.environmentObject(type = LoginViewModel::class)!!

        return super.Evaluate(context, options)
    }

    internal val loginForm: View
        get() {
            return Form { ->
                ComposeBuilder { composectx: ComposeContext ->
                    Section(header = Text(LocalizedStringKey(stringLiteral = "Login")).bold().font(Font.title).foregroundColor(Color.white)) { ->
                        ComposeBuilder { composectx: ComposeContext ->
                            TextField(LocalizedStringKey(stringLiteral = "Email"), text = Binding({ _loginViewModel.wrappedValue.email }, { it -> _loginViewModel.wrappedValue.email = it })).Compose(composectx)
                            HStack { ->
                                ComposeBuilder { composectx: ComposeContext ->
                                    if (isPasswordVisible) {
                                        TextField(LocalizedStringKey(stringLiteral = "Password"), text = Binding({ _loginViewModel.wrappedValue.password }, { it -> _loginViewModel.wrappedValue.password = it })).Compose(composectx)
                                    } else {
                                        SecureField(LocalizedStringKey(stringLiteral = "Password"), text = Binding({ _loginViewModel.wrappedValue.password }, { it -> _loginViewModel.wrappedValue.password = it })).Compose(composectx)
                                    }
                                    Image(if (isPasswordVisible) "eye.fill" else "eye.slash.fill", bundle = Bundle.module).foregroundColor(Color.gray)
                                        .onTapGesture { it -> isPasswordVisible = !isPasswordVisible }.Compose(composectx)
                                    ComposeResult.ok
                                }
                            }.Compose(composectx)
                            ComposeResult.ok
                        }
                    }.Compose(composectx)
                    ComposeResult.ok
                }
            }
            .scrollContentBackground(Visibility.hidden)
            .frame(maxWidth = maxWidth, maxHeight = 175.0)
        }

    internal val loginButton: View
        get() {
            return Button(action = { ->
                withAnimation(Animation.easeIn(duration = 0.5)) { -> loginViewModel.login() }
            }) { ->
                ComposeBuilder { composectx: ComposeContext ->
                    Text(LocalizedStringKey(stringLiteral = "Login")).frame(minWidth = 0.0, maxWidth = maxWidth).Compose(composectx)
                    ComposeResult.ok
                }
            }
            .disabled(loginViewModel.loginState.isLoading)
            .padding()
            .background(RoundedRectangle(cornerRadius = Double.infinity).stroke(Color.white, lineWidth = 1.0))
            .foregroundColor(Color.white)
            .padding(Double(20))
        }

    private constructor(isPasswordVisible: Boolean = false, privatep: Nothing? = null) {
        this._isPasswordVisible = skip.ui.State(isPasswordVisible)
    }

    constructor(): this(privatep = null) {
    }
}

// #Preview omitted
