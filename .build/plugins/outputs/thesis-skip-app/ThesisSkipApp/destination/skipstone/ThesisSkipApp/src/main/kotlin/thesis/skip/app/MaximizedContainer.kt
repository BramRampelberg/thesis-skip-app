//
//  FullSizeContainer.swift
//  MADiOS
//
//  Created by Bram Rampelberg on 01/01/2025.
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

import skip.ui.*
import skip.foundation.*
import skip.model.*

internal class MaximizedContainer<Content>: View where Content: View {
    internal val content: Content

    internal constructor(content: () -> Content) {
        this.content = content()
    }

    override fun body(): View {
        return ComposeBuilder { composectx: ComposeContext ->
            content
                .frame(maxWidth = Double.infinity, maxHeight = Double.infinity).Compose(composectx)
        }
    }
}
