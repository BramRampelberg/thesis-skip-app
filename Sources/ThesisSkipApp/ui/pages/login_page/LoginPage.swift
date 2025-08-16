//
//  LoginPage.swift
//  MADiOS
//
//  Created by Bram Rampelberg on 03/01/2025.
//  Copyright © 2025 HOGENT. All rights reserved.
//

import SwiftUI

#if SKIP
    import android.content.Context
#endif

struct LoginPage: View {
    @Environment(\.verticalSizeClass) private var verticalSizeClass
    @EnvironmentObject var loginViewModel: LoginViewModel
    @State private var isPasswordVisible: Bool = false
    
    private let baseLogoSize: CGFloat = 100
    private let maxWidth: CGFloat = 600

    var isVerticalCompact: Bool {
        verticalSizeClass == .compact
    }
    
    var scaledLogoSize: CGFloat {
            #if SKIP
        let context = ProcessInfo.processInfo.androidContext
            let scale = context.resources.configuration.fontScale
            return baseLogoSize * CGFloat(scale)
            
            #else
            return UIFontMetrics.default.scaledValue(for: baseLogoSize)
            #endif
        }

    var body: some View {
        VStack {
            Spacer()
            buutLogo
            loginForm
            if loginViewModel.loginState.isLoading {
                ProgressView()
            }
            if loginViewModel.loginState.hasError {
                Text(loginViewModel.loginState.errorDescription!).padding(
                    .horizontal,
                    20
                ).foregroundColor(Colors.red)
            }
            loginButton
            Spacer()
        }
        .frame(minWidth: 0, maxWidth: .infinity)
        .background(
            Colors.primary
        )
        #if !SKIP
            .ignoresSafeArea()
        #endif

    }

    var buutLogo: some View {
        Image("buut_log_white", bundle: .module)
            .resizable()
            .scaledToFit()
            .frame(width: scaledLogoSize, height: scaledLogoSize)
    }

    var loginForm: some View {
        Form {
            Section(
                header: Text("Login").bold().font(.title).foregroundColor(
                    .white
                )
            ) {
                TextField("Email", text: $loginViewModel.email)
                HStack {
                    if isPasswordVisible {
                        TextField("Password", text: $loginViewModel.password)
                    } else {
                        SecureField("Password", text: $loginViewModel.password)
                    }
                    Image(
                        systemName: isPasswordVisible
                            ? "eye.slash.fill" : "eye.fill"
                    )
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
            withAnimation(.easeIn(duration: 0.5)) {
                loginViewModel.login()
            }
        }) {
            Text("Login").frame(minWidth: 0, maxWidth: maxWidth)
        }
        .disabled(loginViewModel.loginState.isLoading)
        .padding()
        .background(
            RoundedRectangle(cornerRadius: .infinity).stroke(
                .white,
                lineWidth: 1
            )
        )
        .foregroundColor(Color.white)
        .padding(isVerticalCompact ? CGFloat(8) : CGFloat(20))
    }
}

#Preview {
    @Previewable @StateObject var loginViewModel = LoginViewModel()

    LoginPage().environmentObject(loginViewModel)
}
