//
//  Colors.swift
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

internal class Colors {

    companion object {
        internal val primary: Color = Color.from(hex = "FF42C4BE")
        internal val red: Color = Color.from(hex = "FFC44244")
    }
}
