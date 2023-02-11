package com.lelestacia.lelenimexml.core.domain.usecase.home

import androidx.paging.PagingData
import com.lelestacia.lelenimexml.core.data.impl.anime.IAnimeRepository
import com.lelestacia.lelenimexml.core.model.anime.Anime
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class HomeAnimeUseCase @Inject constructor(
    private val animeRepository: IAnimeRepository
) : IHomeAnimeUseCase {
    override fun getAiringAnime(): Flow<PagingData<Anime>> {
        return animeRepository.getAiringAnime()
    }

    override fun getUpcomingAnime(): Flow<PagingData<Anime>> {
        return animeRepository.getUpcomingAnime()
    }

    override fun getTopAnime(): Flow<PagingData<Anime>> {
        return animeRepository.getTopAnime()
    }

    override fun getAnimeByTitle(query: String): Flow<PagingData<Anime>> {
        return animeRepository.searchAnimeByTitle(query = query)
    }
}
