//
//  Color.swift
//  MADiOS
//
//  Created by Bram Rampelberg on 03/01/2025.
//  Copyright © 2025 HOGENT. All rights reserved.
//

import Foundation
import SwiftUI

//Code from: https://stackoverflow.com/a/56874327
extension Color {
    static func from(hex: String) -> Color {
        var cleanHex = hex
        if cleanHex.hasPrefix("#") {
            cleanHex = String(cleanHex.dropFirst())
        }

        guard let intValue = parseHex(cleanHex) else {
            return .black
        }

        var a: Int32 = 0
        var r: Int32 = 0
        var g: Int32 = 0
        var b: Int32 = 0
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
    
    private static func parseHex(_ hex: String) -> Int32? {
            #if SKIP
            return hex.toIntOrNull(16)

            #else
            return Int32(hex, radix: 16)
            #endif
        }
}
