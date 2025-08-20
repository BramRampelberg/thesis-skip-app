//
//  UserService.swift
//  MADiOS
//
//  Created by Bram Rampelberg on 12/08/2025.
//  Copyright © 2025 HOGENT. All rights reserved.
//

package thesis.skip.app

import skip.lib.*

import skip.foundation.*

internal class UserService {

    private val serviceHelper = APIServiceHelper.shared

    internal val logger = SkipLogger(subsystem = "be.hogent.ThesisSkipApp", category = "ThesisSkipApp")

    private constructor() {
    }

    internal suspend fun fetchUserDetails(): Result<UserDetailsDto> = Async.run l@{
        return@l serviceHelper.sendRequest(to = "api/User/profile", method = HTTPMethod.get)
    }

    companion object {
        internal val shared = UserService()
    }
}
