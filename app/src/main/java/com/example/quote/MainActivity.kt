package com.example.quote

import android.content.Context
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.*
import androidx.lifecycle.viewmodel.compose.viewModel // Make sure you have this import
import com.example.quote.screens.AuthScreen
import com.example.quote.screens.MainScreen // Import the new screen
import com.example.quote.ui.theme.QuoteTheme
import com.example.quote.viewModel.QuoteViewModel
import com.example.quote.viewModel.SessionViewModel
import java.util.Calendar
import java.util.concurrent.TimeUnit
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.example.quote.viewModel.AuthViewModel
import com.example.quote.viewModel.SettingsViewModel

class MainActivity : ComponentActivity() {
    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (isGranted) {
            // Permission granted, schedule the notification now
            scheduleNotification(this)
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            requestPermissionLauncher.launch(android.Manifest.permission.POST_NOTIFICATIONS)
        } else {
            scheduleNotification(this)
        }
        enableEdgeToEdge()
        setContent {
            QuoteTheme {
                val sessionViewModel = remember { SessionViewModel() }
                // 1. Create QuoteViewModel HERE to share it between Home and Favs
                val quoteViewModel: QuoteViewModel = viewModel()
                val authViewModel : AuthViewModel = viewModel()

                val settingsViewModel: SettingsViewModel = viewModel()
                val notifTime by settingsViewModel.notificationTime.collectAsState()

                // 2. Whenever the time changes in Settings, Reschedule the Notification!
                LaunchedEffect(notifTime) {
                    scheduleNotification(this@MainActivity, notifTime.first, notifTime.second)
                }
                var isLoggedIn by remember {
                    mutableStateOf(sessionViewModel.isUserLoggedIn())
                }

                if (isLoggedIn) {
                    // 2. CHANGE THIS: Call MainScreen instead of QuoteScreen
                    MainScreen(
                        viewModel = quoteViewModel,
                        authViewModel = authViewModel, // Pass AuthViewModel
                        onLogoutSuccess = {
                            isLoggedIn = false // This switches the UI back to AuthScreen!
                        }
                    )
                } else {
                    AuthScreen(
                        onAuthSuccess = {
                            isLoggedIn = true
                        }
                    )
                }
            }
        }
    }
    private fun scheduleNotification(context: Context) {
        val req = androidx.work.PeriodicWorkRequestBuilder<com.example.quote.worker.NotificationWorker>(
            24,
            java.util.concurrent.TimeUnit.HOURS
        ).build()

        androidx.work.WorkManager.getInstance(context).enqueueUniquePeriodicWork(
            "daily_work",
            androidx.work.ExistingPeriodicWorkPolicy.KEEP,
            req
        )
    }
}

private fun scheduleNotification(context: Context, hour: Int = 8, minute: Int = 0) {
    val now = Calendar.getInstance()
    val target = Calendar.getInstance().apply {
        set(Calendar.HOUR_OF_DAY, hour)
        set(Calendar.MINUTE, minute)
        set(Calendar.SECOND, 0)
    }

    // If the target time has already passed today, schedule it for tomorrow
    if (target.before(now)) {
        target.add(Calendar.DAY_OF_YEAR, 1)
    }

    // Calculate how long to wait from NOW until the target time
    val initialDelay = target.timeInMillis - now.timeInMillis

    val workRequest = PeriodicWorkRequestBuilder<com.example.quote.worker.NotificationWorker>(
        24, TimeUnit.HOURS
    )
        .setInitialDelay(initialDelay, TimeUnit.MILLISECONDS) // <--- CRITICAL: Waits until the correct time
        .build()

    WorkManager.getInstance(context).enqueueUniquePeriodicWork(
        "daily_work",
        ExistingPeriodicWorkPolicy.UPDATE, // <--- CRITICAL: Updates the time if you change it
        workRequest
    )
}