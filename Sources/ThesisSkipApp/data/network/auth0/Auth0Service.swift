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
    import kotlinx.coroutines.withContext
    import kotlin.coroutines.resume
    import kotlin.coroutines.resumeWithException
    import kotlin.coroutines.suspendCoroutine
    import kotlinx.coroutines.Dispatchers
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
        func androidLogin(email: String, password: String) async throws
            -> Credentials
        {
            let credentials = withContext(Dispatchers.IO) {
                suspendCoroutine {
                    continuation in androidClient.login(email, password)
                        .setAudience(Auth0Manager.shared.audience)
                        .setScope("openid profile email roles")
                        .validateClaims()
                        .start(
                            object: Callback<
                                Credentials, AuthenticationException
                            > {
                                override func onSuccess(result: Credentials) {
                                    continuation.resume(result)
                                }

                                override func onFailure(
                                    error: AuthenticationException
                                ) {
                                    continuation.resumeWithException(error)
                                }
                            }
                        )
                }
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
