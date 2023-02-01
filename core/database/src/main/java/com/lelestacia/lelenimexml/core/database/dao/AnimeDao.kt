package com.lelestacia.lelenimexml.core.database.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.lelestacia.lelenimexml.core.database.entity.anime.AnimeEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface AnimeDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrUpdateAnime(anime: AnimeEntity)

    @Query("SELECT * FROM anime_table ORDER BY last_viewed DESC")
    fun getAllAnimeHistory(): PagingSource<Int, AnimeEntity>

    @Query("SELECT * FROM anime_table WHERE favorite = 1")
    fun getAllFavoriteAnime(): PagingSource<Int, AnimeEntity>

    @Query("SELECT * FROM anime_table WHERE anime_id = :animeID")
    fun getAnimeByAnimeId(animeID: Int): AnimeEntity?

    @Query("SELECT * FROM anime_table WHERE anime_id = :animeId")
    fun getNewestAnimeDataByAnimeId(animeId: Int): Flow<AnimeEntity>

    @Update
    suspend fun updateAnime(anime: AnimeEntity)
}
