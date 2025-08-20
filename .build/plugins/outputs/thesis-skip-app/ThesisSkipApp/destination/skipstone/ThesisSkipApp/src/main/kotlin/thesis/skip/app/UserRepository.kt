//
//  UserRepository.swift
//  MADiOS
//
//  Created by Bram Rampelberg on 12/08/2025.
//  Copyright © 2025 HOGENT. All rights reserved.
//

package thesis.skip.app

import skip.lib.*

import skip.foundation.*

internal class UserRepository {

    private val userService = UserService.shared

    internal suspend fun getUser(): Result<User> = Async.run l@{
        val result = userService.fetchUserDetails()
        if (result.isSuccess) {
            return@l Result.success(resultData = User(fromDto = result.data!!))
        } else {
            return@l failureWithLog(cause = "Failed to get user data.", error = result.error)
        }

    }

    companion object {
        internal val shared = UserRepository()
    }
}
