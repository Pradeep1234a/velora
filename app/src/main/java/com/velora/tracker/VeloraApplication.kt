package com.velora.tracker

import android.app.Application
import android.content.Context
import androidx.datastore.preferences.preferencesDataStore
import com.velora.tracker.data.ai.RuleBasedCategorizationService
import com.velora.tracker.data.ai.UserPreferenceLearningStore
import com.velora.tracker.data.local.VeloraDatabase
import com.velora.tracker.data.repository.CategoryRepositoryImpl
import com.velora.tracker.data.repository.SettingsRepositoryImpl
import com.velora.tracker.data.repository.TransactionRepositoryImpl
import com.velora.tracker.data.ai.CategorizationService
import com.velora.tracker.domain.usecase.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

val Context.settingsDataStore by preferencesDataStore(name = "velora_settings")
val Context.aiPreferencesDataStore by preferencesDataStore(name = "velora_ai_preferences")

class VeloraApplication : Application() {

    // Database
    val database by lazy { VeloraDatabase.getDatabase(this) }
    
    // Repositories
    val transactionRepository by lazy { TransactionRepositoryImpl(database.transactionDao(), database.categoryDao()) }
    val categoryRepository by lazy { CategoryRepositoryImpl(database.categoryDao(), database.transactionDao()) }
    val settingsRepository by lazy { SettingsRepositoryImpl(settingsDataStore) }
    
    // AI
    val userPreferenceLearningStore by lazy { UserPreferenceLearningStore(aiPreferencesDataStore) }
    val categorizationService: CategorizationService by lazy { RuleBasedCategorizationService(userPreferenceLearningStore) }
    
    // Use Cases
    val addTransactionUseCase by lazy { AddTransactionUseCase(transactionRepository) }
    val updateTransactionUseCase by lazy { UpdateTransactionUseCase(transactionRepository) }
    val deleteTransactionUseCase by lazy { DeleteTransactionUseCase(transactionRepository) }
    val getTransactionsUseCase by lazy { GetTransactionsUseCase(transactionRepository) }
    val getDashboardDataUseCase by lazy { GetDashboardDataUseCase(transactionRepository, categoryRepository, settingsRepository) }
    val getAnalyticsDataUseCase by lazy { GetAnalyticsDataUseCase(transactionRepository, categoryRepository) }
    val categorizeTransactionUseCase by lazy { CategorizeTransactionUseCase(categorizationService, settingsRepository, categoryRepository) }
    val manageCategoryUseCase by lazy { ManageCategoryUseCase(categoryRepository, transactionRepository) }

    override fun onCreate() {
        super.onCreate()
        CoroutineScope(Dispatchers.IO).launch {
            SampleDataSeeder.seedIfEmpty(transactionRepository, settingsRepository)
        }
    }
}
