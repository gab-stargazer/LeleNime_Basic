package com.lelestacia.lelenimexml.core.network.impl.anime

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.lelestacia.lelenimexml.core.common.util.Constant.AFTER_FIRST_PAGE_DELAY
import com.lelestacia.lelenimexml.core.common.util.Constant.FIRST_PAGE_DELAY
import com.lelestacia.lelenimexml.core.network.model.anime.AnimeResponse
import com.lelestacia.lelenimexml.core.network.source.AnimeAPI
import kotlinx.coroutines.delay
import timber.log.Timber

class SeasonAnimePaging(
    private val animeAPI: AnimeAPI
) : PagingSource<Int, AnimeResponse>() {

    override fun getRefreshKey(state: PagingState<Int, AnimeResponse>): Int? {
        return state.anchorPosition
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, AnimeResponse> {
        return try {
            val currentPage = params.key ?: 1
            delay(
                if (currentPage == 1) FIRST_PAGE_DELAY * 2
                else AFTER_FIRST_PAGE_DELAY
            )
            val apiResponse = animeAPI.getCurrentSeason(currentPage)
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
