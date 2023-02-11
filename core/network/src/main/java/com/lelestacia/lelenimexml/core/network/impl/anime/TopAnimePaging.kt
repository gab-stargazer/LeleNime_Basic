package com.lelestacia.lelenimexml.core.network.impl.anime

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.lelestacia.lelenimexml.core.network.model.anime.AnimeResponse
import com.lelestacia.lelenimexml.core.network.source.AnimeAPI
import kotlinx.coroutines.delay
import retrofit2.HttpException
import timber.log.Timber

class TopAnimePaging(
    private val animeAPI: AnimeAPI
) : PagingSource<Int, AnimeResponse>() {

    override fun getRefreshKey(state: PagingState<Int, AnimeResponse>): Int? {
        return state.anchorPosition
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, AnimeResponse> {
        return try {
            val currentPage = params.key ?: 1
            val apiResponse = animeAPI.getTopAnime(page = currentPage)
            delay(
                if (currentPage == 1) 400
                else 500
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
            Timber.e(e.message)
            LoadResult.Error(e)
        } catch (e: HttpException) {
            LoadResult.Error(e)
        }
    }
}