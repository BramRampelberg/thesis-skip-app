//
//  UserRepository.swift
//  MADiOS
//
//  Created by Bram Rampelberg on 12/08/2025.
//  Copyright © 2025 HOGENT. All rights reserved.
//

import Foundation

final class UserRepository {
    nonisolated(unsafe) static let shared = UserRepository()

    private let userService = UserService.shared

    func getUser() async -> Result<User> {
        let result = await userService.fetchUserDetails()
        return result.isSuccess
            ? .success(data: User(fromDto: result.data!))
            : .failureWithLog(
                cause: "Failed to get user data.",
                error: result.error
            )
    }
}
