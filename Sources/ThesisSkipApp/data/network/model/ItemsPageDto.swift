//
//  ItemsPageDto.swift
//  MADiOS
//
//  Created by Bram Rampelberg on 04/01/2025.
//  Copyright © 2025 HOGENT. All rights reserved.
//

import Foundation

struct ItemsPageDto<T: Codable & Sendable>: Codable, Sendable {
    let data: [T]
    let nextId: Int?
    let previousId: Int?
    let isFirstPage: Bool
}
