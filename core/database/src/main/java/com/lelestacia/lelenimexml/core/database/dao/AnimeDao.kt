package com.lelestacia.lelenimexml.core.database.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.lelestacia.lelenimexml.core.model.database.AnimeEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface AnimeDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrUpdateAnime(anime: AnimeEntity)

    @Query("SELECT * FROM anime_table ORDER BY lastViewed DESC")
    fun getAllAnimeHistory(): PagingSource<Int, AnimeEntity>

    @Query("SELECT * FROM anime_table WHERE malId = :animeId")
    fun getNewestAnimeDataByAnimeId(animeId: Int): Flow<AnimeEntity>

    @Query("SELECT * FROM anime_table WHERE malId =:animeId")
    fun getAnimeByAnimeId(animeId: Int): AnimeEntity?

    @Query("SELECT * FROM anime_table WHERE isFavorite = 1")
    fun getAllFavoriteAnime(): PagingSource<Int, AnimeEntity>

    @Update
    suspend fun updateAnime(anime: AnimeEntity)
}
