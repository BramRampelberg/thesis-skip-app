//
//  ReservationsList.swift
//  MADiOS
//
//  Created by Bram Rampelberg on 02/01/2025.
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

internal class ReservationsList: View {
    internal var reservationsViewModel: ReservationsViewModel
        get() = _reservationsViewModel.wrappedValue
        set(newValue) {
            _reservationsViewModel.wrappedValue = newValue
        }
    internal var _reservationsViewModel = skip.ui.Environment<ReservationsViewModel>()
    private var selected: Int?
        get() = _selected.wrappedValue
        set(newValue) {
            _selected.wrappedValue = newValue
        }
    private var _selected: skip.ui.State<Int?> = skip.ui.State(null)

    override fun body(): View {
        return ComposeBuilder { composectx: ComposeContext ->
            List(reservationsViewModel.reservations) { reservation ->
                ComposeBuilder { composectx: ComposeContext ->
                    Button(action = { -> reservationsViewModel.selectedReservation = reservation }) { ->
                        ComposeBuilder { composectx: ComposeContext ->
                            ImportantReservationInfo(reservation = reservation).Compose(composectx)
                            ComposeResult.ok
                        }
                    }.Compose(composectx)
                    ComposeResult.ok
                }
            }.Compose(composectx)
        }
    }

    @Composable
    @Suppress("UNCHECKED_CAST")
    override fun Evaluate(context: ComposeContext, options: Int): kotlin.collections.List<Renderable> {
        val rememberedselected by rememberSaveable(stateSaver = context.stateSaver as Saver<skip.ui.State<Int?>, Any>) { mutableStateOf(_selected) }
        _selected = rememberedselected

        _reservationsViewModel.wrappedValue = EnvironmentValues.shared.environmentObject(type = ReservationsViewModel::class)!!

        return super.Evaluate(context, options)
    }

    private constructor(selected: Int? = null, privatep: Nothing? = null) {
        this._selected = skip.ui.State(selected)
    }

    constructor(): this(privatep = null) {
    }
}

// #Preview omitted
