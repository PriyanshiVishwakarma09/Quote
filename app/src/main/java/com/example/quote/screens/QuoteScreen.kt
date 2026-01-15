package com.example.quote.screens

import android.content.Context
import android.content.Intent
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
import com.example.quote.model.Quote
import com.example.quote.viewModel.QuoteViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun QuoteScreen(viewModel: QuoteViewModel = viewModel()) {

    val context = LocalContext.current
    LaunchedEffect(Unit) {
        viewModel.fetchQuotes()
    }

    // --- THEME COLORS ---
    val deepBlue = Color(0xFF1A1A2E)
    val accentPink = Color(0xFFE94057)

    // Background Gradient (Matches Auth Screen)
    val bgGradient = Brush.verticalGradient(
        colors = listOf(deepBlue, Color.Black)
    )

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        "Daily Inspiration",
                        style = MaterialTheme.typography.titleLarge.copy(
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 1.sp
                        )
                    )
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = Color.Transparent, // Transparent to show gradient
                    titleContentColor = Color.White
                )
            )
        },
        containerColor = Color.Transparent // Important so Box gradient shows
    ) { paddingValues ->

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(bgGradient) // Apply Dark Gradient
                .padding(paddingValues),
            contentAlignment = Alignment.Center
        ) {
            if (viewModel.loading) {
                CircularProgressIndicator(color = accentPink)
            } else {
                LazyColumn(
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp) // Spacing between cards
                ) {
                    items(viewModel.quotes) { quote ->
                        val isFav = viewModel.favoriteIds.contains(quote.id)

                        QuoteCard(
                            quote = quote,
                            isFavorite = isFav,
                            onToggleFavorite = { viewModel.toggleFavorite(quote) },
                            onShare = { shareQuote(context, quote) }
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
    onShare: () -> Unit,
    onToggleFavorite: () -> Unit,
    modifier: Modifier = Modifier
) {
    // Glassmorphism Card Style
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White.copy(alpha = 0.08f) // Very transparent white
        ),
        border = BorderStroke(1.dp, Color.White.copy(alpha = 0.1f)), // Subtle border
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Column(
            modifier = Modifier.padding(24.dp)
        ) {
            // --- Quote Text ---
            Text(
                text = "“${quote.text}”",
                style = MaterialTheme.typography.headlineSmall.copy(
                    fontFamily = FontFamily.Serif,
                    fontStyle = FontStyle.Italic,
                    lineHeight = 32.sp
                ),
                color = Color.White.copy(alpha = 0.95f) // Bright White
            )

            Spacer(modifier = Modifier.height(24.dp))

            // --- Bottom Row: Author + Actions ---
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                // Author Name
                Text(
                    text = "— ${quote.author}",
                    style = MaterialTheme.typography.bodyLarge.copy(
                        fontWeight = FontWeight.Medium
                    ),
                    color = Color.White.copy(alpha = 0.7f) // Softer White
                )

                // Actions Row
                Row {
                    // Share Button
                    IconButton(onClick = onShare) {
                        Icon(
                            imageVector = Icons.Default.Share,
                            contentDescription = "Share",
                            tint = Color.White.copy(alpha = 0.7f),
                            modifier = Modifier.size(22.dp)
                        )
                    }

                    // Heart Button
                    IconButton(onClick = onToggleFavorite) {
                        Icon(
                            imageVector = if (isFavorite) Icons.Filled.Favorite else Icons.Outlined.FavoriteBorder,
                            contentDescription = "Favorite",
                            // Pink if favorite, White-ish if not
                            tint = if (isFavorite) Color(0xFFE94057) else Color.White.copy(alpha = 0.7f),
                            modifier = Modifier.size(26.dp)
                        )
                    }
                }
            }
        }
    }
}

fun shareQuote(context: Context, quote: Quote) {
    val sendIntent = Intent().apply {
        action = Intent.ACTION_SEND
        putExtra(Intent.EXTRA_TEXT, "“${quote.text}”\n\n- ${quote.author}\n\nShared via QuoteVault")
        type = "text/plain"
    }
    val shareIntent = Intent.createChooser(sendIntent, "Share this quote via...")
    context.startActivity(shareIntent)
}