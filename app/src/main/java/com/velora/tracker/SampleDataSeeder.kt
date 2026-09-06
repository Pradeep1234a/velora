package com.velora.tracker

import com.velora.tracker.domain.model.PaymentMethod
import com.velora.tracker.domain.model.Transaction
import com.velora.tracker.domain.model.TransactionType
import com.velora.tracker.domain.repository.SettingsRepository
import com.velora.tracker.domain.repository.TransactionRepository
import kotlinx.coroutines.flow.first
import java.time.LocalDateTime

object SampleDataSeeder {
    suspend fun seedIfEmpty(transactionRepository: TransactionRepository, settingsRepository: SettingsRepository) {
        val currentTransactions = transactionRepository.getAllTransactions().first()
        if (currentTransactions.isEmpty()) {
            settingsRepository.setOpeningBalance(2000000L) // 20,000 INR
            
            val transactions = createSampleTransactions()
            for (transaction in transactions) {
                transactionRepository.insertTransaction(transaction)
            }
        }
    }
    
    private fun createSampleTransactions(): List<Transaction> {
        val now = LocalDateTime.now()
        val transactions = mutableListOf<Transaction>()
        
        // --- Current Month Income ---
        // Salary on 1st of current month
        val currentMonth1st = now.withDayOfMonth(1).withHour(9).withMinute(0)
        transactions.add(Transaction(
            amountMinorUnits = 5000000L, // 50,000 INR
            type = TransactionType.INCOME,
            title = "Monthly Salary",
            categoryId = 10, // Assuming 10 is Salary
            dateTime = currentMonth1st,
            paymentMethod = PaymentMethod.BANK_TRANSFER
        ))
        
        // Freelance work on 10th
        val currentMonth10th = if (now.dayOfMonth >= 10) now.withDayOfMonth(10).withHour(14).withMinute(30) else now.minusMonths(1).withDayOfMonth(10).withHour(14).withMinute(30)
        transactions.add(Transaction(
            amountMinorUnits = 1000000L, // 10,000 INR
            type = TransactionType.INCOME,
            title = "Freelance Project - Web App",
            categoryId = 11, // Assuming 11 is Freelance/Other Income
            dateTime = currentMonth10th,
            paymentMethod = PaymentMethod.UPI
        ))

        // --- Current Month Expenses ---
        // Rent on 2nd
        val currentMonth2nd = now.withDayOfMonth(2).withHour(10).withMinute(15)
        transactions.add(Transaction(
            amountMinorUnits = 1500000L, // 15,000 INR
            type = TransactionType.EXPENSE,
            title = "Monthly Rent",
            categoryId = 7, // Rent/Housing
            dateTime = currentMonth2nd,
            paymentMethod = PaymentMethod.BANK_TRANSFER
        ))

        // Electricity on 5th
        val currentMonth5th = if (now.dayOfMonth >= 5) now.withDayOfMonth(5).withHour(18).withMinute(0) else now.minusMonths(1).withDayOfMonth(5).withHour(18).withMinute(0)
        transactions.add(Transaction(
            amountMinorUnits = 150000L, // 1,500 INR
            type = TransactionType.EXPENSE,
            title = "Electricity Bill",
            categoryId = 6, // Bills & Utilities
            dateTime = currentMonth5th,
            paymentMethod = PaymentMethod.UPI
        ))

        // Internet on 6th
        val currentMonth6th = if (now.dayOfMonth >= 6) now.withDayOfMonth(6).withHour(20).withMinute(30) else now.minusMonths(1).withDayOfMonth(6).withHour(20).withMinute(30)
        transactions.add(Transaction(
            amountMinorUnits = 99900L, // 999 INR
            type = TransactionType.EXPENSE,
            title = "JioFiber Broadband",
            categoryId = 6,
            dateTime = currentMonth6th,
            paymentMethod = PaymentMethod.CREDIT_CARD
        ))

        // Groceries on 3rd
        val currentMonth3rd = now.withDayOfMonth(3).withHour(16).withMinute(45)
        transactions.add(Transaction(
            amountMinorUnits = 250000L, // 2,500 INR
            type = TransactionType.EXPENSE,
            title = "BigBasket Weekly",
            categoryId = 2, // Groceries
            dateTime = currentMonth3rd,
            paymentMethod = PaymentMethod.UPI
        ))

        // Food/Dining multiple times
        transactions.add(Transaction(
            amountMinorUnits = 45000L, // 450 INR
            type = TransactionType.EXPENSE,
            title = "Swiggy Dinner",
            categoryId = 1, // Food & Dining
            dateTime = now.minusDays(1).withHour(20).withMinute(15),
            paymentMethod = PaymentMethod.UPI
        ))
        
        transactions.add(Transaction(
            amountMinorUnits = 35000L, // 350 INR
            type = TransactionType.EXPENSE,
            title = "Lunch at Udupi",
            categoryId = 1,
            dateTime = now.minusDays(3).withHour(13).withMinute(30),
            paymentMethod = PaymentMethod.CASH
        ))

        transactions.add(Transaction(
            amountMinorUnits = 15000L, // 150 INR
            type = TransactionType.EXPENSE,
            title = "Starbucks Coffee",
            categoryId = 1,
            dateTime = now.minusDays(2).withHour(17).withMinute(0),
            paymentMethod = PaymentMethod.UPI
        ))

        // Transport
        transactions.add(Transaction(
            amountMinorUnits = 32000L, // 320 INR
            type = TransactionType.EXPENSE,
            title = "Uber to Office",
            categoryId = 4, // Transport
            dateTime = now.minusDays(4).withHour(8).withMinute(45),
            paymentMethod = PaymentMethod.CREDIT_CARD
        ))
        
        transactions.add(Transaction(
            amountMinorUnits = 8000L, // 80 INR
            type = TransactionType.EXPENSE,
            title = "Metro Recharge",
            categoryId = 4,
            dateTime = now.minusDays(5).withHour(18).withMinute(15),
            paymentMethod = PaymentMethod.UPI
        ))

        // Shopping
        transactions.add(Transaction(
            amountMinorUnits = 350000L, // 3,500 INR
            type = TransactionType.EXPENSE,
            title = "Amazon Shoes",
            categoryId = 3, // Shopping
            dateTime = now.minusDays(7).withHour(11).withMinute(20),
            paymentMethod = PaymentMethod.CREDIT_CARD
        ))

        // Entertainment
        transactions.add(Transaction(
            amountMinorUnits = 50000L, // 500 INR
            type = TransactionType.EXPENSE,
            title = "Movie Tickets - PVR",
            categoryId = 8, // Entertainment
            dateTime = now.minusDays(10).withHour(19).withMinute(30),
            paymentMethod = PaymentMethod.UPI
        ))

        // Subscriptions
        transactions.add(Transaction(
            amountMinorUnits = 64900L, // 649 INR
            type = TransactionType.EXPENSE,
            title = "Netflix",
            categoryId = 12, // Subscriptions
            dateTime = now.minusDays(15).withHour(10).withMinute(0),
            paymentMethod = PaymentMethod.CREDIT_CARD
        ))

        // Health
        transactions.add(Transaction(
            amountMinorUnits = 35000L, // 350 INR
            type = TransactionType.EXPENSE,
            title = "Apollo Pharmacy",
            categoryId = 9, // Health
            dateTime = now.minusDays(8).withHour(14).withMinute(10),
            paymentMethod = PaymentMethod.UPI
        ))

        // Fuel
        transactions.add(Transaction(
            amountMinorUnits = 200000L, // 2,000 INR
            type = TransactionType.EXPENSE,
            title = "Indian Oil Petrol",
            categoryId = 5, // Fuel
            dateTime = now.minusDays(12).withHour(9).withMinute(30),
            paymentMethod = PaymentMethod.CREDIT_CARD
        ))

        // --- Previous Month Data (for analytics) ---
        val lastMonth = now.minusMonths(1)
        
        // Previous Month Salary
        transactions.add(Transaction(
            amountMinorUnits = 5000000L,
            type = TransactionType.INCOME,
            title = "Monthly Salary",
            categoryId = 10,
            dateTime = lastMonth.withDayOfMonth(1).withHour(9).withMinute(0),
            paymentMethod = PaymentMethod.BANK_TRANSFER
        ))
        
        // Previous Month Rent
        transactions.add(Transaction(
            amountMinorUnits = 1500000L,
            type = TransactionType.EXPENSE,
            title = "Monthly Rent",
            categoryId = 7,
            dateTime = lastMonth.withDayOfMonth(2).withHour(10).withMinute(15),
            paymentMethod = PaymentMethod.BANK_TRANSFER
        ))

        // Previous Month Groceries
        transactions.add(Transaction(
            amountMinorUnits = 280000L,
            type = TransactionType.EXPENSE,
            title = "DMart Groceries",
            categoryId = 2,
            dateTime = lastMonth.withDayOfMonth(5).withHour(17).withMinute(0),
            paymentMethod = PaymentMethod.CREDIT_CARD
        ))

        // Previous Month Shopping
        transactions.add(Transaction(
            amountMinorUnits = 120000L,
            type = TransactionType.EXPENSE,
            title = "Myntra T-Shirts",
            categoryId = 3,
            dateTime = lastMonth.withDayOfMonth(12).withHour(15).withMinute(30),
            paymentMethod = PaymentMethod.UPI
        ))
        
        return transactions
    }
}
