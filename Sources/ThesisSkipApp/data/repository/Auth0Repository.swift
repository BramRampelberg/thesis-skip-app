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
    import kotlinx.coroutines.withContext
    import kotlin.coroutines.resume
    import kotlin.coroutines.resumeWithException
    import kotlin.coroutines.suspendCoroutine
    import kotlinx.coroutines.Dispatchers
    import kotlinx.coroutines.suspendCancellableCoroutine
    import kotlinx.coroutines.CancellableContinuation
#else
    import Auth0
    import CoreData
#endif

final class Auth0Repository {
    nonisolated(unsafe) static let shared = Auth0Repository()

    #if SKIP
        private let androidCredentialsManager = Auth0Manager.shared
            .androidCredentialsManager
    #else
        private let credentialsManager = Auth0Manager.shared.credentialsManager
        private let context = CoreDataStack.shared.persistentContainer
            .viewContext
        private let sharedCoreDataStack = CoreDataStack.shared
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
                return Result.success(data: Void())
            } catch let error as AuthenticationException {
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
        #else
            do {
                let credentials = try await auth0Service.login(
                    email: email,
                    password: password
                )
                return credentialsManager.store(credentials: credentials)
                    ? Result.success(data: Void())
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
            //TODO: clear room database
            return true
        #else
            let success = credentialsManager.clear()
            if success {
                clearLocalDatabase()
                return success
            }
            return false
        #endif
    }

    #if SKIP
        private class GetCredentialsCallback: Callback {
            // The continuation now needs to be generic over the Android Credentials type
            let continuation:
                CancellableContinuation<
                    com_auth0_android_result.Credentials, Error
                >

            init(
                continuation: CancellableContinuation<
                    com_auth0_android_result.Credentials, Error
                >
            ) {
                self.continuation = continuation
            }

            override func onSuccess(result: Any?) {
                let credentials =
                    result as! com_auth0_android_result.Credentials
                continuation.resume(returning: credentials)
            }

            override func onFailure(error: Any?) {
                let exception = error as! CredentialsManagerException
                continuation.resume(throwing: exception)
            }
        }
    #endif

    func getCredentials() async throws -> AppCredentials {
        #if SKIP
            let credentials = try await suspendCancellableCoroutine<Credentials> {
                (continuation:CancellableContinuation) in
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
                expiresIn: credentials.expiresAt,
                scope: credentials.scope,
                recoveryCode: credentials.recoveryCode
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

    #if !SKIP
        func clearLocalDatabase() {
            let entities = sharedCoreDataStack.persistentContainer
                .managedObjectModel.entities.filter { entity in
                    entity.name != nil
                }.map { entity in
                    entity.name!
                }

            for entityName in entities {
                let fetchRequest = NSFetchRequest<NSFetchRequestResult>(
                    entityName: entityName
                )
                let deleteRequest = NSBatchDeleteRequest(
                    fetchRequest: fetchRequest
                )

                do {
                    try context.execute(deleteRequest)
                } catch {
                    print(
                        "Error deleting all data for entity \(entityName): \(error)"
                    )
                }
            }

            do {
                try context.save()
            } catch {
                print("Error saving context after deleting data: \(error)")
            }
        }
    #endif

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
