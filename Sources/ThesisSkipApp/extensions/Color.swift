//
//  Color.swift
//  MADiOS
//
//  Created by Bram Rampelberg on 03/01/2025.
//  Copyright © 2025 HOGENT. All rights reserved.
//

import Foundation
import SwiftUICore

//Code from: https://stackoverflow.com/a/56874327
extension Color {
    static func from(hex: String) -> Color {
        var cleanHex = hex
        if cleanHex.hasPrefix("#") {
            cleanHex = String(cleanHex.dropFirst())
        }

        guard let intValue = Int64(cleanHex, radix: 16) else {
            // If the string is not a valid hex color, return a default.
            // Black is a good choice as it's obvious when something is wrong.
            return .black
        }

        let a: Int64
        let r: Int64
        let g: Int64
        let b: Int64
        switch cleanHex.count {
        case 3:  // RGB (12-bit)
            (a, r, g, b) = (
                255, (intValue >> 8) * 17, (intValue >> 4 & 0xF) * 17,
                (intValue & 0xF) * 17
            )
        case 6:  // RGB (24-bit)
            (a, r, g, b) = (
                255, intValue >> 16, intValue >> 8 & 0xFF, intValue & 0xFF
            )
        case 8:  // ARGB (32-bit)
            (a, r, g, b) = (
                intValue >> 24, intValue >> 16 & 0xFF, intValue >> 8 & 0xFF,
                intValue & 0xFF
            )
        default:
            (a, r, g, b) = (1, 1, 1, 0)
        }

        return Color(
            Color.RGBColorSpace.sRGB,
            red: Double(r) / 255,
            green: Double(g) / 255,
            blue: Double(b) / 255,
            opacity: Double(a) / 255
        )
    }
}
