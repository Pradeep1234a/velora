package com.velora.tracker.domain.repository

import com.velora.tracker.domain.model.Category
import com.velora.tracker.domain.model.TransactionType
import kotlinx.coroutines.flow.Flow

interface CategoryRepository {
    fun getAllCategories(): Flow<List<Category>>
    fun getCategoriesByType(type: TransactionType): Flow<List<Category>>
    fun getCategoryById(id: Long): Flow<Category?>
    suspend fun insertCategory(category: Category): Long
    suspend fun updateCategory(category: Category)
    suspend fun deleteCategory(id: Long)
    fun getTransactionCountForCategory(categoryId: Long): Flow<Int>
}
