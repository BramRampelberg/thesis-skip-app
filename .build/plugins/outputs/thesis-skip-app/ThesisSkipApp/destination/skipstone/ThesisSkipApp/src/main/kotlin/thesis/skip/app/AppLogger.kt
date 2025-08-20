//
//  AppLogger.swift
//  MADiOS
//
//  Created by Bram Rampelberg on 04/01/2025.
//  Copyright © 2025 HOGENT. All rights reserved.
//

package thesis.skip.app

import skip.lib.*

import skip.foundation.*

internal class AppLogger {


    private enum class Level {
        info,
        error,
        debug,
        warning;
    }

    private constructor() {
    }

    companion object {
        private val logger = SkipLogger(subsystem = "be.hogent.ThesisSkipApp", category = "ThesisSkipApp")

        private val timestamp: String
            get() {
                val dateFormatter = DateFormatter()
                dateFormatter.dateFormat = "yyyy-MM-dd HH:mm:ss"
                return dateFormatter.string(from = Date())
            }
        internal fun info(message: Any): Unit = log(level = Level.info, message = message)

        internal fun error(message: Any): Unit = log(level = Level.error, message = message)

        internal fun debug(message: Any): Unit = log(level = Level.debug, message = message)

        internal fun warning(message: Any): Unit = log(level = Level.warning, message = message)

        private fun log(level: AppLogger.Level, message: Any) {

            val formattedMessage = "${level} : [${timestamp}] ${message}"

            when (level) {
                AppLogger.Level.info -> logger.info("${formattedMessage}")
                AppLogger.Level.error -> logger.error("${formattedMessage}")
                AppLogger.Level.debug -> logger.debug("${formattedMessage}")
                AppLogger.Level.warning -> logger.warning("${formattedMessage}")
            }
        }
    }
}
