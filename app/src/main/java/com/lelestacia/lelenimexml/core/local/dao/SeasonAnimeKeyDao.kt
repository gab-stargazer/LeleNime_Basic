package com.lelestacia.lelenimexml.core.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.lelestacia.lelenimexml.core.local.model.SeasonAnimeKeyEntity

@Dao
interface SeasonAnimeKeyDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrReplaceKey(seasonAnimeKeyEntity: List<SeasonAnimeKeyEntity>)

    @Query("SELECT * FROM season_anime_key WHERE id = :id")
    suspend fun seasonAnimeRemoteKey(id: Int): SeasonAnimeKeyEntity

    @Query("DELETE FROM season_anime_key")
    suspend fun clearSeasonAnimeKey()
}