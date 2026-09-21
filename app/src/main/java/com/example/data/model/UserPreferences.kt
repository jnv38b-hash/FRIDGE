package com.example.data.model

enum class DietaryRestriction(val id: String, val label: String, val emoji: String) {
    VEGETARIAN("Vegetarian", "Vegetarian", "🥦"),
    VEGAN("Vegan", "Vegan", "🌱"),
    EGGETARIAN("Eggetarian", "Eggetarian", "🥚"),
    NON_VEGETARIAN("Non-vegetarian", "Non-Vegetarian", "🍗"),
    GLUTEN_FREE("Gluten-free", "Gluten-Free", "🌾"),
    DAIRY_FREE("Dairy-free", "Dairy-Free", "🥛"),
    LOW_CARB("Low-carb", "Low-Carb", "🥑"),
    HIGH_PROTEIN("High-protein", "High-Protein", "🍳"),
    LOW_CALORIE("Low-calorie", "Low-Calorie", "🥗"),
    NO_ADDED_SUGAR("No-sugar", "No Added Sugar", "🍏"),
    JAIN_FRIENDLY("Jain", "Jain Friendly", "🪷")
}

enum class Allergen(val id: String, val label: String, val emoji: String) {
    PEANUTS("Peanuts", "Peanuts", "🥜"),
    TREE_NUTS("Tree Nuts", "Tree Nuts", "🌰"),
    DAIRY("Dairy", "Dairy / Milk", "🥛"),
    EGGS("Eggs", "Eggs", "🥚"),
    SHELLFISH("Shellfish", "Shellfish", "🦐"),
    SOY("Soy", "Soy", "🫘"),
    WHEAT("Gluten", "Wheat / Gluten", "🌾"),
    MUSTARD("Mustard", "Mustard", "🟡"),
    SESAME("Sesame", "Sesame", "⚪")
}

enum class KitchenEquipment(val id: String, val label: String, val emoji: String) {
    STOVE("Stove", "Stove / Cooktop", "🔥"),
    PAN("Pan", "Frying Pan / Tawa", "🍳"),
    POT("Pot", "Cooking Pot / Kadai", "🍲"),
    OVEN("Oven", "Oven", "♨️"),
    MICROWAVE("Microwave", "Microwave", "📻"),
    AIR_FRYER("Air Fryer", "Air Fryer", "💨"),
    BLENDER("Blender", "Mixer / Blender", "🌪️"),
    TOASTER("Toaster", "Toaster", "🥪")
}

data class UserProfilePreferences(
    val selectedDiet: String? = null,
    val excludedAllergens: Set<String> = emptySet(),
    val availableEquipment: Set<String> = setOf("Stove", "Pan", "Pot", "Toaster"),
    val favoriteCuisines: Set<String> = emptySet(),
    val maxCookTimeMinutes: Int = 60
)
