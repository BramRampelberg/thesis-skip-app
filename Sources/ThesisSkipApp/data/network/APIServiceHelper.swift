//
//  APIServiceHelper.swift
//  MADiOS
//
//  Created by Bram Rampelberg on 04/01/2025.
//  Copyright © 2025 HOGENT. All rights reserved.
//

import Foundation

#if SKIP
    import com.auth0.android.authentication.storage.CredentialsManagerException
    import com.auth0.android.Auth0
#else
    import Auth0
#endif

final class APIServiceHelper {
    nonisolated(unsafe) static let shared = APIServiceHelper()

    private let baseUrl = AppConfiguration.shared.baseUrl
    private let auth0Repo = Auth0Repository.shared

    private init() {}

    @inline(__always) func sendRequest<ReturnType: Decodable>(
        to url: String,
        method: HTTPMethod,
        queryParams: [String: String]? = [:],
    ) async -> Result<ReturnType> {
        if var components = URLComponents(
            string: "\(baseUrl.absoluteString)/\(url)"
        ) {
            if queryParams != nil {
                let queryItems = queryParams?.map({ key, value in
                    URLQueryItem(name: key, value: value)
                })
                components.queryItems = queryItems
            }
            if let url = components.url {
                var request = URLRequest(url: url)

                request.httpMethod = method.methodName

                request.setValue(
                    "application/json",
                    forHTTPHeaderField: "Content-Type"
                )

                let result = await addAuthentication(to: request)
                if result.isSuccess {
                    request = result.data!
                } else {
                    #if SKIP
                        return failureWithLog(
                            cause: result.failureCause!,
                            error: result.error
                        )
                    #else
                        return .failureWithLog(
                            cause: result.failureCause!,
                            error: result.error
                        )
                    #endif

                }

                let session = URLSession.shared

                do {
                    let (data, response) = try await session.data(for: request)
                    guard let response = response as? HTTPURLResponse else {
                        #if SKIP
                            return failureWithLog(
                                cause:
                                    "Unexpected error: could not process response."
                            )
                        #else
                            return .failureWithLog(
                                cause:
                                    "Unexpected error: could not process response."
                            )
                        #endif

                    }

                    AppLogger.info(
                        "\(method) | \(url.absoluteString) | Status Code: \(response.statusCode)"
                    )

                    if let jsonString = String(data: data, encoding: .utf8) {
                        AppLogger.debug("Received JSON: \(jsonString)")
                    }

                    if (400..<600).contains(response.statusCode) {
                        var reasonPhrase: String? = nil
                        if let jsonString = String(data: data, encoding: .utf8)
                        {
                            reasonPhrase = jsonString
                        }
                        let error = APIError.init(
                            errorStatusCode: response.statusCode,
                            errorMessage: reasonPhrase
                        )
                        #if SKIP
                            return failureWithLog(
                                cause: error.localizedDescription,
                                error: APIError(
                                    errorStatusCode: response.statusCode
                                )
                            )
                        #else
                            return .failureWithLog(
                                cause: error.localizedDescription,
                                error: APIError(
                                    errorStatusCode: response.statusCode
                                )
                            )
                        #endif

                    }
                    if ReturnType.self == EmptyBody.self {
                        return .success(resultData: EmptyBody() as! ReturnType)
                    }
                    let returnValue = try JSONDecoder().decode(
                        ReturnType.self,
                        from: data
                    )
                    return .success(resultData: returnValue)
                } catch {
                    #if SKIP
                        return failureWithLog(
                            cause: error.localizedDescription,
                            error: error
                        )
                    #else
                        return .failureWithLog(
                            cause: error.localizedDescription,
                            error: error
                        )
                    #endif

                }
            }
        }
        #if SKIP
            return failureWithLog(
                cause: "Could not parse url.",
                error: URLError(.badURL)
            )
        #else
            return .failureWithLog(
                cause: "Could not parse url.",
                error: URLError(.badURL)
            )
        #endif

    }

    private func addAuthentication(to request: URLRequest) async -> Result<
        URLRequest
    > {
        #if SKIP
            do {
                var request = request
                let credentials = try await auth0Repo.getCredentials()
                let accessToken = credentials.accessToken
                let type = credentials.tokenType
                request.setValue(
                    "\(type) \(accessToken)",
                    forHTTPHeaderField: "Authorization"
                )
                return .success(resultData: request)
            } catch let error as CredentialsManagerException {
                return failureWithLog(
                    cause: error.message
                        ?? "Unexpected error while fetching the credentials.",
                    error: error as Error?
                )
            } catch {
                return failureWithLog(
                    cause: error.localizedDescription,
                    error: error
                )
            }
        #else
            do {
                var request = request
                let credentials = try await auth0Repo.getCredentials()
                let accessToken = credentials.accessToken
                let type = credentials.tokenType
                request.setValue(
                    "\(type) \(accessToken)",
                    forHTTPHeaderField: "Authorization"
                )
                return .success(resultData: request)
            } catch let error as CredentialsManagerError {
                return .failureWithLog(
                    cause: error.cause?.localizedDescription
                        ?? "Unexpected error while fetching the credentials.",
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
}

enum HTTPMethod {
    case get
    case post
    case put
    case patch
    case delete

    var methodName: String {
        switch self {
        case .get: return "GET"
        case .post: return "POST"
        case .put: return "PUT"
        case .patch: return "PATCH"
        case .delete: return "DELETE"
        }
    }
}

struct EmptyBody: Codable {}
