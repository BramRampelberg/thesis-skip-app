//
//  ReservationsPage.swift
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
import skip.lib.Set

import skip.ui.*
import skip.foundation.*
import skip.model.*

internal class ReservationsPage: View {
    internal var reservationsViewModel: ReservationsViewModel
        get() = _reservationsViewModel.wrappedValue
        set(newValue) {
            _reservationsViewModel.wrappedValue = newValue
        }
    internal var _reservationsViewModel = skip.ui.Environment<ReservationsViewModel>()

    private val reservationDetailsState: ReservationDetailsState
        get() = reservationsViewModel.reservationDetailsState

    override fun body(): View {
        return ComposeBuilder { composectx: ComposeContext ->
            VStack { ->
                ComposeBuilder { composectx: ComposeContext ->
                    MaximizedContainer { ->
                        ComposeBuilder { composectx: ComposeContext ->
                            VStack(spacing = 0.0) { ->
                                ComposeBuilder { composectx: ComposeContext ->
                                    ReservationTypePicker().Compose(composectx)
                                    errorMessage.Compose(composectx)
                                    if ((reservationsViewModel.areReservationsLoading)) {
                                        Spacer().Compose(composectx)
                                        ProgressView().Compose(composectx)
                                        Spacer().Compose(composectx)
                                    } else {
                                        ReservationsList().Compose(composectx)
                                    }
                                    ComposeResult.ok
                                }
                            }
                            .sheet(isPresented = Binding({ _reservationsViewModel.wrappedValue.isReservationSelected }, { it -> _reservationsViewModel.wrappedValue.isReservationSelected = it })) { ->
                                ComposeBuilder { composectx: ComposeContext ->
                                    if (reservationDetailsState.isLoading) {
                                        ProgressView().presentationDetents(setOf(PresentationDetent.medium, PresentationDetent.large)).Compose(composectx)
                                    } else if (reservationDetailsState.hasError) {
                                        Text(reservationDetailsState.errorDescription!!).font(Font.title2).foregroundColor(Colors.red).presentationDetents(setOf(
                                            PresentationDetent.medium,
                                            PresentationDetent.large
                                        )).Compose(composectx)
                                    } else {
                                        ReservationDetail(reservation = reservationsViewModel
                                            .selectedReservation!!, reservationDetails = reservationDetailsState
                                            .reservationDetails, isReservationCancelable = reservationsViewModel
                                            .isReservationCancelable).Compose(composectx)
                                    }
                                    ComposeResult.ok
                                }
                            }.environmentObject(reservationsViewModel).Compose(composectx)
                            ComposeResult.ok
                        }
                    }.Compose(composectx)
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

    internal val errorMessage: View
        get() {
            return VStack { ->
                ComposeBuilder { composectx: ComposeContext ->
                    if (reservationsViewModel.hasReservationsError) {
                        Text(reservationsViewModel.reservationsErrorMessage!!)
                            .font(Font.title)
                            .bold()
                            .frame(minWidth = 0.0, maxWidth = Double.infinity)
                            .padding()
                            .foregroundColor(Color.white)
                            .background((Rectangle().fill(Colors.red)))
                            .multilineTextAlignment(TextAlignment.center).Compose(composectx)
                    }
                    ComposeResult.ok
                }
            }
        }
}

// #Preview omitted
