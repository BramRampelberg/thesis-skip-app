//
//  Color.swift
//  MADiOS
//
//  Created by Bram Rampelberg on 03/01/2025.
//  Copyright © 2025 HOGENT. All rights reserved.
//

package thesis.skip.app

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import skip.lib.*

import skip.foundation.*
import skip.ui.*
import skip.model.*

internal fun Color.Companion.from(hex: String): Color {
    var cleanHex = hex
    if (cleanHex.hasPrefix("#")) {
        cleanHex = String(cleanHex.dropFirst())
    }
    val intValue_0 = parseHex(cleanHex)
    if (intValue_0 == null) {
        return Color.black
    }

    var a: Long = 0
    var r: Long = 0
    var g: Long = 0
    var b: Long = 0
    when (cleanHex.count) {
        3 -> for (unusedi in 0..0) { val tmptuple = Tuple4(255L, (intValue_0 shr 8) * 17, (intValue_0 shr 4 and 0xF) * 17, (intValue_0 and 0xF) * 17); a = tmptuple.element0; r = tmptuple.element1; g = tmptuple.element2; b = tmptuple.element3 }
        6 -> for (unusedi in 0..0) { val tmptuple = Tuple4(255L, intValue_0 shr 16, intValue_0 shr 8 and 0xFF, intValue_0 and 0xFF); a = tmptuple.element0; r = tmptuple.element1; g = tmptuple.element2; b = tmptuple.element3 }
        8 -> for (unusedi in 0..0) { val tmptuple = Tuple4(intValue_0 shr 24, intValue_0 shr 16 and 0xFF, intValue_0 shr 8 and 0xFF, intValue_0 and 0xFF); a = tmptuple.element0; r = tmptuple.element1; g = tmptuple.element2; b = tmptuple.element3 }
        else -> for (unusedi in 0..0) { val tmptuple = Tuple4(255L, 0L, 0L, 0L); a = tmptuple.element0; r = tmptuple.element1; g = tmptuple.element2; b = tmptuple.element3 }
    }

    return Color(Color.RGBColorSpace.sRGB, red = Double(r) / 255, green = Double(g) / 255, blue = Double(b) / 255, opacity = Double(a) / 255)
}

private fun Color.Companion.parseHex(hex: String): Long? {
    return hex.toLongOrNull(16)

}
