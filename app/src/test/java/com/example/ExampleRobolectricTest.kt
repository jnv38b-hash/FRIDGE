package com.example

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.example.data.local.InitialData
import com.example.data.model.RecipeSortOrder
import com.example.data.model.RecipeWithIngredients
import com.example.data.model.UserProfilePreferences
import com.example.data.repository.RecipeMatcher
import com.example.data.repository.RecipeValidator
import com.example.data.repository.SubstitutionDatabase
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [34])
class ExampleRobolectricTest {

    @Test
    fun `read string from context`() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        val appName = context.getString(R.string.app_name)
        assertEquals("FridgeChef", appName)
    }

    @Test
    fun `substitution database finds valid substitutes`() {
        val butterSubs = SubstitutionDatabase.getSubstitutesFor("butter")
        assertTrue(butterSubs.isNotEmpty())
        assertTrue(SubstitutionDatabase.isAcceptableSubstitute("butter", "ghee"))
        assertTrue(SubstitutionDatabase.isAcceptableSubstitute("paneer", "tofu"))
        assertFalse(SubstitutionDatabase.isAcceptableSubstitute("potato", "milk"))
    }

    @Test
    fun `recipe matcher correctly matches and ranks recipes`() {
        val initialRecipes = InitialData.getInitialRecipes().map { pair ->
            RecipeWithIngredients(recipe = pair.first, ingredients = pair.second)
        }

        val selected = listOf("Egg", "Bread", "Onion", "Butter", "Salt")
        val matches = RecipeMatcher.matchRecipes(
            allRecipes = initialRecipes,
            selectedIngredients = selected,
            sortOrder = RecipeSortOrder.BEST_MATCH
        )

        assertTrue(matches.isNotEmpty())
        val topMatch = matches.first()
        // Masala Egg Toast should be top match or 100% match
        assertTrue(topMatch.matchPercentage >= 80)
    }

    @Test
    fun `recipe matcher strictly excludes allergen`() {
        val initialRecipes = InitialData.getInitialRecipes().map { pair ->
            RecipeWithIngredients(recipe = pair.first, ingredients = pair.second)
        }

        val prefs = UserProfilePreferences(
            excludedAllergens = setOf("Eggs")
        )

        val matches = RecipeMatcher.matchRecipes(
            allRecipes = initialRecipes,
            selectedIngredients = listOf("Egg", "Bread", "Onion", "Tomato", "Potato"),
            userPreferences = prefs
        )

        // All matched recipes must not contain eggs
        matches.forEach { match ->
            assertFalse(match.recipe.name.contains("Egg", ignoreCase = true))
            assertFalse(match.allIngredients.any { it.ingredientName.equals("Egg", ignoreCase = true) })
        }
    }

    @Test
    fun `recipe validator validates well-formed recipes`() {
        val firstPair = InitialData.getInitialRecipes().first()
        val recipeWithIngs = RecipeWithIngredients(firstPair.first, firstPair.second)
        val result = RecipeValidator.validate(
            recipeWithIngredients = recipeWithIngs,
            availableIngredients = listOf("Egg", "Bread")
        )
        assertTrue(result.isValid)
    }
}
