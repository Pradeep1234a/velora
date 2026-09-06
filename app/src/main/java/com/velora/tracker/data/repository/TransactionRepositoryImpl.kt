package com.velora.tracker.data.repository

import com.velora.tracker.data.local.dao.CategoryDao
import com.velora.tracker.data.local.dao.TransactionDao
import com.velora.tracker.data.local.entity.toDomain
import com.velora.tracker.data.local.entity.toEntity
import com.velora.tracker.domain.model.Transaction
import com.velora.tracker.domain.repository.TransactionRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map

class TransactionRepositoryImpl(
    private val transactionDao: TransactionDao,
    private val categoryDao: CategoryDao
) : TransactionRepository {

    override fun getAllTransactions(): Flow<List<Transaction>> {
        return combine(
            transactionDao.getAllTransactions(),
            categoryDao.getAllCategories()
        ) { txs, cats ->
            val catMap = cats.associateBy { it.id }
            txs.map { tx ->
                val cat = catMap[tx.categoryId]
                tx.toDomain(
                    categoryName = cat?.name ?: "Unknown",
                    categoryIconName = cat?.iconName ?: "help",
                    categoryColorHex = cat?.colorHex ?: "#9E9E9E"
                )
            }
        }
    }

    override fun getTransactionsByDateRange(startMillis: Long, endMillis: Long): Flow<List<Transaction>> {
        return combine(
            transactionDao.getTransactionsByDateRange(startMillis, endMillis),
            categoryDao.getAllCategories()
        ) { txs, cats ->
            val catMap = cats.associateBy { it.id }
            txs.map { tx ->
                val cat = catMap[tx.categoryId]
                tx.toDomain(
                    categoryName = cat?.name ?: "Unknown",
                    categoryIconName = cat?.iconName ?: "help",
                    categoryColorHex = cat?.colorHex ?: "#9E9E9E"
                )
            }
        }
    }

    override fun getTransactionById(id: Long): Flow<Transaction?> {
        return combine(
            transactionDao.getTransactionById(id),
            categoryDao.getAllCategories()
        ) { tx, cats ->
            if (tx == null) return@combine null
            val cat = cats.find { it.id == tx.categoryId }
            tx.toDomain(
                categoryName = cat?.name ?: "Unknown",
                categoryIconName = cat?.iconName ?: "help",
                categoryColorHex = cat?.colorHex ?: "#9E9E9E"
            )
        }
    }

    override fun searchTransactions(query: String): Flow<List<Transaction>> {
        return combine(
            transactionDao.searchTransactions(query),
            categoryDao.getAllCategories()
        ) { txs, cats ->
            val catMap = cats.associateBy { it.id }
            txs.map { tx ->
                val cat = catMap[tx.categoryId]
                tx.toDomain(
                    categoryName = cat?.name ?: "Unknown",
                    categoryIconName = cat?.iconName ?: "help",
                    categoryColorHex = cat?.colorHex ?: "#9E9E9E"
                )
            }
        }
    }

    override suspend fun insertTransaction(transaction: Transaction): Long {
        return transactionDao.insert(transaction.toEntity())
    }

    override suspend fun updateTransaction(transaction: Transaction) {
        transactionDao.update(transaction.toEntity())
    }

    override suspend fun deleteTransaction(id: Long) {
        transactionDao.deleteById(id)
    }

    override fun getTotalIncome(): Flow<Long> = transactionDao.getTotalIncome().map { it ?: 0L }

    override fun getTotalExpenses(): Flow<Long> = transactionDao.getTotalExpenses().map { it ?: 0L }

    override fun getTotalIncomeForPeriod(startMillis: Long, endMillis: Long): Flow<Long> =
        transactionDao.getTotalIncomeForPeriod(startMillis, endMillis).map { it ?: 0L }

    override fun getTotalExpensesForPeriod(startMillis: Long, endMillis: Long): Flow<Long> =
        transactionDao.getTotalExpensesForPeriod(startMillis, endMillis).map { it ?: 0L }

    override fun getTransactionCountByCategory(categoryId: Long): Flow<Int> =
        transactionDao.getTransactionCountByCategory(categoryId)

    override fun getRecentTransactions(limit: Int): Flow<List<Transaction>> {
        return combine(
            transactionDao.getRecentTransactions(limit),
            categoryDao.getAllCategories()
        ) { txs, cats ->
            val catMap = cats.associateBy { it.id }
            txs.map { tx ->
                val cat = catMap[tx.categoryId]
                tx.toDomain(
                    categoryName = cat?.name ?: "Unknown",
                    categoryIconName = cat?.iconName ?: "help",
                    categoryColorHex = cat?.colorHex ?: "#9E9E9E"
                )
            }
        }
    }
}
