package com.lelestacia.lelenimexml.core.data.impl.recommendation

import androidx.paging.PagingData
import com.lelestacia.lelenimexml.core.model.recommendation.Recommendation
import kotlinx.coroutines.flow.Flow

interface IRecommendationRepository {
    fun getRecentAnimeRecommendation(): Flow<PagingData<Recommendation>>
    fun getRecentMangaRecommendation(): Flow<PagingData<Recommendation>>
}