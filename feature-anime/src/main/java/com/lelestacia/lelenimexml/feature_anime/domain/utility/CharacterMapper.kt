package com.lelestacia.lelenimexml.feature_anime.domain.utility

import com.lelestacia.lelenimexml.core.model.local.character.CharacterDetailEntity
import com.lelestacia.lelenimexml.core.model.local.character.CharacterEntity
import com.lelestacia.lelenimexml.feature_anime.domain.model.Character
import com.lelestacia.lelenimexml.feature_anime.domain.model.CharacterFullProfile

object CharacterMapper {

    fun entityToCharacter(characterEntity: CharacterEntity): Character {
        return Character(
            characterMalId = characterEntity.characterId,
            images = characterEntity.characterImage,
            name = characterEntity.characterName,
            role = characterEntity.role,
            favoriteBy = characterEntity.favoriteBy
        )
    }

    fun fullProfileEntityToFullProfile(fullProfile: CharacterDetailEntity)
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