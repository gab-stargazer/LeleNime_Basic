package com.lelestacia.lelenimexml.core.network.impl.character

import com.lelestacia.lelenimexml.core.network.model.character.NetworkCharacter
import com.lelestacia.lelenimexml.core.network.model.character.NetworkCharacterDetail
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
    override suspend fun getCharactersByAnimeID(animeID: Int): List<NetworkCharacter> {
        return withContext(ioDispatcher) {
            val apiResponse = animeAPI.getCharactersByAnimeID(id = animeID).data
            delay(750)
            apiResponse
        }
    }

    override suspend fun getCharacterDetailByCharacterID(characterID: Int): NetworkCharacterDetail {
        return withContext(ioDispatcher) {
            val apiResponse = characterAPI.getCharacterDetailByCharacterID(id = characterID).data
            delay(750)
            apiResponse
        }
    }
}
