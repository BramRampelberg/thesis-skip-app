//
//  Auth0Repository.swift
//  MADiOS
//
//  Created by Bram Rampelberg on 03/01/2025.
//  Copyright © 2025 HOGENT. All rights reserved.
//

package thesis.skip.app

import skip.lib.*

import skip.foundation.*

import com.auth0.android.authentication.AuthenticationException
import com.auth0.android.authentication.storage.CredentialsManager
import com.auth0.android.authentication.storage.CredentialsManagerException
import com.auth0.android.Auth0
import com.auth0.android.result.Credentials
import com.auth0.android.callback.Callback
import kotlinx.coroutines.withContext
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.CancellableContinuation
import java.util.Date

internal class Auth0Repository {

    private val androidCredentialsManager = Auth0Manager.shared
        .androidCredentialsManager.sref()
    private val auth0Service = Auth0Service.shared

    internal suspend fun login(email: String, password: String): Result<Unit> = Async.run l@{
        try {
            val credentials = auth0Service.androidLogin(email = email, password = password)
            androidCredentialsManager.saveCredentials(credentials)
            return@l Result.success(resultData = Unit)
        } catch (error: AuthenticationException) {
            return@l failureWithLog(cause = error.message ?: "", error = error as Error?)
        } catch (error: Throwable) {
            @Suppress("NAME_SHADOWING") val error = error.aserror()
            return@l failureWithLog(cause = error.localizedDescription, error = error)
        }

    }

    internal fun logout(): Boolean {
        androidCredentialsManager.clearCredentials()
        return true
    }

    private open class GetCredentialsCallback: Callback<Credentials, CredentialsManagerException> {
        // The continuation now needs to be generic over the Android Credentials type
        internal val continuation: CancellableContinuation<Credentials>

        internal constructor(continuation: CancellableContinuation<Credentials>) {
            this.continuation = continuation.sref()
        }

        override fun onSuccess(result: Credentials) {
            val credentials = (result as Credentials).sref()
            continuation.resume(value = credentials)
        }

        override fun onFailure(error: CredentialsManagerException) {
            val exception = (error as CredentialsManagerException).sref()
            continuation.resumeWithException(exception = exception)
        }
    }

    internal suspend fun getCredentials(): AppCredentials = Async.run l@{
        val credentials = suspendCancellableCoroutine<Credentials> { continuation: CancellableContinuation<Credentials> ->
            val callback = GetCredentialsCallback(continuation = continuation)
            androidCredentialsManager.getCredentials(callback)
        }
        return@l AppCredentials(accessToken = credentials.accessToken, tokenType = credentials.type, idToken = credentials.idToken, refreshToken = credentials.refreshToken, recoveryCode = credentials.recoveryCode, scope = credentials.scope, expiresIn = credentials.expiresAt)
    }

    internal fun userIsLoggedIn(): Boolean {
        return androidCredentialsManager.hasValidCredentials()

    }

    private constructor() {
    }

    companion object {
        internal val shared = Auth0Repository()
    }
}

internal class AppCredentials {
    internal val accessToken: String
    internal val tokenType: String
    internal val idToken: String
    internal val refreshToken: String?
    internal val expiresIn: Date
    internal val scope: String?
    internal val recoveryCode: String?

    constructor(accessToken: String, tokenType: String, idToken: String, refreshToken: String? = null, expiresIn: Date, scope: String? = null, recoveryCode: String? = null) {
        this.accessToken = accessToken
        this.tokenType = tokenType
        this.idToken = idToken
        this.refreshToken = refreshToken
        this.expiresIn = expiresIn.sref()
        this.scope = scope
        this.recoveryCode = recoveryCode
    }
}
