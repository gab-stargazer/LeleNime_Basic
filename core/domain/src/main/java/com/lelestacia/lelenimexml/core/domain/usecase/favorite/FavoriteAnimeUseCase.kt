package com.lelestacia.lelenimexml.core.domain.usecase.favorite

import androidx.paging.PagingData
import com.lelestacia.lelenimexml.core.data.impl.anime.IAnimeRepository
import com.lelestacia.lelenimexml.core.model.anime.Anime
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class FavoriteAnimeUseCase @Inject constructor(
    private val animeRepository: IAnimeRepository
) : IFavoriteAnimeUseCase {
    override fun getAllFavoriteAnime(): Flow<PagingData<Anime>> {
        return animeRepository.getAllFavoriteAnime()
    }
}
