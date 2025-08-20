//
//  APIServiceHelper.swift
//  MADiOS
//
//  Created by Bram Rampelberg on 04/01/2025.
//  Copyright © 2025 HOGENT. All rights reserved.
//

package thesis.skip.app

import skip.lib.*

import skip.foundation.*

import com.auth0.android.authentication.storage.CredentialsManagerException
import com.auth0.android.Auth0

internal class APIServiceHelper {

    private val baseUrl = AppConfiguration.shared.baseUrl.sref()
    private val auth0Repo = Auth0Repository.shared

    private constructor() {
    }

    internal suspend inline fun <reified ReturnType> sendRequest(to: String, method: HTTPMethod, queryParams: Dictionary<String, String>? = dictionaryOf()): Result<ReturnType> where ReturnType: Decodable {
        val url = to
        (try { URLComponents(string = "${baseUrl.absoluteString}/${url}") } catch (_: NullReturnException) { null })?.let { components ->
            var components = components
            if (queryParams != null) {
                val queryItems = queryParams?.map({ (key, value) -> URLQueryItem(name = key, value = value) })
                components.queryItems = queryItems
            }
            components.url.sref()?.let { url ->
                var request = URLRequest(url = url)

                request.httpMethod = method.methodName

                request.setValue("application/json", forHTTPHeaderField = "Content-Type")

                val result = addAuthentication(to = request)
                if (result.isSuccess) {
                    request = result.data!!.sref()
                } else {
                    return failureWithLog(cause = result.failureCause!!, error = result.error)

                }

                val session = URLSession.shared

                try {
                    val (data, response) = session.data(for_ = request)
                    val response_0 = response as? HTTPURLResponse
                    if (response_0 == null) {
                        return failureWithLog(cause = "Unexpected error: could not process response.")

                    }

                    AppLogger.info("${method} | ${url.absoluteString} | Status Code: ${response_0.statusCode}")

                    String(data = data, encoding = StringEncoding.utf8)?.let { jsonString ->
                        AppLogger.debug("Received JSON: ${jsonString}")
                    }

                    if ((400..<600).contains(response_0.statusCode)) {
                        var reasonPhrase: String? = null
                        String(data = data, encoding = StringEncoding.utf8)?.let { jsonString ->
                            reasonPhrase = jsonString
                        }
                        val error = APIError(errorStatusCode = response_0.statusCode, errorMessage = reasonPhrase)
                        return failureWithLog(cause = error.localizedDescription, error = APIError(errorStatusCode = response_0.statusCode))

                    }
                    if (ReturnType::class == EmptyBody::class) {
                        return Result.success(resultData = EmptyBody() as ReturnType)
                    }
                    val returnValue = JSONDecoder().decode(ReturnType::class, from = data)
                    return Result.success(resultData = returnValue)
                } catch (error: Throwable) {
                    @Suppress("NAME_SHADOWING") val error = error.aserror()
                    return failureWithLog(cause = error.localizedDescription, error = error)

                }
            }
        }
        return failureWithLog(cause = "Could not parse url.", error = URLError(URLError.Code.badURL))

    }

    private suspend fun addAuthentication(to: URLRequest): Result<URLRequest> = Async.run l@{
        val request = to
        try {
            var request = request.sref()
            val credentials = auth0Repo.getCredentials()
            val accessToken = credentials.accessToken
            val type = credentials.tokenType
            request.setValue("${type} ${accessToken}", forHTTPHeaderField = "Authorization")
            return@l Result.success(resultData = request)
        } catch (error: CredentialsManagerException) {
            return@l failureWithLog(cause = error.message ?: "Unexpected error while fetching the credentials.", error = error as Error?)
        } catch (error: Throwable) {
            @Suppress("NAME_SHADOWING") val error = error.aserror()
            return@l failureWithLog(cause = error.localizedDescription, error = error)
        }
    }

    companion object {
        internal val shared = APIServiceHelper()
    }
}

internal enum class HTTPMethod {
    get,
    post,
    put,
    patch,
    delete;

    internal val methodName: String
        get() {
            when (this) {
                HTTPMethod.get -> return "GET"
                HTTPMethod.post -> return "POST"
                HTTPMethod.put -> return "PUT"
                HTTPMethod.patch -> return "PATCH"
                HTTPMethod.delete -> return "DELETE"
            }
        }
}

internal class EmptyBody: Codable {

    private enum class CodingKeys(override val rawValue: String, @Suppress("UNUSED_PARAMETER") unusedp: Nothing? = null): CodingKey, RawRepresentable<String> {
        ;

        companion object {
            fun init(rawValue: String): CodingKeys? {
                return when (rawValue) {
                    else -> null
                }
            }
        }
    }

    override fun encode(to: Encoder) {
        val container = to.container(keyedBy = CodingKeys::class)
    }

    constructor(from: Decoder) {
    }

    constructor() {
    }

    companion object: DecodableCompanion<EmptyBody> {
        override fun init(from: Decoder): EmptyBody = EmptyBody(from = from)

        private fun CodingKeys(rawValue: String): CodingKeys? = CodingKeys.init(rawValue = rawValue)
    }
}
