package com.lelestacia.lelenimexml.feature.anime.domain.usecase

import androidx.paging.PagingData
import androidx.paging.map
import com.lelestacia.lelenimexml.core.model.local.AnimeEntity
import com.lelestacia.lelenimexml.core.repository.AnimeRepository
import com.lelestacia.lelenimexml.feature.anime.domain.model.Anime
import com.lelestacia.lelenimexml.feature.anime.domain.util.AnimeMapperUtil
import com.lelestacia.lelenimexml.feature.anime.domain.util.AnimeMapperUtil.animeEntityToAnime
import com.lelestacia.lelenimexml.feature.anime.domain.util.AnimeMapperUtil.networkToAnime
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import timber.log.Timber
import javax.inject.Inject

class AnimeInteractor @Inject constructor(
    private val animeRepository: AnimeRepository
) : AnimeUseCase {

    override fun getAiringAnime(): Flow<PagingData<Anime>> =
        animeRepository
            .seasonAnimePagingData()
            .map { pagingData ->
                pagingData.map(::networkToAnime)
            }

    override fun getAnimeByTitle(query: String): Flow<PagingData<Anime>> =
        animeRepository
            .searchAnimeByTitle(query)
            .map { pagingData ->
                pagingData.map(::networkToAnime)
            }

    override suspend fun getAnimeByMalID(animeId: Int): Flow<Anime> =
        animeRepository
            .getNewestAnimeDataByAnimeId(animeId)
            .map(::animeEntityToAnime)

    override suspend fun getAnimeByAnimeId(animeId: Int): AnimeEntity? =
        animeRepository.getAnimeByAnimeId(animeId)

    override fun getAnimeHistory(): Flow<PagingData<Anime>> =
        animeRepository
            .getAnimeHistory()
            .map { pagingData ->
                pagingData.map(::animeEntityToAnime)
            }

    override suspend fun insertOrUpdateNewAnimeToHistory(anime: Anime) {
        val localAnime = animeRepository.getAnimeByAnimeId(anime.malID)

        if (localAnime != null) {
            val newAnime = AnimeMapperUtil
                .animeToEntity(
                    anime = anime,
                    isFavorite = localAnime.isFavorite
                )
            animeRepository.insertAnimeToHistory(newAnime)
            Timber.d("Anime Updated")
            return
        }

        val newAnime = AnimeMapperUtil
            .animeToEntity(
                anime = anime,
                isFavorite = false
            )
        animeRepository.insertAnimeToHistory(newAnime)
        Timber.d("Anime Inserted")
    }

    override suspend fun updateAnimeFavorite(malID: Int) {
        animeRepository.updateAnimeFavorite(malID)
    }
}