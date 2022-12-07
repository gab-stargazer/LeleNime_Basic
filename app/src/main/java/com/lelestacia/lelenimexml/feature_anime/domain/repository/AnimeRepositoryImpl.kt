package com.lelestacia.lelenimexml.feature_anime.domain.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.lelestacia.lelenimexml.core.domain.dto.season.Data
import com.lelestacia.lelenimexml.core.domain.remote.JikanAPI
import com.lelestacia.lelenimexml.feature_anime.domain.remote.SearchAnimePaging
import com.lelestacia.lelenimexml.feature_anime.domain.remote.SeasonAnimePaging
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.buffer
import javax.inject.Inject

class AnimeRepositoryImpl @Inject constructor(
    private val apiService : JikanAPI
) : AnimeRepository {

    override fun seasonAnimePagingData(): Flow<PagingData<Data>> {
        return Pager(
            config = PagingConfig(pageSize = 25, prefetchDistance = 10, initialLoadSize = 25),
            pagingSourceFactory = {
                SeasonAnimePaging(apiService)
            }
        ).flow.buffer()
    }

    override fun searchAnimeByTitle(query: String): Flow<PagingData<Data>> {
        return Pager(
            config = PagingConfig(pageSize = 25, prefetchDistance = 10, initialLoadSize = 25),
            pagingSourceFactory = {
                SearchAnimePaging(query, apiService)
            }
        ).flow.buffer()
    }
}