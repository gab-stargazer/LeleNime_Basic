package com.lelestacia.lelenimexml.core.domain.usecase.favorite

import androidx.paging.PagingData
import com.lelestacia.lelenimexml.core.model.anime.Anime
import kotlinx.coroutines.flow.Flow

interface IFavoriteAnimeUseCase {
    fun getAllFavoriteAnime(): Flow<PagingData<Anime>>
}
