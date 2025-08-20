//
//  APIResource.swift
//  MADiOS
//
//  Created by Bram Rampelberg on 03/01/2025.
//  Copyright © 2025 HOGENT. All rights reserved.
//

package thesis.skip.app

import skip.lib.*

import skip.foundation.*

internal sealed class Result<out T> {
    class SuccessCase<T>(val associated0: T): Result<T>() {
        val resultData = associated0
    }
    class FailureCase(val associated0: String, val associated1: Error?): Result<Nothing>() {
        val cause = associated0
        val failureError = associated1
    }


    internal val isSuccess: Boolean
        get() {
            when (this) {
                is Result.SuccessCase -> return true
                is Result.FailureCase -> return false
            }
        }

    internal val isFailure: Boolean
        get() = !isSuccess

    internal val data: T?
        get() {
            when (this) {
                is Result.SuccessCase -> {
                    val data = this.associated0
                    return data
                }
                else -> return null
            }
        }

    internal val failureCause: String?
        get() {
            when (this) {
                is Result.FailureCase -> {
                    val cause = this.associated0
                    return cause
                }
                else -> return null
            }
        }

    internal val error: Error?
        get() {
            when (this) {
                is Result.FailureCase -> {
                    val error = this.associated1
                    return error
                }
                else -> return null
            }
        }

    companion object {
        fun <T> success(resultData: T): Result<T> = SuccessCase(resultData)
        fun failure(cause: String, failureError: Error? = null): Result<Nothing> = FailureCase(cause, failureError)

        fun <T> init(failureAndroid: String, error: Error? = null): Result<T> {
            val cause = failureAndroid
            AppLogger.error("Failure created with cause: ${cause}, error: ${error?.localizedDescription ?: "None"}")
            return Result.failure(cause = cause, failureError = error)
        }
    }
}
internal fun <T> Result(failureAndroid: String, error: Error? = null): Result<T> {
    val cause = failureAndroid
    return Result.init(failureAndroid = cause, error = error)
}


internal fun <T> failureWithLog(cause: String, error: Error? = null): Result<T> {
    AppLogger.error("Failure created with cause: ${cause}, error: ${error?.localizedDescription ?: "None"}")
    return Result.failure(cause = cause, failureError = error)
}

