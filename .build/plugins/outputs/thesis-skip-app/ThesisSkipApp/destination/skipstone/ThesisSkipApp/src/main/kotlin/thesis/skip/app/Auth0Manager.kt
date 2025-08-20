//
//  Auth0Manager.swift
//  MADiOS
//
//  Created by Bram Rampelberg on 03/01/2025.
//  Copyright © 2025 HOGENT. All rights reserved.
//

package thesis.skip.app

import skip.lib.*

import skip.foundation.*

import android.content.Context
import com.auth0.android.Auth0
import com.auth0.android.authentication.AuthenticationAPIClient
import com.auth0.android.authentication.storage.SecureCredentialsManager
import com.auth0.android.authentication.storage.SharedPreferencesStorage
import android.content.pm.PackageManager

internal class Auth0Manager {

    internal val clientId: String
    internal val domain: String
    internal val audience: String

    internal val auth0: Auth0

    internal var androidClient: AuthenticationAPIClient
        get() {
            if (!::androidClientstorage.isInitialized) {
                androidClientstorage = linvoke l@{ -> return@l AuthenticationAPIClient(auth0) }
            }
            return androidClientstorage.sref({ this.androidClient = it })
        }
        set(newValue) {
            androidClientstorage = newValue.sref()
        }
    private lateinit var androidClientstorage: AuthenticationAPIClient

    internal var androidCredentialsManager: SecureCredentialsManager
        get() {
            if (!::androidCredentialsManagerstorage.isInitialized) {
                androidCredentialsManagerstorage = linvoke l@{ ->
                    val context = ProcessInfo.processInfo.androidContext.sref()
                    return@l SecureCredentialsManager(context, auth0, SharedPreferencesStorage(context))
                }
            }
            return androidCredentialsManagerstorage.sref({ this.androidCredentialsManager = it })
        }
        set(newValue) {
            androidCredentialsManagerstorage = newValue.sref()
        }
    private lateinit var androidCredentialsManagerstorage: SecureCredentialsManager

    private constructor() {
        val context = ProcessInfo.processInfo.androidContext.sref()
        try {
            val appInfo = context.packageManager
                .getApplicationInfo(context.packageName, PackageManager.GET_META_DATA)
            val metaData = appInfo.metaData.sref()
            val clientId_0 = metaData?.getString("AUTH0_CLIENT_ID")
            if (clientId_0 == null) {
                fatalError("Auth0 config is missing from AndroidManifest.xml meta-data. Check your skip.env file.")
            }
            val domain_0 = metaData?.getString("AUTH0_DOMAIN")
            if (domain_0 == null) {
                fatalError("Auth0 config is missing from AndroidManifest.xml meta-data. Check your skip.env file.")
            }
            val audience_0 = metaData?.getString("AUTH0_AUDIENCE")
            if (audience_0 == null) {
                fatalError("Auth0 config is missing from AndroidManifest.xml meta-data. Check your skip.env file.")
            }
            this.auth0 = Auth0.getInstance(clientId_0, domain_0)
            this.clientId = clientId_0
            this.domain = domain_0
            this.audience = audience_0
        } catch (error: Throwable) {
            @Suppress("NAME_SHADOWING") val error = error.aserror()
            fatalError("Failed to read AndroidManifest.xml: ${error.localizedDescription}")
        }

    }

    companion object {
        internal val shared = Auth0Manager()
    }
}
