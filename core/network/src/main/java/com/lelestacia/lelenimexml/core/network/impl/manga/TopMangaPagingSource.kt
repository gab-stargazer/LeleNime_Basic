package com.lelestacia.lelenimexml.core.network.impl.manga

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.lelestacia.lelenimexml.core.network.model.manga.MangaResponse
import com.lelestacia.lelenimexml.core.network.source.MangaAPI
import kotlinx.coroutines.delay
import retrofit2.HttpException
import timber.log.Timber

class TopMangaPagingSource(
    private val mangaAPI: MangaAPI
) : PagingSource<Int, MangaResponse>() {
    override fun getRefreshKey(state: PagingState<Int, MangaResponse>): Int? {
        return state.anchorPosition
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, MangaResponse> {
        return try {
            val currentPage = params.key ?: 1
            delay(
                if (currentPage == 1) 1500
                else 500
            )
            val apiResponse = mangaAPI.getTopManga(page = currentPage)
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