package com.lelestacia.lelenimexml.feature_anime.domain.usecases

import androidx.paging.PagingData
import androidx.paging.map
import com.lelestacia.lelenimexml.core.model.local.AnimeEntity
import com.lelestacia.lelenimexml.core.repository.AnimeRepository
import com.lelestacia.lelenimexml.feature_anime.domain.model.Anime
import com.lelestacia.lelenimexml.feature_anime.domain.model.CharacterData
import com.lelestacia.lelenimexml.feature_anime.domain.utility.AnimeMapperUtil
import com.lelestacia.lelenimexml.feature_anime.domain.utility.CharacterMapper
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class AnimeInteractor @Inject constructor(
    private val animeRepository: AnimeRepository
) : AnimeUseCases {

    override fun seasonAnimePagingData(): Flow<PagingData<Anime>> {
        return animeRepository.seasonAnimePagingData().map { pagingData ->
            pagingData.map { networkAnime ->
                AnimeMapperUtil.networkToAnime(networkAnime)
            }
        }
    }

    override fun searchAnimeByTitle(query: String): Flow<PagingData<Anime>> {
        return animeRepository.searchAnimeByTitle(query).map { pagingData ->
            pagingData.map { networkAnime ->
                AnimeMapperUtil.networkToAnime(networkAnime)
            }
        }
    }

    override suspend fun insertOrUpdateNewAnimeToHistory(animeEntity: AnimeEntity) {
        animeRepository.insertOrUpdateNewAnimeToHistory(animeEntity)
    }

    override fun getAnimeHistory(): Flow<List<Anime>> {
        return animeRepository.getAnimeHistory().map {
            it.map { animeEntity ->
                AnimeMapperUtil.animeEntityToAnime(animeEntity)
            }
        }
    }

    override fun getAnimeCharacterById(id: Int): Flow<List<CharacterData>> {
        return animeRepository.getAnimeCharactersById(id).map { list ->
            list.map { characterEntity ->
                CharacterMapper.entityToCharacter(characterEntity)
            }
        }
    }
}