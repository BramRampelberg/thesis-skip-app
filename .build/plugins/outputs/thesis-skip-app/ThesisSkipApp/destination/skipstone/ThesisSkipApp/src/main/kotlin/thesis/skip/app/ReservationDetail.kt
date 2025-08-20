//
//  ReservationDetail.swift
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
import skip.lib.Set

import skip.ui.*
import skip.foundation.*
import skip.model.*

internal class ReservationDetail: View {
    private var verticalSizeClass: UserInterfaceSizeClass? = null
    internal var reservationViewModel: ReservationsViewModel
        get() = _reservationViewModel.wrappedValue
        set(newValue) {
            _reservationViewModel.wrappedValue = newValue
        }
    internal var _reservationViewModel = skip.ui.Environment<ReservationsViewModel>()
    internal val reservation: Reservation
    internal val reservationDetails: ReservationDetails?
    internal val isReservationCancelable: Boolean

    private var showCancelErrorAlert: Boolean
        get() = _showCancelErrorAlert.wrappedValue
        set(newValue) {
            _showCancelErrorAlert.wrappedValue = newValue
        }
    private var _showCancelErrorAlert: skip.ui.State<Boolean>

    override fun body(): View {
        return ComposeBuilder { composectx: ComposeContext ->
            Form { ->
                ComposeBuilder { composectx: ComposeContext ->
                    Section(header = Text(LocalizedStringKey(stringLiteral = "Reservation Details")).font(Font.title2)) { ->
                        ComposeBuilder { composectx: ComposeContext ->
                            ImportantReservationInfo(reservation = reservation).Compose(composectx)
                            ComposeResult.ok
                        }
                    }.Compose(composectx)

                    if (reservationDetailsAreValid()) {
                        Section(header = Text(LocalizedStringKey(stringLiteral = "Acceptee Info"))) { ->
                            ComposeBuilder { composectx: ComposeContext ->
                                infoRow(label = "Name", value = reservationDetails!!.currentBatteryUserName!!).Compose(composectx)
                                infoRow(label = "Phone", value = reservationDetails!!.currentHolderPhoneNumber!!).Compose(composectx)
                                infoRow(label = "E-mail", value = reservationDetails!!.currentHolderEmail!!).Compose(composectx)
                                ComposeResult.ok
                            }
                        }.Compose(composectx)

                        Section(header = Text(LocalizedStringKey(stringLiteral = "Address"))) { ->
                            ComposeBuilder { composectx: ComposeContext ->
                                infoRow(label = "Street", value = "${reservationDetails!!.currentHolderStreet!!} ${reservationDetails!!.currentHolderNumber!!}").Compose(composectx)
                                infoRow(label = "City", value = "${reservationDetails!!.currentHolderPostalCode!!} ${reservationDetails!!.currentHolderCity!!}").Compose(composectx)
                                ComposeResult.ok
                            }
                        }.Compose(composectx)

                        reservationDetails?.mentorName?.let { mentorName ->
                            if (!mentorName.isEmpty) {
                                Section(header = Text(LocalizedStringKey(stringLiteral = "Mentor"))) { ->
                                    ComposeBuilder { composectx: ComposeContext ->
                                        Text(mentorName).Compose(composectx)
                                        ComposeResult.ok
                                    }
                                }.Compose(composectx)
                            }
                        }
                    } else {
                        Section { ->
                            ComposeBuilder { composectx: ComposeContext ->
                                detailsNotAvailable.Compose(composectx)
                                ComposeResult.ok
                            }
                        }.Compose(composectx)
                    }

                    if (reservationViewModel.isCancelationPending) {
                        Section { ->
                            ComposeBuilder { composectx: ComposeContext ->
                                HStack { ->
                                    ComposeBuilder { composectx: ComposeContext ->
                                        Spacer().Compose(composectx)
                                        ProgressView().padding().Compose(composectx)
                                        Spacer().Compose(composectx)
                                        ComposeResult.ok
                                    }
                                }.Compose(composectx)
                                ComposeResult.ok
                            }
                        }.Compose(composectx)
                    }

                    if (isReservationCancelable) {
                        Section { ->
                            ComposeBuilder { composectx: ComposeContext ->
                                cancelButton.Compose(composectx)
                                ComposeResult.ok
                            }
                        }.Compose(composectx)
                    }
                    ComposeResult.ok
                }
            }
            .presentationDetents(setOf(
                if (reservationDetailsAreValid()) PresentationDetent.fraction(0.7) else PresentationDetent.fraction(0.5),
                PresentationDetent.large
            )).Compose(composectx)
        }
    }

    @Composable
    @Suppress("UNCHECKED_CAST")
    override fun Evaluate(context: ComposeContext, options: Int): kotlin.collections.List<Renderable> {
        val rememberedshowCancelErrorAlert by rememberSaveable(stateSaver = context.stateSaver as Saver<skip.ui.State<Boolean>, Any>) { mutableStateOf(_showCancelErrorAlert) }
        _showCancelErrorAlert = rememberedshowCancelErrorAlert

        this.verticalSizeClass = EnvironmentValues.shared.verticalSizeClass
        _reservationViewModel.wrappedValue = EnvironmentValues.shared.environmentObject(type = ReservationsViewModel::class)!!

        return super.Evaluate(context, options)
    }

    internal fun infoRow(label: String, value: String): View {
        return HStack { ->
            ComposeBuilder { composectx: ComposeContext ->
                Text(label)
                    .foregroundColor(Color.secondary).Compose(composectx)
                Spacer().Compose(composectx)
                Text(value)
                    .multilineTextAlignment(TextAlignment.trailing).Compose(composectx)
                ComposeResult.ok
            }
        }
    }

    internal val detailsNotAvailable: View
        get() {
            return VStack { ->
                ComposeBuilder { composectx: ComposeContext ->
                    Image(systemName = "info.circle.fill")
                        .resizable()
                        .frame(width = 48.0, height = 48.0)
                        .foregroundColor(Color.blue)
                        .padding(Edge.Set.top, 20.0).Compose(composectx)

                    Text(LocalizedStringKey(stringLiteral = "No pickup information available"))
                        .font(Font.body)
                        .multilineTextAlignment(TextAlignment.center)
                        .padding(Edge.Set.top, 8.0).Compose(composectx)
                    ComposeResult.ok
                }
            }
            .frame(maxWidth = Double.infinity)
        }

    internal val cancelButton: View
        get() {
            return Button(role = ButtonRole.destructive, action = { -> reservationViewModel.cancelReservation() }) { ->
                ComposeBuilder { composectx: ComposeContext ->
                    Label(LocalizedStringKey(stringLiteral = "Cancel Reservation"), systemImage = "xmark.circle.fill")
                        .frame(maxWidth = Double.infinity)
                        .padding()
                        .foregroundColor(Color.red).Compose(composectx)
                    ComposeResult.ok
                }
            }
        }

    internal fun reservationDetailsAreValid(): Boolean {
        return !(reservationDetails?.currentBatteryUserName?.isEmpty ?: true || reservationDetails?.currentHolderPhoneNumber?.isEmpty ?: true || reservationDetails?.currentHolderEmail?.isEmpty ?: true || reservationDetails?.currentHolderStreet?.isEmpty ?: true || reservationDetails?.currentHolderNumber?.isEmpty ?: true || reservationDetails?.currentHolderCity?.isEmpty ?: true || reservationDetails?.currentHolderPostalCode?.isEmpty ?: true)
    }

    private constructor(reservation: Reservation, reservationDetails: ReservationDetails? = null, isReservationCancelable: Boolean, showCancelErrorAlert: Boolean = false, privatep: Nothing? = null) {
        this.reservation = reservation.sref()
        this.reservationDetails = reservationDetails.sref()
        this.isReservationCancelable = isReservationCancelable
        this._showCancelErrorAlert = skip.ui.State(showCancelErrorAlert)
    }

    constructor(reservation: Reservation, reservationDetails: ReservationDetails? = null, isReservationCancelable: Boolean): this(reservation = reservation, reservationDetails = reservationDetails, isReservationCancelable = isReservationCancelable, privatep = null) {
    }
}

// #Preview omitted
