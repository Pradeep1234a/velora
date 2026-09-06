package com.velora.tracker.domain.repository

import kotlinx.coroutines.flow.Flow

interface SettingsRepository {
    fun getOpeningBalance(): Flow<Long> // in minor units
    suspend fun setOpeningBalance(amountMinorUnits: Long)
    fun getCurrencySymbol(): Flow<String>
    suspend fun setCurrencySymbol(symbol: String)
    fun getThemeMode(): Flow<Int> // 0=system, 1=light, 2=dark
    suspend fun setThemeMode(mode: Int)
    fun isAiCategorizationEnabled(): Flow<Boolean>
    suspend fun setAiCategorizationEnabled(enabled: Boolean)
}
