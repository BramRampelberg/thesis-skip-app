//
//  ReservationTypePicker.swift
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

internal class ReservationTypePicker: View {
    internal var reservationsViewModel: ReservationsViewModel
        get() = _reservationsViewModel.wrappedValue
        set(newValue) {
            _reservationsViewModel.wrappedValue = newValue
        }
    internal var _reservationsViewModel = skip.ui.Environment<ReservationsViewModel>()

    override fun body(): View {
        return ComposeBuilder { composectx: ComposeContext ->
            VStack(spacing = 0.0) { ->
                ComposeBuilder { composectx: ComposeContext ->
                    HStack { ->
                        ComposeBuilder { composectx: ComposeContext ->
                            Text(LocalizedStringKey(stringLiteral = "Type:"))
                                .font(Font.headline).Compose(composectx)
                            Picker(LocalizedStringKey(stringLiteral = "Type"), selection = Binding({ _reservationsViewModel.wrappedValue.selectedReservationType }, { it -> _reservationsViewModel.wrappedValue.selectedReservationType = it })) { ->
                                ComposeBuilder { composectx: ComposeContext ->
                                    Text(LocalizedStringKey(stringLiteral = "Upcoming")).tag(ReservationType.upcoming).Compose(composectx)
                                    Text(LocalizedStringKey(stringLiteral = "Past")).tag(ReservationType.past).Compose(composectx)
                                    Text(LocalizedStringKey(stringLiteral = "Canceled")).tag(ReservationType.canceled).Compose(composectx)
                                    ComposeResult.ok
                                }
                            }.Compose(composectx)
                            Spacer().Compose(composectx)
                            ComposeResult.ok
                        }
                    }
                    .padding(Edge.Set.horizontal, 20.0)
                    .padding(Edge.Set.bottom, 16.0).Compose(composectx)

                    Divider()
                        .background(Color.gray)
                        .frame(height = 1.0).Compose(composectx)
                    ComposeResult.ok
                }
            }.Compose(composectx)
        }
    }

    @Composable
    override fun Evaluate(context: ComposeContext, options: Int): kotlin.collections.List<Renderable> {
        _reservationsViewModel.wrappedValue = EnvironmentValues.shared.environmentObject(type = ReservationsViewModel::class)!!

        return super.Evaluate(context, options)
    }
}

// #Preview omitted
