//
//  UserModel.swift
//  MADiOS
//
//  Created by Bram Rampelberg on 12/08/2025.
//  Copyright © 2025 HOGENT. All rights reserved.
//

import Foundation

struct UserModel {
    private(set) var userState = UserState.loading
    private(set) var email: String
    private(set) var firstName: String
    private(set) var familyName: String
    
    mutating func changeUserState(to state: UserState){
        userState = state
    }
    
    mutating func setEmail(to email: String) {
        self.email = email
    }
    
    mutating func setFirstName(to firstName: String) {
        self.firstName = firstName
    }
    
    mutating func setFamilyName(to familyName: String) {
        self.familyName = familyName
    }
}

enum UserState {
    case loading
    case success
    case error(String)
    
    var isLoading: Bool {
        switch self {
        case .loading: return true
        default: return false
        }
    }
    
    var isSuccess: Bool {
        switch self {
        case .success: return true
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
