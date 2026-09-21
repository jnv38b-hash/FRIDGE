package com.example.data.repository

import com.example.data.model.RecipeEntity
import com.example.data.model.RecipeIngredientEntity
import com.example.data.model.RecipeWithIngredients
import com.example.data.model.UserProfilePreferences

interface RecipeGenerator {
    suspend fun generateRecipe(
        availableIngredients: List<String>,
        userPreferences: UserProfilePreferences?,
        cuisineHint: String? = null
    ): Result<RecipeWithIngredients>
}

object RecipeValidator {
    data class ValidationResult(
        val isValid: Boolean,
        val validationErrors: List<String> = emptyList()
    )

    fun validate(
        recipeWithIngredients: RecipeWithIngredients,
        availableIngredients: List<String>,
        userPreferences: UserProfilePreferences? = null
    ): ValidationResult {
        val errors = mutableListOf<String>()
        val recipe = recipeWithIngredients.recipe
        val ingredients = recipeWithIngredients.ingredients

        if (recipe.name.isBlank()) {
            errors.add("Recipe name cannot be empty")
        }
        if (ingredients.isEmpty()) {
            errors.add("Recipe must contain at least one ingredient")
        }
        if (recipe.getStepsList().isEmpty()) {
            errors.add("Recipe must provide cooking instructions")
        }
        if (recipe.cookTimeMinutes <= 0 && recipe.prepTimeMinutes <= 0) {
            errors.add("Invalid cooking/preparation time")
        }

        // Allergy check
        userPreferences?.excludedAllergens?.forEach { allergen ->
            val allergenLower = allergen.lowercase()
            if (recipe.allergenTags.lowercase().contains(allergenLower) ||
                ingredients.any { it.ingredientName.lowercase().contains(allergenLower) }
            ) {
                errors.add("Recipe contains excluded allergen: $allergen")
            }
        }

        // Check unavailable ingredients for AI generated recipes
        if (recipe.isAiGenerated) {
            val availableLower = availableIngredients.map { it.lowercase() }
            val missing = ingredients.filter { ing ->
                !availableLower.any { avail ->
                    avail.contains(ing.ingredientName.lowercase()) ||
                            ing.ingredientName.lowercase().contains(avail) ||
                            SubstitutionDatabase.isAcceptableSubstitute(ing.ingredientName, avail)
                }
            }
            if (missing.size > 2) {
                errors.add("Generated recipe requires too many ingredients not in your selection: ${missing.map { it.ingredientName }}")
            }
        }

        return ValidationResult(
            isValid = errors.isEmpty(),
            validationErrors = errors
        )
    }
}
