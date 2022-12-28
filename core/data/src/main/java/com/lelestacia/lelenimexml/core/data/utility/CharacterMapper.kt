package com.lelestacia.lelenimexml.core.data.utility

import com.lelestacia.lelenimexml.core.model.database.character.CharacterAdditionalInformationEntity
import com.lelestacia.lelenimexml.core.model.database.character.CharacterEntity
import com.lelestacia.lelenimexml.core.model.network.character.NetworkCharacter
import com.lelestacia.lelenimexml.core.model.network.character.NetworkCharacterDetail

fun NetworkCharacterDetail.asCharacterAdditionalInformationEntity(): CharacterAdditionalInformationEntity =
    CharacterAdditionalInformationEntity(
        characterId = characterMalId,
        characterKanjiName = characterKanjiName ?: "",
        characterNickNames = characterNickNames,
        characterFavoriteCount = characterFavoriteCount,
        characterInformation = characterInformation ?: ""
    )

fun NetworkCharacter.asCharacterEntity(animeID: Int): CharacterEntity =
    CharacterEntity(
        animeId = animeID,
        characterId = characterData.characterMalId,
        characterName = characterData.name,
        characterImage = characterData.images.webp.imageUrl,
        role = role,
        favoriteBy = favoriteBy
    )