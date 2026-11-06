package com.taqiyyaghazi.eventapp.data.local.room

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.taqiyyaghazi.eventapp.data.local.entity.EventEntity

@Dao
interface EventDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFavorite(event: EventEntity)

    @Delete
    suspend fun deleteFavorite(event: EventEntity)

    @Query("SELECT * FROM favorite_event ORDER BY beginTime DESC")
    fun getAllFavoriteEvents(): LiveData<List<EventEntity>>

    @Query("SELECT EXISTS(SELECT 1 FROM favorite_event WHERE id = :id)")
    fun isFavoriteEvent(id: String): LiveData<Boolean>
}
