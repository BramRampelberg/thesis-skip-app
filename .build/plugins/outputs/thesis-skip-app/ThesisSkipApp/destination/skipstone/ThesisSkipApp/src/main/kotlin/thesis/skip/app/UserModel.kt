//
//  UserModel.swift
//  MADiOS
//
//  Created by Bram Rampelberg on 12/08/2025.
//  Copyright © 2025 HOGENT. All rights reserved.
//

package thesis.skip.app

import skip.lib.*

import skip.foundation.*

@Suppress("MUST_BE_INITIALIZED")
internal class UserModel: MutableStruct {
    internal var userState: UserState
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
    internal var firstName: String
        private set(newValue) {
            willmutate()
            field = newValue
            didmutate()
        }
    internal var familyName: String
        private set(newValue) {
            willmutate()
            field = newValue
            didmutate()
        }

    internal fun changeUserState(to: UserState) {
        val state = to
        willmutate()
        try {
            userState = state
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

    internal fun setFirstName(to: String) {
        val firstName = to
        willmutate()
        try {
            this.firstName = firstName
        } finally {
            didmutate()
        }
    }

    internal fun setFamilyName(to: String) {
        val familyName = to
        willmutate()
        try {
            this.familyName = familyName
        } finally {
            didmutate()
        }
    }

    constructor(userState: UserState = UserState.loading, email: String, firstName: String, familyName: String) {
        this.userState = userState
        this.email = email
        this.firstName = firstName
        this.familyName = familyName
    }

    override var supdate: ((Any) -> Unit)? = null
    override var smutatingcount = 0
    override fun scopy(): MutableStruct = UserModel(userState, email, firstName, familyName)
}

internal sealed class UserState {
    class LoadingCase: UserState() {
    }
    class SuccessCase: UserState() {
    }
    class ErrorCase(val associated0: String): UserState() {
    }

    internal val isLoading: Boolean
        get() {
            when (this) {
                is UserState.LoadingCase -> return true
                else -> return false
            }
        }

    internal val isSuccess: Boolean
        get() {
            when (this) {
                is UserState.SuccessCase -> return true
                else -> return false
            }
        }

    internal val hasError: Boolean
        get() {
            when (this) {
                is UserState.ErrorCase -> return true
                else -> return false
            }
        }

    internal val errorDescription: String?
        get() {
            when (this) {
                is UserState.ErrorCase -> {
                    val description = this.associated0
                    return description
                }
                else -> return null
            }
        }

    companion object {
        val loading: UserState = LoadingCase()
        val success: UserState = SuccessCase()
        fun error(associated0: String): UserState = ErrorCase(associated0)
    }
}
