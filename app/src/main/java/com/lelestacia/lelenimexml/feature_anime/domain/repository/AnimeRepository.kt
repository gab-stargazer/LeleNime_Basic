package com.lelestacia.lelenimexml.feature_anime.domain.repository

import androidx.paging.PagingData
import com.lelestacia.lelenimexml.core.domain.dto.season.Data
import kotlinx.coroutines.flow.Flow

interface AnimeRepository {

    fun seasonAnimePagingData() : Flow<PagingData<Data>>

    fun searchAnimeByTitle(query: String): Flow<PagingData<Data>>
}