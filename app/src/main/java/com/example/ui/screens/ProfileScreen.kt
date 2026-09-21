package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
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
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Kitchen
import androidx.compose.material.icons.filled.Sync
import androidx.compose.material.icons.rounded.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.data.model.Allergen
import com.example.data.model.DietaryRestriction
import com.example.data.model.IngredientCategory
import com.example.data.model.KitchenEquipment
import com.example.data.model.PantryItemEntity
import com.example.ui.components.EmptyStateView
import com.example.ui.components.SectionHeader
import com.example.ui.theme.AmberWarning
import com.example.ui.theme.FreshHerbGreen
import com.example.ui.theme.WarmOrange
import com.example.ui.viewmodel.FridgeChefViewModel

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun ProfileScreen(
    viewModel: FridgeChefViewModel,
    onNavigateToResults: () -> Unit,
    modifier: Modifier = Modifier
) {
    val pantryItems by viewModel.pantryItems.collectAsStateWithLifecycle()
    val preferences by viewModel.userPreferences.collectAsStateWithLifecycle()

    var showAddPantryDialog by remember { mutableStateOf(false) }

    val diets = remember {
        listOf("None") + DietaryRestriction.values().map { it.label }
    }
    val allergens = remember {
        Allergen.values().map { it.label }
    }
    val equipments = remember {
        KitchenEquipment.values().map { it.label }
    }

    if (showAddPantryDialog) {
        AddPantryItemDialog(
            onDismiss = { showAddPantryDialog = false },
            onAdd = { name, cat, qty, unit, expDays ->
                viewModel.addPantryItem(name, cat, qty, unit, expDays)
                showAddPantryDialog = false
            }
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("My Kitchen & Settings", fontWeight = FontWeight.Bold) }
            )
        },
        modifier = modifier.testTag("profile_screen")
    ) { innerPadding ->
        LazyColumn(
            contentPadding = PaddingValues(
                top = innerPadding.calculateTopPadding(),
                bottom = 90.dp
            ),
            verticalArrangement = Arrangement.spacedBy(18.dp),
            modifier = Modifier.fillMaxSize()
        ) {
            // Pantry Manager Section Header
            item {
                Column(modifier = Modifier.padding(horizontal = 16.dp)) {
                    SectionHeader(
                        title = "Pantry Inventory (${pantryItems.size})",
                        subtitle = "Track what you have and expiry alerts",
                        actionText = "+ Add Item",
                        onActionClick = { showAddPantryDialog = true }
                    )
                    Spacer(modifier = Modifier.height(10.dp))

                    if (pantryItems.isNotEmpty()) {
                        Button(
                            onClick = {
                                viewModel.loadIngredientsFromPantry()
                                onNavigateToResults()
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = WarmOrange),
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier.fillMaxWidth().height(46.dp)
                        ) {
                            Icon(imageVector = Icons.Default.Sync, contentDescription = null, modifier = Modifier.size(18.dp))
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Cook with Everything in Pantry (${pantryItems.size} items)")
                        }
                    }
                }
            }

            // Pantry Items List
            if (pantryItems.isEmpty()) {
                item {
                    EmptyStateView(
                        emoji = "🥫",
                        title = "Pantry is empty",
                        message = "Add staple items from your fridge to get personalized recipe suggestions automatically.",
                        actionLabel = "+ Add Pantry Item",
                        onActionClick = { showAddPantryDialog = true }
                    )
                }
            } else {
                items(pantryItems) { item ->
                    val isExpiring = item.isExpiringWithinDays(3)
                    Card(
                        shape = RoundedCornerShape(14.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween,
                            modifier = Modifier.padding(14.dp)
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.weight(1f)) {
                                Text(text = item.emoji, fontSize = 24.sp)
                                Spacer(modifier = Modifier.width(12.dp))
                                Column {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Text(
                                            text = item.ingredientName,
                                            fontSize = 15.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = MaterialTheme.colorScheme.onSurface
                                        )
                                        if (isExpiring) {
                                            Spacer(modifier = Modifier.width(6.dp))
                                            Surface(
                                                shape = RoundedCornerShape(6.dp),
                                                color = AmberWarning.copy(alpha = 0.15f)
                                            ) {
                                                Text(
                                                    text = "Expiring Soon",
                                                    fontSize = 10.sp,
                                                    color = AmberWarning,
                                                    fontWeight = FontWeight.Bold,
                                                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                                )
                                            }
                                        }
                                    }
                                    Spacer(modifier = Modifier.height(2.dp))
                                    Text(
                                        text = "${item.quantity.toInt()} ${item.unit} • ${item.category}",
                                        fontSize = 12.sp,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }
                            }

                            IconButton(onClick = { viewModel.deletePantryItem(item) }) {
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

            // Dietary Preferences Section
            item {
                Column(modifier = Modifier.padding(horizontal = 16.dp)) {
                    SectionHeader(
                        title = "Dietary Preference",
                        subtitle = "Filtered automatically across all recipes"
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    FlowRow(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        diets.forEach { diet ->
                            val isSelected = (preferences.selectedDiet == null && diet == "None") ||
                                    (preferences.selectedDiet.equals(diet, ignoreCase = true))
                            FilterChip(
                                selected = isSelected,
                                onClick = {
                                    viewModel.setDietaryPreference(if (diet == "None") null else diet)
                                },
                                label = { Text(diet) },
                                colors = FilterChipDefaults.filterChipColors(
                                    selectedContainerColor = MaterialTheme.colorScheme.primaryContainer,
                                    selectedLabelColor = MaterialTheme.colorScheme.onPrimaryContainer
                                )
                            )
                        }
                    }
                }
            }

            // Strict Allergens Section
            item {
                Column(modifier = Modifier.padding(horizontal = 16.dp)) {
                    SectionHeader(
                        title = "Strict Allergens (Strict Exclusion)",
                        subtitle = "Recipes containing these will never be suggested"
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    FlowRow(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        allergens.forEach { allergen ->
                            val isExcluded = preferences.excludedAllergens.contains(allergen)
                            FilterChip(
                                selected = isExcluded,
                                onClick = { viewModel.toggleAllergen(allergen) },
                                label = {
                                    Text(if (isExcluded) "🚫 $allergen" else allergen)
                                },
                                colors = FilterChipDefaults.filterChipColors(
                                    selectedContainerColor = MaterialTheme.colorScheme.errorContainer,
                                    selectedLabelColor = MaterialTheme.colorScheme.onErrorContainer
                                )
                            )
                        }
                    }
                }
            }

            // Available Kitchen Equipment Section
            item {
                Column(modifier = Modifier.padding(horizontal = 16.dp)) {
                    SectionHeader(
                        title = "My Kitchen Equipment",
                        subtitle = "Select what equipment you have"
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    FlowRow(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        equipments.forEach { equipment ->
                            val isSelected = preferences.availableEquipment.contains(equipment)
                            FilterChip(
                                selected = isSelected,
                                onClick = { viewModel.toggleEquipment(equipment) },
                                label = { Text(equipment) },
                                colors = FilterChipDefaults.filterChipColors(
                                    selectedContainerColor = MaterialTheme.colorScheme.secondaryContainer,
                                    selectedLabelColor = MaterialTheme.colorScheme.onSecondaryContainer
                                )
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun AddPantryItemDialog(
    onDismiss: () -> Unit,
    onAdd: (name: String, category: String, quantity: Double, unit: String, expiryDays: Int?) -> Unit
) {
    var name by remember { mutableStateOf("") }
    var category by remember { mutableStateOf(IngredientCategory.VEGETABLES.displayName) }
    var quantityText by remember { mutableStateOf("2") }
    var unit by remember { mutableStateOf("pcs") }
    var expiryDaysText by remember { mutableStateOf("4") }

    Dialog(onDismissRequest = onDismiss) {
        Card(
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(20.dp)) {
                Text(
                    text = "Add to My Pantry",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Spacer(modifier = Modifier.height(14.dp))

                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Item Name") },
                    placeholder = { Text("e.g. Tomato, Milk, Eggs, Bread") },
                    singleLine = true,
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(10.dp))

                Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                    OutlinedTextField(
                        value = quantityText,
                        onValueChange = { quantityText = it },
                        label = { Text("Quantity") },
                        singleLine = true,
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.weight(1f)
                    )
                    OutlinedTextField(
                        value = unit,
                        onValueChange = { unit = it },
                        label = { Text("Unit") },
                        singleLine = true,
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.weight(1f)
                    )
                }

                Spacer(modifier = Modifier.height(10.dp))

                OutlinedTextField(
                    value = expiryDaysText,
                    onValueChange = { expiryDaysText = it },
                    label = { Text("Expires in (Days)") },
                    placeholder = { Text("e.g. 2 for expiring soon alert") },
                    singleLine = true,
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(18.dp))

                Row(
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    OutlinedButton(
                        onClick = onDismiss,
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("Cancel")
                    }

                    Button(
                        onClick = {
                            if (name.isNotBlank()) {
                                val qty = quantityText.toDoubleOrNull() ?: 1.0
                                val expDays = expiryDaysText.toIntOrNull()
                                onAdd(name.trim(), category, qty, unit.trim().ifBlank { "pcs" }, expDays)
                            }
                        },
                        enabled = name.isNotBlank(),
                        colors = ButtonDefaults.buttonColors(containerColor = WarmOrange),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("Add")
                    }
                }
            }
        }
    }
}
