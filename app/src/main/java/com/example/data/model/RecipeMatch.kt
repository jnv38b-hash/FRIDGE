package com.example.data.model

data class RecipeMatch(
    val recipeWithIngredients: RecipeWithIngredients,
    val matchScore: Float,
    val matchPercentage: Int,
    val matchingIngredients: List<RecipeIngredientEntity>,
    val missingIngredients: List<RecipeIngredientEntity>,
    val substituteMatches: Map<String, String> = emptyMap(), // missingIngredientName -> substituteName used
    val usesExpiringIngredients: Boolean = false
) {
    val recipe: RecipeEntity get() = recipeWithIngredients.recipe
    val allIngredients: List<RecipeIngredientEntity> get() = recipeWithIngredients.ingredients
    val isPerfectMatch: Boolean get() = missingIngredients.isEmpty()
}

enum class RecipeSortOrder(val label: String) {
    BEST_MATCH("Best Match"),
    FASTEST("Fastest"),
    FEWEST_MISSING("Fewest Missing"),
    HIGH_PROTEIN("Highest Protein"),
    RANDOM("Random")
}
