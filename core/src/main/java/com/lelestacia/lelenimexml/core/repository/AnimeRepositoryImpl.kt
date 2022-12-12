package com.lelestacia.lelenimexml.core.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.lelestacia.lelenimexml.core.model.remote.anime.AnimeResponse
import com.lelestacia.lelenimexml.core.model.remote.animefull.AnimeFUll
import com.lelestacia.lelenimexml.core.source.remote.JikanAPI
import com.lelestacia.lelenimexml.core.source.remote.SearchAnimePaging
import com.lelestacia.lelenimexml.core.source.remote.SeasonAnimePaging
import com.lelestacia.lelenimexml.core.utility.NetworkResponse
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
                NetworkResponse.Success(apiService.getAnimeById(animeID))
            } catch (e: Exception) {
                when (e) {
                    is IOException -> NetworkResponse.NetworkException
                    is HttpException -> NetworkResponse.GenericException(
                        e.code(),
                        e.localizedMessage
                    )
                    else -> NetworkResponse.GenericException(null, e.localizedMessage)
                }
            }
        }
    }

    override fun seasonAnimePagingData(): Flow<PagingData<AnimeResponse>> {
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

    override fun searchAnimeByTitle(query: String): Flow<PagingData<AnimeResponse>> {
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