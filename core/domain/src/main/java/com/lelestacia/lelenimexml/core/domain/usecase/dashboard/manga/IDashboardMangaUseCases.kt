package com.lelestacia.lelenimexml.core.domain.usecase.dashboard.manga

import androidx.paging.PagingData
import com.lelestacia.lelenimexml.core.model.manga.Manga
import com.lelestacia.lelenimexml.core.model.recommendation.Recommendation
import kotlinx.coroutines.flow.Flow

interface IDashboardMangaUseCases {
    fun getTopManga(): Flow<PagingData<Manga>>
    fun getRecentMangaRecommendation(): Flow<PagingData<Recommendation>>
}