package com.lelestacia.lelenimexml.core.domain.usecase

import androidx.paging.PagingData
import com.lelestacia.lelenimexml.core.model.domain.anime.Anime
import kotlinx.coroutines.flow.Flow

interface IAnimeUseCase {
    fun getAiringAnime(): Flow<PagingData<Anime>>
    fun getAnimeByTitle(query: String): Flow<PagingData<Anime>>
    suspend fun getAnimeByMalID(animeId: Int): Flow<Anime>
    fun getAnimeHistory(): Flow<PagingData<Anime>>
    suspend fun insertOrUpdateNewAnimeToHistory(anime: Anime)
    suspend fun updateAnimeFavorite(malID: Int)
    fun getAllFavoriteAnime(): Flow<PagingData<Anime>>
}