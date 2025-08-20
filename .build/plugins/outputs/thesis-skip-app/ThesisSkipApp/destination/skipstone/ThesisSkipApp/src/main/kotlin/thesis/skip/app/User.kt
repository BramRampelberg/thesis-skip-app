//
//  User.swift
//  MADiOS
//
//  Created by Bram Rampelberg on 12/08/2025.
//  Copyright © 2025 HOGENT. All rights reserved.
//

package thesis.skip.app

import skip.lib.*

import skip.foundation.*

internal class User {
    internal val firstName: String
    internal val email: String
    internal val familyName: String

    internal constructor(firstName: String, email: String, familyName: String, id: Int) {
        this.firstName = firstName
        this.email = email
        this.familyName = familyName
    }

    internal constructor(fromDto: UserDetailsDto) {
        val dto = fromDto
        this.firstName = dto.firstName
        this.email = dto.email
        this.familyName = dto.familyName
    }
}
