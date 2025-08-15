////
////  ReservationRepository.swift
////  MADiOS
////
////  Created by Bram Rampelberg on 02/01/2025.
////  Copyright © 2025 HOGENT. All rights reserved.
////
//
//import Foundation
//import CoreData
//
//final class ReservationRepository {
//    nonisolated(unsafe) static let shared = ReservationRepository()
//    
//    private let context = CoreDataStack.shared.persistentContainer.viewContext
//    private let sharedCoreDataStack = CoreDataStack.shared
//    
//    func getReservations (isPast: Bool, isCanceled: Bool) -> [ReservationEntity] {
//        let calendar = Calendar(identifier: .gregorian)
//        let startOfDay = calendar.startOfDay(for: Date()) as NSDate
//        
//        let request = NSFetchRequest<ReservationEntity>(entityName: "ReservationEntity")
//        let isCanceledPredicate = NSPredicate(format: "isRemoved = %@", isCanceled as NSNumber)
//        
//        if !isCanceled {
//            if isPast {
//                request.predicate = NSCompoundPredicate(type: .and, subpredicates: [isCanceledPredicate, NSPredicate(format: "date < %@", startOfDay)])
//            }
//            else {
//                request.predicate = NSCompoundPredicate(type: .and, subpredicates: [isCanceledPredicate, NSPredicate(format: "date >= %@", startOfDay)])
//            }
//        }
//        else {
//            request.predicate = isCanceledPredicate
//        }
//        
//        do {
//            return try context.fetch(request)
//        }catch {
//            print("DEBUG: Some error occured while fetching")
//        }
//        return []
//    }
//    
//    func addReservation (_ reservation: Reservation) -> Result<Void> {
//        let reservationEntity = ReservationEntity(context: context)
//        reservationEntity.id = Int32(reservation.id)
//        reservationEntity.isRemoved = reservation.isDeleted
//        reservationEntity.boatId = Int32(reservation.boatId)
//        reservationEntity.boatPersonalName = reservation.boatPersonalName
//        reservationEntity.date = reservation.date
//        reservationEntity.start = reservation.start
//        reservationEntity.end = reservation.end
//        
//        do{
//            try sharedCoreDataStack.save()
//            return .success(data: Void())
//        } catch {
//            return .failure(cause: error.localizedDescription, error: error)
//        }
//    }
//    
//    func addReservations(_ reservations: [Reservation]) -> Result<Void> {
//        let reservationDictionaries = reservations.map { reservation in
//            return [
//                "id": Int32(reservation.id),
//                "isRemoved": reservation.isDeleted,
//                "boatId": Int32(reservation.boatId),
//                "boatPersonalName": reservation.boatPersonalName,
//                "date": reservation.date,
//                "start": reservation.start,
//                "end": reservation.end
//            ] as [String: Any]
//        }
//        let request = NSBatchInsertRequest(entity: ReservationEntity.entity(), objects: reservationDictionaries)
//        
//        do {
//            try context.execute(request)
//            try sharedCoreDataStack.save()
//        } catch{
//            return .failureWithLog(cause: error.localizedDescription, error: error)
//        }
//        
//        return .success(data: Void())
//    }
//    
//    func clearReservations(){
//        
//        let fetchRequest = NSFetchRequest<NSFetchRequestResult>(
//            entityName: "ReservationEntity"
//        )
//        let deleteRequest = NSBatchDeleteRequest(fetchRequest: fetchRequest)
//        
//        do {
//            try context.execute(deleteRequest)
//        } catch {
//            print(
//                "Error deleting all data for entity \("ReservationEntity"): \(error)"
//            )
//        }
//        
//        do {
//            try context.save()
//        } catch {
//            print("Error saving context after deleting data: \(error)")
//        }
//    }
//    
//    private init() { }
//}
