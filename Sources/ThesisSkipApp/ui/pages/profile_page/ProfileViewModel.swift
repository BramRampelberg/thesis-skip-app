//
//  ProfileViewModel.swift
//  MADiOS
//
//  Created by Bram Rampelberg on 12/08/2025.
//  Copyright © 2025 HOGENT. All rights reserved.
//

import Foundation

class ProfileViewModel: ObservableObject {
    private let userRepo = UserRepository.shared
    
    @Published private var userModel: UserModel
    
    @MainActor
    init() {
        self.userModel = UserModel(email: "", firstName: "", familyName: "")
        getUser()
    }
    
    var userState: UserState {
        userModel.userState
    }
    
    var email: String {
        get {
            userModel.email
        }
    }
    
    var firstName: String {
        get {
            userModel.firstName
        }
    }
    
    var familyName: String {
        get {
            userModel.familyName
        }
    }
    
    @MainActor
    func refreshUserData(){
        getUser()
    }
    
    @MainActor
    private func getUser() {
        Task{
            let result = await userRepo.getUser()
            if (result.isSuccess){
                let data = result.data!
                userModel.setEmail(to: data.email)
                userModel.setFirstName(to: data.firstName)
                userModel.setFamilyName(to: data.familyName)
                userModel.changeUserState(to: UserState.success)
            }
            else {
                userModel.changeUserState(to: UserState.error(result.failureCause!))
            }
        }
       
    }
}
