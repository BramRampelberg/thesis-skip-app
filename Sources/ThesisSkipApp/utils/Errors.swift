//
//  Errors.swift
//  MADiOS
//
//  Created by Bram Rampelberg on 04/01/2025.
//  Copyright © 2025 HOGENT. All rights reserved.
//

import Foundation

enum APIError: Error {
    case badRequest(message: String? = nil)
    case unauthorized(message: String? = nil)
    case forbidden(message: String? = nil)
    case notFound(message: String? = nil)
    case conflict(message: String? = nil)
    case internalServerError(message: String? = nil)
    case serviceUnavailable(message: String? = nil)
    case custom(statusCode: Int, message: String? = nil)
    
    init(statusCode: Int, message: String? = nil) {
            switch statusCode {
            case 400:
                self = .badRequest(message: message)
            case 401:
                self = .unauthorized(message: message)
            case 403:
                self = .forbidden(message: message)
            case 404:
                self = .notFound(message: message)
            case 409:
                self = .conflict(message: message)
            case 500:
                self = .internalServerError(message: message)
            case 503:
                self = .serviceUnavailable(message: message)
            default:
                self = .custom(statusCode: statusCode, message: message)
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
