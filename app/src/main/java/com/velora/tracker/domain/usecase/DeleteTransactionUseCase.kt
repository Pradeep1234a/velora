package com.velora.tracker.domain.usecase

import com.velora.tracker.domain.repository.TransactionRepository

class DeleteTransactionUseCase(
    private val transactionRepository: TransactionRepository
) {
    suspend operator fun invoke(id: Long) {
        transactionRepository.deleteTransaction(id)
    }
}
