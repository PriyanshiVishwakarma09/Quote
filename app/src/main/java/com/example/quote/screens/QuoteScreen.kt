package com.example.quote.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.quote.viewModel.QuoteViewModel
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.quote.model.Quote

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun QuoteScreen(viewModel: QuoteViewModel = viewModel()) {

    // Load data when screen opens
    LaunchedEffect(Unit) {
        viewModel.fetchQuotes()
        // viewModel.loadFavorites() // Uncomment when you add this function
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Daily Inspiration") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer
                )
            )
        }
    ) { paddingValues ->

        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(Color(0xFFF5F5F5)), // Light Grey Background
            contentAlignment = Alignment.Center
        ) {
            if (viewModel.loading) {
                CircularProgressIndicator()
            } else {
                LazyColumn(
                    contentPadding = PaddingValues(bottom = 16.dp)
                ) {
                    items(viewModel.quotes) { quote ->
                        // Check if this quote is in our favorites list
                        val isFav = viewModel.favoriteIds.contains(quote.id) // Or quote.text

                        QuoteCard(
                            quote = quote,
                            isFavorite = isFav,
                            onToggleFavorite = {
                                viewModel.toggleFavorite(quote)
                            }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun QuoteCard(
    quote: Quote,
    isFavorite: Boolean,
    onToggleFavorite: () -> Unit, // Callback when heart is clicked
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp, horizontal = 16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
        shape = MaterialTheme.shapes.medium
    ) {
        Column(
            modifier = Modifier
                // Gradient Background for a "Premium" feel
                .background(
                    Brush.linearGradient(
                        colors = listOf(
                            Color(0xFFE0C3FC), // Light Purple
                            Color(0xFF8EC5FC)  // Light Blue
                        )
                    )
                )
                .padding(24.dp)
        ) {
            // --- Top Row: Heart Icon ---
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {
                IconButton(onClick = onToggleFavorite) {
                    Icon(
                        imageVector = if (isFavorite) Icons.Filled.Favorite else Icons.Outlined.FavoriteBorder,
                        contentDescription = "Favorite",
                        tint = if (isFavorite) Color.Red else Color.Gray,
                        modifier = Modifier.size(28.dp)
                    )
                }
            }

            // --- The Quote Text ---
            Text(
                text = "“${quote.text}”",
                style = MaterialTheme.typography.headlineSmall.copy(
                    fontFamily = FontFamily.Serif, // Looks like a book
                    fontStyle = FontStyle.Italic,
                    lineHeight = 32.sp
                ),
                color = Color.Black.copy(alpha = 0.8f)
            )

            Spacer(modifier = Modifier.height(16.dp))

            // --- The Author ---
            Text(
                text = "— ${quote.author}",
                style = MaterialTheme.typography.titleMedium.copy(
                    fontFamily = FontFamily.SansSerif,
                    fontWeight = androidx.compose.ui.text.font.FontWeight.Bold
                ),
                color = Color.DarkGray,
                modifier = Modifier.align(Alignment.End)
            )
        }
    }
}
