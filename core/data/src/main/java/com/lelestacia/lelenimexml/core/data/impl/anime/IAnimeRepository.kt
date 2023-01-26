package com.lelestacia.lelenimexml.core.data.impl.anime

import androidx.paging.PagingData
import com.lelestacia.lelenimexml.core.model.anime.Anime
import kotlinx.coroutines.flow.Flow

interface IAnimeRepository {
    fun seasonAnimePagingData(): Flow<PagingData<Anime>>
    fun searchAnimeByTitle(query: String): Flow<PagingData<Anime>>
    fun getNewestAnimeDataByAnimeID(animeID: Int): Flow<Anime>
    suspend fun getAnimeByAnimeID(animeID: Int): Anime?
    fun getAnimeHistory(): Flow<PagingData<Anime>>
    suspend fun insertAnimeToHistory(anime: Anime)
    fun getAllFavoriteAnime(): Flow<PagingData<Anime>>
    suspend fun updateAnimeFavorite(malID: Int)
    fun isSafeMode(): Boolean
    fun changeSafeMode(isSafeMode: Boolean)
}
