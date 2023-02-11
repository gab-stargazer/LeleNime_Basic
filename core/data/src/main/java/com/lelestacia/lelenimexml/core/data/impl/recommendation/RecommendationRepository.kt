package com.lelestacia.lelenimexml.core.data.impl.recommendation

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.map
import com.lelestacia.lelenimexml.core.data.utility.asRecommendation
import com.lelestacia.lelenimexml.core.model.recommendation.Recommendation
import com.lelestacia.lelenimexml.core.network.impl.recommendation.IRecommendationNetworkService
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class RecommendationRepository @Inject constructor(
    private val recommendationAPI: IRecommendationNetworkService
) : IRecommendationRepository {

    override fun getRecentAnimeRecommendation(): Flow<PagingData<Recommendation>> =
        Pager(
            config = PagingConfig(
                pageSize = 100,
                prefetchDistance = 10,
                enablePlaceholders = false,
                initialLoadSize = 100
            ),
            pagingSourceFactory = {
                recommendationAPI.getRecentAnimeRecommendation()
            }
        ).flow.map { pagingData ->
            pagingData.map { it.asRecommendation() }
        }

    override fun getRecentMangaRecommendation(): Flow<PagingData<Recommendation>> =
        Pager(
            config = PagingConfig(
                pageSize = 100,
                prefetchDistance = 10,
                enablePlaceholders = false,
                initialLoadSize = 100
            ),
            pagingSourceFactory = {
                recommendationAPI.getRecentMangaRecommendation()
            }
        ).flow.map { pagingData ->
            pagingData.map { it.asRecommendation() }
        }
}