package com.lelestacia.lelenimexml.feature_anime.domain.usecases

import androidx.paging.PagingData
import com.lelestacia.lelenimexml.core.model.local.AnimeEntity
import com.lelestacia.lelenimexml.feature_anime.domain.model.Anime
import com.lelestacia.lelenimexml.feature_anime.domain.model.CharacterData
import kotlinx.coroutines.flow.Flow

interface AnimeUseCases {
    fun seasonAnimePagingData(): Flow<PagingData<Anime>>
    fun searchAnimeByTitle(query: String): Flow<PagingData<Anime>>
    suspend fun insertOrUpdateNewAnimeToHistory(animeEntity: AnimeEntity)
    fun getAnimeHistory(): Flow<PagingData<Anime>>
    fun getAnimeCharacterById(id: Int): Flow<List<CharacterData>>
}