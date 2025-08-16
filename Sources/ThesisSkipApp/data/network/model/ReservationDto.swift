//
//  ReservationDto.swift
//  MADiOS
//
//  Created by Bram Rampelberg on 04/01/2025.
//  Copyright © 2025 HOGENT. All rights reserved.
//

import Foundation

struct ReservationDto: Codable, Sendable {
    let start: Date
    let end: Date
    let date: Date
    let boatId: Int
    let boatPersonalName: String
    let id: Int
    let isDeleted: Bool
    
    //Code from: ChatGPT
    enum CodingKeys: String, CodingKey {
            case start
            case end
            case date
            case boatId
            case boatPersonalName
            case id
            case isDeleted
        }

        init(from decoder: Decoder) throws {
            let container = try decoder.container(keyedBy: CodingKeys.self)

            // Decode the string values for each date field
            let startString = try container.decode(String.self, forKey: .start)
            let endString = try container.decode(String.self, forKey: .end)
            let dateString = try container.decode(String.self, forKey: .date)

            // Convert string to Date using different formatters
            guard let start = timeFormatter.date(from: startString),
                  let end = timeFormatter.date(from: endString),
                  let date = dateFormatter.date(from: dateString) else {
                throw DecodingError.dataCorruptedError(forKey: ReservationDto.CodingKeys.start, in: container, debugDescription: "Date format is incorrect")
            }

            self.start = start
            self.end = end
            self.date = date
            self.boatId = try container.decode(Int.self, forKey: .boatId)
            self.boatPersonalName = try container.decode(String.self, forKey: .boatPersonalName)
            self.id = try container.decode(Int.self, forKey: .id)
            self.isDeleted = try container.decode(Bool.self, forKey: .isDeleted)
        }
}

private let dateFormatter: DateFormatter = {
    let formatter = DateFormatter()
    formatter.dateFormat = "yyyy-MM-dd"
    return formatter
}()

private let timeFormatter: DateFormatter = {
    let formatter = DateFormatter()
    formatter.dateFormat = "HH:mm:ss"
    return formatter
}()
