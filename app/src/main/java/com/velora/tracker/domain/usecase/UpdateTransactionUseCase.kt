package com.velora.tracker.domain.usecase

import com.velora.tracker.domain.model.Transaction
import com.velora.tracker.domain.repository.TransactionRepository

class UpdateTransactionUseCase(
    private val transactionRepository: TransactionRepository
) {
    suspend operator fun invoke(transaction: Transaction) {
        val updatedTx = transaction.copy(updatedAt = System.currentTimeMillis())
        transactionRepository.updateTransaction(updatedTx)
    }
}
