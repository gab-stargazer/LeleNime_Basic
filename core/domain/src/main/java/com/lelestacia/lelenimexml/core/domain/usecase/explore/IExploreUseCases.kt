package com.lelestacia.lelenimexml.core.domain.usecase.explore

import androidx.paging.PagingData
import com.lelestacia.lelenimexml.core.model.anime.Anime
import com.lelestacia.lelenimexml.core.model.recommendation.Recommendation
import kotlinx.coroutines.flow.Flow

interface IExploreUseCases {
    fun getTopAnime(): Flow<PagingData<Anime>>
    fun getAiringAnime(): Flow<PagingData<Anime>>
    fun getUpcomingAnime(): Flow<PagingData<Anime>>
    fun getRecentAnimeRecommendation(): Flow<PagingData<Recommendation>>
    fun getRecentMangaRecommendation(): Flow<PagingData<Recommendation>>
}