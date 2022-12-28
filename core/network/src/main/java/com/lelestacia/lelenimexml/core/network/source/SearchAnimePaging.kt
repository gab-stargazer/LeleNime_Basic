package com.lelestacia.lelenimexml.core.network.source

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.lelestacia.lelenimexml.core.model.network.anime.NetworkAnime
import kotlinx.coroutines.delay
import timber.log.Timber

class SearchAnimePaging(
    private val query: String,
    private val apiService: ApiService,
    private val isSafety: Boolean
) : PagingSource<Int, NetworkAnime>() {

    override fun getRefreshKey(state: PagingState<Int, NetworkAnime>): Int? {
        return state.anchorPosition
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, NetworkAnime> {
        return try {
            val currentPage = params.key ?: 1
            delay(500)
            val apiResponse =
                if (isSafety) {
                    apiService.searchAnimeByTitle(q = query, page = currentPage, sfw = true)
                } else {
                    apiService.searchAnimeByTitle(q = query, page = currentPage)
                }
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