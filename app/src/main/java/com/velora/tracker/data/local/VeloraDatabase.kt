package com.velora.tracker.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.velora.tracker.data.local.dao.CategoryDao
import com.velora.tracker.data.local.dao.TransactionDao
import com.velora.tracker.data.local.entity.CategoryEntity
import com.velora.tracker.data.local.entity.TransactionEntity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(entities = [TransactionEntity::class, CategoryEntity::class], version = 1, exportSchema = true)
abstract class VeloraDatabase : RoomDatabase() {
    abstract fun transactionDao(): TransactionDao
    abstract fun categoryDao(): CategoryDao
    
    companion object {
        @Volatile private var INSTANCE: VeloraDatabase? = null
        
        fun getDatabase(context: Context): VeloraDatabase {
            return INSTANCE ?: synchronized(this) {
                Room.databaseBuilder(context, VeloraDatabase::class.java, "velora_database")
                    .addCallback(PrepopulateCallback())
                    .build().also { INSTANCE = it }
            }
        }
    }

    private class PrepopulateCallback : RoomDatabase.Callback() {
        override fun onCreate(db: SupportSQLiteDatabase) {
            super.onCreate(db)
            INSTANCE?.let { database ->
                CoroutineScope(Dispatchers.IO).launch {
                    val categoryDao = database.categoryDao()
                    val categories = listOf(
                        CategoryEntity(0, "Food", "restaurant", "#E57373", "EXPENSE", true, 0),
                        CategoryEntity(0, "Groceries", "shopping_cart", "#FFB74D", "EXPENSE", true, 1),
                        CategoryEntity(0, "Shopping", "shopping_bag", "#F06292", "EXPENSE", true, 2),
                        CategoryEntity(0, "Transport", "directions_car", "#4DB6AC", "EXPENSE", true, 3),
                        CategoryEntity(0, "Fuel", "local_gas_station", "#AED581", "EXPENSE", true, 4),
                        CategoryEntity(0, "Bills", "receipt_long", "#7986CB", "EXPENSE", true, 5),
                        CategoryEntity(0, "Rent", "home", "#A1887F", "EXPENSE", true, 6),
                        CategoryEntity(0, "Entertainment", "movie", "#4DD0E1", "EXPENSE", true, 7),
                        CategoryEntity(0, "Health", "favorite", "#81C784", "EXPENSE", true, 8),
                        CategoryEntity(0, "Education", "school", "#9575CD", "EXPENSE", true, 9),
                        CategoryEntity(0, "Travel", "flight", "#FF8A65", "EXPENSE", true, 10),
                        CategoryEntity(0, "Subscriptions", "subscriptions", "#64B5F6", "EXPENSE", true, 11),
                        CategoryEntity(0, "Other", "more_horiz", "#90A4AE", "EXPENSE", true, 12),

                        CategoryEntity(0, "Salary", "account_balance", "#66BB6A", "INCOME", true, 0),
                        CategoryEntity(0, "Freelance", "computer", "#42A5F5", "INCOME", true, 1),
                        CategoryEntity(0, "Business", "business_center", "#FFC107", "INCOME", true, 2),
                        CategoryEntity(0, "Investment", "trending_up", "#AB47BC", "INCOME", true, 3),
                        CategoryEntity(0, "Gift", "redeem", "#EC407A", "INCOME", true, 4),
                        CategoryEntity(0, "Refund", "replay", "#26A69A", "INCOME", true, 5),
                        CategoryEntity(0, "Other", "more_horiz", "#BDBDBD", "INCOME", true, 6)
                    )
                    categories.forEach { categoryDao.insert(it) }
                }
            }
        }
    }
}
