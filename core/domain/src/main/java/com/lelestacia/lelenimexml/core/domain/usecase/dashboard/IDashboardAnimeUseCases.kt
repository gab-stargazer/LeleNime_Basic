package com.lelestacia.lelenimexml.core.domain.usecase.dashboard

import androidx.paging.PagingData
import com.lelestacia.lelenimexml.core.model.anime.Anime
import com.lelestacia.lelenimexml.core.model.manga.Manga
import com.lelestacia.lelenimexml.core.model.recommendation.Recommendation
import kotlinx.coroutines.flow.Flow

interface IDashboardAnimeUseCases {
    suspend fun insertOrReplaceAnimeOnHistory(anime: Anime)
    fun getTopAnime(): Flow<PagingData<Anime>>
    fun getAiringAnime(): Flow<PagingData<Anime>>
    fun getUpcomingAnime(): Flow<PagingData<Anime>>
    fun getTopManga(): Flow<PagingData<Manga>>
    fun getRecentAnimeRecommendation(): Flow<PagingData<Recommendation>>
}