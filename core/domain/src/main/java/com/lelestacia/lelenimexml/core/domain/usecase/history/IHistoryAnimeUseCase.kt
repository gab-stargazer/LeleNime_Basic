package com.lelestacia.lelenimexml.core.domain.usecase.history

import androidx.paging.PagingData
import com.lelestacia.lelenimexml.core.model.anime.Anime
import kotlinx.coroutines.flow.Flow

interface IHistoryAnimeUseCase {
    fun getAnimeHistory(): Flow<PagingData<Anime>>
}
