package com.example.quote.viewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.quote.data.UserPreferences
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class SettingsViewModel(application: Application) : AndroidViewModel(application) {
    private val prefs = UserPreferences(application)

    // State for UI to observe
    val isDarkMode = prefs.isDarkMode.stateIn(viewModelScope, SharingStarted.WhileSubscribed(), false)
    val notificationTime = prefs.notificationTime.stateIn(viewModelScope, SharingStarted.WhileSubscribed(), Pair(8, 0))

    fun toggleDarkMode(enabled: Boolean) {
        viewModelScope.launch { prefs.setDarkMode(enabled) }
    }

    fun saveNotificationTime(hour: Int, minute: Int) {
        viewModelScope.launch { prefs.setNotificationTime(hour, minute) }
    }
}