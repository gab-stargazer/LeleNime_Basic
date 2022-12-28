package com.lelestacia.lelenimexml.core.database.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.lelestacia.lelenimexml.core.model.database.SeasonAnimeEntity

@Dao
interface SeasonAnimeDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(seasonAnimeEntity: List<SeasonAnimeEntity>)

    @Query("SELECT * FROM season_anime")
    fun pagingSourceSeasonAnime(): PagingSource<Int, SeasonAnimeEntity>

    @Query("DELETE FROM season_anime")
    suspend fun clearSeasonAnime()
}