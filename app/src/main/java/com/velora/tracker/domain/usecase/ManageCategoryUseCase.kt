package com.velora.tracker.domain.usecase

import com.velora.tracker.domain.model.Category
import com.velora.tracker.domain.repository.CategoryRepository
import com.velora.tracker.domain.repository.TransactionRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first

class ManageCategoryUseCase(
    private val categoryRepository: CategoryRepository,
    private val transactionRepository: TransactionRepository
) {
    suspend fun addCategory(category: Category): Long {
        return categoryRepository.insertCategory(category)
    }

    suspend fun updateCategory(category: Category) {
        categoryRepository.updateCategory(category)
    }

    suspend fun deleteCategory(categoryId: Long, moveTransactionsToCategoryId: Long?) {
        if (moveTransactionsToCategoryId != null) {
            val transactions = transactionRepository.getAllTransactions().first().filter { it.categoryId == categoryId }
            transactions.forEach { tx ->
                transactionRepository.updateTransaction(tx.copy(categoryId = moveTransactionsToCategoryId))
            }
        }
        categoryRepository.deleteCategory(categoryId)
    }

    fun getTransactionCount(categoryId: Long): Flow<Int> {
        return categoryRepository.getTransactionCountForCategory(categoryId)
    }

    suspend fun getCategoryStats(categoryId: Long): CategoryStats {
        val transactions = transactionRepository.getAllTransactions().first().filter { it.categoryId == categoryId }
        val count = transactions.size
        val total = transactions.sumOf { it.amountMinorUnits }
        return CategoryStats(transactionCount = count, totalAmount = total)
    }

    suspend fun createCategory(name: String, type: com.velora.tracker.domain.model.TransactionType, iconName: String, colorHex: String): Long {
        return addCategory(Category(name = name, type = type, iconName = iconName, colorHex = colorHex))
    }

    suspend fun updateCategory(id: Long, name: String, iconName: String, colorHex: String) {
        val existing = categoryRepository.getCategoryById(id).first() ?: return
        updateCategory(existing.copy(name = name, iconName = iconName, colorHex = colorHex))
    }
}

data class CategoryStats(
    val transactionCount: Int,
    val totalAmount: Long
)
