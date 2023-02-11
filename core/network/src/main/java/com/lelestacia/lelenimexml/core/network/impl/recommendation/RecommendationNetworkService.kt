package com.lelestacia.lelenimexml.core.network.impl.recommendation

import androidx.paging.PagingSource
import com.lelestacia.lelenimexml.core.network.model.GenericRecommendationResponse
import com.lelestacia.lelenimexml.core.network.source.RecommendationAPI
import javax.inject.Inject

class RecommendationNetworkService @Inject constructor(
    private val recommendationAPI: RecommendationAPI
) : IRecommendationNetworkService {

    override fun getRecentAnimeRecommendation(): PagingSource<Int, GenericRecommendationResponse> {
        return AnimeRecommendationPagingSource(recommendationAPI = recommendationAPI)
    }

    override fun getRecentMangaRecommendation(): PagingSource<Int, GenericRecommendationResponse> {
        return MangaRecommendationPagingSource(recommendationAPI = recommendationAPI)
    }
}