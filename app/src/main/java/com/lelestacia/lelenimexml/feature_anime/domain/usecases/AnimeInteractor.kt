package com.lelestacia.lelenimexml.feature_anime.domain.usecases

import androidx.paging.PagingData
import androidx.paging.map
import com.lelestacia.lelenimexml.core.model.remote.animefull.AnimeFUll
import com.lelestacia.lelenimexml.core.repository.AnimeRepository
import com.lelestacia.lelenimexml.core.utility.NetworkResponse
import com.lelestacia.lelenimexml.feature_anime.domain.model.Anime
import com.lelestacia.lelenimexml.feature_anime.domain.utility.SeasonAnimeMapper
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class AnimeInteractor @Inject constructor(
    private val animeRepository: AnimeRepository
) : AnimeUseCases {

    override suspend fun getAnimeById(animeID: Int): NetworkResponse<AnimeFUll> {
        return animeRepository.getAnimeById(animeID)
    }

    override fun seasonAnimePagingData(): Flow<PagingData<Anime>> {
        return animeRepository.seasonAnimePagingData().map { pagingData ->
            pagingData.map { networkAnime ->
                SeasonAnimeMapper.networkToAnime(networkAnime)
            }
        }
    }

    override fun searchAnimeByTitle(query: String): Flow<PagingData<Anime>> {
        return animeRepository.searchAnimeByTitle(query).map { pagingData ->
            pagingData.map { networkAnime ->
                SeasonAnimeMapper.networkToAnime(networkAnime)
            }
        }
    }
}