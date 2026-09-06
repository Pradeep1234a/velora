package com.velora.tracker.domain.usecase

import com.velora.tracker.domain.model.Transaction
import com.velora.tracker.domain.repository.TransactionRepository

class AddTransactionUseCase(
    private val transactionRepository: TransactionRepository
) {
    suspend operator fun invoke(transaction: Transaction): Long {
        val now = System.currentTimeMillis()
        val newTx = transaction.copy(
            createdAt = now,
            updatedAt = now
        )
        return transactionRepository.insertTransaction(newTx)
    }
}
