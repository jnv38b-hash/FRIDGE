package com.example.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.data.model.RecipeSortOrder
import com.example.data.repository.RecipeMatcher
import com.example.ui.components.EmptyStateView
import com.example.ui.components.RecipeCard
import com.example.ui.viewmodel.FridgeChefViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExploreScreen(
    viewModel: FridgeChefViewModel,
    onNavigateToRecipeDetail: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val allRecipes by viewModel.allRecipes.collectAsStateWithLifecycle()
    val selectedIngredients by viewModel.selectedIngredients.collectAsStateWithLifecycle()

    var searchQuery by remember { mutableStateOf("") }
    var selectedCuisineTab by remember { mutableIntStateOf(0) }
    var selectedMealType by remember { mutableStateOf("All") }

    val cuisines = remember { listOf("All Cuisines", "Indian", "South Indian", "Italian", "Chinese") }
    val mealTypes = remember { listOf("All", "Breakfast", "Lunch", "Dinner") }

    // Filter recipes based on explore query and tabs
    val filteredMatches = remember(allRecipes, selectedIngredients, searchQuery, selectedCuisineTab, selectedMealType) {
        val cuisine = if (selectedCuisineTab == 0) null else cuisines[selectedCuisineTab]
        val meal = if (selectedMealType == "All") null else selectedMealType

        val matches = RecipeMatcher.matchRecipes(
            allRecipes = allRecipes,
            selectedIngredients = selectedIngredients,
            sortOrder = RecipeSortOrder.BEST_MATCH,
            cuisineFilter = cuisine,
            mealTypeFilter = meal
        )

        if (searchQuery.isBlank()) matches
        else matches.filter {
            it.recipe.name.contains(searchQuery, ignoreCase = true) ||
                    it.recipe.cuisine.contains(searchQuery, ignoreCase = true) ||
                    it.allIngredients.any { ing -> ing.ingredientName.contains(searchQuery, ignoreCase = true) }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Explore Recipes", fontWeight = FontWeight.Bold) }
            )
        },
        modifier = modifier.testTag("explore_screen")
    ) { innerPadding ->
        LazyColumn(
            contentPadding = PaddingValues(
                top = innerPadding.calculateTopPadding(),
                bottom = 90.dp
            ),
            verticalArrangement = Arrangement.spacedBy(14.dp),
            modifier = Modifier.fillMaxSize()
        ) {
            // Search Input
            item {
                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = { searchQuery = it },
                    placeholder = { Text("Search by name or dish, e.g. pasta, dal...") },
                    leadingIcon = { Icon(imageVector = Icons.Default.Search, contentDescription = null) },
                    trailingIcon = {
                        if (searchQuery.isNotEmpty()) {
                            IconButton(onClick = { searchQuery = "" }) {
                                Icon(imageVector = Icons.Default.Clear, contentDescription = "Clear")
                            }
                        }
                    },
                    singleLine = true,
                    shape = RoundedCornerShape(14.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                )
            }

            // Cuisine Tabs
            item {
                ScrollableTabRow(
                    selectedTabIndex = selectedCuisineTab,
                    edgePadding = 16.dp,
                    divider = {},
                    modifier = Modifier.fillMaxWidth()
                ) {
                    cuisines.forEachIndexed { index, name ->
                        Tab(
                            selected = selectedCuisineTab == index,
                            onClick = { selectedCuisineTab = index },
                            text = {
                                Text(
                                    text = name,
                                    fontWeight = if (selectedCuisineTab == index) FontWeight.Bold else FontWeight.Normal
                                )
                            }
                        )
                    }
                }
            }

            // Meal Type Filter Chips
            item {
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    contentPadding = PaddingValues(horizontal = 16.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    items(mealTypes) { type ->
                        val isSelected = selectedMealType == type
                        FilterChip(
                            selected = isSelected,
                            onClick = { selectedMealType = type },
                            label = { Text(type) },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = MaterialTheme.colorScheme.primaryContainer,
                                selectedLabelColor = MaterialTheme.colorScheme.onPrimaryContainer
                            )
                        )
                    }
                }
            }

            // Results count
            item {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                ) {
                    Text(
                        text = "${filteredMatches.size} recipes available",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            if (filteredMatches.isEmpty()) {
                item {
                    EmptyStateView(
                        emoji = "🔍",
                        title = "No recipes found",
                        message = "Try searching with a different term or cuisine filter."
                    )
                }
            } else {
                items(filteredMatches) { match ->
                    RecipeCard(
                        match = match,
                        isFavorite = viewModel.isFavorite(match.recipe.id),
                        onFavoriteClick = { viewModel.toggleFavorite(match.recipe.id) },
                        onClick = { onNavigateToRecipeDetail(match.recipe.id) },
                        modifier = Modifier.padding(horizontal = 16.dp)
                    )
                }
            }
        }
    }
}
