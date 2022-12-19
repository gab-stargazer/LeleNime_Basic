package com.lelestacia.lelenimexml.feature_anime.domain.usecases

import androidx.paging.PagingData
import androidx.paging.map
import com.lelestacia.lelenimexml.core.model.local.AnimeEntity
import com.lelestacia.lelenimexml.core.repository.AnimeRepository
import com.lelestacia.lelenimexml.feature_anime.domain.model.Anime
import com.lelestacia.lelenimexml.feature_anime.domain.utility.AnimeMapperUtil.animeEntityToAnime
import com.lelestacia.lelenimexml.feature_anime.domain.utility.AnimeMapperUtil.networkToAnime
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class AnimeInteractor @Inject constructor(
    private val animeRepository: AnimeRepository
) : AnimeUseCases {

    override fun seasonAnimePagingData(): Flow<PagingData<Anime>> =
        animeRepository.seasonAnimePagingData().map { pagingData ->
            pagingData.map(::networkToAnime)
        }

    override fun searchAnimeByTitle(query: String): Flow<PagingData<Anime>> =
        animeRepository.searchAnimeByTitle(query).map { pagingData ->
            pagingData.map(::networkToAnime)
        }

    override suspend fun insertOrUpdateNewAnimeToHistory(animeEntity: AnimeEntity) {
        animeRepository.insertOrUpdateNewAnimeToHistory(animeEntity)
    }

    override fun getAnimeHistory(): Flow<PagingData<Anime>> =
        animeRepository.getAnimeHistory().map { pagingData ->
            pagingData.map(::animeEntityToAnime)
        }

    override suspend fun getNewestAnimeDataByAnimeId(animeId: Int): Flow<AnimeEntity?> =
        animeRepository.getNewestAnimeDataByAnimeId(animeId)

    override suspend fun getAnimeByAnimeId(animeId: Int): AnimeEntity? =
        animeRepository.getAnimeByAnimeId(animeId)

    override suspend fun updateAnime(anime: AnimeEntity) {
        animeRepository.updateAnime(anime)
    }
}