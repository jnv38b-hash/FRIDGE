package com.example.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.data.model.CookingHistoryEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface CookingHistoryDao {
    @Query("SELECT * FROM cooking_history ORDER BY lastCookedAt DESC")
    fun getAllHistory(): Flow<List<CookingHistoryEntity>>

    @Query("SELECT * FROM cooking_history WHERE recipeId = :recipeId LIMIT 1")
    suspend fun getHistoryItem(recipeId: String): CookingHistoryEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrUpdate(item: CookingHistoryEntity)

    suspend fun recordCooking(recipeId: String, recipeName: String) {
        val existing = getHistoryItem(recipeId)
        if (existing != null) {
            insertOrUpdate(
                existing.copy(
                    lastCookedAt = System.currentTimeMillis(),
                    timesCooked = existing.timesCooked + 1
                )
            )
        } else {
            insertOrUpdate(
                CookingHistoryEntity(
                    recipeId = recipeId,
                    recipeName = recipeName,
                    lastCookedAt = System.currentTimeMillis(),
                    timesCooked = 1
                )
            )
        }
    }
}
