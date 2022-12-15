package com.lelestacia.lelenimexml.core.source.remote

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.lelestacia.lelenimexml.core.model.remote.anime.AnimeResponse
import kotlinx.coroutines.delay
import timber.log.Timber

class SeasonAnimePaging(private val apiService: JikanAPI) : PagingSource<Int, AnimeResponse>() {

    override fun getRefreshKey(state: PagingState<Int, AnimeResponse>): Int? {
        return state.anchorPosition
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, AnimeResponse> {
        return try {
            val currentPage = params.key ?: 1

            val apiResponse = apiService.getCurrentSeason(currentPage)
            delay(500)

            LoadResult.Page(
                data = apiResponse.data,
                prevKey =
                if (currentPage == 1) null
                else currentPage.minus(1),
                nextKey =
                if (apiResponse.pagination.hasNextPage) currentPage.plus(1)
                else null
            )
        } catch (e: Exception) {
            Timber.e(e, e.localizedMessage)
            LoadResult.Error(e)
        }
    }
}