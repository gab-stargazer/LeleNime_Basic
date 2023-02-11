package com.lelestacia.lelenimexml.core.network.impl.manga

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.lelestacia.lelenimexml.core.network.model.manga.MangaResponse
import com.lelestacia.lelenimexml.core.network.source.MangaAPI
import kotlinx.coroutines.delay

class SearchMangaPagingSource(
    private val mangaAPI: MangaAPI,
    private val query: String,
    private val sfw: Boolean
) : PagingSource<Int, MangaResponse>() {

    override fun getRefreshKey(state: PagingState<Int, MangaResponse>): Int? {
        return state.anchorPosition
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, MangaResponse> {
        return try {
            val currentPage = params.key ?: 1
            val apiResponse =
                if (sfw) mangaAPI.searchMangaByTitle(q = query, page = currentPage, sfw = true)
                else mangaAPI.searchMangaByTitle(q = query, page = currentPage)
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
                if (apiResponse.pagination.hasNextPage) currentPage.plus(1)
                else null
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }
}