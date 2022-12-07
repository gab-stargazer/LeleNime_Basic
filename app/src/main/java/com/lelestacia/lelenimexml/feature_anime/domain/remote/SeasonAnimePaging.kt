package com.lelestacia.lelenimexml.feature_anime.domain.remote

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.lelestacia.lelenimexml.core.domain.dto.season.Data
import com.lelestacia.lelenimexml.core.domain.remote.JikanAPI

class SeasonAnimePaging(
    private val apiService: JikanAPI
) : PagingSource<Int, Data>() {

    override fun getRefreshKey(state: PagingState<Int, Data>): Int? {
        return state.anchorPosition
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Data> {
        return try {
            val apiResponse = apiService.getCurrentSeason(params.key ?: 1)
            LoadResult.Page(
                data = apiResponse.data,
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