package com.lelestacia.lelenimexml.core.network.impl.character

import com.lelestacia.lelenimexml.core.network.model.character.CharacterResponse
import com.lelestacia.lelenimexml.core.network.model.character.CharacterDetailResponse
import com.lelestacia.lelenimexml.core.network.source.AnimeAPI
import com.lelestacia.lelenimexml.core.network.source.CharacterAPI
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import javax.inject.Inject

class CharacterNetworkService @Inject constructor(
    private val animeAPI: AnimeAPI,
    private val characterAPI: CharacterAPI,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) : ICharacterNetworkService {
    override suspend fun getCharactersByAnimeID(animeID: Int): List<CharacterResponse> {
        return withContext(ioDispatcher) {
            delay(1000)
            animeAPI.getCharactersByAnimeID(animeID = animeID).data
        }
    }

    override suspend fun getCharacterDetailByCharacterID(characterID: Int): CharacterDetailResponse {
        return withContext(ioDispatcher) {
            delay(1000)
            characterAPI.getCharacterDetailByCharacterID(id = characterID).data
        }
    }
}
