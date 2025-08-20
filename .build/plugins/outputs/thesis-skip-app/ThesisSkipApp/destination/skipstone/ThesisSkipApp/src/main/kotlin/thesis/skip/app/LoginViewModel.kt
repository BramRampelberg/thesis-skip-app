//
//  LoginViewModel.swift
//  MADiOS
//
//  Created by Bram Rampelberg on 03/01/2025.
//  Copyright © 2025 HOGENT. All rights reserved.
//

package thesis.skip.app

import androidx.compose.runtime.Stable
import androidx.compose.runtime.mutableStateOf
import skip.lib.*

import skip.foundation.*
import skip.model.*

@Stable
internal open class LoginViewModel: ObservableObject {
    override val objectWillChange = ObservableObjectPublisher()
    private val auth0Repo = Auth0Repository.shared

    private var loginModel: LoginModel
        get() = _loginModel.wrappedValue.sref({ this.loginModel = it })
        set(newValue) {
            objectWillChange.send()
            _loginModel.wrappedValue = newValue.sref()
        }
    private var _loginModel: skip.model.Published<LoginModel>

    internal constructor() {
        this._loginModel = skip.model.Published(LoginModel(email = "", password = ""))
        if (auth0Repo.userIsLoggedIn()) {
            loginModel.changeLoginState(to = LoginState.loggedIn)
        }
    }

    internal open val loginState: LoginState
        get() = loginModel.loginState

    internal open var email: String
        get() = loginModel.email
        set(newValue) {
            loginModel.setEmail(to = newValue)
        }

    internal open var password: String
        get() = loginModel.password
        set(newValue) {
            loginModel.setPassword(to = newValue)
        }

    internal open fun login() {
        loginModel.changeLoginState(to = LoginState.loading)

        Task(isMainActor = true) { ->
            val result = auth0Repo.login(email = loginModel.email, password = loginModel.password)
            if ((result.isSuccess)) {
                loginModel.setEmail(to = "")
                loginModel.setPassword(to = "")
                loginModel.changeLoginState(to = LoginState.loggedIn)
            } else {
                loginModel.changeLoginState(to = LoginState.error(result.failureCause!!))
            }
        }
    }

    internal open fun logout() {
        val success = auth0Repo.logout()
        if (success) {
            loginModel.changeLoginState(to = LoginState.loggedOut)
        }
    }
}
