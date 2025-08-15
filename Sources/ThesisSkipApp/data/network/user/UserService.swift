//
//  UserService.swift
//  MADiOS
//
//  Created by Bram Rampelberg on 12/08/2025.
//  Copyright © 2025 HOGENT. All rights reserved.
//

import Foundation
import os

final class UserService {
    nonisolated(unsafe) static let shared = UserService()
    
    private let serviceHelper = APIServiceHelper.shared
    
    let logger = Logger()
    
    private init(){}
    
    func fetchUserDetails() async -> Result<UserDetailsDto> {
        return await serviceHelper.sendRequest(to: "api/User/profile", method: .get)
    }
}
