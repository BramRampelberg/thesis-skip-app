//
//  LoginViewModel.swift
//  MADiOS
//
//  Created by Bram Rampelberg on 03/01/2025.
//  Copyright © 2025 HOGENT. All rights reserved.
//

import Foundation

class LoginViewModel: ObservableObject {
    private let auth0Repo = Auth0Repository.shared
    
    @Published private var loginModel: LoginModel
    
    init() {
        self.loginModel = LoginModel(email: "", password: "")
        if auth0Repo.userIsLoggedIn() {
            loginModel.changeLoginState(to: .loggedIn)
        }
    }
    
    var loginState: LoginState {
        loginModel.loginState
    }
    
    var email: String {
        get {
            loginModel.email
        }
        set {
            loginModel.setEmail(to: newValue)
        }
    }
    
    var password: String {
        get {
            loginModel.password
        }
        set {
            loginModel.setPassword(to: newValue)
        }
    }
    
    @MainActor
    func login () {
        loginModel.changeLoginState(to: .loading)
        
        Task {
            let result = await auth0Repo.login(email: loginModel.email, password: loginModel.password)
            if (result.isSuccess){
                loginModel.setEmail(to: "")
                loginModel.setPassword(to: "")
                loginModel.changeLoginState(to: .loggedIn)
            }
            else {
                loginModel.changeLoginState(to: .error(result.failureCause!))
            }
        }
    }
    
    func logout () {
        let success = auth0Repo.logout()
        if success {
            loginModel.changeLoginState(to: .loggedOut)
        }
    }
}
