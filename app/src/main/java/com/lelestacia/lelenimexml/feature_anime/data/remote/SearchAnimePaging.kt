package com.lelestacia.lelenimexml.feature_anime.data.remote

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.lelestacia.lelenimexml.core.network.api.JikanAPI
import com.lelestacia.lelenimexml.feature_anime.domain.model.Anime
import com.lelestacia.lelenimexml.feature_anime.utility.AnimeMapper

class SearchAnimePaging(
    private val query: String,
    private val apiService: JikanAPI
) : PagingSource<Int, Anime>() {
    override fun getRefreshKey(state: PagingState<Int, Anime>): Int? {
        return state.anchorPosition
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Anime> {
        return try {
            val apiResponse = apiService.searchAnimeByTitle(query, params.key ?: 1)
            LoadResult.Page(
                data = apiResponse.data.map { networkAnime ->
                    AnimeMapper.networkToAnime(networkAnime)
                },
                prevKey =
                if (apiResponse.pagination.currentPage == 1) null
                else apiResponse.pagination.currentPage.minus(1),
                nextKey =
                if (apiResponse.pagination.hasNextPage) apiResponse.pagination.currentPage.plus(1)
                else null
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }
}