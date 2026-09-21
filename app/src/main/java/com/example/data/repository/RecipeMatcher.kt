package com.example.data.repository

import com.example.data.model.RecipeIngredientEntity
import com.example.data.model.RecipeMatch
import com.example.data.model.RecipeSortOrder
import com.example.data.model.RecipeWithIngredients
import com.example.data.model.UserProfilePreferences

object RecipeMatcher {

    fun matchRecipes(
        allRecipes: List<RecipeWithIngredients>,
        selectedIngredients: List<String>,
        expiringIngredientNames: Set<String> = emptySet(),
        userPreferences: UserProfilePreferences? = null,
        sortOrder: RecipeSortOrder = RecipeSortOrder.BEST_MATCH,
        maxCookTimeMinutes: Int? = null,
        cuisineFilter: String? = null,
        mealTypeFilter: String? = null,
        quickModeFilter: String? = null
    ): List<RecipeMatch> {
        val selectedLower = selectedIngredients.map { it.trim().lowercase() }.filter { it.isNotBlank() }

        val matches = allRecipes.mapNotNull { recipeWithIngredients ->
            val recipe = recipeWithIngredients.recipe
            val ingredients = recipeWithIngredients.ingredients

            // 1. Strict Allergen Exclusion
            val excludedAllergens = userPreferences?.excludedAllergens ?: emptySet()
            val containsAllergen = excludedAllergens.any { allergen ->
                val allergenLower = allergen.lowercase()
                recipe.allergenTags.lowercase().contains(allergenLower) ||
                        ingredients.any { it.ingredientName.lowercase().contains(allergenLower) }
            }
            if (containsAllergen) return@mapNotNull null

            // 2. Dietary Restriction Filter
            val diet = userPreferences?.selectedDiet
            if (!diet.isNullOrBlank()) {
                val dietLower = diet.lowercase()
                val recipeTags = recipe.dietaryTags.lowercase()
                // If user specifies vegetarian, ensure non-vegetarian recipes aren't shown
                if (dietLower == "vegetarian" && !recipeTags.contains("vegetarian") && !recipeTags.contains("vegan")) {
                    return@mapNotNull null
                }
                if (dietLower == "vegan" && !recipeTags.contains("vegan")) {
                    return@mapNotNull null
                }
                if (dietLower == "gluten-free" && !recipeTags.contains("gluten-free")) {
                    return@mapNotNull null
                }
                if (dietLower == "dairy-free" && !recipeTags.contains("dairy-free")) {
                    return@mapNotNull null
                }
            }

            // 3. Cuisine Filter
            if (!cuisineFilter.isNullOrBlank() && cuisineFilter != "All") {
                if (!recipe.cuisine.equals(cuisineFilter, ignoreCase = true)) {
                    return@mapNotNull null
                }
            }

            // 4. Meal Type Filter
            if (!mealTypeFilter.isNullOrBlank() && mealTypeFilter != "All") {
                if (!recipe.mealType.equals(mealTypeFilter, ignoreCase = true)) {
                    return@mapNotNull null
                }
            }

            // 5. Max Cook Time Filter
            if (maxCookTimeMinutes != null && recipe.totalTimeMinutes > maxCookTimeMinutes) {
                return@mapNotNull null
            }

            // 6. Quick Modes Filter
            if (!quickModeFilter.isNullOrBlank()) {
                when (quickModeFilter) {
                    "Under 15 Minutes" -> if (recipe.totalTimeMinutes > 15) return@mapNotNull null
                    "High Protein" -> if (recipe.proteinGrams < 15) return@mapNotNull null
                    "Healthy" -> if (!recipe.dietaryTags.contains("Healthy", ignoreCase = true) && recipe.calories > 450) return@mapNotNull null
                    "Spicy" -> if (!recipe.dietaryTags.contains("Spicy", ignoreCase = true) && !recipe.name.contains("Masala", ignoreCase = true)) return@mapNotNull null
                    "Budget Meal" -> if (!recipe.dietaryTags.contains("Budget", ignoreCase = true) && ingredients.size > 7) return@mapNotNull null
                }
            }

            // 7. Calculate Ingredient Match
            val matchingIngredients = mutableListOf<RecipeIngredientEntity>()
            val missingIngredients = mutableListOf<RecipeIngredientEntity>()
            val substituteMatches = mutableMapOf<String, String>()
            var usesExpiring = false

            val requiredIngredients = ingredients.filter { it.isRequired }
            val totalRequiredCount = if (requiredIngredients.isNotEmpty()) requiredIngredients.size else ingredients.size

            for (ingredient in ingredients) {
                val ingNameLower = ingredient.ingredientName.lowercase()
                // Direct or alias match
                val matchedSelected = selectedLower.firstOrNull { sel ->
                    sel.contains(ingNameLower) || ingNameLower.contains(sel)
                }

                if (matchedSelected != null) {
                    matchingIngredients.add(ingredient)
                    if (expiringIngredientNames.any { exp -> exp.contains(ingNameLower, ignoreCase = true) || ingNameLower.contains(exp, ignoreCase = true) }) {
                        usesExpiring = true
                    }
                } else {
                    // Check substitutions
                    val substitute = selectedLower.firstOrNull { sel ->
                        SubstitutionDatabase.isAcceptableSubstitute(ingredient.ingredientName, sel)
                    }
                    if (substitute != null) {
                        matchingIngredients.add(ingredient)
                        substituteMatches[ingredient.ingredientName] = substitute
                    } else {
                        missingIngredients.add(ingredient)
                    }
                }
            }

            // If no ingredients were selected, show all as 0% or neutral
            val rawScore = if (selectedLower.isEmpty()) {
                0.5f
            } else {
                val matchedRequiredCount = matchingIngredients.count { it.isRequired }
                (matchedRequiredCount.toFloat() / totalRequiredCount.coerceAtLeast(1).toFloat())
            }

            // Expiring bonus
            val bonus = if (usesExpiring) 0.05f else 0.0f
            val finalScore = (rawScore + bonus).coerceIn(0f, 1f)
            val matchPercentage = (finalScore * 100).toInt()

            RecipeMatch(
                recipeWithIngredients = recipeWithIngredients,
                matchScore = finalScore,
                matchPercentage = matchPercentage,
                matchingIngredients = matchingIngredients,
                missingIngredients = missingIngredients,
                substituteMatches = substituteMatches,
                usesExpiringIngredients = usesExpiring
            )
        }

        // Apply Sorting
        return when (sortOrder) {
            RecipeSortOrder.BEST_MATCH -> matches.sortedWith(
                compareByDescending<RecipeMatch> { it.matchScore }
                    .thenBy { it.missingIngredients.size }
                    .thenBy { it.recipe.totalTimeMinutes }
            )
            RecipeSortOrder.FASTEST -> matches.sortedBy { it.recipe.totalTimeMinutes }
            RecipeSortOrder.FEWEST_MISSING -> matches.sortedWith(
                compareBy<RecipeMatch> { it.missingIngredients.size }
                    .thenByDescending { it.matchScore }
            )
            RecipeSortOrder.HIGH_PROTEIN -> matches.sortedByDescending { it.recipe.proteinGrams }
            RecipeSortOrder.RANDOM -> matches.shuffled()
        }
    }
}
