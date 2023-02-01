package com.lelestacia.lelenimexml.core.domain.usecase.home

import androidx.paging.PagingData
import com.lelestacia.lelenimexml.core.model.anime.Anime
import kotlinx.coroutines.flow.Flow

interface IHomeAnimeUseCase {
    fun getAiringAnime(): Flow<PagingData<Anime>>
    fun getAnimeByTitle(query: String): Flow<PagingData<Anime>>
}
