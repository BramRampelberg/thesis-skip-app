//
//  ProfilePage.swift
//  MADiOS
//
//  Created by Bram Rampelberg on 03/01/2025.
//  Copyright © 2025 HOGENT. All rights reserved.
//

import SwiftUI

struct ProfilePage: View {
    @EnvironmentObject var profileViewModel: ProfileViewModel
    @EnvironmentObject var loginViewModel: LoginViewModel
    private var maxWidth: CGFloat = 600
    
    var body: some View {
        MaximizedContainer {
            VStack{
                if (profileViewModel.userState.hasError){
                    Text(profileViewModel.userState.errorDescription!).font(.title2).foregroundColor(Colors.red)
                }
                if (profileViewModel.userState.isLoading){
                    ProgressView()
                }
                if (profileViewModel.userState.isSuccess){
                    Text(profileViewModel.firstName + " " + profileViewModel.familyName).bold().font(.title)
                    Text(profileViewModel.email)
                }
                logoutButton.padding(.top, 24)
            }
            .padding(24)
        }.background(Color.from(hex: "F2F2F7"))
    }
    
    var logoutButton: some View {
        Button(action: {
            loginViewModel.logout()
        }){
            HStack{
                Image("rectangle.portrait.and.arrow.right", bundle: .module)
                Text("Log out")
            }.frame(minWidth: 0, maxWidth: maxWidth)
        }
        .padding()
        .background(RoundedRectangle(cornerRadius: .infinity).stroke(.gray, lineWidth: 1))
        .foregroundColor(.black)
    }
}

#Preview {
    @Previewable @StateObject var profileViewModel = ProfileViewModel()
    @Previewable @StateObject var loginViewModel = LoginViewModel()
    ProfilePage().environmentObject(profileViewModel).environmentObject(loginViewModel)
}
