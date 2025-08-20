//
//  ProfileViewModel.swift
//  MADiOS
//
//  Created by Bram Rampelberg on 12/08/2025.
//  Copyright © 2025 HOGENT. All rights reserved.
//

package thesis.skip.app

import androidx.compose.runtime.Stable
import androidx.compose.runtime.mutableStateOf
import skip.lib.*

import skip.foundation.*
import skip.model.*

@Stable
internal open class ProfileViewModel: ObservableObject {
    override val objectWillChange = ObservableObjectPublisher()
    private val userRepo = UserRepository.shared

    private var userModel: UserModel
        get() = _userModel.wrappedValue.sref({ this.userModel = it })
        set(newValue) {
            objectWillChange.send()
            _userModel.wrappedValue = newValue.sref()
        }
    private var _userModel: skip.model.Published<UserModel>

    internal constructor() {
        this._userModel = skip.model.Published(UserModel(email = "", firstName = "", familyName = ""))
    }

    internal open val userState: UserState
        get() = userModel.userState

    internal open val email: String
        get() = userModel.email

    internal open val firstName: String
        get() = userModel.firstName

    internal open val familyName: String
        get() = userModel.familyName

    internal open fun refreshUserData(): Unit = getUser()

    internal open fun getUser() {
        Task(isMainActor = true) { ->
            val result = userRepo.getUser()
            if ((result.isSuccess)) {
                val data = result.data!!
                userModel.setEmail(to = data.email)
                userModel.setFirstName(to = data.firstName)
                userModel.setFamilyName(to = data.familyName)
                userModel.changeUserState(to = UserState.success)
            } else {
                userModel.changeUserState(to = UserState.error(result.failureCause!!))
            }
        }

    }
}
