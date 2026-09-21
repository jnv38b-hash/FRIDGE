package com.example.ui.components

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import coil.compose.AsyncImage
import com.example.data.model.RecipeMatch
import com.example.ui.theme.FreshHerbGreen
import com.example.ui.theme.WarmOrange
import kotlinx.coroutines.delay

@Composable
fun SurpriseMeDialog(
    candidateMatches: List<RecipeMatch>,
    selectedIngredients: List<String>,
    onDismiss: () -> Unit,
    onViewRecipe: (String) -> Unit,
    onStartCooking: (String) -> Unit
) {
    var isSpinning by remember { mutableStateOf(true) }
    var currentIndex by remember { mutableIntStateOf(0) }
    val eligibleMatches = remember(candidateMatches) {
        if (candidateMatches.isNotEmpty()) candidateMatches.take(8) else emptyList()
    }

    // Animation rapid cycling
    LaunchedEffect(eligibleMatches) {
        if (eligibleMatches.isEmpty()) {
            isSpinning = false
            return@LaunchedEffect
        }
        var delayMs = 60L
        for (i in 0..18) {
            currentIndex = (currentIndex + 1) % eligibleMatches.size
            delay(delayMs)
            delayMs += 15L // progressively decelerate
        }
        // Pick best eligible match or highest match
        val bestIndex = eligibleMatches.indices.maxByOrNull { eligibleMatches[it].matchScore } ?: 0
        currentIndex = bestIndex
        isSpinning = false
    }

    val currentMatch = eligibleMatches.getOrNull(currentIndex)

    Dialog(onDismissRequest = onDismiss) {
        Card(
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
                .testTag("surprise_me_dialog")
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.padding(20.dp)
            ) {
                // Header
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(text = "🎲", fontSize = 28.sp)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = if (isSpinning) "Chef is Deciding..." else "Your Surprise Recipe!",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }

                Spacer(modifier = Modifier.height(6.dp))

                Text(
                    text = if (isSpinning) "Finding delicious recipes with your ingredients..."
                    else "Specially selected based on what you have right now!",
                    fontSize = 13.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(16.dp))

                if (currentMatch != null) {
                    AnimatedContent(
                        targetState = currentMatch,
                        transitionSpec = {
                            fadeIn(animationSpec = tween(120)) togetherWith fadeOut(animationSpec = tween(120))
                        },
                        label = "slot_animation"
                    ) { match ->
                        val recipe = match.recipe
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(160.dp)
                                    .clip(RoundedCornerShape(16.dp))
                            ) {
                                AsyncImage(
                                    model = recipe.imageUrl,
                                    contentDescription = recipe.name,
                                    contentScale = ContentScale.Crop,
                                    modifier = Modifier.fillMaxWidth()
                                )

                                Surface(
                                    shape = RoundedCornerShape(8.dp),
                                    color = FreshHerbGreen.copy(alpha = 0.95f),
                                    contentColor = Color.White,
                                    modifier = Modifier
                                        .padding(10.dp)
                                        .align(Alignment.TopStart)
                                ) {
                                    Text(
                                        text = "${match.matchPercentage}% Match",
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Bold,
                                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(12.dp))

                            Text(
                                text = recipe.name,
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSurface,
                                textAlign = TextAlign.Center
                            )

                            Spacer(modifier = Modifier.height(4.dp))

                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.Center
                            ) {
                                Text(
                                    text = "⏱ ${recipe.totalTimeMinutes} min",
                                    fontSize = 12.sp,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                                Spacer(modifier = Modifier.width(10.dp))
                                Text(
                                    text = "🔥 ${recipe.calories} kcal",
                                    fontSize = 12.sp,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                                Spacer(modifier = Modifier.width(10.dp))
                                Text(
                                    text = "👨‍🍳 ${recipe.difficulty}",
                                    fontSize = 12.sp,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }
                    }
                } else {
                    CircularProgressIndicator(
                        color = WarmOrange,
                        modifier = Modifier.padding(32.dp)
                    )
                }

                Spacer(modifier = Modifier.height(20.dp))

                if (isSpinning) {
                    CircularProgressIndicator(
                        color = WarmOrange,
                        strokeWidth = 3.dp,
                        modifier = Modifier.size(24.dp)
                    )
                } else if (currentMatch != null) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        OutlinedButton(
                            onClick = { onViewRecipe(currentMatch.recipe.id) },
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier.weight(1f)
                        ) {
                            Text("View Details")
                        }

                        Button(
                            onClick = { onStartCooking(currentMatch.recipe.id) },
                            colors = ButtonDefaults.buttonColors(containerColor = WarmOrange),
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier.weight(1f)
                        ) {
                            Icon(imageVector = Icons.Default.PlayArrow, contentDescription = null)
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("Start Cooking")
                        }
                    }
                }
            }
        }
    }
}
