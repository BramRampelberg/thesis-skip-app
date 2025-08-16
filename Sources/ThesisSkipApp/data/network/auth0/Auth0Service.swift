//
//  Auth0Service.swift
//  MADiOS
//
//  Created by Bram Rampelberg on 03/01/2025.
//  Copyright © 2025 HOGENT. All rights reserved.
//

import Foundation

#if SKIP
    import com.auth0.android.Auth0
    import com.auth0.android.result.Credentials
    import com.auth0.android.authentication.AuthenticationException
    import com.auth0.android.callback.Callback
    import com.auth0.android.callback.Callback
    import kotlinx.coroutines.withContext
    import kotlin.coroutines.resume
    import kotlin.coroutines.resumeWithException
    import kotlinx.coroutines.suspendCancellableCoroutine
    import kotlinx.coroutines.Dispatchers
    import kotlinx.coroutines.CancellableContinuation
#else
    import Auth0
#endif

final class Auth0Service {
    nonisolated(unsafe) static let shared = Auth0Service()

    #if SKIP
        private let androidClient = Auth0Manager.shared.androidClient
    #else
        private let client = Auth0Manager.shared.client
    #endif

    #if SKIP
        private class GetCredentialsCallback: Callback<
            Credentials, AuthenticationException
        >
        {
            // The continuation now needs to be generic over the Android Credentials type
            let continuation:
                CancellableContinuation<
                    Credentials
                >

            init(
                continuation: CancellableContinuation<
                    Credentials
                >
            ) {
                self.continuation = continuation
            }

            override func onSuccess(result: Credentials) {
                let credentials =
                    result as! Credentials
                continuation.resume(value: credentials)
            }

            override func onFailure(error: AuthenticationException) {
                let exception = error as! AuthenticationException
                continuation.resumeWithException(exception: exception)
            }
        }
    #endif

    #if SKIP
        func androidLogin(email: String, password: String) async throws
            -> Credentials
        {
            let credentials = try await suspendCancellableCoroutine<Credentials>
            {
                (continuation: CancellableContinuation<Credentials>) in
                let callback = GetCredentialsCallback(
                    continuation: continuation
                )
                androidClient.login(email, password)
                    .setAudience(Auth0Manager.shared.audience)
                    .setScope("openid profile email roles")
                    .validateClaims()
                    .start(
                        callback: callback
                    )
            }
            return credentials
        }
    #else
        func login(email: String, password: String) async throws -> Credentials
        {
            let request = client.loginDefaultDirectory(
                withUsername: email,
                password: password,
                audience: Auth0Manager.shared.audience,
                scope: "openid profile email roles"
            )
            return try await request.start()
        }
    #endif

    private init() {}
}
