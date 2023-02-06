package com.lelestacia.lelenimexml.core.domain.usecase.anime

import com.lelestacia.lelenimexml.core.common.Resource
import com.lelestacia.lelenimexml.core.data.impl.anime.IAnimeRepository
import com.lelestacia.lelenimexml.core.model.anime.Anime
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class AnimeUseCase @Inject constructor(
    private val animeRepository: IAnimeRepository
) : IAnimeUseCase {

    override fun getAnimeByMalID(animeID: Int): Flow<Anime> =
        animeRepository.getNewestAnimeDataByAnimeID(animeID)

    override suspend fun insertOrUpdateNewAnimeToHistory(anime: Anime) {
        animeRepository.insertAnimeToHistory(anime)
    }

    override suspend fun updateAnimeFavorite(animeID: Int) {
        animeRepository.updateAnimeFavorite(animeID)
    }

    override suspend fun updateAnimeIfNecessary(animeID: Int): Resource<Boolean> {
        return animeRepository.updateAnimeIfNecessary(animeID = animeID)
    }

    override fun isSafeMode(): Boolean = animeRepository.isNsfwMode()

    override fun changeSafeMode(isNsfw: Boolean) {
        animeRepository.changeSafeMode(isNsfw)
    }
}
