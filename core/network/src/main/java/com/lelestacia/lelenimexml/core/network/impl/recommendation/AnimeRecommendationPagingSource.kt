package com.lelestacia.lelenimexml.core.network.impl.recommendation

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.lelestacia.lelenimexml.core.network.model.GenericRecommendationResponse
import com.lelestacia.lelenimexml.core.network.source.RecommendationAPI
import kotlinx.coroutines.delay
import timber.log.Timber

class AnimeRecommendationPagingSource(
    private val recommendationAPI: RecommendationAPI
) : PagingSource<Int, GenericRecommendationResponse>() {

    var shouldError: Boolean = true
    override fun getRefreshKey(state: PagingState<Int, GenericRecommendationResponse>): Int? {
        return state.anchorPosition
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, GenericRecommendationResponse> {
        return try {
            val currentPage = params.key ?: 1
            delay(
                if (currentPage == 1) 5000
                else 400
            )
            if(shouldError) {
                shouldError = false
                throw Exception("Test Recommendation Exception on First Page")
            }
            val apiResponse = recommendationAPI.getRecentAnimeRecommendation(page = currentPage)
            if (apiResponse.data.isEmpty()) {
                Timber.e("Api Returned 0 data or failed to parse")
            } else {
                Timber.i("Api returned data ${apiResponse.data}")
            }
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
            Timber.e(e.message)
            LoadResult.Error(e)
        }
    }
}