//
//  APIResource.swift
//  MADiOS
//
//  Created by Bram Rampelberg on 03/01/2025.
//  Copyright © 2025 HOGENT. All rights reserved.
//

import Foundation

enum Result<T: Sendable>: Sendable {
    case success(data: T)
    case failure(cause: String, error: Error? = nil)

    #if SKIP
        private init(failureAndroid cause: String, error: Error? = nil) {
            AppLogger.error(
                "Failure created with cause: \(cause), error: \(error?.localizedDescription ?? "None")"
            )
            self = .failure(cause: cause, error: error)
        }

        static func failureWithLog(cause: String, error: Error? = nil) -> Result
        {
            return Result(failure: cause, error: error)
        }
    #else
        private init(
            failure cause: String,
            error: Error? = nil,
            file: String = #file,
            function: String = #function,
            line: Int = #line
        ) {
            AppLogger.error(
                "Failure created with cause: \(cause), error: \(error?.localizedDescription ?? "None")",
                file: file,
                function: function,
                line: line
            )
            self = .failure(cause: cause, error: error)
        }

        static func failureWithLog(
            cause: String,
            error: Error? = nil,
            file: String = #file,
            function: String = #function,
            line: Int = #line
        ) -> Result {
            return Result(
                failure: cause,
                error: error,
                file: file,
                function: function,
                line: line
            )
        }
    #endif

    var isSuccess: Bool {
        switch self {
        case .success: return true
        case .failure: return false
        }
    }

    var isFailure: Bool {
        !isSuccess
    }

    var data: T? {
        switch self {
        case .success(let data): return data
        default: return nil
        }
    }

    var failureCause: String? {
        switch self {
        case .failure(let cause, _): return cause
        default: return nil
        }
    }

    var error: Error? {
        switch self {
        case .failure(_, let error): return error
        default: return nil
        }
    }
}
