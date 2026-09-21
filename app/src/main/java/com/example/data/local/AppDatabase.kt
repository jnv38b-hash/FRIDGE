package com.example.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.data.model.CookingHistoryEntity
import com.example.data.model.FavoriteEntity
import com.example.data.model.IngredientEntity
import com.example.data.model.PantryItemEntity
import com.example.data.model.RecipeEntity
import com.example.data.model.RecipeIngredientEntity
import com.example.data.model.ShoppingListItemEntity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(
    entities = [
        IngredientEntity::class,
        RecipeEntity::class,
        RecipeIngredientEntity::class,
        PantryItemEntity::class,
        FavoriteEntity::class,
        CookingHistoryEntity::class,
        ShoppingListItemEntity::class
    ],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun ingredientDao(): IngredientDao
    abstract fun recipeDao(): RecipeDao
    abstract fun pantryDao(): PantryDao
    abstract fun favoriteDao(): FavoriteDao
    abstract fun cookingHistoryDao(): CookingHistoryDao
    abstract fun shoppingListDao(): ShoppingListDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context, scope: CoroutineScope): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "fridgechef_database"
                )
                    .addCallback(DatabaseCallback(scope))
                    .build()
                INSTANCE = instance
                instance
            }
        }

        private class DatabaseCallback(
            private val scope: CoroutineScope
        ) : RoomDatabase.Callback() {
            override fun onCreate(db: SupportSQLiteDatabase) {
                super.onCreate(db)
                INSTANCE?.let { database ->
                    scope.launch(Dispatchers.IO) {
                        populateDatabase(database)
                    }
                }
            }

            suspend fun populateDatabase(database: AppDatabase) {
                // Populate ingredients
                database.ingredientDao().insertIngredients(InitialData.getInitialIngredients())

                // Populate recipes and recipe ingredients
                val recipes = InitialData.getInitialRecipes()
                database.recipeDao().insertRecipes(recipes.map { it.first })
                val allRecipeIngredients = recipes.flatMap { it.second }
                database.recipeDao().insertRecipeIngredients(allRecipeIngredients)

                // Populate initial pantry
                database.pantryDao().insertPantryItems(InitialData.getInitialPantryItems())
            }
        }
    }
}
