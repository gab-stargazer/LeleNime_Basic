package com.lelestacia.lelenimexml.core.domain.usecase

import androidx.paging.PagingData
import androidx.paging.map
import com.lelestacia.lelenimexml.core.data.IAnimeRepository
import com.lelestacia.lelenimexml.core.model.domain.anime.Anime
import com.lelestacia.lelenimexml.core.model.domain.anime.asAnime
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class AnimeUseCase @Inject constructor(
    private val animeRepository: IAnimeRepository
) : IAnimeUseCase {

    override fun getAiringAnime(): Flow<PagingData<Anime>> =
        animeRepository
            .seasonAnimePagingData()
            .map { pagingData ->
                pagingData.map { it.asAnime() }
            }

    override fun getAnimeByTitle(query: String): Flow<PagingData<Anime>> =
        animeRepository
            .searchAnimeByTitle(query)
            .map { pagingData ->
                pagingData.map { it.asAnime() }
            }

    override fun getAnimeByMalID(animeId: Int): Flow<Anime> =
        animeRepository
            .getNewestAnimeDataByAnimeId(animeId)
            .map { entity ->
                entity.asAnime()
            }

    override fun getAnimeHistory(): Flow<PagingData<Anime>> =
        animeRepository
            .getAnimeHistory()
            .map { pagingData ->
                pagingData.map { it.asAnime() }
            }

    override suspend fun insertOrUpdateNewAnimeToHistory(anime: Anime) {
        animeRepository.insertAnimeToHistory(anime)
    }

    override suspend fun updateAnimeFavorite(malID: Int) {
        animeRepository.updateAnimeFavorite(malID)
    }

    override fun getAllFavoriteAnime(): Flow<PagingData<Anime>> =
        animeRepository.getAllFavoriteAnime().map { pagingData ->
            pagingData.map { it.asAnime() }
        }

    override fun isSafeMode(): Boolean = animeRepository.isSafeMode()

    override fun changeSafeMode(isSafeMode: Boolean) {
        animeRepository.changeSafeMode(isSafeMode)
    }
}
