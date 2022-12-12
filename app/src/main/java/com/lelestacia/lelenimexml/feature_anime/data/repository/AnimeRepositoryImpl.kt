package com.lelestacia.lelenimexml.feature_anime.data.repository

import androidx.paging.*
import com.lelestacia.lelenimexml.core.domain.dto.animefull.AnimeFUll
import com.lelestacia.lelenimexml.core.network.api.JikanAPI
import com.lelestacia.lelenimexml.core.utililty.NetworkResponse
import com.lelestacia.lelenimexml.feature_anime.data.remote.SearchAnimePaging
import com.lelestacia.lelenimexml.feature_anime.data.remote.SeasonAnimePaging
import com.lelestacia.lelenimexml.feature_anime.domain.model.Anime
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.buffer
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class AnimeRepositoryImpl @Inject constructor(
    private val apiService: JikanAPI,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) : AnimeRepository {

    override suspend fun getAnimeById(animeID: Int): NetworkResponse<AnimeFUll> {
        return withContext(ioDispatcher) {
            try {
                NetworkResponse.Success(
                    data = apiService.getAnimeById(animeID)
                )
            } catch (e: Exception) {
                when (e) {
                    is HttpException -> NetworkResponse.GenericException(e.code(), e.message())
                    is IOException -> NetworkResponse.NetworkException
                    else -> NetworkResponse.GenericException(null, e.message)
                }
            }
        }
    }

    @OptIn(ExperimentalPagingApi::class)
    override fun seasonAnimePagingData(): Flow<PagingData<Anime>> {
        return Pager(
            config = PagingConfig(
                pageSize = 25,
                prefetchDistance = 5,
                initialLoadSize = 25,
                enablePlaceholders = false
            ),
            pagingSourceFactory = {
                SeasonAnimePaging(apiService)
            }
        ).flow
    }

    override fun searchAnimeByTitle(query: String): Flow<PagingData<Anime>> {
        return Pager(
            config = PagingConfig(
                pageSize = 25,
                prefetchDistance = 10,
                initialLoadSize = 25,
                enablePlaceholders = false
            ),
            pagingSourceFactory = {
                SearchAnimePaging(query, apiService)
            }
        ).flow.buffer()
    }
}