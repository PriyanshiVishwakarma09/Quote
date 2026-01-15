package com.example.quote.screens

import android.app.TimePickerDialog
import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.workDataOf
import com.example.quote.utils.DailyQuoteManager
import com.example.quote.viewModel.QuoteViewModel
import com.example.quote.viewModel.SettingsViewModel
import com.example.quote.worker.NotificationWorker
import java.util.Calendar

@Composable
fun SettingsScreen(
    settingsViewModel: SettingsViewModel = viewModel(),
    quoteViewModel: QuoteViewModel = viewModel() // Need this for the quote list
) {
    val isDark by settingsViewModel.isDarkMode.collectAsState()
    val notifTime by settingsViewModel.notificationTime.collectAsState()
    val context = LocalContext.current

    // --- DAILY QUOTE LOGIC ---
    // 1. Initialize Manager
    val dailyManager = remember { DailyQuoteManager(context) }

    // 2. Get the specific quote for TODAY
    // This ensures consistency with Widget and Notification
    val dailyQuote = remember(quoteViewModel.quotes) {
        dailyManager.getDailyQuote(quoteViewModel.quotes)
    }

    // --- THEME COLORS ---
    val deepBlue = Color(0xFF1A1A2E)
    val accentPink = Color(0xFFE94057)
    val bgGradient = Brush.verticalGradient(colors = listOf(deepBlue, Color.Black))

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(bgGradient)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
                .verticalScroll(rememberScrollState()) // Allow scrolling for small screens
        ) {
            // Header
            Text(
                text = "Settings",
                style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Bold),
                color = Color.White,
                modifier = Modifier.padding(vertical = 16.dp)
            )

            // --- 1. QUOTE OF THE DAY CARD ---
            if (dailyQuote != null) {
                Text(
                    text = "Today's Inspiration",
                    style = MaterialTheme.typography.titleMedium,
                    color = accentPink,
                    modifier = Modifier.padding(bottom = 8.dp)
                )

                Card(
                    shape = RoundedCornerShape(24.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White.copy(alpha = 0.1f)),
                    border = androidx.compose.foundation.BorderStroke(1.dp, Color.White.copy(alpha = 0.2f)),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(24.dp)) {
                        Text(
                            text = "“${dailyQuote.text}”",
                            style = MaterialTheme.typography.headlineSmall.copy(
                                fontFamily = FontFamily.Serif,
                                fontStyle = FontStyle.Italic
                            ),
                            color = Color.White
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                        Text(
                            text = "— ${dailyQuote.author}",
                            style = MaterialTheme.typography.labelLarge,
                            color = Color.White.copy(alpha = 0.7f),
                            modifier = Modifier.align(Alignment.End)
                        )
                    }
                }
                Spacer(modifier = Modifier.height(32.dp))
            }

            // --- 2. APPEARANCE SECTION ---
            SettingsSectionTitle("Appearance")

            GlassContainer {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text("Dark Mode", style = MaterialTheme.typography.bodyLarge, color = Color.White)
                        Text("Adjust app appearance", style = MaterialTheme.typography.bodySmall, color = Color.Gray)
                    }
                    Switch(
                        checked = isDark,
                        onCheckedChange = { settingsViewModel.toggleDarkMode(it) },
                        colors = SwitchDefaults.colors(
                            checkedThumbColor = accentPink,
                            checkedTrackColor = accentPink.copy(alpha = 0.5f)
                        )
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // --- 3. NOTIFICATION SECTION ---
            SettingsSectionTitle("Daily Reminder")

            GlassContainer {
                Column {
                    // Time Picker Row
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.Schedule, contentDescription = null, tint = Color.Gray)
                            Spacer(modifier = Modifier.width(12.dp))
                            Column {
                                Text("Notification Time", style = MaterialTheme.typography.bodyLarge, color = Color.White)
                                Text(
                                    text = String.format("%02d:%02d", notifTime.first, notifTime.second),
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = accentPink
                                )
                            }
                        }

                        Button(
                            onClick = {
                                TimePickerDialog(
                                    context,
                                    { _, hour, minute -> settingsViewModel.saveNotificationTime(hour, minute) },
                                    notifTime.first,
                                    notifTime.second,
                                    false
                                ).show()
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = Color.White.copy(alpha = 0.1f))
                        ) {
                            Text("Edit")
                        }
                    }

                    HorizontalDivider(
                        modifier = Modifier.padding(vertical = 16.dp),
                        color = Color.White.copy(alpha = 0.1f)
                    )

                    // Test Button
                    Button(
                        onClick = {
                            // PASS THE QUOTE TEXT TO THE WORKER!
                            val workData = workDataOf(
                                "quote_text" to (dailyQuote?.text ?: "Time for inspiration!"),
                                "quote_author" to (dailyQuote?.author ?: "")
                            )

                            val request = OneTimeWorkRequestBuilder<NotificationWorker>()
                                .setInputData(workData) // Sending data
                                .build()

                            WorkManager.getInstance(context).enqueue(request)
                        },
                        modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.buttonColors(containerColor = accentPink)
                    ) {
                        Icon(Icons.Default.Notifications, contentDescription = null, modifier = Modifier.size(18.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Test Notification Now")
                    }
                }
            }
        }
    }
}

// --- Helper Composable for Glass Cards ---
@Composable
fun GlassContainer(content: @Composable ColumnScope.() -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White.copy(alpha = 0.05f)),
        border = androidx.compose.foundation.BorderStroke(1.dp, Color.White.copy(alpha = 0.05f))
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            content = content
        )
    }
}

@Composable
fun SettingsSectionTitle(text: String) {
    Text(
        text = text,
        style = MaterialTheme.typography.titleMedium,
        color = Color.White.copy(alpha = 0.7f),
        modifier = Modifier.padding(bottom = 8.dp, start = 4.dp)
    )
}