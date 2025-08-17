//
//  Auth0Repository.swift
//  MADiOS
//
//  Created by Bram Rampelberg on 03/01/2025.
//  Copyright © 2025 HOGENT. All rights reserved.
//

import Foundation

#if SKIP
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
#else
    import Auth0
#endif

final class Auth0Repository {
    nonisolated(unsafe) static let shared = Auth0Repository()

    #if SKIP
        private let androidCredentialsManager = Auth0Manager.shared
            .androidCredentialsManager
    #else
        private let credentialsManager = Auth0Manager.shared.credentialsManager
    #endif
    private let auth0Service = Auth0Service.shared

    func login(email: String, password: String) async -> Result<Void> {
        #if SKIP
            do {
                let credentials = try await auth0Service.androidLogin(
                    email: email,
                    password: password
                )
                androidCredentialsManager.saveCredentials(credentials)
                return Result.success(resultData: Void)
            } catch let error as AuthenticationException {
                return failureWithLog(
                    cause: error.message
                        ?? "",
                    error: error as Error?
                )
            } catch {
                return failureWithLog(
                    cause: error.localizedDescription,
                    error: error
                )
            }
        #else
            do {
                let credentials = try await auth0Service.login(
                    email: email,
                    password: password
                )
                return credentialsManager.store(credentials: credentials)
                ? Result.success(resultData: Void())
                    : .failureWithLog(cause: "Failed to store credentials")
            } catch let error as Auth0APIError {
                return .failureWithLog(
                    cause: error.cause?.localizedDescription
                        ?? error.localizedDescription,
                    error: error
                )
            } catch {
                return .failureWithLog(
                    cause: error.localizedDescription,
                    error: error
                )
            }
        #endif

    }

    func logout() -> Bool {
        #if SKIP
            androidCredentialsManager.clearCredentials()
            return true
        #else
            return credentialsManager.clear()
        #endif
    }

    #if SKIP
        private class GetCredentialsCallback: Callback<
            Credentials, CredentialsManagerException
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

            override func onFailure(error: CredentialsManagerException) {
                let exception = error as! CredentialsManagerException
                continuation.resumeWithException(exception: exception)
            }
        }
    #endif

    func getCredentials() async throws -> AppCredentials {
        #if SKIP
            let credentials = try await suspendCancellableCoroutine<Credentials>
            {
                (continuation: CancellableContinuation<Credentials>) in
                let callback = GetCredentialsCallback(
                    continuation: continuation
                )
                androidCredentialsManager.getCredentials(callback)
            }
            return AppCredentials(
                accessToken: credentials.accessToken,
                tokenType: credentials.type,
                idToken: credentials.idToken,
                refreshToken: credentials.refreshToken,
                recoveryCode: credentials.recoveryCode,
                scope: credentials.scope,
                expiresIn: credentials.expiresAt
            )
        #else
            let credentials = try await credentialsManager.credentials()
            return AppCredentials(
                accessToken: credentials.accessToken,
                tokenType: credentials.tokenType,
                idToken: credentials.idToken,
                refreshToken: credentials.refreshToken,
                expiresIn: credentials.expiresIn,
                scope: credentials.scope,
                recoveryCode: credentials.recoveryCode
            )
        #endif
    }

    func userIsLoggedIn() -> Bool {
        #if SKIP
            androidCredentialsManager.hasValidCredentials()
        #else
            credentialsManager.hasValid()
        #endif

    }

    private init() {}
}

struct AppCredentials {
    let accessToken: String
    let tokenType: String
    let idToken: String
    let refreshToken: String?
    let expiresIn: Date
    let scope: String?
    let recoveryCode: String?
}
