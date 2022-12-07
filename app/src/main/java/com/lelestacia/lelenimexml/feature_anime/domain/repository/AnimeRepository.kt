package com.lelestacia.lelenimexml.feature_anime.domain.repository

import androidx.paging.PagingData
import com.lelestacia.lelenimexml.feature_anime.domain.model.AnimeCard
import kotlinx.coroutines.flow.Flow

interface AnimeRepository {

    fun seasonAnimePagingData(): Flow<PagingData<AnimeCard>>

    fun searchAnimeByTitle(): Flow<PagingData<AnimeCard>>

    fun searchNewQuery(newQuery: String)
}