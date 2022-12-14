package com.lelestacia.lelenimexml.core.source.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.lelestacia.lelenimexml.core.model.local.AnimeEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface AnimeDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrUpdateAnime(anime: AnimeEntity)

    @Query("SELECT * FROM anime_table ORDER BY lastViewed DESC LIMIT 30")
    fun getAllAnime(): Flow<List<AnimeEntity>>
}