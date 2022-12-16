package com.lelestacia.lelenimexml.feature_anime.domain.utility

import com.lelestacia.lelenimexml.core.model.local.character.CharacterEntity
import com.lelestacia.lelenimexml.core.model.local.character.CharacterFullProfileEntity
import com.lelestacia.lelenimexml.core.model.remote.character.CharacterResponse
import com.lelestacia.lelenimexml.feature_anime.domain.model.Character
import com.lelestacia.lelenimexml.feature_anime.domain.model.CharacterFullProfile

object CharacterMapper {
    fun fromNetwork(characterNetworkResponse: CharacterResponse): Character {
        return Character(
            characterMalId = characterNetworkResponse.characterData.characterMalId,
            images = characterNetworkResponse.characterData.images.webp.imageUrl,
            name = characterNetworkResponse.characterData.name,
            role = characterNetworkResponse.role,
            favoriteBy = characterNetworkResponse.favoriteBy
        )
    }

    fun entityToCharacter(characterEntity: CharacterEntity): Character {
        return Character(
            characterMalId = characterEntity.characterId,
            images = characterEntity.characterImage,
            name = characterEntity.characterName,
            role = characterEntity.role,
            favoriteBy = characterEntity.favoriteBy
        )
    }

    fun fullProfileEntityToFullProfile(fullProfile: CharacterFullProfileEntity)
            : CharacterFullProfile =
        CharacterFullProfile(
            characterMalId = fullProfile.character.characterId,
            name = fullProfile.character.characterName,
            characterNickNames = fullProfile.additionalInformation.characterNickNames,
            characterKanjiName = fullProfile.additionalInformation.characterKanjiName,
            images = fullProfile.character.characterImage,
            role = fullProfile.character.role,
            favoriteBy = fullProfile.character.favoriteBy,
            characterInformation = fullProfile.additionalInformation.characterInformation
        )
}