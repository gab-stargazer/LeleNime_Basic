package com.lelestacia.lelenimexml.feature_anime.domain.repository

import androidx.paging.PagingData
import com.lelestacia.lelenimexml.core.domain.dto.animefull.AnimeFUll
import com.lelestacia.lelenimexml.core.utililty.NetworkResponse
import com.lelestacia.lelenimexml.feature_anime.domain.model.Anime
import kotlinx.coroutines.flow.Flow

interface AnimeRepository {

    suspend fun getAnimeById(animeID: Int): NetworkResponse<AnimeFUll>

    fun seasonAnimePagingData(): Flow<PagingData<Anime>>

    fun searchAnimeByTitle(query: String): Flow<PagingData<Anime>>
}