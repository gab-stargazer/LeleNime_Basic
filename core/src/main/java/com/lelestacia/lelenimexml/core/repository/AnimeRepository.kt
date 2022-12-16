package com.lelestacia.lelenimexml.core.repository

import androidx.paging.PagingData
import com.lelestacia.lelenimexml.core.model.local.AnimeEntity
import com.lelestacia.lelenimexml.core.model.local.character.CharacterEntity
import com.lelestacia.lelenimexml.core.model.local.character.CharacterFullProfileEntity
import com.lelestacia.lelenimexml.core.model.remote.anime.AnimeResponse
import kotlinx.coroutines.flow.Flow

interface AnimeRepository {
    fun seasonAnimePagingData(): Flow<PagingData<AnimeResponse>>
    fun searchAnimeByTitle(query: String): Flow<PagingData<AnimeResponse>>
    suspend fun insertOrUpdateNewAnimeToHistory(anime: AnimeEntity)
    fun getAnimeHistory(): Flow<PagingData<AnimeEntity>>
    fun getAnimeCharactersById(id: Int) : Flow<List<CharacterEntity>>
    fun getAnimeCharacterFullProfileByCharacterId(characterId: Int) : Flow<CharacterFullProfileEntity>
}