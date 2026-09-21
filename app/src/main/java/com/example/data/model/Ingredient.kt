package com.example.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

enum class IngredientCategory(val displayName: String, val emoji: String) {
    VEGETABLES("Vegetables", "🥦"),
    FRUITS("Fruits", "🍎"),
    DAIRY("Dairy", "🧀"),
    EGGS("Eggs", "🥚"),
    MEAT("Meat", "🍗"),
    SEAFOOD("Seafood", "🐟"),
    GRAINS("Grains", "🌾"),
    RICE("Rice", "🍚"),
    PASTA("Pasta", "🍝"),
    BREAD("Bread", "🍞"),
    PULSES("Pulses & Lentils", "🫘"),
    SPICES("Spices & Herbs", "🌿"),
    SAUCES("Sauces & Oils", "🫒"),
    SNACKS("Snacks", "🥨"),
    FROZEN("Frozen", "🧊"),
    PANTRY_STAPLES("Pantry Staples", "🧂"),
    OTHER("Other", "📦")
}

@Entity(tableName = "ingredients")
data class IngredientEntity(
    @PrimaryKey val id: String,
    val name: String,
    val category: String,
    val emoji: String,
    val aliases: String = "", // Comma-separated synonyms (e.g. "Dhaniya, Coriander, Cilantro")
    val defaultUnit: String = "pcs"
) {
    fun matchesQuery(query: String): Boolean {
        val cleanQuery = query.trim().lowercase()
        if (cleanQuery.isEmpty()) return true
        if (name.lowercase().contains(cleanQuery)) return true
        return aliases.split(",").any { it.trim().lowercase().contains(cleanQuery) }
    }
}
