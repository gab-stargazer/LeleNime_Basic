package com.lelestacia.lelenimexml.core.network.impl.recommendation

import androidx.paging.PagingSource
import com.lelestacia.lelenimexml.core.network.model.GenericRecommendationResponse

interface IRecommendationNetworkService {
    fun getRecentAnimeRecommendation(): PagingSource<Int, GenericRecommendationResponse>
    fun getRecentMangaRecommendation(): PagingSource<Int, GenericRecommendationResponse>
}