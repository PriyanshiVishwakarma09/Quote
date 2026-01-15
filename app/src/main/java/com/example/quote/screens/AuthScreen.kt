package com.example.quote.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.quote.viewModel.AuthViewModel

@Composable
fun AuthScreen(
    onAuthSuccess: () -> Unit,
    viewModel: AuthViewModel = viewModel()
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var mode by remember { mutableStateOf(AuthMode.LOGIN) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Text(
            text = if (mode == AuthMode.LOGIN) "Login" else "Sign Up",
            style = MaterialTheme.typography.headlineMedium
        )

        Spacer(modifier = Modifier.height(24.dp))

        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Email") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(12.dp))

        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Password") },
            singleLine = true,
            visualTransformation = PasswordVisualTransformation(),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        if (viewModel.errorMessage != null) {
            Text(
                text = viewModel.errorMessage ?: "",
                color = MaterialTheme.colorScheme.error
            )
            Spacer(modifier = Modifier.height(8.dp))
        }

        Button(
            onClick = {
                if (mode == AuthMode.LOGIN) {
                    viewModel.login(email, password, onAuthSuccess)
                } else {
                    viewModel.signUp(email, password, onAuthSuccess)
                }
            },
            modifier = Modifier.fillMaxWidth(),
            enabled = !viewModel.isLoading
        ) {
            if (viewModel.isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier.size(20.dp),
                    strokeWidth = 2.dp
                )
            } else {
                Text(if (mode == AuthMode.LOGIN) "Login" else "Create Account")
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        TextButton(
            onClick = {
                mode =
                    if (mode == AuthMode.LOGIN) AuthMode.SIGN_UP else AuthMode.LOGIN
            }
        ) {
            Text(
                if (mode == AuthMode.LOGIN)
                    "Don't have an account? Sign up"
                else
                    "Already have an account? Login"
            )
        }
    }
}
