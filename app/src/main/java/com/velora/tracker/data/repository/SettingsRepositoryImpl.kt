package com.velora.tracker.data.repository

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.velora.tracker.domain.repository.SettingsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

val Context.settingsDataStore: DataStore<Preferences> by preferencesDataStore(name = "velora_settings")

class SettingsRepositoryImpl(
    private val dataStore: DataStore<Preferences>
) : SettingsRepository {

    companion object {
        val OPENING_BALANCE = longPreferencesKey("opening_balance")
        val CURRENCY_SYMBOL = stringPreferencesKey("currency_symbol")
        val THEME_MODE = intPreferencesKey("theme_mode")
        val AI_ENABLED = booleanPreferencesKey("ai_enabled")
    }

    override fun getOpeningBalance(): Flow<Long> {
        return dataStore.data.map { preferences ->
            preferences[OPENING_BALANCE] ?: 0L
        }
    }

    override suspend fun setOpeningBalance(amountMinorUnits: Long) {
        dataStore.edit { preferences ->
            preferences[OPENING_BALANCE] = amountMinorUnits
        }
    }

    override fun getCurrencySymbol(): Flow<String> {
        return dataStore.data.map { preferences ->
            preferences[CURRENCY_SYMBOL] ?: "₹"
        }
    }

    override suspend fun setCurrencySymbol(symbol: String) {
        dataStore.edit { preferences ->
            preferences[CURRENCY_SYMBOL] = symbol
        }
    }

    override fun getThemeMode(): Flow<Int> {
        return dataStore.data.map { preferences ->
            preferences[THEME_MODE] ?: 0
        }
    }

    override suspend fun setThemeMode(mode: Int) {
        dataStore.edit { preferences ->
            preferences[THEME_MODE] = mode
        }
    }

    override fun isAiCategorizationEnabled(): Flow<Boolean> {
        return dataStore.data.map { preferences ->
            preferences[AI_ENABLED] ?: true
        }
    }

    override suspend fun setAiCategorizationEnabled(enabled: Boolean) {
        dataStore.edit { preferences ->
            preferences[AI_ENABLED] = enabled
        }
    }
}
