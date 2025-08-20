//
//  ImportantReservationInfo.swift
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

internal class ImportantReservationInfo: View {
    internal val reservation: Reservation

    override fun body(): View {
        return ComposeBuilder { composectx: ComposeContext ->
            VStack(alignment = HorizontalAlignment.leading) { ->
                ComposeBuilder { composectx: ComposeContext ->
                    Label(reservation.date.formatted(date = Date.FormatStyle.DateStyle.abbreviated, time = Date.FormatStyle.TimeStyle.omitted), systemImage = "calendar")
                        .font(Font.headline)
                        .foregroundColor(Color.primary).Compose(composectx)

                    Label(title = { ->
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
                    }, icon = { ->
                        ComposeBuilder { composectx: ComposeContext ->
                            Image("clock", bundle = Bundle.module)
                                .resizable()
                                .aspectRatio(contentMode = ContentMode.fit)
                                .frame(width = 20.0, height = 20.0).Compose(composectx)
                            ComposeResult.ok
                        }
                    }).font(Font.subheadline)
                        .foregroundColor(Color.primary).Compose(composectx)

                    Label(title = { ->
                        ComposeBuilder { composectx: ComposeContext ->
                            Text(reservation.boatPersonalName).Compose(composectx)
                            ComposeResult.ok
                        }
                    }, icon = { ->
                        ComposeBuilder { composectx: ComposeContext ->
                            Image("sailboat.fill", bundle = Bundle.module)
                                .resizable()
                                .aspectRatio(contentMode = ContentMode.fit)
                                .frame(width = 26.0, height = 26.0).Compose(composectx)
                            ComposeResult.ok
                        }
                    }).font(Font.subheadline)
                        .foregroundColor(Color.secondary).Compose(composectx)
                    ComposeResult.ok
                }
            }.padding(Edge.Set.vertical, 5.0).Compose(composectx)
        }
    }

    constructor(reservation: Reservation) {
        this.reservation = reservation.sref()
    }
}

// #Preview omitted
