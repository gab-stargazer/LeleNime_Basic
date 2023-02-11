package com.lelestacia.lelenimexml.core.network.impl.recommendation

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.lelestacia.lelenimexml.core.network.model.GenericRecommendationResponse
import com.lelestacia.lelenimexml.core.network.source.RecommendationAPI
import kotlinx.coroutines.delay

class MangaRecommendationPagingSource(
    private val recommendationAPI: RecommendationAPI
) : PagingSource<Int, GenericRecommendationResponse>() {
    override fun getRefreshKey(state: PagingState<Int, GenericRecommendationResponse>): Int? {
        return state.anchorPosition
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, GenericRecommendationResponse> {
        return try {
            val currentPage = params.key ?: 1
            val apiResponse = recommendationAPI.getRecentMangaRecommendation(page = currentPage)
            delay(
                if (currentPage == 1) 800
                else 400
            )
            LoadResult.Page(
                data = apiResponse.data,
                prevKey =
                if (currentPage == 1) null
                else currentPage.minus(1),
                nextKey =
                if (apiResponse.scrapPagination.hasNextPage) currentPage.plus(1)
                else null
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }
}