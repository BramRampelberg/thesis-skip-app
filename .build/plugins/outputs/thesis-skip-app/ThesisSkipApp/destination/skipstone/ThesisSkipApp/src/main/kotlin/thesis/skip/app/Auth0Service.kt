//
//  Auth0Service.swift
//  MADiOS
//
//  Created by Bram Rampelberg on 03/01/2025.
//  Copyright © 2025 HOGENT. All rights reserved.
//

package thesis.skip.app

import skip.lib.*

import skip.foundation.*

import com.auth0.android.Auth0
import com.auth0.android.result.Credentials
import com.auth0.android.authentication.AuthenticationException
import com.auth0.android.callback.Callback
import kotlinx.coroutines.withContext
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.CancellableContinuation

internal class Auth0Service {

    private val androidClient = Auth0Manager.shared.androidClient.sref()

    private open class GetCredentialsCallback: Callback<Credentials, AuthenticationException> {
        // The continuation now needs to be generic over the Android Credentials type
        internal val continuation: CancellableContinuation<Credentials>

        internal constructor(continuation: CancellableContinuation<Credentials>) {
            this.continuation = continuation.sref()
        }

        override fun onSuccess(result: Credentials) {
            val credentials = (result as Credentials).sref()
            continuation.resume(value = credentials)
        }

        override fun onFailure(error: AuthenticationException) {
            val exception = (error as AuthenticationException).sref()
            continuation.resumeWithException(exception = exception)
        }
    }

    internal suspend fun androidLogin(email: String, password: String): Credentials = Async.run l@{
        val credentials = suspendCancellableCoroutine<Credentials> { continuation: CancellableContinuation<Credentials> ->
            val callback = GetCredentialsCallback(continuation = continuation)
            androidClient.login(email, password)
                .setAudience(Auth0Manager.shared.audience)
                .setScope("openid profile email roles")
                .validateClaims()
                .start(callback = callback)
        }
        return@l credentials.sref()
    }

    private constructor() {
    }

    companion object {
        internal val shared = Auth0Service()
    }
}
