package com.example.quote.data

import android.content.Context
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

// Create the DataStore (File name: settings)
val Context.dataStore by preferencesDataStore(name = "settings")

class UserPreferences(private val context: Context) {

    companion object {
        val IS_DARK_MODE = booleanPreferencesKey("is_dark_mode")
        val NOTIF_HOUR = intPreferencesKey("notif_hour")
        val NOTIF_MINUTE = intPreferencesKey("notif_minute")
    }

    // --- READ SETTINGS ---
    val isDarkMode: Flow<Boolean> = context.dataStore.data.map { it[IS_DARK_MODE] ?: false }
    // Default time: 8:00 AM
    val notificationTime: Flow<Pair<Int, Int>> = context.dataStore.data.map {
        Pair(it[NOTIF_HOUR] ?: 8, it[NOTIF_MINUTE] ?: 0)
    }

    // --- WRITE SETTINGS ---
    suspend fun setDarkMode(enabled: Boolean) {
        context.dataStore.edit { it[IS_DARK_MODE] = enabled }
    }

    suspend fun setNotificationTime(hour: Int, minute: Int) {
        context.dataStore.edit {
            it[NOTIF_HOUR] = hour
            it[NOTIF_MINUTE] = minute
        }
    }
}