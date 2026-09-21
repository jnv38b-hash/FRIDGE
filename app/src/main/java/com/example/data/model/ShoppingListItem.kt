package com.example.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "shopping_list")
data class ShoppingListItemEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val ingredientName: String,
    val category: String = "Pantry Staples",
    val quantity: String = "1",
    val unit: String = "item",
    val isChecked: Boolean = false,
    val recipeSource: String = ""
)
