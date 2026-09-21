package com.example.data.repository

object SubstitutionDatabase {
    // Map of canonical ingredient or lowercased ingredient keyword to list of viable substitutes
    private val substitutionMap: Map<String, List<String>> = mapOf(
        "butter" to listOf("Ghee", "Olive Oil", "Coconut Oil"),
        "ghee" to listOf("Butter", "Cooking Oil", "Mustard Oil"),
        "milk" to listOf("Soy Milk", "Oat Milk", "Almond Milk", "Yogurt diluted with water"),
        "oat milk" to listOf("Soy Milk", "Almond Milk", "Dairy Milk"),
        "paneer" to listOf("Tofu", "Halloumi", "Cottage Cheese"),
        "tofu" to listOf("Paneer", "Tempeh", "Mushrooms"),
        "spinach" to listOf("Kale", "Fenugreek Leaves (Methi)", "Swiss Chard"),
        "lemon" to listOf("Lime", "Vinegar", "Amchur (Dry Mango Powder)"),
        "lime" to listOf("Lemon", "Vinegar", "Amchur"),
        "yogurt" to listOf("Curd (Dahi)", "Greek Yogurt", "Sour Cream", "Coconut Yogurt"),
        "curd" to listOf("Yogurt", "Sour Cream", "Greek Yogurt"),
        "mozzarella" to listOf("Cheddar", "Paneer", "Processed Cheese", "Gouda"),
        "parmesan" to listOf("Pecorino", "Sharp Cheddar", "Nutritional Yeast"),
        "heavy cream" to listOf("Coconut Milk", "Milk with Butter", "Cashew Cream"),
        "cilantro" to listOf("Coriander", "Dhaniya", "Parsley"),
        "coriander" to listOf("Cilantro", "Dhaniya", "Parsley"),
        "dhaniya" to listOf("Coriander", "Cilantro", "Parsley"),
        "bread" to listOf("Roti", "Tortilla", "Pita Bread", "Baguette"),
        "rice" to listOf("Quinoa", "Cauliflower Rice", "Brown Rice", "Poha"),
        "tomato" to listOf("Tomato Puree", "Canned Tomatoes", "Tomato Paste"),
        "sugar" to listOf("Honey", "Jaggery (Gur)", "Maple Syrup", "Brown Sugar"),
        "egg" to listOf("Mashed Tofu", "Besan (Chickpea Flour batter)", "Flaxseed meal"),
        "soy sauce" to listOf("Tamari", "Coconut Aminos", "Worcestershire sauce"),
        "garlic" to listOf("Garlic Powder", "Shallots", "Asafoetida (Hing)")
    )

    fun getSubstitutesFor(ingredientName: String): List<String> {
        val lower = ingredientName.trim().lowercase()
        // Direct match
        substitutionMap[lower]?.let { return it }

        // Partial match
        for ((key, substitutes) in substitutionMap) {
            if (lower.contains(key) || key.contains(lower)) {
                return substitutes
            }
        }
        return emptyList()
    }

    fun isAcceptableSubstitute(missingIngredientName: String, availableIngredientName: String): Boolean {
        val missingLower = missingIngredientName.trim().lowercase()
        val availLower = availableIngredientName.trim().lowercase()

        val subs = getSubstitutesFor(missingLower).map { it.lowercase() }
        return subs.any { availLower.contains(it) || it.contains(availLower) }
    }
}
