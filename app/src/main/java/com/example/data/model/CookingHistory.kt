package com.example.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "cooking_history")
data class CookingHistoryEntity(
    @PrimaryKey val recipeId: String,
    val recipeName: String,
    val lastCookedAt: Long = System.currentTimeMillis(),
    val timesCooked: Int = 1
)
