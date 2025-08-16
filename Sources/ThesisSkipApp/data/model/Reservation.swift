//
//  ReservationModel.swift
//  MADiOS
//
//  Created by Bram Rampelberg on 02/01/2025.
//  Copyright © 2025 HOGENT. All rights reserved.
//

#if !SKIP
import CoreData
#endif

import Foundation


struct Reservation {
    var start: Date
    var end: Date
    var date: Date
    var boatId: Int
    var boatPersonalName: String
    var id: Int
    var isDeleted: Bool
    
    init(start: Date, end: Date, date: Date, boatId: Int, boatPersonalName: String, id: Int, isDeleted: Bool) {
        self.start = start
        self.end = end
        self.date = date
        self.boatId = boatId
        self.boatPersonalName = boatPersonalName
        self.id = id
        self.isDeleted = isDeleted
    }
    
#if !SKIP
    init(fromEntity entity: ReservationEntity) throws {
        if (entity.start == nil || entity.end == nil || entity.date == nil) {
            throw EntityConversionError(localizedDescription: "Failed to convert ReservationEnitity to Reservation: start, end or date was nil")
        }
        start = entity.start!
        end = entity.end!
        date = entity.date!
        boatId = Int(entity.boatId)
        boatPersonalName = entity.boatPersonalName!
        id = Int(entity.id)
        isDeleted = entity.isRemoved
    }
#endif
    
    init(fromDto dto: ReservationDto) {
        start = dto.start
        end = dto.end
        date = dto.date
        boatId = dto.boatId
        boatPersonalName = dto.boatPersonalName
        id = dto.id
        isDeleted = dto.isDeleted
    }
}

extension Reservation: Identifiable, Hashable {
    var identifier: Int { id }
}
