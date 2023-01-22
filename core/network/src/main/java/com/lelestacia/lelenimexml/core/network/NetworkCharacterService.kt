package com.lelestacia.lelenimexml.core.network

import com.lelestacia.lelenimexml.core.network.model.character.NetworkCharacter
import com.lelestacia.lelenimexml.core.network.model.character.NetworkCharacterDetail
import com.lelestacia.lelenimexml.core.network.source.ApiService
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class NetworkCharacterService @Inject constructor(
    private val apiService: ApiService,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) : INetworkCharacterService {
    override suspend fun getCharactersByAnimeID(animeID: Int): List<NetworkCharacter> {
        return withContext(ioDispatcher) {
            apiService.getCharactersByAnimeID(id = animeID).data
        }
    }

    override suspend fun getCharacterDetailByCharacterID(characterID: Int): NetworkCharacterDetail {
        return withContext(ioDispatcher) {
            apiService.getCharacterDetailByCharacterID(id = characterID).data
        }
    }
}
