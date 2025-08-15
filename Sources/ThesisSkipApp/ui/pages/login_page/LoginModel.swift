//
//  LoginModel.swift
//  MADiOS
//
//  Created by Bram Rampelberg on 03/01/2025.
//  Copyright © 2025 HOGENT. All rights reserved.
//

import Foundation

struct LoginModel {
    private(set) var loginState = LoginState.loggedOut
    private(set) var email: String
    private(set) var password: String
    
    mutating func changeLoginState(to state: LoginState){
        loginState = state
    }
    
    mutating func setEmail(to email: String) {
        self.email = email
    }
    
    mutating func setPassword(to password: String) {
        self.password = password
    }
}

enum LoginState {
    case loggedOut
    case loading
    case loggedIn
    case error(String)
    
    var isLoading: Bool {
        switch self {
        case .loading: return true
        default: return false
        }
    }
    
    var isLoggedIn: Bool {
        switch self {
        case .loggedIn: return true
        default: return false
        }
    }
    
    var hasError: Bool {
        switch self {
        case .error: return true
        default: return false
        }
    }
    
    var errorDescription: String? {
        switch self {
        case .error(let description): return description
        default: return nil
        }
    }
}
