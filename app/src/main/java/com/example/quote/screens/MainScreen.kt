package com.example.quote.screens

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.quote.viewModel.AuthViewModel
import com.example.quote.viewModel.QuoteViewModel
import com.example.quote.viewModel.SettingsViewModel

// Define your tabs
sealed class BottomTab(val route: String, val title: String, val icon: ImageVector) {
    object Home : BottomTab("home", "Home", Icons.Filled.Home)
    object Favorites : BottomTab("favorites", "Favorites", Icons.Filled.Favorite)
    object Settings : BottomTab("settings", "Settings", Icons.Filled.Settings)
    object Profile : BottomTab("profile", "Profile", Icons.Filled.Person)
}

@Composable
fun MainScreen(
    viewModel: QuoteViewModel,
    authViewModel: AuthViewModel,
    onLogoutSuccess: () -> Unit
) {
    val navController = rememberNavController()
    val tabs = listOf(BottomTab.Home, BottomTab.Favorites, BottomTab.Settings, BottomTab.Profile)

    // --- THEME COLORS ---
    val deepBlue = Color(0xFF1A1A2E)
    val accentPink = Color(0xFFE94057)

    Scaffold(
        // --- CUSTOM STYLED BOTTOM BAR ---
        bottomBar = {
            NavigationBar(
                containerColor = deepBlue, // Dark Background
                contentColor = Color.White
            ) {
                val navBackStackEntry by navController.currentBackStackEntryAsState()
                val currentRoute = navBackStackEntry?.destination?.route

                tabs.forEach { tab ->
                    NavigationBarItem(
                        icon = { Icon(tab.icon, contentDescription = tab.title) },
                        label = { Text(tab.title) },
                        selected = currentRoute == tab.route,
                        onClick = {
                            navController.navigate(tab.route) {
                                popUpTo(navController.graph.findStartDestination().id) { saveState = true }
                                launchSingleTop = true
                                restoreState = true
                            }
                        },
                        // --- COLOR CUSTOMIZATION ---
                        colors = NavigationBarItemDefaults.colors(
                            // Selected State
                            selectedIconColor = Color.White,
                            selectedTextColor = accentPink,
                            indicatorColor = accentPink.copy(alpha = 0.6f), // The "Glow" behind the icon

                            // Unselected State
                            unselectedIconColor = Color.Gray,
                            unselectedTextColor = Color.Gray
                        )
                    )
                }
            }
        },
        // Set the background of the scaffold container to match the app theme
        containerColor = deepBlue
    ) { innerPadding ->

        // --- CONTENT SWITCHER ---
        NavHost(
            navController = navController,
            startDestination = BottomTab.Home.route,
            modifier = Modifier.padding(innerPadding)
        ) {
            // Tab 1: Home
            composable(BottomTab.Home.route) {
                QuoteScreen(viewModel = viewModel)
            }

            // Tab 2: Favorites
            composable(BottomTab.Favorites.route) {
                FavoritesScreen(
                    viewModel = viewModel,
                    onNavigateBack = { navController.navigate(BottomTab.Home.route) }
                )
            }

            // Tab 3: Settings
            composable(BottomTab.Settings.route) {
                val context = LocalContext.current
                val settingsViewModel: SettingsViewModel = viewModel(
                    factory = ViewModelProvider.AndroidViewModelFactory.getInstance(context.applicationContext as android.app.Application)
                )

                SettingsScreen(
                    settingsViewModel = settingsViewModel,
                    quoteViewModel = viewModel
                )
            }

            // Tab 4: Profile
            composable(BottomTab.Profile.route) {
                ProfileScreen(
                    authViewModel = authViewModel,
                    onLogout = onLogoutSuccess
                )
            }
        }
    }
}