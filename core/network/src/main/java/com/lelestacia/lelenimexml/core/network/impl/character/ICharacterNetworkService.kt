package com.lelestacia.lelenimexml.core.network.impl.character

import com.lelestacia.lelenimexml.core.network.model.character.NetworkCharacter
import com.lelestacia.lelenimexml.core.network.model.character.NetworkCharacterDetail

interface ICharacterNetworkService {
    suspend fun getCharactersByAnimeID(animeID: Int): List<NetworkCharacter>
    suspend fun getCharacterDetailByCharacterID(characterID: Int): NetworkCharacterDetail
}
