package com.example.quote.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.quote.viewModel.AuthViewModel


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AuthScreen(
    onAuthSuccess: () -> Unit,
    viewModel: AuthViewModel = viewModel()
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var mode by remember { mutableStateOf(AuthMode.LOGIN) }
    var isPasswordVisible by remember { mutableStateOf(false) }

    // --- THEME COLORS (Extracted from your Reference Image) ---
    val deepBlue = Color(0xFF1A1A2E)
    val darkPurple = Color(0xFF16213E)
    val accentPink = Color(0xFFE94057)
    val accentPurple = Color(0xFF8A2387)

    // Gradient for Background
    val bgGradient = Brush.verticalGradient(
        colors = listOf(deepBlue, Color.Black)
    )

    // Gradient for Buttons
    val buttonGradient = Brush.horizontalGradient(
        colors = listOf(accentPink, accentPurple)
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(bgGradient), // 1. Full Screen Gradient
        contentAlignment = Alignment.Center
    ) {
        // --- CONTENT CARD ---
        Card(
            modifier = Modifier
                .fillMaxWidth(0.9f) // Take 90% of width
                .wrapContentHeight(),
            colors = CardDefaults.cardColors(
                containerColor = Color.White.copy(alpha = 0.05f) // Glassy look
            ),
            shape = RoundedCornerShape(24.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
        ) {
            Column(
                modifier = Modifier.padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Title
                Text(
                    text = if (mode == AuthMode.LOGIN) "Welcome Back" else "Join QuoteVault",
                    style = MaterialTheme.typography.headlineMedium.copy(
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 1.sp
                    ),
                    color = Color.White
                )

                Text(
                    text = if (mode == AuthMode.LOGIN) "Login to access your collection" else "Sign up to start saving wisdom",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.Gray
                )

                Spacer(modifier = Modifier.height(32.dp))

                // --- EMAIL INPUT ---
                OutlinedTextField(
                    value = email,
                    onValueChange = { email = it },
                    label = { Text("Email") },
                    leadingIcon = { Icon(Icons.Default.Email, contentDescription = null, tint = Color.LightGray) },
                    singleLine = true,
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = accentPink,
                        unfocusedBorderColor = Color.White.copy(alpha = 0.3f),
                        focusedLabelColor = accentPink,
                        unfocusedLabelColor = Color.Gray,
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Color.White,
                        cursorColor = accentPink
                    ),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email, imeAction = ImeAction.Next),
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(16.dp))

                // --- PASSWORD INPUT ---
                OutlinedTextField(
                    value = password,
                    onValueChange = { password = it },
                    label = { Text("Password") },
                    leadingIcon = { Icon(Icons.Default.Lock, contentDescription = null, tint = Color.LightGray) },
                    trailingIcon = {
                        IconButton(onClick = { isPasswordVisible = !isPasswordVisible }) {
                            Icon(
                                imageVector = if (isPasswordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                                contentDescription = "Toggle Password",
                                tint = Color.Gray
                            )
                        }
                    },
                    singleLine = true,
                    visualTransformation = if (isPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = accentPink,
                        unfocusedBorderColor = Color.White.copy(alpha = 0.3f),
                        focusedLabelColor = accentPink,
                        unfocusedLabelColor = Color.Gray,
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Color.White,
                        cursorColor = accentPink
                    ),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password, imeAction = ImeAction.Done),
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(24.dp))

                // Error Message
                AnimatedVisibility(visible = viewModel.errorMessage != null) {
                    Text(
                        text = viewModel.errorMessage ?: "",
                        color = Color(0xFFFF5252),
                        style = MaterialTheme.typography.bodySmall,
                        modifier = Modifier.padding(bottom = 16.dp)
                    )
                }

                // --- GRADIENT BUTTON ---
                Button(
                    onClick = {
                        if (mode == AuthMode.LOGIN) {
                            viewModel.login(email, password, onAuthSuccess)
                        } else {
                            viewModel.signUp(email, password, onAuthSuccess)
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp)
                        .background(buttonGradient, shape = RoundedCornerShape(25.dp)), // Apply gradient here
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent), // Make native button transparent
                    contentPadding = PaddingValues() // Remove default padding
                ) {
                    if (viewModel.isLoading) {
                        CircularProgressIndicator(color = Color.White, modifier = Modifier.size(24.dp))
                    } else {
                        Text(
                            text = if (mode == AuthMode.LOGIN) "LOGIN" else "CREATE ACCOUNT",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                // --- TOGGLE MODE ---
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = if (mode == AuthMode.LOGIN) "New here? " else "Have an account? ",
                        color = Color.Gray
                    )
                    TextButton(onClick = {
                        mode = if (mode == AuthMode.LOGIN) AuthMode.SIGN_UP else AuthMode.LOGIN
                    }) {
                        Text(
                            text = if (mode == AuthMode.LOGIN) "Sign Up" else "Log In",
                            color = accentPink,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }
    }
}