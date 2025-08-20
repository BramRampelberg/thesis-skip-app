//
//  ItemsPageDto.swift
//  MADiOS
//
//  Created by Bram Rampelberg on 04/01/2025.
//  Copyright © 2025 HOGENT. All rights reserved.
//

package thesis.skip.app

import skip.lib.*
import skip.lib.Array

import skip.foundation.*

internal class ReservationsPageDto: Codable {
    internal val data: Array<ReservationDto>
    internal val nextId: Int?
    internal val previousId: Int?
    internal val isFirstPage: Boolean

    constructor(data: Array<ReservationDto>, nextId: Int? = null, previousId: Int? = null, isFirstPage: Boolean) {
        this.data = data.sref()
        this.nextId = nextId
        this.previousId = previousId
        this.isFirstPage = isFirstPage
    }

    private enum class CodingKeys(override val rawValue: String, @Suppress("UNUSED_PARAMETER") unusedp: Nothing? = null): CodingKey, RawRepresentable<String> {
        data_("data"),
        nextId("nextId"),
        previousId("previousId"),
        isFirstPage("isFirstPage");

        companion object {
            fun init(rawValue: String): CodingKeys? {
                return when (rawValue) {
                    "data" -> CodingKeys.data_
                    "nextId" -> CodingKeys.nextId
                    "previousId" -> CodingKeys.previousId
                    "isFirstPage" -> CodingKeys.isFirstPage
                    else -> null
                }
            }
        }
    }

    override fun encode(to: Encoder) {
        val container = to.container(keyedBy = CodingKeys::class)
        container.encode(data, forKey = CodingKeys.data_)
        container.encodeIfPresent(nextId, forKey = CodingKeys.nextId)
        container.encodeIfPresent(previousId, forKey = CodingKeys.previousId)
        container.encode(isFirstPage, forKey = CodingKeys.isFirstPage)
    }

    constructor(from: Decoder) {
        val container = from.container(keyedBy = CodingKeys::class)
        this.data = container.decode(Array::class, elementType = ReservationDto::class, forKey = CodingKeys.data_)
        this.nextId = container.decodeIfPresent(Int::class, forKey = CodingKeys.nextId)
        this.previousId = container.decodeIfPresent(Int::class, forKey = CodingKeys.previousId)
        this.isFirstPage = container.decode(Boolean::class, forKey = CodingKeys.isFirstPage)
    }

    companion object: DecodableCompanion<ReservationsPageDto> {
        override fun init(from: Decoder): ReservationsPageDto = ReservationsPageDto(from = from)

        private fun CodingKeys(rawValue: String): CodingKeys? = CodingKeys.init(rawValue = rawValue)
    }
}
