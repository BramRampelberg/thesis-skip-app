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
    internal var horizontalSizeClass: UserInterfaceSizeClass? = null
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

    internal val isHorizontalCompact: Boolean
        get() = horizontalSizeClass == UserInterfaceSizeClass.compact

    override fun body(): View {
        return ComposeBuilder { composectx: ComposeContext ->
            if (isHorizontalCompact) {
                listView.Compose(composectx)
            } else {
                tableView.Compose(composectx)
            }
            ComposeResult.ok
        }
    }

    @Composable
    @Suppress("UNCHECKED_CAST")
    override fun Evaluate(context: ComposeContext, options: Int): kotlin.collections.List<Renderable> {
        val rememberedselected by rememberSaveable(stateSaver = context.stateSaver as Saver<skip.ui.State<Int?>, Any>) { mutableStateOf(_selected) }
        _selected = rememberedselected

        this.horizontalSizeClass = EnvironmentValues.shared.horizontalSizeClass
        _reservationsViewModel.wrappedValue = EnvironmentValues.shared.environmentObject(type = ReservationsViewModel::class)!!

        return super.Evaluate(context, options)
    }

    private val listView: View
        get() {
            return List(reservationsViewModel.reservations) { reservation ->
                ComposeBuilder { composectx: ComposeContext ->
                    Button(action = { -> reservationsViewModel.selectedReservation = reservation }) { ->
                        ComposeBuilder { composectx: ComposeContext ->
                            ImportantReservationInfo(reservation = reservation).Compose(composectx)
                            ComposeResult.ok
                        }
                    }.Compose(composectx)
                    ComposeResult.ok
                }
            }
        }

    private val tableView: View
        get() {
            return Table(reservationsViewModel.reservations, selection = Binding({ _selected.wrappedValue }, { it -> _selected.wrappedValue = it })) { it ->
                ComposeBuilder { composectx: ComposeContext ->
                    TableColumn(it, LocalizedStringKey(stringLiteral = "Date"), content = { reservation: Reservation ->
                        ComposeBuilder { composectx: ComposeContext ->
                            Text({
                                val str = LocalizedStringKey.StringInterpolation(literalCapacity = 0, interpolationCount = 0)
                                str.appendLiteral("Date: ")
                                str.appendInterpolation(reservation.date.formatted(date = Date.FormatStyle.DateStyle.numeric, time = Date.FormatStyle.TimeStyle.omitted))
                                LocalizedStringKey(stringInterpolation = str)
                            }()).Compose(composectx)
                            ComposeResult.ok
                        }
                    }).Compose(composectx)

                    TableColumn(it, LocalizedStringKey(stringLiteral = "Time"), content = { reservation: Reservation ->
                        ComposeBuilder { composectx: ComposeContext ->
                            Text({
                                val str = LocalizedStringKey.StringInterpolation(literalCapacity = 0, interpolationCount = 0)
                                str.appendInterpolation(reservation.start.formatted(date = Date.FormatStyle.DateStyle.omitted, time = Date.FormatStyle.TimeStyle.shortened))
                                str.appendLiteral(" - ")
                                str.appendInterpolation(reservation.end.formatted(date = Date.FormatStyle.DateStyle.omitted, time = Date.FormatStyle.TimeStyle.shortened))
                                LocalizedStringKey(stringInterpolation = str)
                            }()).Compose(composectx)
                            ComposeResult.ok
                        }
                    }).Compose(composectx)

                    TableColumn(it, LocalizedStringKey(stringLiteral = "Boat"), content = { reservation: Reservation ->
                        ComposeBuilder { composectx: ComposeContext ->
                            Text({
                                val str = LocalizedStringKey.StringInterpolation(literalCapacity = 0, interpolationCount = 0)
                                str.appendLiteral("Boat: ")
                                str.appendInterpolation(reservation.boatPersonalName)
                                LocalizedStringKey(stringInterpolation = str)
                            }()).Compose(composectx)
                            ComposeResult.ok
                        }
                    }).Compose(composectx)
                    ComposeResult.ok
                }
            }.onChange(of = selected) { oldId, newId ->
                val reservation = reservationsViewModel.reservations.first { reservation -> reservation.identifier == newId }
                reservationsViewModel.selectedReservation = reservation
            }
        }

    private constructor(selected: Int? = null, privatep: Nothing? = null) {
        this._selected = skip.ui.State(selected)
    }

    constructor(): this(privatep = null) {
    }
}

// #Preview omitted
