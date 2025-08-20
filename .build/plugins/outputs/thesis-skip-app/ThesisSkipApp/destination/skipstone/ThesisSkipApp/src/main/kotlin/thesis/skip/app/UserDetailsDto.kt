//
//  UserDetailsDto.swift
//  MADiOS
//
//  Created by Bram Rampelberg on 12/08/2025.
//  Copyright © 2025 HOGENT. All rights reserved.
//

package thesis.skip.app

import skip.lib.*

import skip.foundation.*

internal class UserDetailsDto: Codable {
    internal val firstName: String
    internal val email: String
    internal val familyName: String

    constructor(firstName: String, email: String, familyName: String) {
        this.firstName = firstName
        this.email = email
        this.familyName = familyName
    }

    private enum class CodingKeys(override val rawValue: String, @Suppress("UNUSED_PARAMETER") unusedp: Nothing? = null): CodingKey, RawRepresentable<String> {
        firstName("firstName"),
        email("email"),
        familyName("familyName");

        companion object {
            fun init(rawValue: String): CodingKeys? {
                return when (rawValue) {
                    "firstName" -> CodingKeys.firstName
                    "email" -> CodingKeys.email
                    "familyName" -> CodingKeys.familyName
                    else -> null
                }
            }
        }
    }

    override fun encode(to: Encoder) {
        val container = to.container(keyedBy = CodingKeys::class)
        container.encode(firstName, forKey = CodingKeys.firstName)
        container.encode(email, forKey = CodingKeys.email)
        container.encode(familyName, forKey = CodingKeys.familyName)
    }

    constructor(from: Decoder) {
        val container = from.container(keyedBy = CodingKeys::class)
        this.firstName = container.decode(String::class, forKey = CodingKeys.firstName)
        this.email = container.decode(String::class, forKey = CodingKeys.email)
        this.familyName = container.decode(String::class, forKey = CodingKeys.familyName)
    }

    companion object: DecodableCompanion<UserDetailsDto> {
        override fun init(from: Decoder): UserDetailsDto = UserDetailsDto(from = from)

        private fun CodingKeys(rawValue: String): CodingKeys? = CodingKeys.init(rawValue = rawValue)
    }
}
