package com.lelestacia.lelenimexml.core.network.impl.character

import com.lelestacia.lelenimexml.core.network.model.character.CharacterResponse
import com.lelestacia.lelenimexml.core.network.model.character.CharacterDetailResponse

interface ICharacterNetworkService {
    suspend fun getCharactersByAnimeID(animeID: Int): List<CharacterResponse>
    suspend fun getCharacterDetailByCharacterID(characterID: Int): CharacterDetailResponse
}
