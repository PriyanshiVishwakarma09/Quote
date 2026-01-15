package com.example.quote.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.quote.viewModel.QuoteViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FavoritesScreen(
    viewModel: QuoteViewModel,
    onNavigateBack: () -> Unit
) {
    // Logic: Filter the main list to find only the quotes that are in our favorites list
    // 'remember' ensures we don't re-calculate this on every small animation frame
    val favoriteQuotes = remember(viewModel.quotes, viewModel.favoriteIds) {
        viewModel.quotes.filter { quote ->
            viewModel.favoriteIds.contains(quote.id)
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("My Collection") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                },
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
                .background(Color(0xFFF5F5F5)), // Consistent background
            contentAlignment = Alignment.Center
        ) {
            if (favoriteQuotes.isEmpty()) {
                // --- Empty State UI ---
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center,
                    modifier = Modifier.padding(32.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.FavoriteBorder,
                        contentDescription = "No Favorites",
                        modifier = Modifier.size(64.dp),
                        tint = Color.Gray
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = "No favorites yet",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Gray
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Save quotes you love to see them here.",
                        textAlign = TextAlign.Center,
                        color = Color.Gray
                    )
                }
            } else {
                // --- List of Favorites ---
                LazyColumn(
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    modifier = Modifier.fillMaxSize()
                ) {
                    items(favoriteQuotes) { quote ->
                        QuoteCard(
                            quote = quote,
                            isFavorite = true, // It's always true in this screen!
                            onToggleFavorite = {
                                // This will remove it from the list immediately
                                viewModel.toggleFavorite(quote)
                            }
                        )
                    }
                }
            }
        }
    }
}