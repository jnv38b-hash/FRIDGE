package com.example.data.model

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import androidx.room.Relation

enum class RecipeDifficulty(val label: String) {
    EASY("Easy"),
    MEDIUM("Medium"),
    HARD("Hard")
}

@Entity(tableName = "recipes")
data class RecipeEntity(
    @PrimaryKey val id: String,
    val name: String,
    val description: String,
    val cuisine: String,
    val mealType: String,
    val prepTimeMinutes: Int,
    val cookTimeMinutes: Int,
    val difficulty: String,
    val servings: Int,
    val calories: Int,
    val proteinGrams: Int,
    val carbsGrams: Int,
    val fatGrams: Int,
    val instructions: String, // Pipe-separated steps: "Step 1|Step 2|..."
    val stepTimersMinutes: String = "", // Comma-separated timers: "0,5,10,0"
    val imageUrl: String,
    val requiredEquipment: String = "Pan,Stove",
    val dietaryTags: String = "", // e.g. "Vegetarian,High-protein"
    val allergenTags: String = "", // e.g. "Dairy,Eggs"
    val isAiGenerated: Boolean = false
) {
    val totalTimeMinutes: Int get() = prepTimeMinutes + cookTimeMinutes

    fun getStepsList(): List<String> = instructions.split("|").map { it.trim() }.filter { it.isNotEmpty() }

    fun getTimersList(): List<Int> {
        if (stepTimersMinutes.isBlank()) return emptyList()
        return stepTimersMinutes.split(",").mapNotNull { it.trim().toIntOrNull() }
    }

    fun getDietaryTagsList(): List<String> =
        if (dietaryTags.isBlank()) emptyList() else dietaryTags.split(",").map { it.trim() }

    fun getAllergenTagsList(): List<String> =
        if (allergenTags.isBlank()) emptyList() else allergenTags.split(",").map { it.trim() }
}

@Entity(
    tableName = "recipe_ingredients",
    indices = [Index(value = ["recipeId"])]
)
data class RecipeIngredientEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val recipeId: String,
    val ingredientId: String,
    val ingredientName: String,
    val quantity: String,
    val unit: String,
    val isRequired: Boolean = true,
    val substitutions: String = "" // Comma-separated
) {
    fun getSubstitutionsList(): List<String> =
        if (substitutions.isBlank()) emptyList() else substitutions.split(",").map { it.trim() }
}

data class RecipeWithIngredients(
    @Embedded val recipe: RecipeEntity,
    @Relation(
        parentColumn = "id",
        entityColumn = "recipeId"
    )
    val ingredients: List<RecipeIngredientEntity>
)
