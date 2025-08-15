//
//  LoginPage.swift
//  MADiOS
//
//  Created by Bram Rampelberg on 03/01/2025.
//  Copyright © 2025 HOGENT. All rights reserved.
//

import SwiftUI

struct LoginPage: View {
    @Environment(\.verticalSizeClass) private var verticalSizeClass
    @EnvironmentObject var loginViewModel: LoginViewModel
    @State private var isPasswordVisible: Bool = false
    @ScaledMetric var logoSize: CGFloat = 100
    private let maxWidth: CGFloat = 600
    
    var isVerticalCompact: Bool {
        verticalSizeClass == .compact
    }
    
    var body: some View {
        VStack {
            Spacer()
            buutLogo
            loginForm   
            if (loginViewModel.loginState.isLoading) {
                ProgressView()
            }
            if (loginViewModel.loginState.hasError) {
                Text(loginViewModel.loginState.errorDescription!).padding(.horizontal, 20).foregroundColor(Colors.red)
            }
            loginButton
            Spacer()
        }
        .frame(minWidth: 0, maxWidth: .infinity)
        .background(Rectangle().fill(Colors.primary).ignoresSafeArea())
    }
    
    var buutLogo: some View {
        Image(.buutLogoWhite)
            .resizable()
            .scaledToFit()
            .frame(width: logoSize, height: logoSize)
    }
    
    var loginForm: some View {
        Form{
            Section (header: Text("Login").bold().font(.title).foregroundColor(.white)) {
                TextField("Email", text: $loginViewModel.email)
                HStack {
                    if isPasswordVisible {
                        TextField("Password", text: $loginViewModel.password)
                    }
                    else {
                        SecureField("Password", text: $loginViewModel.password)
                    }
                    Image(systemName: isPasswordVisible ? "eye.slash.fill" : "eye.fill")
                        .foregroundColor(.gray)
                        .onTapGesture {
                            isPasswordVisible = !isPasswordVisible
                        }
                }
            }
        }
        .scrollContentBackground(.hidden)
        .frame(maxWidth: maxWidth, maxHeight: 175)
    }
    
    var loginButton: some View {
        Button(action: {
            withAnimation (.easeIn(duration: 0.5)) {
                loginViewModel.login()
            }
        }) {
            Text("Login").frame(minWidth: 0, maxWidth: maxWidth)
        }
        .disabled(loginViewModel.loginState.isLoading)
        .padding()
        .background(RoundedRectangle(cornerRadius: .infinity).stroke(.white, lineWidth: 1))
        .padding(isVerticalCompact ? 8 : 20)
        .foregroundColor(.white)
    }
}

#Preview {
    @Previewable @StateObject var loginViewModel = LoginViewModel()
    
    LoginPage().environmentObject(loginViewModel)
}
