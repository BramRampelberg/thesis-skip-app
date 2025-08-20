//
//  AppConfiguration.swift
//  MADiOS
//
//  Created by Bram Rampelberg on 04/01/2025.
//  Copyright © 2025 HOGENT. All rights reserved.
//

package thesis.skip.app

import skip.lib.*

import skip.foundation.*

import android.content.Context
import android.content.pm.PackageManager

internal class AppConfiguration {

    internal val baseUrl: URL

    private constructor() {
        val context = ProcessInfo.processInfo.androidContext.sref()
        try {
            val appInfo = context.packageManager
                .getApplicationInfo(context.packageName, PackageManager.GET_META_DATA)
            val bundle = appInfo.metaData.sref()
            val baseUrlString = bundle?.getString("BASE_URL")

            if (baseUrlString != null) {
                val matchtarget_0 = (try { URL(string = baseUrlString) } catch (_: NullReturnException) { null })
                if (matchtarget_0 != null) {
                    val url = matchtarget_0
                    this.baseUrl = url.sref()
                } else {
                    fatalError("BASE_URL is missing or invalid in AndroidManifest.xml meta-data.")
                }
            } else {
                fatalError("BASE_URL is missing or invalid in AndroidManifest.xml meta-data.")
            }
        } catch (error: (PackageManager.NameNotFoundException)) {
            fatalError("BASE_URL is missing or invalid in AndroidManifest.xml")
        }

        AppLogger.info("App config initialized.")
    }

    companion object {
        internal val shared = AppConfiguration()
    }
}
