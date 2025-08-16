//
//  Auth0Manager.swift
//  MADiOS
//
//  Created by Bram Rampelberg on 03/01/2025.
//  Copyright © 2025 HOGENT. All rights reserved.
//

import Foundation

#if SKIP
    import android.content.Context
    import com.auth0.android.Auth0
    import com.auth0.android.authentication.AuthenticationAPIClient
    import com.auth0.android.authentication.storage.SecureCredentialsManager
    import com.auth0.android.authentication.storage.SharedPreferencesStorage
    import android.content.pm.PackageManager
#else
    import Auth0
#endif

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
        #if SKIP
            let context = ProcessInfo.processInfo.androidContext
            do {
                let appInfo = context.packageManager
                    .getApplicationInfo(
                        context.packageName,
                        PackageManager.GET_META_DATA
                    )
                let metaData = appInfo.metaData

                guard let clientId = metaData?.getString("AUTH0_CLIENT_ID"),
                    let domain = metaData?.getString("AUTH0_DOMAIN"),
                    let audience = metaData?.getString("AUTH0_AUDIENCE")
                else {
                    fatalError(
                        "Auth0 config is missing from AndroidManifest.xml meta-data. Check your skip.env file."
                    )
                }
                self.auth0 = Auth0.getInstance(clientId, domain)
                self.clientId = clientId
                self.domain = domain
                self.audience = audience
            } catch {
                fatalError(
                    "Failed to read AndroidManifest.xml: \(error.localizedDescription)"
                )
            }

        #else
            guard let infoDict = Bundle.main.infoDictionary,
                let clientId = infoDict["AUTH0_CLIENT_ID"] as? String,
                let domain = infoDict["AUTH0_DOMAIN"] as? String,
                let audience = infoDict["AUTH0_AUDIENCE"] as? String
            else {
                fatalError(
                    "Auth0 config is missing or invalid in Info.plist. Check your skip.env file."
                )
            }
            self.clientId = clientId
            self.domain = domain
            self.audience = audience
        #endif
    }
}
