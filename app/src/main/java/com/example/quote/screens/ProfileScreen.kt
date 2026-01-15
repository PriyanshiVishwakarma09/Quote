package com.example.quote.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.quote.data.SupabaseClient
import com.example.quote.viewModel.AuthViewModel
import io.github.jan.supabase.auth.auth
import java.util.Locale

@Composable
fun ProfileScreen(
    authViewModel: AuthViewModel,
    onLogout: () -> Unit
) {
    // 1. Get User Data
    val currentUser = SupabaseClient.client.auth.currentUserOrNull()
    val userEmail = currentUser?.email ?: "Guest"

    // --- SMART NAME LOGIC ---
    // Try to get name from metadata, or extract it from email
    val displayName = remember(currentUser) {
        val metaName = currentUser?.userMetadata?.get("full_name")?.toString() ?: ""
        if (metaName.isNotEmpty()) {
            metaName.replace("\"", "") // Remove quotes if JSON adds them
        } else {
            // Fallback: "john.doe@email.com" -> "John Doe"
            userEmail.substringBefore("@")
                .split(".", "_")
                .joinToString(" ") { it.replaceFirstChar { char -> char.uppercase() } }
        }
    }

    val userInitial = displayName.firstOrNull()?.uppercaseChar()?.toString() ?: "?"

    // --- THEME COLORS ---
    val deepBlue = Color(0xFF1A1A2E)
    val accentPink = Color(0xFFE94057)
    val accentPurple = Color(0xFF8A2387)

    val bgGradient = Brush.verticalGradient(colors = listOf(deepBlue, Color.Black))
    val avatarGradient = Brush.linearGradient(colors = listOf(accentPink, accentPurple))

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(bgGradient),
        contentAlignment = Alignment.TopCenter
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            // --- HEADER ---
            Text(
                text = "My Profile",
                style = MaterialTheme.typography.headlineMedium.copy(
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 1.sp
                ),
                color = Color.White,
                modifier = Modifier.padding(top = 16.dp, bottom = 32.dp)
            )

            // --- MAIN PROFILE CARD ---
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(
                    containerColor = Color.White.copy(alpha = 0.08f)
                ),
                border = androidx.compose.foundation.BorderStroke(1.dp, Color.White.copy(alpha = 0.1f))
            ) {
                Column(
                    modifier = Modifier.padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    // Avatar
                    Box(
                        modifier = Modifier
                            .size(90.dp)
                            .clip(CircleShape)
                            .background(avatarGradient)
                            .border(2.dp, Color.White.copy(alpha = 0.3f), CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = userInitial,
                            fontSize = 36.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    }

                    Spacer(modifier = Modifier.height(26.dp))

                    // UPDATED: Name & Email Display
                    Text(
                        text = displayName,
                        style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold),
                        color = Color.White
                    )
                    Spacer(modifier = Modifier.height(14.dp))

                    Text(
                        text = userEmail,
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color.White.copy(alpha = 0.5f) // Dimmed email
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    // "Member" Badge
                    Surface(
                        color = accentPink.copy(alpha = 0.1f),
                        shape = RoundedCornerShape(8.dp),
                        border = androidx.compose.foundation.BorderStroke(1.dp, accentPink.copy(alpha = 0.3f))
                    ) {
                        Text(
                            text = "PRO MEMBER",
                            style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                            color = accentPink,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            // --- MENU OPTIONS ---
            Text(
                text = "General",
                style = MaterialTheme.typography.titleMedium,
                color = Color.Gray,
                modifier = Modifier.align(Alignment.Start)
            )

            Spacer(modifier = Modifier.height(16.dp))

            ProfileOptionItem(
                title = "Account Settings",
                icon = Icons.Default.Person,
                onClick = { /* Navigate */ }
            )

            Spacer(modifier = Modifier.height(12.dp))

            ProfileOptionItem(
                title = "App Preferences",
                icon = Icons.Default.Settings,
                onClick = { /* Navigate */ }
            )

            Spacer(modifier = Modifier.weight(1f))

            // --- LOGOUT BUTTON ---
            Button(
                onClick = { authViewModel.logout { onLogout() } },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF2D2D44),
                    contentColor = Color(0xFFFF5252)
                )
            ) {
                Icon(Icons.AutoMirrored.Filled.ExitToApp, contentDescription = null)
                Spacer(modifier = Modifier.width(12.dp))
                Text("Log Out", fontSize = 16.sp, fontWeight = FontWeight.Bold)
            }
        }
    }
}

// Helper (Keep this same as before)
@Composable
fun ProfileOptionItem(title: String, icon: ImageVector, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(60.dp)
            .clickable { onClick() },
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White.copy(alpha = 0.05f))
    ) {
        Row(
            modifier = Modifier.fillMaxSize().padding(horizontal = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(imageVector = icon, contentDescription = null, tint = Color.White.copy(alpha = 0.7f))
            Spacer(modifier = Modifier.width(16.dp))
            Text(
                text = title,
                style = MaterialTheme.typography.bodyLarge,
                color = Color.White.copy(alpha = 0.9f),
                modifier = Modifier.weight(1f)
            )
            Icon(imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight, contentDescription = null, tint = Color.Gray)
        }
    }
}