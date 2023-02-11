package com.lelestacia.lelenimexml.core.domain.usecase.explore

import androidx.paging.PagingData
import com.lelestacia.lelenimexml.core.data.impl.anime.IAnimeRepository
import com.lelestacia.lelenimexml.core.data.impl.recommendation.IRecommendationRepository
import com.lelestacia.lelenimexml.core.model.anime.Anime
import com.lelestacia.lelenimexml.core.model.recommendation.Recommendation
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ExploreUseCases @Inject constructor(
    private val animeRepository: IAnimeRepository,
    private val recommendationRepository: IRecommendationRepository
) : IExploreUseCases {

    override fun getTopAnime(): Flow<PagingData<Anime>> {
        return animeRepository.getTopAnime()
    }

    override fun getAiringAnime(): Flow<PagingData<Anime>> {
        return animeRepository.getAiringAnime()
    }

    override fun getUpcomingAnime(): Flow<PagingData<Anime>> {
        return animeRepository.getUpcomingAnime()
    }

    override fun getRecentAnimeRecommendation(): Flow<PagingData<Recommendation>> {
        return recommendationRepository.getRecentAnimeRecommendation()
    }

    override fun getRecentMangaRecommendation(): Flow<PagingData<Recommendation>> {
        return recommendationRepository.getRecentMangaRecommendation()
    }
}