//
//  Auth0Manager.swift
//  MADiOS
//
//  Created by Bram Rampelberg on 03/01/2025.
//  Copyright © 2025 HOGENT. All rights reserved.
//

#if SKIP
    import android.content.Context
    import com.auth0.android.Auth0
    import com.auth0.android.authentication.AuthenticationAPIClient
    import com.auth0.android.authentication.storage.SecureCredentialsManager
    import com.auth0.android.authentication.storage.SharedPreferencesStorage
#else
    import Auth0
#endif
import Foundation

final class Auth0Manager {
    nonisolated(unsafe) static let shared = Auth0Manager()

    let clientId: String
    let domain: String
    let audience: String

    #if SKIP
        let auth0: Auth0

        lazy var androidClient: AuthenticationAPIClient = {
            return AuthenticationAPIClient(auth0)
        }()

        lazy var androidCredentialsManager: SecureCredentialsManager = {
            let context = ProcessInfo.processInfo.androidContext
            return SecureCredentialsManager(
                context,
                auth0,
                SharedPreferencesStorage(context)
            )
        }()
    #else
        lazy var client: Auth0.Authentication = {
            return Auth0.authentication(clientId: clientId, domain: domain)
        }()

        lazy var credentialsManager: CredentialsManager = {
            return CredentialsManager(authentication: client)
        }()
    #endif

    private init() {
        guard
            let url = Bundle.module.url(
                forResource: "Auth0",
                withExtension: "plist"
            ),
            let auth0Dict = NSDictionary(contentsOf: url)
                as? [String: Any],
            let clientId = auth0Dict["ClientId"] as? String,
            let domain = auth0Dict["Domain"] as? String,
            let audience = auth0Dict["Audience"] as? String
        else {
            fatalError("Auth0.plist file is missing or invalid")
        }

        #if SKIP
            self.auth0 = Auth0.getInstance(clientId, domain)
        #endif
        self.clientId = clientId
        self.domain = domain
        self.audience = audience
    }
}
