package com.velora.tracker.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.velora.tracker.data.local.entity.TransactionEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface TransactionDao {
    @Query("SELECT * FROM transactions ORDER BY dateTimeMillis DESC")
    fun getAllTransactions(): Flow<List<TransactionEntity>>

    @Query("SELECT * FROM transactions WHERE dateTimeMillis BETWEEN :startMillis AND :endMillis ORDER BY dateTimeMillis DESC")
    fun getTransactionsByDateRange(startMillis: Long, endMillis: Long): Flow<List<TransactionEntity>>

    @Query("SELECT * FROM transactions WHERE id = :id LIMIT 1")
    fun getTransactionById(id: Long): Flow<TransactionEntity?>

    @Query("SELECT * FROM transactions WHERE title LIKE '%' || :query || '%' OR notes LIKE '%' || :query || '%' ORDER BY dateTimeMillis DESC")
    fun searchTransactions(query: String): Flow<List<TransactionEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(entity: TransactionEntity): Long

    @Update
    suspend fun update(entity: TransactionEntity)

    @Query("DELETE FROM transactions WHERE id = :id")
    suspend fun deleteById(id: Long)

    @Query("SELECT SUM(amountMinorUnits) FROM transactions WHERE type = 'INCOME'")
    fun getTotalIncome(): Flow<Long?>

    @Query("SELECT SUM(amountMinorUnits) FROM transactions WHERE type = 'EXPENSE'")
    fun getTotalExpenses(): Flow<Long?>

    @Query("SELECT SUM(amountMinorUnits) FROM transactions WHERE type = 'INCOME' AND dateTimeMillis BETWEEN :startMillis AND :endMillis")
    fun getTotalIncomeForPeriod(startMillis: Long, endMillis: Long): Flow<Long?>

    @Query("SELECT SUM(amountMinorUnits) FROM transactions WHERE type = 'EXPENSE' AND dateTimeMillis BETWEEN :startMillis AND :endMillis")
    fun getTotalExpensesForPeriod(startMillis: Long, endMillis: Long): Flow<Long?>

    @Query("SELECT COUNT(*) FROM transactions WHERE categoryId = :categoryId")
    fun getTransactionCountByCategory(categoryId: Long): Flow<Int>

    @Query("SELECT * FROM transactions ORDER BY dateTimeMillis DESC LIMIT :limit")
    fun getRecentTransactions(limit: Int): Flow<List<TransactionEntity>>

    @Query("UPDATE transactions SET categoryId = :newId WHERE categoryId = :oldId")
    suspend fun updateTransactionCategories(oldId: Long, newId: Long)
}
