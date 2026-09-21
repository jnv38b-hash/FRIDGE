package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.Casino
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.HourglassEmpty
import androidx.compose.material.icons.filled.Kitchen
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.rounded.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.data.model.RecipeMatch
import com.example.ui.components.RecipeCard
import com.example.ui.components.ScanFridgeDialog
import com.example.ui.components.SectionHeader
import com.example.ui.components.SurpriseMeDialog
import com.example.ui.theme.AmberWarning
import com.example.ui.theme.FreshHerbGreen
import com.example.ui.theme.WarmOrange
import com.example.ui.theme.WarmOrangeVariant
import com.example.ui.viewmodel.FridgeChefViewModel

data class QuickModeItem(
    val title: String,
    val emoji: String,
    val subtitle: String
)

@Composable
fun HomeScreen(
    viewModel: FridgeChefViewModel,
    onNavigateToIngredients: () -> Unit,
    onNavigateToResults: () -> Unit,
    onNavigateToRecipeDetail: (String) -> Unit,
    onNavigateToCookingMode: (String) -> Unit,
    onNavigateToPantry: () -> Unit,
    modifier: Modifier = Modifier
) {
    val selectedIngredients by viewModel.selectedIngredients.collectAsStateWithLifecycle()
    val recipeMatches by viewModel.recipeMatches.collectAsStateWithLifecycle()
    val expiringItems by viewModel.expiringIngredients.collectAsStateWithLifecycle()
    val activeQuickMode by viewModel.activeQuickMode.collectAsStateWithLifecycle()
    val cookingHistory by viewModel.cookingHistory.collectAsStateWithLifecycle()

    var showSurpriseDialog by remember { mutableStateOf(false) }
    var showScanDialog by remember { mutableStateOf(false) }

    val quickModes = remember {
        listOf(
            QuickModeItem("Surprise Me", "🎲", "Pick for me"),
            QuickModeItem("Under 15 Minutes", "⚡", "Fast meals"),
            QuickModeItem("Empty My Fridge", "🧹", "Use everything"),
            QuickModeItem("Budget Meal", "💰", "Simple & cheap"),
            QuickModeItem("Healthy", "🥗", "Nutritious & light"),
            QuickModeItem("Spicy", "🌶️", "Bold Indian flavors"),
            QuickModeItem("High Protein", "🍳", "15g+ protein"),
            QuickModeItem("Explore Cuisines", "🌍", "Global dishes")
        )
    }

    if (showSurpriseDialog) {
        SurpriseMeDialog(
            candidateMatches = recipeMatches,
            selectedIngredients = selectedIngredients,
            onDismiss = { showSurpriseDialog = false },
            onViewRecipe = { id ->
                showSurpriseDialog = false
                onNavigateToRecipeDetail(id)
            },
            onStartCooking = { id ->
                showSurpriseDialog = false
                onNavigateToCookingMode(id)
            }
        )
    }

    if (showScanDialog) {
        ScanFridgeDialog(
            onDismiss = { showScanDialog = false },
            onAddDetectedIngredients = { items ->
                items.forEach { viewModel.toggleIngredientSelection(it) }
            }
        )
    }

    LazyColumn(
        contentPadding = PaddingValues(bottom = 90.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        modifier = modifier
            .fillMaxSize()
            .testTag("home_screen")
    ) {
        // Hero Card: "What's in your fridge?"
        item {
            Card(
                shape = RoundedCornerShape(bottomStart = 28.dp, bottomEnd = 28.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surface
                ),
                elevation = CardDefaults.cardElevation(defaultElevation = 3.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(
                                    text = "FridgeChef",
                                    fontSize = 24.sp,
                                    fontWeight = FontWeight.ExtraBold,
                                    color = MaterialTheme.colorScheme.primary
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(text = "🍳", fontSize = 22.sp)
                            }
                            Text(
                                text = "Cook with what you have",
                                fontSize = 13.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }

                        // Surprise Me mini FAB
                        Surface(
                            shape = CircleShape,
                            color = MaterialTheme.colorScheme.primaryContainer,
                            modifier = Modifier
                                .size(44.dp)
                                .clickable { showSurpriseDialog = true }
                                .testTag("home_surprise_fab")
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Text(text = "🎲", fontSize = 20.sp)
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(18.dp))

                    Text(
                        text = "What's in your fridge?",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Tell us what you have. We'll figure out what you can cook.",
                        fontSize = 14.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    // Action Buttons: + Add Ingredients & 📷 Scan Fridge
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(10.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Button(
                            onClick = onNavigateToIngredients,
                            colors = ButtonDefaults.buttonColors(containerColor = WarmOrange),
                            shape = RoundedCornerShape(14.dp),
                            modifier = Modifier
                                .weight(1.2f)
                                .height(48.dp)
                                .testTag("add_ingredients_button")
                        ) {
                            Icon(imageVector = Icons.Default.Add, contentDescription = null)
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("+ Add Ingredients", fontWeight = FontWeight.SemiBold)
                        }

                        OutlinedButton(
                            onClick = { showScanDialog = true },
                            shape = RoundedCornerShape(14.dp),
                            modifier = Modifier
                                .weight(1f)
                                .height(48.dp)
                                .testTag("scan_fridge_button")
                        ) {
                            Icon(imageVector = Icons.Default.CameraAlt, contentDescription = null)
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("Scan Fridge", fontWeight = FontWeight.SemiBold)
                        }
                    }

                    // Currently selected ingredients horizontal chip bar
                    if (selectedIngredients.isNotEmpty()) {
                        Spacer(modifier = Modifier.height(16.dp))
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(
                                text = "Selected (${selectedIngredients.size}):",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            Text(
                                text = "Clear All",
                                fontSize = 12.sp,
                                color = MaterialTheme.colorScheme.error,
                                fontWeight = FontWeight.Medium,
                                modifier = Modifier.clickable { viewModel.clearSelectedIngredients() }
                            )
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        LazyRow(
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            items(selectedIngredients) { ingredientName ->
                                Surface(
                                    shape = RoundedCornerShape(12.dp),
                                    color = MaterialTheme.colorScheme.surfaceVariant,
                                    modifier = Modifier.clip(RoundedCornerShape(12.dp))
                                ) {
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
                                    ) {
                                        Text(text = ingredientName, fontSize = 13.sp, fontWeight = FontWeight.Medium)
                                        Spacer(modifier = Modifier.width(6.dp))
                                        Icon(
                                            imageVector = Icons.Default.Close,
                                            contentDescription = "Remove",
                                            tint = MaterialTheme.colorScheme.onSurfaceVariant,
                                            modifier = Modifier
                                                .size(14.dp)
                                                .clickable { viewModel.removeSelectedIngredient(ingredientName) }
                                        )
                                    }
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(14.dp))

                        // Find Recipes Call-to-action button
                        Button(
                            onClick = onNavigateToResults,
                            colors = ButtonDefaults.buttonColors(
                                containerColor = FreshHerbGreen
                            ),
                            shape = RoundedCornerShape(14.dp),
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(46.dp)
                                .testTag("find_recipes_cta")
                        ) {
                            Text(
                                text = "Find Recipes (${recipeMatches.size} Available)",
                                fontWeight = FontWeight.Bold
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Icon(imageVector = Icons.Default.ArrowForward, contentDescription = null)
                        }
                    }
                }
            }
        }

        // "Use It Before It Expires" Alert Banner
        if (expiringItems.isNotEmpty()) {
            item {
                Card(
                    onClick = onNavigateToPantry,
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = AmberWarning.copy(alpha = 0.12f)
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(16.dp)
                    ) {
                        Text(text = "🍅", fontSize = 28.sp)
                        Spacer(modifier = Modifier.width(12.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = "Use It Before It Expires!",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold,
                                color = AmberWarning
                            )
                            Text(
                                text = "${expiringItems.first().ingredientName} & ${expiringItems.size} items expire soon. Check matching recipes!",
                                fontSize = 12.sp,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        }
                        Icon(
                            imageVector = Icons.Default.ArrowForward,
                            contentDescription = null,
                            tint = AmberWarning,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }
            }
        }

        // Quick Modes Grid
        item {
            Column(modifier = Modifier.padding(horizontal = 16.dp)) {
                SectionHeader(
                    title = "Quick Modes",
                    subtitle = "Filter by mood, time, and goal"
                )
                Spacer(modifier = Modifier.height(12.dp))

                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    items(quickModes) { mode ->
                        val isSelected = activeQuickMode == mode.title
                        Card(
                            onClick = {
                                if (mode.title == "Surprise Me") {
                                    showSurpriseDialog = true
                                } else {
                                    viewModel.setQuickMode(mode.title)
                                }
                            },
                            shape = RoundedCornerShape(16.dp),
                            colors = CardDefaults.cardColors(
                                containerColor = if (isSelected) MaterialTheme.colorScheme.primaryContainer
                                else MaterialTheme.colorScheme.surface
                            ),
                            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                            modifier = Modifier
                                .width(135.dp)
                                .height(115.dp)
                                .testTag("quick_mode_${mode.title}")
                        ) {
                            Column(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(12.dp),
                                verticalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(text = mode.emoji, fontSize = 26.sp)
                                Column {
                                    Text(
                                        text = mode.title,
                                        fontSize = 13.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = if (isSelected) MaterialTheme.colorScheme.onPrimaryContainer else MaterialTheme.colorScheme.onSurface,
                                        maxLines = 1
                                    )
                                    Text(
                                        text = mode.subtitle,
                                        fontSize = 11.sp,
                                        color = if (isSelected) MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.8f) else MaterialTheme.colorScheme.onSurfaceVariant,
                                        maxLines = 1
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }

        // Top Matched Recipes section
        item {
            Column(modifier = Modifier.padding(horizontal = 16.dp)) {
                SectionHeader(
                    title = if (activeQuickMode != null) "$activeQuickMode Recipes" else "What You Can Make Right Now",
                    subtitle = "Ranked by available ingredients",
                    actionText = "See All (${recipeMatches.size})",
                    onActionClick = onNavigateToResults
                )
            }
        }

        items(recipeMatches.take(4)) { match ->
            RecipeCard(
                match = match,
                isFavorite = viewModel.isFavorite(match.recipe.id),
                onFavoriteClick = { viewModel.toggleFavorite(match.recipe.id) },
                onClick = { onNavigateToRecipeDetail(match.recipe.id) },
                modifier = Modifier.padding(horizontal = 16.dp)
            )
        }

        // Recently Cooked section if any exists
        if (cookingHistory.isNotEmpty()) {
            item {
                Column(modifier = Modifier.padding(horizontal = 16.dp)) {
                    Spacer(modifier = Modifier.height(8.dp))
                    SectionHeader(
                        title = "Recently Cooked",
                        subtitle = "Quickly revisit meals you enjoyed"
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    LazyRow(
                        horizontalArrangement = Arrangement.spacedBy(10.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        items(cookingHistory) { item ->
                            Card(
                                onClick = { onNavigateToRecipeDetail(item.recipeId) },
                                shape = RoundedCornerShape(14.dp),
                                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                                modifier = Modifier.width(180.dp)
                            ) {
                                Column(modifier = Modifier.padding(12.dp)) {
                                    Text(text = "🍳 Cooked ${item.timesCooked}x", fontSize = 11.sp, color = WarmOrange, fontWeight = FontWeight.SemiBold)
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Text(text = item.recipeName, fontSize = 14.sp, fontWeight = FontWeight.Bold, maxLines = 1)
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
