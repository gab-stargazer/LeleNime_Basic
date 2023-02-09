package com.lelestacia.lelenimexml.core.data.impl.anime

import androidx.paging.PagingData
import com.lelestacia.lelenimexml.core.common.Resource
import com.lelestacia.lelenimexml.core.model.anime.Anime
import kotlinx.coroutines.flow.Flow

interface IAnimeRepository {
    fun seasonAnimePagingData(): Flow<PagingData<Anime>>
    fun upcomingAnimePagingData(): Flow<PagingData<Anime>>
    fun topAnimePagingData(): Flow<PagingData<Anime>>
    fun searchAnimeByTitle(query: String): Flow<PagingData<Anime>>
    fun getNewestAnimeDataByAnimeID(animeID: Int): Flow<Anime>
    suspend fun updateAnimeIfNecessary(animeID: Int): Resource<Boolean>
    fun getAnimeHistory(): Flow<PagingData<Anime>>
    suspend fun insertAnimeToHistory(anime: Anime)
    fun getAllFavoriteAnime(): Flow<PagingData<Anime>>
    suspend fun updateAnimeFavorite(malID: Int)
    fun isNsfwMode(): Boolean
    fun changeSafeMode(isNsfw: Boolean)
}
