package com.lelestacia.lelenimexml.core.domain.usecase.dashboard.manga

import androidx.paging.PagingData
import com.lelestacia.lelenimexml.core.data.impl.manga.IMangaRepository
import com.lelestacia.lelenimexml.core.data.impl.recommendation.IRecommendationRepository
import com.lelestacia.lelenimexml.core.model.manga.Manga
import com.lelestacia.lelenimexml.core.model.recommendation.Recommendation
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class DashboardMangaUseCases @Inject constructor(
    private val mangaRepository: IMangaRepository,
    private val recommendationRepository: IRecommendationRepository
) : IDashboardMangaUseCases {

    override fun getTopManga(): Flow<PagingData<Manga>> {
        return mangaRepository.getTopManga()
    }

    override fun getRecentMangaRecommendation(): Flow<PagingData<Recommendation>> {
        return recommendationRepository.getRecentMangaRecommendation()
    }
}