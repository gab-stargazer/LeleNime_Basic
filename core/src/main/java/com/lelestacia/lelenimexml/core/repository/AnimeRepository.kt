package com.lelestacia.lelenimexml.core.repository

import androidx.paging.PagingData
import com.lelestacia.lelenimexml.core.model.local.AnimeEntity
import com.lelestacia.lelenimexml.core.model.local.CharacterEntity
import com.lelestacia.lelenimexml.core.model.remote.anime.AnimeResponse
import com.lelestacia.lelenimexml.core.model.remote.character.CharacterResponse
import com.lelestacia.lelenimexml.core.utility.Resource
import kotlinx.coroutines.flow.Flow

interface AnimeRepository {
    fun seasonAnimePagingData(): Flow<PagingData<AnimeResponse>>
    fun searchAnimeByTitle(query: String): Flow<PagingData<AnimeResponse>>
    suspend fun insertOrUpdateNewAnimeToHistory(anime: AnimeEntity)
    fun getAnimeHistory(): Flow<PagingData<AnimeEntity>>
    suspend fun getAnimeCharacters(id: Int) : Resource<List<CharacterResponse>>
    fun getAnimeCharactersById(id: Int) : Flow<List<CharacterEntity>>
}