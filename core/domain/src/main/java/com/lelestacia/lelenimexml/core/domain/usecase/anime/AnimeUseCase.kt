package com.lelestacia.lelenimexml.core.domain.usecase.anime

import com.lelestacia.lelenimexml.core.data.impl.anime.IAnimeRepository
import com.lelestacia.lelenimexml.core.model.anime.Anime
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class AnimeUseCase @Inject constructor(
    private val animeRepository: IAnimeRepository
) : IAnimeUseCase {

    override fun getAnimeByMalID(animeId: Int): Flow<Anime> =
        animeRepository.getNewestAnimeDataByAnimeID(animeId)

    override suspend fun insertOrUpdateNewAnimeToHistory(anime: Anime) {
        animeRepository.insertAnimeToHistory(anime)
    }

    override suspend fun updateAnimeFavorite(malID: Int) {
        animeRepository.updateAnimeFavorite(malID)
    }

    override fun isSafeMode(): Boolean = animeRepository.isNsfwMode()

    override fun changeSafeMode(isSafeMode: Boolean) {
        animeRepository.changeSafeMode(isSafeMode)
    }
}
