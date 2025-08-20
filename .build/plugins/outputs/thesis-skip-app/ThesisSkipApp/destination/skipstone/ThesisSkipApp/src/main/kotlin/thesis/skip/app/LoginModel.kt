//
//  LoginModel.swift
//  MADiOS
//
//  Created by Bram Rampelberg on 03/01/2025.
//  Copyright © 2025 HOGENT. All rights reserved.
//

package thesis.skip.app

import skip.lib.*

import skip.foundation.*

@Suppress("MUST_BE_INITIALIZED")
internal class LoginModel: MutableStruct {
    internal var loginState: LoginState
        private set(newValue) {
            willmutate()
            field = newValue
            didmutate()
        }
    internal var email: String
        private set(newValue) {
            willmutate()
            field = newValue
            didmutate()
        }
    internal var password: String
        private set(newValue) {
            willmutate()
            field = newValue
            didmutate()
        }

    internal fun changeLoginState(to: LoginState) {
        val state = to
        willmutate()
        try {
            loginState = state
        } finally {
            didmutate()
        }
    }

    internal fun setEmail(to: String) {
        val email = to
        willmutate()
        try {
            this.email = email
        } finally {
            didmutate()
        }
    }

    internal fun setPassword(to: String) {
        val password = to
        willmutate()
        try {
            this.password = password
        } finally {
            didmutate()
        }
    }

    constructor(loginState: LoginState = LoginState.loggedOut, email: String, password: String) {
        this.loginState = loginState
        this.email = email
        this.password = password
    }

    override var supdate: ((Any) -> Unit)? = null
    override var smutatingcount = 0
    override fun scopy(): MutableStruct = LoginModel(loginState, email, password)
}

internal sealed class LoginState {
    class LoggedOutCase: LoginState() {
    }
    class LoadingCase: LoginState() {
    }
    class LoggedInCase: LoginState() {
    }
    class ErrorCase(val associated0: String): LoginState() {
    }

    internal val isLoading: Boolean
        get() {
            when (this) {
                is LoginState.LoadingCase -> return true
                else -> return false
            }
        }

    internal val isLoggedIn: Boolean
        get() {
            when (this) {
                is LoginState.LoggedInCase -> return true
                else -> return false
            }
        }

    internal val hasError: Boolean
        get() {
            when (this) {
                is LoginState.ErrorCase -> return true
                else -> return false
            }
        }

    internal val errorDescription: String?
        get() {
            when (this) {
                is LoginState.ErrorCase -> {
                    val description = this.associated0
                    return description
                }
                else -> return null
            }
        }

    companion object {
        val loggedOut: LoginState = LoggedOutCase()
        val loading: LoginState = LoadingCase()
        val loggedIn: LoginState = LoggedInCase()
        fun error(associated0: String): LoginState = ErrorCase(associated0)
    }
}
