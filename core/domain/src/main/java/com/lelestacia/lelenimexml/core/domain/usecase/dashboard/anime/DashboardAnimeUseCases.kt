package com.lelestacia.lelenimexml.core.domain.usecase.dashboard.anime

import androidx.paging.PagingData
import com.lelestacia.lelenimexml.core.data.impl.anime.IAnimeRepository
import com.lelestacia.lelenimexml.core.data.impl.manga.IMangaRepository
import com.lelestacia.lelenimexml.core.data.impl.recommendation.IRecommendationRepository
import com.lelestacia.lelenimexml.core.model.anime.Anime
import com.lelestacia.lelenimexml.core.model.manga.Manga
import com.lelestacia.lelenimexml.core.model.recommendation.Recommendation
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class DashboardAnimeUseCases @Inject constructor(
    private val animeRepository: IAnimeRepository,
    private val mangaRepository: IMangaRepository,
    private val recommendationRepository: IRecommendationRepository
) : IDashboardAnimeUseCases {

    override suspend fun insertOrReplaceAnimeOnHistory(anime: Anime) {
        animeRepository.insertAnimeToHistory(anime = anime)
    }

    override fun getTopAnime(): Flow<PagingData<Anime>> {
        return animeRepository.getTopAnime()
    }

    override fun getAiringAnime(): Flow<PagingData<Anime>> {
        return animeRepository.getAiringAnime()
    }

    override fun getUpcomingAnime(): Flow<PagingData<Anime>> {
        return animeRepository.getUpcomingAnime()
    }

    override fun getTopManga(): Flow<PagingData<Manga>> {
        return mangaRepository.getTopManga()
    }

    override fun getRecentAnimeRecommendation(): Flow<PagingData<Recommendation>> {
        return recommendationRepository.getRecentAnimeRecommendation()
    }
}