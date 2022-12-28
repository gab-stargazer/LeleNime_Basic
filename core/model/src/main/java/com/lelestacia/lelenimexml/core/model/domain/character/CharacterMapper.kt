package com.lelestacia.lelenimexml.core.model.domain.character

import com.lelestacia.lelenimexml.core.model.database.character.CharacterDetailEntity
import com.lelestacia.lelenimexml.core.model.database.character.CharacterEntity

fun CharacterEntity.asCharacter(): Character =
    Character(
        characterMalId = characterId,
        images = characterImage,
        name = characterName,
        role = characterName,
        favoriteBy = favoriteBy
    )

fun CharacterDetailEntity.asCharacterDetail(): CharacterDetail =
    CharacterDetail(
        characterMalId = character.characterId,
        name = character.characterName,
        characterKanjiName = additionalInformation.characterKanjiName,
        characterNickNames = additionalInformation.characterNickNames,
        images = character.characterImage,
        role = character.role,
        favoriteBy = character.favoriteBy,
        characterInformation = additionalInformation.characterInformation
    )
