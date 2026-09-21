package com.example.data.repository

import com.example.data.local.AppDatabase
import com.example.data.local.InitialData
import com.example.data.model.CookingHistoryEntity
import com.example.data.model.FavoriteEntity
import com.example.data.model.IngredientEntity
import com.example.data.model.PantryItemEntity
import com.example.data.model.RecipeWithIngredients
import com.example.data.model.ShoppingListItemEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.withContext

class FridgeChefRepository(private val database: AppDatabase) {

    private val ingredientDao = database.ingredientDao()
    private val recipeDao = database.recipeDao()
    private val pantryDao = database.pantryDao()
    private val favoriteDao = database.favoriteDao()
    private val cookingHistoryDao = database.cookingHistoryDao()
    private val shoppingListDao = database.shoppingListDao()

    val allIngredients: Flow<List<IngredientEntity>> = ingredientDao.getAllIngredients()
    val allRecipes: Flow<List<RecipeWithIngredients>> = recipeDao.getAllRecipesWithIngredients()
    val pantryItems: Flow<List<PantryItemEntity>> = pantryDao.getAllPantryItems()
    val favorites: Flow<List<FavoriteEntity>> = favoriteDao.getAllFavorites()
    val cookingHistory: Flow<List<CookingHistoryEntity>> = cookingHistoryDao.getAllHistory()
    val shoppingList: Flow<List<ShoppingListItemEntity>> = shoppingListDao.getAllItems()

    suspend fun checkAndSeedInitialData() = withContext(Dispatchers.IO) {
        val existingIngredients = ingredientDao.getAllIngredients().first()
        if (existingIngredients.isEmpty()) {
            ingredientDao.insertIngredients(InitialData.getInitialIngredients())
            val recipes = InitialData.getInitialRecipes()
            recipeDao.insertRecipes(recipes.map { it.first })
            recipeDao.insertRecipeIngredients(recipes.flatMap { it.second })
            pantryDao.insertPantryItems(InitialData.getInitialPantryItems())
        }
    }

    suspend fun searchIngredients(query: String): List<IngredientEntity> = withContext(Dispatchers.IO) {
        ingredientDao.searchIngredients(query)
    }

    suspend fun addCustomIngredient(ingredient: IngredientEntity) = withContext(Dispatchers.IO) {
        ingredientDao.insertIngredient(ingredient)
    }

    suspend fun getRecipeById(recipeId: String): RecipeWithIngredients? = withContext(Dispatchers.IO) {
        recipeDao.getRecipeWithIngredientsById(recipeId)
    }

    fun observeRecipeById(recipeId: String): Flow<RecipeWithIngredients?> =
        recipeDao.observeRecipeWithIngredientsById(recipeId)

    // Pantry operations
    suspend fun addPantryItem(item: PantryItemEntity): Long = withContext(Dispatchers.IO) {
        pantryDao.insertPantryItem(item)
    }

    suspend fun updatePantryItem(item: PantryItemEntity) = withContext(Dispatchers.IO) {
        pantryDao.updatePantryItem(item)
    }

    suspend fun deletePantryItem(item: PantryItemEntity) = withContext(Dispatchers.IO) {
        pantryDao.deletePantryItem(item)
    }

    suspend fun deletePantryItemById(id: Long) = withContext(Dispatchers.IO) {
        pantryDao.deletePantryItemById(id)
    }

    // Favorites
    fun isFavorite(recipeId: String): Flow<Boolean> = favoriteDao.isFavorite(recipeId)

    suspend fun toggleFavorite(recipeId: String) = withContext(Dispatchers.IO) {
        val isFav = favoriteDao.isFavoriteSync(recipeId)
        if (isFav) {
            favoriteDao.removeFavorite(recipeId)
        } else {
            favoriteDao.addFavorite(FavoriteEntity(recipeId = recipeId))
        }
    }

    // Cooking History
    suspend fun recordRecipeCooked(recipeId: String, recipeName: String) = withContext(Dispatchers.IO) {
        cookingHistoryDao.recordCooking(recipeId, recipeName)
    }

    // Shopping List
    suspend fun addShoppingListItem(item: ShoppingListItemEntity): Long = withContext(Dispatchers.IO) {
        shoppingListDao.insertItem(item)
    }

    suspend fun addMissingIngredientsToShoppingList(
        missingIngredients: List<Pair<String, String>>, // name, category
        recipeName: String
    ) = withContext(Dispatchers.IO) {
        val items = missingIngredients.map { (name, category) ->
            ShoppingListItemEntity(
                ingredientName = name,
                category = category.ifBlank { "Pantry Staples" },
                quantity = "1",
                unit = "pack",
                recipeSource = recipeName
            )
        }
        shoppingListDao.insertItems(items)
    }

    suspend fun updateShoppingListItem(item: ShoppingListItemEntity) = withContext(Dispatchers.IO) {
        shoppingListDao.updateItem(item)
    }

    suspend fun deleteShoppingListItem(item: ShoppingListItemEntity) = withContext(Dispatchers.IO) {
        shoppingListDao.deleteItem(item)
    }

    suspend fun clearCompletedShoppingListItems() = withContext(Dispatchers.IO) {
        shoppingListDao.clearCompletedItems()
    }
}
