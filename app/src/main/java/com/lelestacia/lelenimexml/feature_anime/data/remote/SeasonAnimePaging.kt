package com.lelestacia.lelenimexml.feature_anime.data.remote

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.lelestacia.lelenimexml.core.network.api.JikanAPI
import com.lelestacia.lelenimexml.feature_anime.domain.model.Anime
import com.lelestacia.lelenimexml.feature_anime.utility.SeasonAnimeMapper
import kotlinx.coroutines.delay
import timber.log.Timber

class SeasonAnimePaging(
    private val apiService: JikanAPI
) : PagingSource<Int, Anime>() {
    override fun getRefreshKey(state: PagingState<Int, Anime>): Int? {
        return state.anchorPosition
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Anime> {
        return try {
            val currentPage = params.key ?: 1
            delay(500)
            val apiResponse = apiService.getCurrentSeason(currentPage)

            LoadResult.Page(
                data = apiResponse.data.map { networkResponse ->
                    SeasonAnimeMapper.networkToAnime(networkResponse)
                },
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