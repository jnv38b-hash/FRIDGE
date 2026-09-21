package com.example.ui.navigation

sealed class Screen(val route: String) {
    object Home : Screen("home")
    object Explore : Screen("explore")
    object Saved : Screen("saved")
    object Profile : Screen("profile")
    object IngredientSelection : Screen("ingredient_selection")
    object RecipeResults : Screen("recipe_results")
    object RecipeDetail : Screen("recipe_detail/{recipeId}") {
        fun createRoute(recipeId: String) = "recipe_detail/$recipeId"
    }
    object CookingMode : Screen("cooking_mode/{recipeId}") {
        fun createRoute(recipeId: String) = "cooking_mode/$recipeId"
    }
}
