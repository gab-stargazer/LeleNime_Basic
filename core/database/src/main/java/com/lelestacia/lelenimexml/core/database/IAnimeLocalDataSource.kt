package com.lelestacia.lelenimexml.core.database

import androidx.paging.PagingSource
import com.lelestacia.lelenimexml.core.model.database.AnimeEntity
import kotlinx.coroutines.flow.Flow

interface IAnimeLocalDataSource {
    suspend fun insertOrUpdateAnime(anime: AnimeEntity)
    suspend fun updateAnime(anime: AnimeEntity)
    suspend fun getAnimeByAnimeId(animeID: Int): AnimeEntity?
    fun getAllAnimeHistory(): PagingSource<Int, AnimeEntity>
    fun getNewestAnimeDataByAnimeId(animeID: Int): Flow<AnimeEntity>
    fun getAllFavoriteAnime(): PagingSource<Int, AnimeEntity>
}