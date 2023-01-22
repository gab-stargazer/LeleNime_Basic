package com.lelestacia.lelenimexml.core.network

import com.lelestacia.lelenimexml.core.network.model.character.NetworkCharacter
import com.lelestacia.lelenimexml.core.network.model.character.NetworkCharacterDetail

interface INetworkCharacterService {
    suspend fun getCharactersByAnimeID(animeID: Int): List<NetworkCharacter>
    suspend fun getCharacterDetailByCharacterID(characterID: Int): NetworkCharacterDetail
}
