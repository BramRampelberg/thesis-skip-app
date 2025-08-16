//
//  Errors.swift
//  MADiOS
//
//  Created by Bram Rampelberg on 04/01/2025.
//  Copyright © 2025 HOGENT. All rights reserved.
//

import Foundation

enum APIError: Error {
    case badRequest(errorMessage: String? = nil)
    case unauthorized(errorMessage: String? = nil)
    case forbidden(errorMessage: String? = nil)
    case notFound(errorMessage: String? = nil)
    case conflict(errorMessage: String? = nil)
    case internalServerError(errorMessage: String? = nil)
    case serviceUnavailable(errorMessage: String? = nil)
    case custom(errorStatusCode: Int, errorMessage: String? = nil)
    
    init(errorStatusCode: Int, errorMessage: String? = nil) {
            switch errorStatusCode {
            case 400:
                self = .badRequest(errorMessage: errorMessage)
            case 401:
                self = .unauthorized(errorMessage: errorMessage)
            case 403:
                self = .forbidden(errorMessage: errorMessage)
            case 404:
                self = .notFound(errorMessage: errorMessage)
            case 409:
                self = .conflict(errorMessage: errorMessage)
            case 500:
                self = .internalServerError(errorMessage: errorMessage)
            case 503:
                self = .serviceUnavailable(errorMessage: errorMessage)
            default:
                self = .custom(errorStatusCode: errorStatusCode, errorMessage: errorMessage)
            }
        }
    
    var statusCode: Int {
        switch self {
        case .badRequest: return 400
        case .unauthorized: return 401
        case .forbidden: return 403
        case .notFound: return 404
        case .conflict: return 409
        case .internalServerError: return 500
        case .serviceUnavailable: return 503
        case .custom(let statusCode, _): return statusCode
        }
    }
    
    var localizedDescription: String {
        switch self {
        case .badRequest(let message): return message ?? "Bad Request"
        case .unauthorized(let message): return message ?? "Unauthorized"
        case .forbidden(let message): return message ?? "Forbidden"
        case .notFound(let message): return message ?? "Not Found"
        case .conflict(let message): return message ?? "Conflict"
        case .internalServerError(let message): return message ?? "Internal Server Error"
        case .serviceUnavailable(let message): return message ?? "Service Unavailable"
        case .custom(_, let message): return message ?? "Unknown Error"
        }
    }
}

struct EntityConversionError: Error {
    let localizedDescription: String
}
