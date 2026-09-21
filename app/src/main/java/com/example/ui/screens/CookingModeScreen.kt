package com.example.ui.screens

import android.content.Context
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FormatListBulleted
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.ui.theme.FreshHerbGreen
import com.example.ui.theme.WarmOrange
import com.example.ui.viewmodel.FridgeChefViewModel
import kotlinx.coroutines.delay

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CookingModeScreen(
    recipeId: String,
    viewModel: FridgeChefViewModel,
    onNavigateBack: () -> Unit,
    onNavigateHome: () -> Unit,
    onNavigateToShopping: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val allRecipes by viewModel.allRecipes.collectAsStateWithLifecycle()
    val recipeWithIngredients = allRecipes.firstOrNull { it.recipe.id == recipeId }

    if (recipeWithIngredients == null) {
        Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize()) {
            Text("Recipe not found")
        }
        return
    }

    val recipe = recipeWithIngredients.recipe
    val ingredients = recipeWithIngredients.ingredients
    val steps = remember(recipe) { recipe.getStepsList() }
    val stepTimers = remember(recipe) { recipe.getTimersList() }

    var currentStepIndex by remember { mutableIntStateOf(0) }
    var isCompleted by remember { mutableStateOf(false) }
    var showIngredientsSheet by remember { mutableStateOf(false) }

    // Timer State for current step
    val currentTimerMinutes = stepTimers.getOrElse(currentStepIndex) { 0 }
    var timerRunning by remember { mutableStateOf(false) }
    var secondsRemaining by remember(currentStepIndex) {
        mutableLongStateOf(currentTimerMinutes * 60L)
    }

    // Countdown effect
    LaunchedEffect(timerRunning, secondsRemaining, currentStepIndex) {
        if (timerRunning && secondsRemaining > 0) {
            delay(1000L)
            secondsRemaining--
            if (secondsRemaining == 0L) {
                timerRunning = false
                // Vibrate
                try {
                    val vibrator = context.getSystemService(Context.VIBRATOR_SERVICE) as? Vibrator
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        vibrator?.vibrate(VibrationEffect.createWaveform(longArrayOf(0, 300, 200, 300), -1))
                    } else {
                        @Suppress("DEPRECATION")
                        vibrator?.vibrate(500)
                    }
                } catch (e: Exception) {
                    // Ignore vibration failure
                }
            }
        }
    }

    // Modal Sheet for Ingredients Reference
    if (showIngredientsSheet) {
        ModalBottomSheet(
            onDismissRequest = { showIngredientsSheet = false },
            sheetState = rememberModalBottomSheetState()
        ) {
            Column(modifier = Modifier.padding(20.dp)) {
                Text(
                    text = "Ingredients Reference",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Spacer(modifier = Modifier.height(12.dp))
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    items(ingredients) { ing ->
                        Row(
                            horizontalArrangement = Arrangement.SpaceBetween,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(text = ing.ingredientName, fontSize = 14.sp, fontWeight = FontWeight.Medium)
                            Text(text = "${ing.quantity} ${ing.unit}", fontSize = 14.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        }
                    }
                }
                Spacer(modifier = Modifier.height(24.dp))
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text(recipe.name, fontSize = 16.sp, fontWeight = FontWeight.Bold, maxLines = 1)
                        if (!isCompleted) {
                            Text(
                                text = "Step ${currentStepIndex + 1} of ${steps.size}",
                                fontSize = 12.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(imageVector = Icons.Default.Close, contentDescription = "Exit Cooking Mode")
                    }
                },
                actions = {
                    if (!isCompleted) {
                        IconButton(onClick = { showIngredientsSheet = true }) {
                            Icon(imageVector = Icons.Default.FormatListBulleted, contentDescription = "Ingredients")
                        }
                    }
                }
            )
        },
        modifier = modifier.testTag("cooking_mode_screen")
    ) { innerPadding ->
        if (isCompleted) {
            // Completion Celebration View
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .padding(24.dp)
            ) {
                Text(text = "🎉", fontSize = 64.sp)
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = "Nice! You Cooked It!",
                    fontSize = 26.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = MaterialTheme.colorScheme.onSurface,
                    textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "${recipe.name} is ready to serve. Great job making the most of your pantry!",
                    fontSize = 15.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(28.dp))

                val isFav = viewModel.isFavorite(recipe.id)
                OutlinedButton(
                    onClick = { viewModel.toggleFavorite(recipe.id) },
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Icon(
                        imageVector = if (isFav) Icons.Filled.Favorite else Icons.Outlined.FavoriteBorder,
                        contentDescription = null,
                        tint = if (isFav) WarmOrange else MaterialTheme.colorScheme.onSurface
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(if (isFav) "Saved in Favorites" else "Save to Favorites")
                }

                Spacer(modifier = Modifier.height(12.dp))

                Button(
                    onClick = onNavigateHome,
                    colors = ButtonDefaults.buttonColors(containerColor = WarmOrange),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.fillMaxWidth().height(48.dp)
                ) {
                    Text("Cook Something Else", fontWeight = FontWeight.Bold)
                }

                Spacer(modifier = Modifier.height(12.dp))

                OutlinedButton(
                    onClick = onNavigateToShopping,
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("View Shopping List")
                }
            }
        } else {
            // Active Step View
            val progress by animateFloatAsState(
                targetValue = (currentStepIndex + 1).toFloat() / steps.size.toFloat(),
                label = "cooking_progress"
            )

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .padding(horizontal = 20.dp, vertical = 12.dp),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                // Top Progress Indicator
                Column {
                    LinearProgressIndicator(
                        progress = { progress },
                        color = WarmOrange,
                        trackColor = MaterialTheme.colorScheme.surfaceVariant,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(8.dp)
                            .clip(RoundedCornerShape(4.dp))
                    )
                    Spacer(modifier = Modifier.height(24.dp))

                    // Step badge
                    Surface(
                        shape = RoundedCornerShape(10.dp),
                        color = MaterialTheme.colorScheme.primaryContainer,
                        modifier = Modifier.padding(bottom = 12.dp)
                    ) {
                        Text(
                            text = "STEP ${currentStepIndex + 1} OF ${steps.size}",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onPrimaryContainer,
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
                        )
                    }

                    // Large Instruction Text
                    Text(
                        text = steps.getOrElse(currentStepIndex) { "" },
                        fontSize = 24.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.colorScheme.onSurface,
                        lineHeight = 36.sp,
                        modifier = Modifier.testTag("step_instruction_text")
                    )

                    Spacer(modifier = Modifier.height(24.dp))

                    // Optional Step Timer Card
                    if (currentTimerMinutes > 0) {
                        val minutes = secondsRemaining / 60
                        val secs = secondsRemaining % 60
                        val timeFormatted = String.format("%02d:%02d", minutes, secs)

                        Card(
                            shape = RoundedCornerShape(18.dp),
                            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.6f)),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween,
                                modifier = Modifier.padding(16.dp)
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(
                                        imageVector = Icons.Default.Timer,
                                        contentDescription = null,
                                        tint = WarmOrange,
                                        modifier = Modifier.size(28.dp)
                                    )
                                    Spacer(modifier = Modifier.width(12.dp))
                                    Column {
                                        Text(text = "Step Timer", fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                                        Text(text = timeFormatted, fontSize = 24.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurface)
                                    }
                                }

                                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                    IconButton(
                                        onClick = {
                                            timerRunning = !timerRunning
                                        }
                                    ) {
                                        Icon(
                                            imageVector = if (timerRunning) Icons.Default.Pause else Icons.Default.PlayArrow,
                                            contentDescription = if (timerRunning) "Pause" else "Play",
                                            tint = WarmOrange,
                                            modifier = Modifier.size(28.dp)
                                        )
                                    }

                                    IconButton(
                                        onClick = {
                                            timerRunning = false
                                            secondsRemaining = currentTimerMinutes * 60L
                                        }
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.Refresh,
                                            contentDescription = "Reset",
                                            tint = MaterialTheme.colorScheme.onSurfaceVariant
                                        )
                                    }
                                }
                            }
                        }
                    }
                }

                // Bottom Navigation Buttons
                Column {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(12.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        if (currentStepIndex > 0) {
                            OutlinedButton(
                                onClick = { currentStepIndex-- },
                                shape = RoundedCornerShape(14.dp),
                                modifier = Modifier
                                    .weight(1f)
                                    .height(52.dp)
                            ) {
                                Icon(imageVector = Icons.Default.ArrowBack, contentDescription = null)
                                Spacer(modifier = Modifier.width(6.dp))
                                Text("Previous")
                            }
                        }

                        Button(
                            onClick = {
                                if (currentStepIndex < steps.size - 1) {
                                    currentStepIndex++
                                } else {
                                    // Complete cooking!
                                    viewModel.recordRecipeCooked(recipe.id, recipe.name)
                                    isCompleted = true
                                }
                            },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = if (currentStepIndex == steps.size - 1) FreshHerbGreen else WarmOrange
                            ),
                            shape = RoundedCornerShape(14.dp),
                            modifier = Modifier
                                .weight(if (currentStepIndex > 0) 1.5f else 1f)
                                .height(52.dp)
                                .testTag("next_step_button")
                        ) {
                            Text(
                                text = if (currentStepIndex == steps.size - 1) "Finish Cooking" else "Next Step",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Icon(
                                imageVector = if (currentStepIndex == steps.size - 1) Icons.Default.Check else Icons.Default.ArrowForward,
                                contentDescription = null
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                }
            }
        }
    }
}
