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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
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
    val context = LocalContext.current

    // --- THEME COLORS (Consistent with Home) ---
    val deepBlue = Color(0xFF1A1A2E)
    val accentPink = Color(0xFFE94057)

    // Background Gradient
    val bgGradient = Brush.verticalGradient(
        colors = listOf(deepBlue, Color.Black)
    )

    // Filter Logic
    val favoriteQuotes = remember(viewModel.quotes, viewModel.favoriteIds) {
        viewModel.quotes.filter { quote ->
            viewModel.favoriteIds.contains(quote.id)
        }
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        "My Collection",
                        style = MaterialTheme.typography.titleLarge.copy(
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 1.sp
                        )
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = Color.White
                        )
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = Color.Transparent, // Transparent
                    titleContentColor = Color.White
                )
            )
        },
        containerColor = Color.Transparent // Important for gradient visibility
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(bgGradient) // Apply Dark Gradient
                .padding(paddingValues),
            contentAlignment = Alignment.Center
        ) {
            if (favoriteQuotes.isEmpty()) {
                // --- Dark Theme Empty State ---
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center,
                    modifier = Modifier.padding(32.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.FavoriteBorder,
                        contentDescription = "No Favorites",
                        modifier = Modifier.size(80.dp),
                        tint = Color.White.copy(alpha = 0.2f) // Subtle ghostly icon
                    )
                    Spacer(modifier = Modifier.height(24.dp))
                    Text(
                        text = "No favorites yet",
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Save quotes you love to build your personal collection.",
                        textAlign = TextAlign.Center,
                        color = Color.White.copy(alpha = 0.6f),
                        style = MaterialTheme.typography.bodyLarge
                    )
                }
            } else {
                // --- List of Favorites ---
                LazyColumn(
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                    modifier = Modifier.fillMaxSize()
                ) {
                    items(favoriteQuotes) { quote ->
                        // Re-using the beautiful QuoteCard from your Home Screen
                        QuoteCard(
                            quote = quote,
                            isFavorite = true, // Always filled in this screen
                            onToggleFavorite = {
                                viewModel.toggleFavorite(quote)
                            },
                            onShare = {
                                shareQuote(context, quote)
                            }
                        )
                    }
                }
            }
        }
    }
}