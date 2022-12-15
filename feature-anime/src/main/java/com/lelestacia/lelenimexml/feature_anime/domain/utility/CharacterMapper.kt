package com.lelestacia.lelenimexml.feature_anime.domain.utility

import com.lelestacia.lelenimexml.core.model.local.CharacterEntity
import com.lelestacia.lelenimexml.core.model.remote.character.CharacterResponse
import com.lelestacia.lelenimexml.feature_anime.domain.model.CharacterData

object CharacterMapper {
    fun fromNetwork(characterNetworkResponse: CharacterResponse) : CharacterData {
        return CharacterData(
            characterMalId = characterNetworkResponse.characterResponseData.characterMalId,
            images = characterNetworkResponse.characterResponseData.images.webp.imageUrl,
            name = characterNetworkResponse.characterResponseData.name,
            role = characterNetworkResponse.role,
            favoriteBy = characterNetworkResponse.favoriteBy
        )
    }

    fun entityToCharacter(characterEntity: CharacterEntity): CharacterData {
        return CharacterData(
            characterMalId = characterEntity.characterId,
            images = characterEntity.characterImage,
            name = characterEntity.characterName,
            role = characterEntity.role,
            favoriteBy = characterEntity.favoriteBy
        )
    }
}