package com.lelestacia.lelenimexml.feature_anime.domain.usecases

import androidx.paging.PagingData
import com.lelestacia.lelenimexml.core.model.remote.animefull.AnimeFUll
import com.lelestacia.lelenimexml.core.utility.NetworkResponse
import com.lelestacia.lelenimexml.feature_anime.domain.model.Anime
import kotlinx.coroutines.flow.Flow

interface AnimeUseCases {
    suspend fun getAnimeById(animeID: Int): NetworkResponse<AnimeFUll>
    fun seasonAnimePagingData(): Flow<PagingData<Anime>>
    fun searchAnimeByTitle(query: String): Flow<PagingData<Anime>>
}