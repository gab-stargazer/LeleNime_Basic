package com.lelestacia.lelenimexml.core.source.local

import androidx.paging.PagingSource
import androidx.room.*
import com.lelestacia.lelenimexml.core.model.local.AnimeEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface AnimeDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrUpdateAnime(anime: AnimeEntity)

    @Query("SELECT * FROM anime_table ORDER BY lastViewed DESC")
    fun getAllAnime(): PagingSource<Int, AnimeEntity>

    @Query("SELECT * FROM anime_table WHERE malId = :animeId")
    fun getNewestAnimeDataByAnimeId(animeId: Int): Flow<AnimeEntity>

    @Query("SELECT * FROM anime_table WHERE malId =:animeId")
    fun getAnimeByAnimeId(animeId: Int): AnimeEntity?

    @Update
    suspend fun updateAnime(anime: AnimeEntity)
}