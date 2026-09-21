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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
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
import com.example.ui.components.EmptyStateView
import com.example.ui.components.RecipeCard
import com.example.ui.theme.WarmOrange
import com.example.ui.viewmodel.FridgeChefViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RecipeResultsScreen(
    viewModel: FridgeChefViewModel,
    onNavigateBack: () -> Unit,
    onNavigateToRecipeDetail: (String) -> Unit,
    onNavigateToIngredients: () -> Unit,
    modifier: Modifier = Modifier
) {
    val recipeMatches by viewModel.recipeMatches.collectAsStateWithLifecycle()
    val selectedIngredients by viewModel.selectedIngredients.collectAsStateWithLifecycle()
    val sortOrder by viewModel.sortOrder.collectAsStateWithLifecycle()
    val selectedCuisine by viewModel.selectedCuisine.collectAsStateWithLifecycle()

    var showSortMenu by remember { mutableStateOf(false) }

    val cuisines = remember { listOf("All", "Indian", "South Indian", "Italian", "Chinese") }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text("Matching Recipes", fontWeight = FontWeight.Bold)
                        Text(
                            text = "${selectedIngredients.size} ingredients selected",
                            fontSize = 12.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    Surface(
                        shape = RoundedCornerShape(10.dp),
                        color = MaterialTheme.colorScheme.surfaceVariant,
                        modifier = Modifier
                            .padding(end = 8.dp)
                            .clickable { showSortMenu = true }
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Tune,
                                contentDescription = "Sort",
                                modifier = Modifier.size(16.dp),
                                tint = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = sortOrder.label,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Medium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }

                        DropdownMenu(
                            expanded = showSortMenu,
                            onDismissRequest = { showSortMenu = false }
                        ) {
                            RecipeSortOrder.values().forEach { order ->
                                DropdownMenuItem(
                                    text = { Text(order.label) },
                                    onClick = {
                                        viewModel.setSortOrder(order)
                                        showSortMenu = false
                                    }
                                )
                            }
                        }
                    }
                }
            )
        },
        modifier = modifier.testTag("recipe_results_screen")
    ) { innerPadding ->
        LazyColumn(
            contentPadding = PaddingValues(top = innerPadding.calculateTopPadding(), bottom = 32.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier.fillMaxSize()
        ) {
            // Filter chips row
            item {
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    contentPadding = PaddingValues(horizontal = 16.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    items(cuisines) { cuisine ->
                        val isSelected = selectedCuisine == cuisine
                        FilterChip(
                            selected = isSelected,
                            onClick = { viewModel.setCuisineFilter(cuisine) },
                            label = { Text(cuisine) },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = MaterialTheme.colorScheme.primaryContainer,
                                selectedLabelColor = MaterialTheme.colorScheme.onPrimaryContainer
                            )
                        )
                    }
                }
            }

            // Results count banner
            item {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                ) {
                    Text(
                        text = "Found ${recipeMatches.size} recipes",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    val perfectCount = recipeMatches.count { it.isPerfectMatch }
                    if (perfectCount > 0) {
                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = WarmOrange.copy(alpha = 0.12f)
                        ) {
                            Text(
                                text = "✨ $perfectCount ready to cook!",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = WarmOrange,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                            )
                        }
                    }
                }
            }

            if (recipeMatches.isEmpty()) {
                item {
                    EmptyStateView(
                        emoji = "🥣",
                        title = "No Matching Recipes Found",
                        message = "Try selecting different ingredients or clearing specific diet/cuisine filters.",
                        actionLabel = "Modify Ingredients",
                        onActionClick = onNavigateToIngredients
                    )
                }
            } else {
                items(recipeMatches) { match ->
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
