//
//  CoreDataStack.swift
//  MADiOS
//
//  Created by Bram Rampelberg on 02/01/2025.
//  Copyright © 2025 HOGENT. All rights reserved.
//

#if !SKIP
import Foundation
import CoreData

// Code source: https://developer.apple.com/documentation/coredata/setting_up_a_core_data_stack
final class CoreDataStack: ObservableObject {
    nonisolated(unsafe) static let shared = CoreDataStack()
    
    // Create a persistent container as a lazy variable to defer instantiation until its first use.
    lazy var persistentContainer: NSPersistentContainer = {
        
        // Pass the data model filename to the container’s initializer.
        let container = NSPersistentContainer(name: "Model")
        container.viewContext.mergePolicy = NSMergePolicy.mergeByPropertyObjectTrump
        
        // Load any persistent stores, which creates a store if none exists.
        container.loadPersistentStores { _, error in
            if let error {
                // Handle the error appropriately. However, it's useful to use
                // `fatalError(_:file:line:)` during development.
                fatalError("Failed to load persistent stores: \(error.localizedDescription)")
            }
        }
        
        return container
    }()
    
    // Add a convenience method to commit changes to the store.
    func save() throws {
        // Verify that the context has uncommitted changes.
        guard persistentContainer.viewContext.hasChanges else { return }
        
        try persistentContainer.viewContext.save()
    }
    
    private init() { }
}
#endif
