//
//  Errors.swift
//  MADiOS
//
//  Created by Bram Rampelberg on 04/01/2025.
//  Copyright © 2025 HOGENT. All rights reserved.
//

package thesis.skip.app

import skip.lib.*

import skip.foundation.*

internal sealed class APIError: Exception(), Error {
    class BadRequestCase(val associated0: String?): APIError() {
        val errorMessage = associated0
    }
    class UnauthorizedCase(val associated0: String?): APIError() {
        val errorMessage = associated0
    }
    class ForbiddenCase(val associated0: String?): APIError() {
        val errorMessage = associated0
    }
    class NotFoundCase(val associated0: String?): APIError() {
        val errorMessage = associated0
    }
    class ConflictCase(val associated0: String?): APIError() {
        val errorMessage = associated0
    }
    class InternalServerErrorCase(val associated0: String?): APIError() {
        val errorMessage = associated0
    }
    class ServiceUnavailableCase(val associated0: String?): APIError() {
        val errorMessage = associated0
    }
    class CustomCase(val associated0: Int, val associated1: String?): APIError() {
        val errorStatusCode = associated0
        val errorMessage = associated1
    }

    internal val statusCode: Int
        get() {
            when (this) {
                is APIError.BadRequestCase -> return 400
                is APIError.UnauthorizedCase -> return 401
                is APIError.ForbiddenCase -> return 403
                is APIError.NotFoundCase -> return 404
                is APIError.ConflictCase -> return 409
                is APIError.InternalServerErrorCase -> return 500
                is APIError.ServiceUnavailableCase -> return 503
                is APIError.CustomCase -> {
                    val statusCode = this.associated0
                    return statusCode
                }
            }
        }

    override val localizedDescription: String
        get() {
            when (this) {
                is APIError.BadRequestCase -> {
                    val message = this.associated0
                    return message ?: "Bad Request"
                }
                is APIError.UnauthorizedCase -> {
                    val message = this.associated0
                    return message ?: "Unauthorized"
                }
                is APIError.ForbiddenCase -> {
                    val message = this.associated0
                    return message ?: "Forbidden"
                }
                is APIError.NotFoundCase -> {
                    val message = this.associated0
                    return message ?: "Not Found"
                }
                is APIError.ConflictCase -> {
                    val message = this.associated0
                    return message ?: "Conflict"
                }
                is APIError.InternalServerErrorCase -> {
                    val message = this.associated0
                    return message ?: "Internal Server Error"
                }
                is APIError.ServiceUnavailableCase -> {
                    val message = this.associated0
                    return message ?: "Service Unavailable"
                }
                is APIError.CustomCase -> {
                    val message = this.associated1
                    return message ?: "Unknown Error"
                }
            }
        }

    companion object {
        fun badRequest(errorMessage: String? = null): APIError = BadRequestCase(errorMessage)
        fun unauthorized(errorMessage: String? = null): APIError = UnauthorizedCase(errorMessage)
        fun forbidden(errorMessage: String? = null): APIError = ForbiddenCase(errorMessage)
        fun notFound(errorMessage: String? = null): APIError = NotFoundCase(errorMessage)
        fun conflict(errorMessage: String? = null): APIError = ConflictCase(errorMessage)
        fun internalServerError(errorMessage: String? = null): APIError = InternalServerErrorCase(errorMessage)
        fun serviceUnavailable(errorMessage: String? = null): APIError = ServiceUnavailableCase(errorMessage)
        fun custom(errorStatusCode: Int, errorMessage: String? = null): APIError = CustomCase(errorStatusCode, errorMessage)


        fun init(errorStatusCode: Int, errorMessage: String? = null): APIError {
            when (errorStatusCode) {
                400 -> return APIError.badRequest(errorMessage = errorMessage)
                401 -> return APIError.unauthorized(errorMessage = errorMessage)
                403 -> return APIError.forbidden(errorMessage = errorMessage)
                404 -> return APIError.notFound(errorMessage = errorMessage)
                409 -> return APIError.conflict(errorMessage = errorMessage)
                500 -> return APIError.internalServerError(errorMessage = errorMessage)
                503 -> return APIError.serviceUnavailable(errorMessage = errorMessage)
                else -> return APIError.custom(errorStatusCode = errorStatusCode, errorMessage = errorMessage)
            }
        }
    }
}
internal fun APIError(errorStatusCode: Int, errorMessage: String? = null): APIError = APIError.init(errorStatusCode = errorStatusCode, errorMessage = errorMessage)

internal class EntityConversionError: Exception, Error {
    override val localizedDescription: String

    constructor(localizedDescription: String): super() {
        this.localizedDescription = localizedDescription
    }
}
