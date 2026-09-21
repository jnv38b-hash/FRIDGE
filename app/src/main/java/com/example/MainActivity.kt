package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Kitchen
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.outlined.BookmarkBorder
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Kitchen
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.ui.navigation.Screen
import com.example.ui.screens.CookingModeScreen
import com.example.ui.screens.ExploreScreen
import com.example.ui.screens.HomeScreen
import com.example.ui.screens.IngredientSelectionScreen
import com.example.ui.screens.ProfileScreen
import com.example.ui.screens.RecipeDetailScreen
import com.example.ui.screens.RecipeResultsScreen
import com.example.ui.screens.SavedScreen
import com.example.ui.theme.MyApplicationTheme
import com.example.ui.theme.WarmOrange
import com.example.ui.viewmodel.FridgeChefViewModel

data class BottomNavItem(
    val title: String,
    val route: String,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector
)

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyApplicationTheme {
                FridgeChefApp()
            }
        }
    }
}

@Composable
fun FridgeChefApp(viewModel: FridgeChefViewModel = viewModel()) {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    val bottomNavItems = listOf(
        BottomNavItem("Home", Screen.Home.route, Icons.Filled.Home, Icons.Outlined.Home),
        BottomNavItem("Explore", Screen.Explore.route, Icons.Filled.Search, Icons.Outlined.Search),
        BottomNavItem("Saved", Screen.Saved.route, Icons.Filled.Bookmark, Icons.Outlined.BookmarkBorder),
        BottomNavItem("Kitchen", Screen.Profile.route, Icons.Filled.Kitchen, Icons.Outlined.Kitchen)
    )

    val isTopLevelDestination = bottomNavItems.any { it.route == currentRoute }

    Scaffold(
        bottomBar = {
            AnimatedVisibility(
                visible = isTopLevelDestination,
                enter = slideInVertically(initialOffsetY = { it }),
                exit = slideOutVertically(targetOffsetY = { it })
            ) {
                NavigationBar {
                    bottomNavItems.forEach { item ->
                        val isSelected = currentRoute == item.route
                        NavigationBarItem(
                            selected = isSelected,
                            onClick = {
                                navController.navigate(item.route) {
                                    popUpTo(navController.graph.findStartDestination().id) {
                                        saveState = true
                                    }
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            },
                            icon = {
                                Icon(
                                    imageVector = if (isSelected) item.selectedIcon else item.unselectedIcon,
                                    contentDescription = item.title
                                )
                            },
                            label = { Text(item.title) },
                            colors = NavigationBarItemDefaults.colors(
                                selectedIconColor = WarmOrange,
                                selectedTextColor = WarmOrange,
                                indicatorColor = WarmOrange.copy(alpha = 0.15f)
                            )
                        )
                    }
                }
            }
        },
        modifier = Modifier.fillMaxSize()
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Screen.Home.route,
            modifier = Modifier.padding(
                bottom = if (isTopLevelDestination) innerPadding.calculateBottomPadding() else androidx.compose.ui.unit.Dp(0f)
            )
        ) {
            composable(Screen.Home.route) {
                HomeScreen(
                    viewModel = viewModel,
                    onNavigateToIngredients = {
                        navController.navigate(Screen.IngredientSelection.route)
                    },
                    onNavigateToResults = {
                        navController.navigate(Screen.RecipeResults.route)
                    },
                    onNavigateToRecipeDetail = { recipeId ->
                        navController.navigate(Screen.RecipeDetail.createRoute(recipeId))
                    },
                    onNavigateToCookingMode = { recipeId ->
                        navController.navigate(Screen.CookingMode.createRoute(recipeId))
                    },
                    onNavigateToPantry = {
                        navController.navigate(Screen.Profile.route)
                    }
                )
            }

            composable(Screen.Explore.route) {
                ExploreScreen(
                    viewModel = viewModel,
                    onNavigateToRecipeDetail = { recipeId ->
                        navController.navigate(Screen.RecipeDetail.createRoute(recipeId))
                    }
                )
            }

            composable(Screen.Saved.route) {
                SavedScreen(
                    viewModel = viewModel,
                    onNavigateToRecipeDetail = { recipeId ->
                        navController.navigate(Screen.RecipeDetail.createRoute(recipeId))
                    },
                    onNavigateToCookingMode = { recipeId ->
                        navController.navigate(Screen.CookingMode.createRoute(recipeId))
                    }
                )
            }

            composable(Screen.Profile.route) {
                ProfileScreen(
                    viewModel = viewModel,
                    onNavigateToResults = {
                        navController.navigate(Screen.RecipeResults.route)
                    }
                )
            }

            composable(Screen.IngredientSelection.route) {
                IngredientSelectionScreen(
                    viewModel = viewModel,
                    onNavigateBack = { navController.popBackStack() },
                    onNavigateToResults = {
                        navController.navigate(Screen.RecipeResults.route)
                    }
                )
            }

            composable(Screen.RecipeResults.route) {
                RecipeResultsScreen(
                    viewModel = viewModel,
                    onNavigateBack = { navController.popBackStack() },
                    onNavigateToRecipeDetail = { recipeId ->
                        navController.navigate(Screen.RecipeDetail.createRoute(recipeId))
                    },
                    onNavigateToIngredients = {
                        navController.navigate(Screen.IngredientSelection.route)
                    }
                )
            }

            composable(
                route = Screen.RecipeDetail.route,
                arguments = listOf(navArgument("recipeId") { type = NavType.StringType })
            ) { backStackEntry ->
                val recipeId = backStackEntry.arguments?.getString("recipeId") ?: ""
                RecipeDetailScreen(
                    recipeId = recipeId,
                    viewModel = viewModel,
                    onNavigateBack = { navController.popBackStack() },
                    onNavigateToCookingMode = { id ->
                        navController.navigate(Screen.CookingMode.createRoute(id))
                    }
                )
            }

            composable(
                route = Screen.CookingMode.route,
                arguments = listOf(navArgument("recipeId") { type = NavType.StringType })
            ) { backStackEntry ->
                val recipeId = backStackEntry.arguments?.getString("recipeId") ?: ""
                CookingModeScreen(
                    recipeId = recipeId,
                    viewModel = viewModel,
                    onNavigateBack = { navController.popBackStack() },
                    onNavigateHome = {
                        navController.navigate(Screen.Home.route) {
                            popUpTo(Screen.Home.route) { inclusive = true }
                        }
                    },
                    onNavigateToShopping = {
                        navController.navigate(Screen.Saved.route)
                    }
                )
            }
        }
    }
}
