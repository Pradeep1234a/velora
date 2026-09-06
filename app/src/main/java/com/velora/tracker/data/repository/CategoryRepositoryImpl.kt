package com.velora.tracker.data.repository

import com.velora.tracker.data.local.dao.CategoryDao
import com.velora.tracker.data.local.dao.TransactionDao
import com.velora.tracker.data.local.entity.toDomain
import com.velora.tracker.data.local.entity.toEntity
import com.velora.tracker.domain.model.Category
import com.velora.tracker.domain.model.TransactionType
import com.velora.tracker.domain.repository.CategoryRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class CategoryRepositoryImpl(
    private val categoryDao: CategoryDao,
    private val transactionDao: TransactionDao
) : CategoryRepository {
    override fun getAllCategories(): Flow<List<Category>> {
        return categoryDao.getAllCategories().map { list -> list.map { it.toDomain() } }
    }

    override fun getCategoriesByType(type: TransactionType): Flow<List<Category>> {
        return categoryDao.getByType(type.name).map { list -> list.map { it.toDomain() } }
    }

    override fun getCategoryById(id: Long): Flow<Category?> {
        return categoryDao.getById(id).map { it?.toDomain() }
    }

    override suspend fun insertCategory(category: Category): Long {
        return categoryDao.insert(category.toEntity())
    }

    override suspend fun updateCategory(category: Category) {
        categoryDao.update(category.toEntity())
    }

    override suspend fun deleteCategory(id: Long) {
        categoryDao.deleteById(id)
    }

    override fun getTransactionCountForCategory(categoryId: Long): Flow<Int> {
        return transactionDao.getTransactionCountByCategory(categoryId)
    }
}
