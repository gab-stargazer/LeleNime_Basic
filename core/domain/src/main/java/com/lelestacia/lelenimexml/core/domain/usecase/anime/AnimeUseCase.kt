package com.lelestacia.lelenimexml.core.domain.usecase.anime

import androidx.paging.PagingData
import com.lelestacia.lelenimexml.core.data.impl.anime.IAnimeRepository
import com.lelestacia.lelenimexml.core.model.anime.Anime
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class AnimeUseCase @Inject constructor(
    private val animeRepository: IAnimeRepository
) : IAnimeUseCase {

    override fun getAiringAnime(): Flow<PagingData<Anime>> =
        animeRepository.seasonAnimePagingData()

    override fun getAnimeByTitle(query: String): Flow<PagingData<Anime>> =
        animeRepository.searchAnimeByTitle(query)

    override fun getAnimeByMalID(animeId: Int): Flow<Anime> =
        animeRepository.getNewestAnimeDataByAnimeID(animeId)

    override fun getAnimeHistory(): Flow<PagingData<Anime>> =
        animeRepository.getAnimeHistory()

    override suspend fun insertOrUpdateNewAnimeToHistory(anime: Anime) {
        animeRepository.insertAnimeToHistory(anime)
    }

    override suspend fun updateAnimeFavorite(malID: Int) {
        animeRepository.updateAnimeFavorite(malID)
    }

    override fun getAllFavoriteAnime(): Flow<PagingData<Anime>> =
        animeRepository.getAllFavoriteAnime()

    override fun isSafeMode(): Boolean = animeRepository.isSafeMode()

    override fun changeSafeMode(isSafeMode: Boolean) {
        animeRepository.changeSafeMode(isSafeMode)
    }
}
