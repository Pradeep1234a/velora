package com.velora.tracker.domain.repository

import com.velora.tracker.domain.model.Transaction
import kotlinx.coroutines.flow.Flow

interface TransactionRepository {
    fun getAllTransactions(): Flow<List<Transaction>>
    fun getTransactionsByDateRange(startMillis: Long, endMillis: Long): Flow<List<Transaction>>
    fun getTransactionById(id: Long): Flow<Transaction?>
    fun searchTransactions(query: String): Flow<List<Transaction>>
    suspend fun insertTransaction(transaction: Transaction): Long
    suspend fun updateTransaction(transaction: Transaction)
    suspend fun deleteTransaction(id: Long)
    fun getTotalIncome(): Flow<Long>
    fun getTotalExpenses(): Flow<Long>
    fun getTotalIncomeForPeriod(startMillis: Long, endMillis: Long): Flow<Long>
    fun getTotalExpensesForPeriod(startMillis: Long, endMillis: Long): Flow<Long>
    fun getTransactionCountByCategory(categoryId: Long): Flow<Int>
    fun getRecentTransactions(limit: Int): Flow<List<Transaction>>
}
