//
//  FullSizeContainer.swift
//  MADiOS
//
//  Created by Bram Rampelberg on 01/01/2025.
//  Copyright © 2025 HOGENT. All rights reserved.
//

import SwiftUI

struct MaximizedContainer<Content: View>: View {
    let content: Content
    
    init(@ViewBuilder content: () -> Content) {
            self.content = content()
        }

    var body: some View {
        content
            .frame(maxWidth: .infinity, maxHeight: .infinity)
    }
}
