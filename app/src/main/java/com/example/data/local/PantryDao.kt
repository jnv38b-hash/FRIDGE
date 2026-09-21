package com.example.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.data.model.PantryItemEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface PantryDao {
    @Query("SELECT * FROM pantry_items ORDER BY expiryTimestamp ASC, ingredientName ASC")
    fun getAllPantryItems(): Flow<List<PantryItemEntity>>

    @Query("SELECT * FROM pantry_items WHERE id = :id LIMIT 1")
    suspend fun getPantryItemById(id: Long): PantryItemEntity?

    @Query("SELECT * FROM pantry_items WHERE ingredientId = :ingredientId LIMIT 1")
    suspend fun getPantryItemByIngredientId(ingredientId: String): PantryItemEntity?

    @Query("SELECT * FROM pantry_items WHERE expiryTimestamp IS NOT NULL AND expiryTimestamp <= :thresholdTimestamp ORDER BY expiryTimestamp ASC")
    fun getExpiringSoonItems(thresholdTimestamp: Long): Flow<List<PantryItemEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPantryItem(item: PantryItemEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPantryItems(items: List<PantryItemEntity>)

    @Update
    suspend fun updatePantryItem(item: PantryItemEntity)

    @Delete
    suspend fun deletePantryItem(item: PantryItemEntity)

    @Query("DELETE FROM pantry_items WHERE id = :id")
    suspend fun deletePantryItemById(id: Long)
}
