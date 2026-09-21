package com.example.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

enum class PantryStatus(val label: String) {
    AVAILABLE("Available"),
    RUNNING_LOW("Running low"),
    EXPIRING_SOON("Expiring soon")
}

@Entity(tableName = "pantry_items")
data class PantryItemEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val ingredientId: String,
    val ingredientName: String,
    val category: String,
    val emoji: String,
    val quantity: Double,
    val unit: String,
    val expiryTimestamp: Long? = null,
    val status: String = PantryStatus.AVAILABLE.name,
    val notes: String = ""
) {
    fun isExpiringWithinDays(days: Int = 3): Boolean {
        if (expiryTimestamp == null) return false
        val now = System.currentTimeMillis()
        val threshold = now + (days * 24 * 60 * 60 * 1000L)
        return expiryTimestamp in now..threshold
    }

    fun isExpired(): Boolean {
        if (expiryTimestamp == null) return false
        return expiryTimestamp < System.currentTimeMillis()
    }
}
