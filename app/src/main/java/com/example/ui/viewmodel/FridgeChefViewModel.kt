package com.example.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.local.AppDatabase
import com.example.data.model.CookingHistoryEntity
import com.example.data.model.FavoriteEntity
import com.example.data.model.IngredientCategory
import com.example.data.model.IngredientEntity
import com.example.data.model.PantryItemEntity
import com.example.data.model.PantryStatus
import com.example.data.model.RecipeMatch
import com.example.data.model.RecipeSortOrder
import com.example.data.model.RecipeWithIngredients
import com.example.data.model.ShoppingListItemEntity
import com.example.data.model.UserProfilePreferences
import com.example.data.repository.FridgeChefRepository
import com.example.data.repository.RecipeMatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class FridgeChefViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: FridgeChefRepository

    init {
        val database = AppDatabase.getDatabase(application, viewModelScope)
        repository = FridgeChefRepository(database)
        viewModelScope.launch {
            repository.checkAndSeedInitialData()
        }
    }

    // Selected Ingredients for Fridge-to-Recipe flow
    private val _selectedIngredients = MutableStateFlow<List<String>>(listOf("Tomato", "Onion", "Potato", "Egg", "Bread"))
    val selectedIngredients: StateFlow<List<String>> = _selectedIngredients.asStateFlow()

    // Filters
    private val _sortOrder = MutableStateFlow(RecipeSortOrder.BEST_MATCH)
    val sortOrder: StateFlow<RecipeSortOrder> = _sortOrder.asStateFlow()

    private val _selectedCuisine = MutableStateFlow<String?>("All")
    val selectedCuisine: StateFlow<String?> = _selectedCuisine.asStateFlow()

    private val _selectedMealType = MutableStateFlow<String?>("All")
    val selectedMealType: StateFlow<String?> = _selectedMealType.asStateFlow()

    private val _activeQuickMode = MutableStateFlow<String?>(null)
    val activeQuickMode: StateFlow<String?> = _activeQuickMode.asStateFlow()

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    // User Preferences
    private val _userPreferences = MutableStateFlow(UserProfilePreferences())
    val userPreferences: StateFlow<UserProfilePreferences> = _userPreferences.asStateFlow()

    // Database flows
    val allIngredients: StateFlow<List<IngredientEntity>> = repository.allIngredients
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val allRecipes: StateFlow<List<RecipeWithIngredients>> = repository.allRecipes
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val pantryItems: StateFlow<List<PantryItemEntity>> = repository.pantryItems
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val favorites: StateFlow<List<FavoriteEntity>> = repository.favorites
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val cookingHistory: StateFlow<List<CookingHistoryEntity>> = repository.cookingHistory
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val shoppingList: StateFlow<List<ShoppingListItemEntity>> = repository.shoppingList
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // Expiring ingredients check
    val expiringIngredients: StateFlow<List<PantryItemEntity>> = pantryItems.map { items ->
        items.filter { it.isExpiringWithinDays(3) || it.status == PantryStatus.EXPIRING_SOON.name }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // Combined filter settings
    private data class FilterBundle(
        val sortOrder: RecipeSortOrder,
        val cuisine: String?,
        val mealType: String?,
        val quickMode: String?
    )

    private val filterBundle = combine(
        _sortOrder,
        _selectedCuisine,
        _selectedMealType,
        _activeQuickMode
    ) { sort, cuisine, mealType, quickMode ->
        FilterBundle(sort, cuisine, mealType, quickMode)
    }

    // Matched Recipes based on selected ingredients, filters, and user preferences
    val recipeMatches: StateFlow<List<RecipeMatch>> = combine(
        allRecipes,
        _selectedIngredients,
        expiringIngredients,
        _userPreferences,
        filterBundle
    ) { all, selected, expiring, prefs, filters ->
        val expiringNames = expiring.map { it.ingredientName }.toSet()
        RecipeMatcher.matchRecipes(
            allRecipes = all,
            selectedIngredients = selected,
            expiringIngredientNames = expiringNames,
            userPreferences = prefs,
            sortOrder = filters.sortOrder,
            cuisineFilter = if (filters.cuisine == "All") null else filters.cuisine,
            mealTypeFilter = if (filters.mealType == "All") null else filters.mealType,
            quickModeFilter = filters.quickMode
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // Ingredient Selection Actions
    fun toggleIngredientSelection(ingredientName: String) {
        val current = _selectedIngredients.value.toMutableList()
        val existingIndex = current.indexOfFirst { it.equals(ingredientName, ignoreCase = true) }
        if (existingIndex >= 0) {
            current.removeAt(existingIndex)
        } else {
            current.add(ingredientName)
        }
        _selectedIngredients.value = current
    }

    fun removeSelectedIngredient(ingredientName: String) {
        _selectedIngredients.value = _selectedIngredients.value.filterNot { it.equals(ingredientName, ignoreCase = true) }
    }

    fun clearSelectedIngredients() {
        _selectedIngredients.value = emptyList()
    }

    fun loadIngredientsFromPantry() {
        val availablePantryNames = pantryItems.value.map { it.ingredientName }
        if (availablePantryNames.isNotEmpty()) {
            _selectedIngredients.value = availablePantryNames
        }
    }

    fun addCustomIngredient(name: String, category: String, unit: String) {
        viewModelScope.launch {
            val custom = IngredientEntity(
                id = "custom_" + System.currentTimeMillis(),
                name = name.trim(),
                category = category,
                emoji = when (category) {
                    IngredientCategory.VEGETABLES.displayName -> "🥦"
                    IngredientCategory.FRUITS.displayName -> "🍎"
                    IngredientCategory.DAIRY.displayName -> "🧀"
                    IngredientCategory.EGGS.displayName -> "🥚"
                    IngredientCategory.MEAT.displayName -> "🍗"
                    IngredientCategory.BREAD.displayName -> "🍞"
                    IngredientCategory.SPICES.displayName -> "🌿"
                    else -> "🥗"
                },
                defaultUnit = unit
            )
            repository.addCustomIngredient(custom)
            if (!_selectedIngredients.value.contains(name.trim())) {
                _selectedIngredients.value = _selectedIngredients.value + name.trim()
            }
        }
    }

    // Filter controls
    fun setSortOrder(order: RecipeSortOrder) {
        _sortOrder.value = order
    }

    fun setCuisineFilter(cuisine: String?) {
        _selectedCuisine.value = cuisine
    }

    fun setMealTypeFilter(mealType: String?) {
        _selectedMealType.value = mealType
    }

    fun setQuickMode(mode: String?) {
        _activeQuickMode.value = if (_activeQuickMode.value == mode) null else mode
        if (mode == "Empty My Fridge") {
            loadIngredientsFromPantry()
        }
    }

    fun setSearchQuery(query: String) {
        _searchQuery.value = query
    }

    // Favorites
    fun toggleFavorite(recipeId: String) {
        viewModelScope.launch {
            repository.toggleFavorite(recipeId)
        }
    }

    fun isFavorite(recipeId: String): Boolean {
        return favorites.value.any { it.recipeId == recipeId }
    }

    // Cooking History
    fun recordRecipeCooked(recipeId: String, recipeName: String) {
        viewModelScope.launch {
            repository.recordRecipeCooked(recipeId, recipeName)
        }
    }

    // Pantry Management
    fun addPantryItem(name: String, category: String, quantity: Double, unit: String, expiryDays: Int?, notes: String = "") {
        viewModelScope.launch {
            val expiryTimestamp = expiryDays?.let { System.currentTimeMillis() + (it * 24 * 60 * 60 * 1000L) }
            val status = if (expiryDays != null && expiryDays <= 3) PantryStatus.EXPIRING_SOON.name else PantryStatus.AVAILABLE.name
            val item = PantryItemEntity(
                ingredientId = "ing_" + name.lowercase().replace(" ", "_"),
                ingredientName = name.trim(),
                category = category,
                emoji = "🥕",
                quantity = quantity,
                unit = unit,
                expiryTimestamp = expiryTimestamp,
                status = status,
                notes = notes
            )
            repository.addPantryItem(item)
        }
    }

    fun deletePantryItem(item: PantryItemEntity) {
        viewModelScope.launch {
            repository.deletePantryItem(item)
        }
    }

    fun updatePantryItem(item: PantryItemEntity) {
        viewModelScope.launch {
            repository.updatePantryItem(item)
        }
    }

    // Shopping List
    fun addMissingIngredientsToShoppingList(recipeWithIngredients: RecipeWithIngredients, missingList: List<String>) {
        viewModelScope.launch {
            val pairs = recipeWithIngredients.ingredients
                .filter { missingList.contains(it.ingredientName) }
                .map { it.ingredientName to "Recipe Ingredients" }
            repository.addMissingIngredientsToShoppingList(pairs, recipeWithIngredients.recipe.name)
        }
    }

    fun addManualShoppingItem(name: String, category: String = "Pantry Staples") {
        viewModelScope.launch {
            repository.addShoppingListItem(
                ShoppingListItemEntity(
                    ingredientName = name.trim(),
                    category = category,
                    quantity = "1",
                    unit = "item"
                )
            )
        }
    }

    fun toggleShoppingItemChecked(item: ShoppingListItemEntity) {
        viewModelScope.launch {
            repository.updateShoppingListItem(item.copy(isChecked = !item.isChecked))
        }
    }

    fun deleteShoppingItem(item: ShoppingListItemEntity) {
        viewModelScope.launch {
            repository.deleteShoppingListItem(item)
        }
    }

    fun clearCompletedShoppingItems() {
        viewModelScope.launch {
            repository.clearCompletedShoppingListItems()
        }
    }

    // Preferences
    fun setDietaryPreference(diet: String?) {
        _userPreferences.value = _userPreferences.value.copy(selectedDiet = diet)
    }

    fun toggleAllergen(allergen: String) {
        val current = _userPreferences.value.excludedAllergens.toMutableSet()
        if (current.contains(allergen)) {
            current.remove(allergen)
        } else {
            current.add(allergen)
        }
        _userPreferences.value = _userPreferences.value.copy(excludedAllergens = current)
    }

    fun toggleEquipment(equipment: String) {
        val current = _userPreferences.value.availableEquipment.toMutableSet()
        if (current.contains(equipment)) {
            current.remove(equipment)
        } else {
            current.add(equipment)
        }
        _userPreferences.value = _userPreferences.value.copy(availableEquipment = current)
    }
}
