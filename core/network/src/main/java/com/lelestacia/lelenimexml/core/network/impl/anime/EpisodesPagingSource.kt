package com.lelestacia.lelenimexml.core.network.impl.anime

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.lelestacia.lelenimexml.core.network.model.episodes.EpisodeResponse
import com.lelestacia.lelenimexml.core.network.source.AnimeAPI

class EpisodesPagingSource(
    private val animeAPI: AnimeAPI,
    private val animeID: Int
) : PagingSource<Int, EpisodeResponse>() {

    override fun getRefreshKey(state: PagingState<Int, EpisodeResponse>): Int? {
        return state.anchorPosition
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, EpisodeResponse> {
        return try {
            val currentPage = params.key ?: 1
            val apiResponse = animeAPI.getAnimeEpisodesWithPagingByAnimeID(
                animeID = animeID,
                page = currentPage
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