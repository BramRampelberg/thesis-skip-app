//
//  ReservationDto.swift
//  MADiOS
//
//  Created by Bram Rampelberg on 04/01/2025.
//  Copyright © 2025 HOGENT. All rights reserved.
//

package thesis.skip.app

import skip.lib.*

import skip.foundation.*

internal class ReservationDto: Codable {
    internal val start: Date
    internal val end: Date
    internal val date: Date
    internal val boatId: Int
    internal val boatPersonalName: String
    internal val id: Int
    internal val isDeleted: Boolean

    //Code from: ChatGPT
    internal enum class CodingKeys(override val rawValue: String, @Suppress("UNUSED_PARAMETER") unusedp: Nothing? = null): CodingKey, RawRepresentable<String> {
        start("start"),
        end("end"),
        date("date"),
        boatId("boatId"),
        boatPersonalName("boatPersonalName"),
        id("id"),
        isDeleted("isDeleted");

        companion object {
            fun init(rawValue: String): ReservationDto.CodingKeys? {
                return when (rawValue) {
                    "start" -> CodingKeys.start
                    "end" -> CodingKeys.end
                    "date" -> CodingKeys.date
                    "boatId" -> CodingKeys.boatId
                    "boatPersonalName" -> CodingKeys.boatPersonalName
                    "id" -> CodingKeys.id
                    "isDeleted" -> CodingKeys.isDeleted
                    else -> null
                }
            }
        }
    }

    constructor(from: Decoder) {
        val decoder = from
        val container = decoder.container(keyedBy = CodingKeys::class)

        // Decode the string values for each date field
        val startString = container.decode(String::class, forKey = ReservationDto.CodingKeys.start)
        val endString = container.decode(String::class, forKey = ReservationDto.CodingKeys.end)
        val dateString = container.decode(String::class, forKey = ReservationDto.CodingKeys.date)
        val start_0 = timeFormatter.date(from = startString)
        if (start_0 == null) {
            throw DecodingError.dataCorruptedError(forKey = ReservationDto.CodingKeys.start, in_ = container, debugDescription = "Date format is incorrect")
        }
        val end_0 = timeFormatter.date(from = endString)
        if (end_0 == null) {
            throw DecodingError.dataCorruptedError(forKey = ReservationDto.CodingKeys.start, in_ = container, debugDescription = "Date format is incorrect")
        }
        val date_0 = dateFormatter.date(from = dateString)
        if (date_0 == null) {
            throw DecodingError.dataCorruptedError(forKey = ReservationDto.CodingKeys.start, in_ = container, debugDescription = "Date format is incorrect")
        }

        this.start = start_0.sref()
        this.end = end_0.sref()
        this.date = date_0.sref()
        this.boatId = container.decode(Int::class, forKey = ReservationDto.CodingKeys.boatId)
        this.boatPersonalName = container.decode(String::class, forKey = ReservationDto.CodingKeys.boatPersonalName)
        this.id = container.decode(Int::class, forKey = ReservationDto.CodingKeys.id)
        this.isDeleted = container.decode(Boolean::class, forKey = ReservationDto.CodingKeys.isDeleted)
    }

    constructor(start: Date, end: Date, date: Date, boatId: Int, boatPersonalName: String, id: Int, isDeleted: Boolean) {
        this.start = start.sref()
        this.end = end.sref()
        this.date = date.sref()
        this.boatId = boatId
        this.boatPersonalName = boatPersonalName
        this.id = id
        this.isDeleted = isDeleted
    }

    override fun encode(to: Encoder) {
        val container = to.container(keyedBy = CodingKeys::class)
        container.encode(start, forKey = CodingKeys.start)
        container.encode(end, forKey = CodingKeys.end)
        container.encode(date, forKey = CodingKeys.date)
        container.encode(boatId, forKey = CodingKeys.boatId)
        container.encode(boatPersonalName, forKey = CodingKeys.boatPersonalName)
        container.encode(id, forKey = CodingKeys.id)
        container.encode(isDeleted, forKey = CodingKeys.isDeleted)
    }

    companion object: DecodableCompanion<ReservationDto> {
        override fun init(from: Decoder): ReservationDto = ReservationDto(from = from)

        internal fun CodingKeys(rawValue: String): ReservationDto.CodingKeys? = CodingKeys.init(rawValue = rawValue)
    }
}

private val dateFormatter: DateFormatter = linvoke l@{ ->
    val formatter = DateFormatter()
    formatter.dateFormat = "yyyy-MM-dd"
    return@l formatter
}

private val timeFormatter: DateFormatter = linvoke l@{ ->
    val formatter = DateFormatter()
    formatter.dateFormat = "HH:mm:ss"
    return@l formatter
}
