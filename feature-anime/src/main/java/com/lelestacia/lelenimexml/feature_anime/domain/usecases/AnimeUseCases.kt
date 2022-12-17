package com.lelestacia.lelenimexml.feature_anime.domain.usecases

import androidx.paging.PagingData
import com.lelestacia.lelenimexml.core.model.local.AnimeEntity
import com.lelestacia.lelenimexml.feature_anime.domain.model.Anime
import kotlinx.coroutines.flow.Flow

interface AnimeUseCases {
    fun seasonAnimePagingData(): Flow<PagingData<Anime>>
    fun searchAnimeByTitle(query: String): Flow<PagingData<Anime>>
    suspend fun insertOrUpdateNewAnimeToHistory(animeEntity: AnimeEntity)
    fun getAnimeHistory(): Flow<PagingData<Anime>>

    suspend fun getNewestAnimeDataByAnimeId(animeId: Int): Flow<AnimeEntity?>
    suspend fun getAnimeByAnimeId(animeId: Int): AnimeEntity?
    suspend fun updateAnime(anime: AnimeEntity)
}