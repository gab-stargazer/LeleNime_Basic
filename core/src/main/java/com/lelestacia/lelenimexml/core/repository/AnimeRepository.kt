package com.lelestacia.lelenimexml.core.repository

import androidx.paging.PagingData
import com.lelestacia.lelenimexml.core.model.remote.anime.AnimeResponse
import com.lelestacia.lelenimexml.core.model.remote.animefull.AnimeFUll
import com.lelestacia.lelenimexml.core.utility.NetworkResponse
import kotlinx.coroutines.flow.Flow

interface AnimeRepository {
    suspend fun getAnimeById(animeID: Int): NetworkResponse<AnimeFUll>
    fun seasonAnimePagingData(): Flow<PagingData<AnimeResponse>>
    fun searchAnimeByTitle(query: String): Flow<PagingData<AnimeResponse>>
}