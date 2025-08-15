//
//  AppConfiguration.swift
//  MADiOS
//
//  Created by Bram Rampelberg on 04/01/2025.
//  Copyright © 2025 HOGENT. All rights reserved.
//

import Foundation

#if SKIP
    import android.content.Context
    import android.content.pm.PackageManager
#endif

final class AppConfiguration {
    nonisolated(unsafe) static let shared = AppConfiguration()

    let baseUrl: URL

    private init() {
        #if SKIP
            let context = ProcessInfo.processInfo.androidContext
            do {
                let appInfo = context.packageManager
                    .getApplicationInfo(
                        context.packageName,
                        PackageManager.GET_META_DATA
                    )
                let bundle = appInfo.metaData
                self.baseUrl = try bundle?.getString("BASE_URL")
            } catch (e:PackageManager.NameNotFoundException) {
                fatalError(
                    "BASE_URL is missing or invalid in AndroidManifest.xml"
                )
            }
        #else
            guard let infoDict = Bundle.main.infoDictionary else {
                fatalError("Missing Info.plist file.")
            }

            if let baseUrl = infoDict["BASE_URL"] as? String,
                let url = URL(string: baseUrl)
            {
                self.baseUrl = url
            } else {
                fatalError("BASE_URL is missing or invalid in Info.plist.")
            }
        #endif

        AppLogger.info("App config initialized.")
    }
}
