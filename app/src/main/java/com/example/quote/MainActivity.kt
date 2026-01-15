package com.example.quote

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.quote.screens.AuthScreen
import com.example.quote.screens.QuoteScreen
import com.example.quote.ui.theme.QuoteTheme
import com.example.quote.viewModel.SessionViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            QuoteTheme {
                val sessionViewModel = remember { SessionViewModel() }
                var isLoggedIn by remember {
                    mutableStateOf(sessionViewModel.isUserLoggedIn())
                }
                if (isLoggedIn) {
                    QuoteScreen()
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
}
