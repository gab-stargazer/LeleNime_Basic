package com.lelestacia.lelenimexml.core.domain.usecase

import androidx.paging.PagingData
import androidx.paging.map
import com.lelestacia.lelenimexml.core.data.IAnimeRepository
import com.lelestacia.lelenimexml.core.model.domain.anime.Anime
import com.lelestacia.lelenimexml.core.model.domain.anime.asAnime
import com.lelestacia.lelenimexml.core.model.domain.anime.asEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import timber.log.Timber
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

    override suspend fun getAnimeByMalID(animeId: Int): Flow<Anime> =
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
        val localAnime = animeRepository.getAnimeByAnimeId(anime.malID)

        if (localAnime != null) {
            val newAnime = anime.asEntity(
                isFavorite = localAnime.isFavorite
            )
            animeRepository.insertAnimeToHistory(newAnime)
            Timber.d("Anime Updated")
            return
        }

        val newAnime = anime.asEntity()
        animeRepository.insertAnimeToHistory(newAnime)
        Timber.d("Anime Inserted")
    }

    override suspend fun updateAnimeFavorite(malID: Int) {
        animeRepository.updateAnimeFavorite(malID)
    }
}