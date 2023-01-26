package com.lelestacia.lelenimexml.core.database.impl.anime

import androidx.paging.PagingSource
import com.lelestacia.lelenimexml.core.database.model.anime.AnimeEntity
import kotlinx.coroutines.flow.Flow

interface IAnimeDatabaseService {
    suspend fun insertOrUpdateAnimeIntoHistory(anime: AnimeEntity)
    suspend fun updateAnime(anime: AnimeEntity)
    suspend fun getAnimeByAnimeID(animeID: Int): AnimeEntity?
    fun getNewestAnimeDataByAnimeID(animeID: Int): Flow<AnimeEntity>
    fun getAllAnimeHistory(): PagingSource<Int, AnimeEntity>
    fun getAllFavoriteAnime(): PagingSource<Int, AnimeEntity>
}
