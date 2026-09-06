package com.velora.tracker.data.ai

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

val Context.aiPreferencesDataStore: DataStore<Preferences> by preferencesDataStore(name = "velora_ai_preferences")

class UserPreferenceLearningStore(
    private val dataStore: DataStore<Preferences>
) {
    companion object {
        val PREFERENCES_KEY = stringPreferencesKey("user_category_preferences")
    }

    suspend fun recordPreference(merchantTitle: String, categoryId: Long) {
        val normalized = merchantTitle.lowercase().trim()
        if (normalized.isEmpty()) return

        dataStore.edit { prefs ->
            val current = prefs[PREFERENCES_KEY] ?: ""
            val map = parsePreferences(current).toMutableMap()
            map[normalized] = categoryId
            prefs[PREFERENCES_KEY] = serializePreferences(map)
        }
    }

    suspend fun getPreference(merchantTitle: String): Long? {
        val normalized = merchantTitle.lowercase().trim()
        if (normalized.isEmpty()) return null

        val currentStr = dataStore.data.map { it[PREFERENCES_KEY] ?: "" }.first()
        val map = parsePreferences(currentStr)
        return map[normalized]
    }

    private fun parsePreferences(str: String): Map<String, Long> {
        if (str.isEmpty()) return emptyMap()
        val map = mutableMapOf<String, Long>()
        str.split("\n").forEach { line ->
            val parts = line.split("=")
            if (parts.size == 2) {
                try {
                    map[parts[0]] = parts[1].toLong()
                } catch (e: Exception) {
                    // Ignore malformed entries
                }
            }
        }
        return map
    }

    private fun serializePreferences(map: Map<String, Long>): String {
        return map.entries.joinToString("\n") { "${it.key}=${it.value}" }
    }
}
