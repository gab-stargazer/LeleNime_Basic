package com.lelestacia.lelenimexml.feature_anime.domain.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.lelestacia.lelenimexml.core.domain.dto.animefull.AnimeFUll
import com.lelestacia.lelenimexml.core.domain.remote.JikanAPI
import com.lelestacia.lelenimexml.core.utililty.NetworkResponse
import com.lelestacia.lelenimexml.feature_anime.domain.model.AnimeCard
import com.lelestacia.lelenimexml.feature_anime.domain.remote.SearchAnimePaging
import com.lelestacia.lelenimexml.feature_anime.domain.remote.SeasonAnimePaging
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

    private var query = ""
    private var searchAnimePaging: SearchAnimePaging? = null
        get() {
            if (field == null || field?.invalid == true) {
                field = SearchAnimePaging(query, apiService)
            }
            return field
        }

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

    override fun seasonAnimePagingData(): Flow<PagingData<AnimeCard>> {
        return Pager(
            config = PagingConfig(
                pageSize = 25,
                prefetchDistance = 10,
                initialLoadSize = 25,
                enablePlaceholders = false
            ),
            pagingSourceFactory = {
                SeasonAnimePaging(apiService)
            }
        ).flow.buffer()
    }

    override fun searchAnimeByTitle(): Flow<PagingData<AnimeCard>> {
        return Pager(
            config = PagingConfig(
                pageSize = 25,
                prefetchDistance = 10,
                initialLoadSize = 25,
                enablePlaceholders = false
            ),
            pagingSourceFactory = {
                searchAnimePaging!!
            }
        ).flow.buffer()
    }

    override fun searchNewQuery(newQuery: String) {
        query = newQuery
        searchAnimePaging?.invalidate()
    }
}