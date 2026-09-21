package com.example.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ClearAll
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.data.model.RecipeMatch
import com.example.data.model.RecipeSortOrder
import com.example.data.repository.RecipeMatcher
import com.example.ui.components.EmptyStateView
import com.example.ui.components.RecipeCard
import com.example.ui.theme.WarmOrange
import com.example.ui.viewmodel.FridgeChefViewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SavedScreen(
    viewModel: FridgeChefViewModel,
    onNavigateToRecipeDetail: (String) -> Unit,
    onNavigateToCookingMode: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    var selectedTab by remember { mutableIntStateOf(0) }
    val tabs = listOf("Favorites", "History", "Shopping List")

    val favorites by viewModel.favorites.collectAsStateWithLifecycle()
    val allRecipes by viewModel.allRecipes.collectAsStateWithLifecycle()
    val selectedIngredients by viewModel.selectedIngredients.collectAsStateWithLifecycle()
    val cookingHistory by viewModel.cookingHistory.collectAsStateWithLifecycle()
    val shoppingList by viewModel.shoppingList.collectAsStateWithLifecycle()

    var newShoppingItemName by remember { mutableStateOf("") }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Saved & Activity", fontWeight = FontWeight.Bold) }
            )
        },
        modifier = modifier.testTag("saved_screen")
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = innerPadding.calculateTopPadding())
        ) {
            TabRow(selectedTabIndex = selectedTab) {
                tabs.forEachIndexed { index, title ->
                    Tab(
                        selected = selectedTab == index,
                        onClick = { selectedTab = index },
                        text = {
                            Text(
                                text = title,
                                fontWeight = if (selectedTab == index) FontWeight.Bold else FontWeight.Normal
                            )
                        }
                    )
                }
            }

            when (selectedTab) {
                0 -> {
                    // Favorites Tab
                    val favoriteMatches = remember(favorites, allRecipes, selectedIngredients) {
                        val favoriteRecipeIds = favorites.map { it.recipeId }.toSet()
                        val favRecipes = allRecipes.filter { favoriteRecipeIds.contains(it.recipe.id) }
                        RecipeMatcher.matchRecipes(
                            allRecipes = favRecipes,
                            selectedIngredients = selectedIngredients,
                            sortOrder = RecipeSortOrder.BEST_MATCH
                        )
                    }

                    if (favoriteMatches.isEmpty()) {
                        EmptyStateView(
                            emoji = "❤️",
                            title = "No Favorite Recipes Yet",
                            message = "Tap the heart icon on any recipe to save it for quick access later."
                        )
                    } else {
                        LazyColumn(
                            contentPadding = PaddingValues(top = 16.dp, bottom = 90.dp),
                            verticalArrangement = Arrangement.spacedBy(14.dp),
                            modifier = Modifier.fillMaxSize()
                        ) {
                            items(favoriteMatches) { match ->
                                RecipeCard(
                                    match = match,
                                    isFavorite = true,
                                    onFavoriteClick = { viewModel.toggleFavorite(match.recipe.id) },
                                    onClick = { onNavigateToRecipeDetail(match.recipe.id) },
                                    modifier = Modifier.padding(horizontal = 16.dp)
                                )
                            }
                        }
                    }
                }
                1 -> {
                    // Cooking History Tab
                    if (cookingHistory.isEmpty()) {
                        EmptyStateView(
                            emoji = "🍳",
                            title = "No Cooking History Yet",
                            message = "Start cooking any recipe and complete the steps to log your meals here."
                        )
                    } else {
                        val dateFormat = remember { SimpleDateFormat("MMM dd, yyyy", Locale.getDefault()) }
                        LazyColumn(
                            contentPadding = PaddingValues(top = 16.dp, bottom = 90.dp),
                            verticalArrangement = Arrangement.spacedBy(10.dp),
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(horizontal = 16.dp)
                        ) {
                            items(cookingHistory) { item ->
                                Card(
                                    onClick = { onNavigateToRecipeDetail(item.recipeId) },
                                    shape = RoundedCornerShape(16.dp),
                                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        modifier = Modifier.padding(16.dp)
                                    ) {
                                        Column(modifier = Modifier.weight(1f)) {
                                            Text(
                                                text = item.recipeName,
                                                fontSize = 16.sp,
                                                fontWeight = FontWeight.Bold,
                                                color = MaterialTheme.colorScheme.onSurface
                                            )
                                            Spacer(modifier = Modifier.height(4.dp))
                                            Text(
                                                text = "Cooked ${item.timesCooked} time(s) • Last on ${dateFormat.format(Date(item.lastCookedAt))}",
                                                fontSize = 12.sp,
                                                color = MaterialTheme.colorScheme.onSurfaceVariant
                                            )
                                        }

                                        IconButton(
                                            onClick = { onNavigateToCookingMode(item.recipeId) },
                                            modifier = Modifier.size(36.dp)
                                        ) {
                                            Icon(
                                                imageVector = Icons.Default.PlayArrow,
                                                contentDescription = "Cook Again",
                                                tint = WarmOrange
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
                2 -> {
                    // Shopping List Tab
                    LazyColumn(
                        contentPadding = PaddingValues(top = 16.dp, bottom = 90.dp),
                        verticalArrangement = Arrangement.spacedBy(10.dp),
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(horizontal = 16.dp)
                    ) {
                        // Add item bar
                        item {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                OutlinedTextField(
                                    value = newShoppingItemName,
                                    onValueChange = { newShoppingItemName = it },
                                    placeholder = { Text("Add item to grocery list...") },
                                    singleLine = true,
                                    shape = RoundedCornerShape(12.dp),
                                    modifier = Modifier.weight(1f)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Button(
                                    onClick = {
                                        if (newShoppingItemName.isNotBlank()) {
                                            viewModel.addManualShoppingItem(newShoppingItemName.trim())
                                            newShoppingItemName = ""
                                        }
                                    },
                                    enabled = newShoppingItemName.isNotBlank(),
                                    shape = RoundedCornerShape(12.dp),
                                    modifier = Modifier.height(52.dp)
                                ) {
                                    Icon(imageVector = Icons.Default.Add, contentDescription = "Add")
                                }
                            }
                        }

                        // Header with Clear Completed
                        item {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 6.dp)
                            ) {
                                Text(
                                    text = "${shoppingList.count { !it.isChecked }} items remaining",
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )

                                if (shoppingList.any { it.isChecked }) {
                                    Text(
                                        text = "Clear Completed",
                                        fontSize = 13.sp,
                                        fontWeight = FontWeight.SemiBold,
                                        color = MaterialTheme.colorScheme.error,
                                        modifier = Modifier.clickable { viewModel.clearCompletedShoppingItems() }
                                    )
                                }
                            }
                        }

                        if (shoppingList.isEmpty()) {
                            item {
                                EmptyStateView(
                                    emoji = "🛒",
                                    title = "Shopping List is Empty",
                                    message = "Add items manually or tap 'Add Missing to Shopping List' from any recipe."
                                )
                            }
                        } else {
                            items(shoppingList) { item ->
                                Card(
                                    shape = RoundedCornerShape(12.dp),
                                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                                    elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp)
                                    ) {
                                        Row(
                                            verticalAlignment = Alignment.CenterVertically,
                                            modifier = Modifier.weight(1f)
                                        ) {
                                            Checkbox(
                                                checked = item.isChecked,
                                                onCheckedChange = { viewModel.toggleShoppingItemChecked(item) },
                                                colors = CheckboxDefaults.colors(checkedColor = WarmOrange)
                                            )
                                            Spacer(modifier = Modifier.width(8.dp))
                                            Column {
                                                Text(
                                                    text = item.ingredientName,
                                                    fontSize = 14.sp,
                                                    fontWeight = FontWeight.Medium,
                                                    textDecoration = if (item.isChecked) TextDecoration.LineThrough else TextDecoration.None,
                                                    color = if (item.isChecked) MaterialTheme.colorScheme.onSurfaceVariant else MaterialTheme.colorScheme.onSurface
                                                )
                                                if (!item.recipeSource.isNullOrBlank()) {
                                                    Text(
                                                        text = "For ${item.recipeSource}",
                                                        fontSize = 11.sp,
                                                        color = MaterialTheme.colorScheme.primary
                                                    )
                                                }
                                            }
                                        }

                                        IconButton(onClick = { viewModel.deleteShoppingItem(item) }) {
                                            Icon(
                                                imageVector = Icons.Default.Delete,
                                                contentDescription = "Delete",
                                                tint = MaterialTheme.colorScheme.onSurfaceVariant,
                                                modifier = Modifier.size(20.dp)
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
