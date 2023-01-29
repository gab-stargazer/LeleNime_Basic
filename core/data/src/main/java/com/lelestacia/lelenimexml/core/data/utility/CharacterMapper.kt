package com.lelestacia.lelenimexml.core.data.utility

import com.lelestacia.lelenimexml.core.database.model.character.CharacterEntity
import com.lelestacia.lelenimexml.core.database.model.character.CharacterInformationEntity
import com.lelestacia.lelenimexml.core.database.model.character.CharacterProfile
import com.lelestacia.lelenimexml.core.model.character.Character
import com.lelestacia.lelenimexml.core.model.character.CharacterDetail
import com.lelestacia.lelenimexml.core.network.model.character.NetworkCharacter
import com.lelestacia.lelenimexml.core.network.model.character.NetworkCharacterDetail
import java.util.*

fun NetworkCharacterDetail.asEntity(): CharacterInformationEntity =
    CharacterInformationEntity(
        characterID = characterMalId,
        characterKanjiName = characterKanjiName ?: "",
        characterNickNames = characterNickNames,
        characterFavorite = characterFavoriteCount,
        characterInformation = characterInformation ?: "",
        createdAt = Date(),
        updatedAt = null
    )

fun NetworkCharacter.asEntity(animeID: Int): CharacterEntity =
    CharacterEntity(
        animeID = animeID,
        characterID = characterData.characterMalId,
        characterName = characterData.name,
        characterImage = characterData.images.webp.imageUrl,
        characterRole = role,
        characterFavorite = favoriteBy,
        createdAt = Date(),
        updatedAt = null
    )

fun CharacterEntity.asCharacter(): Character =
    Character(
        characterID = characterID,
        images = characterImage,
        name = characterName,
        role = characterName,
        favorite = characterFavorite
    )

fun CharacterProfile.asCharacterDetail(): CharacterDetail =
    CharacterDetail(
        characterID = character.characterID,
        name = character.characterName,
        characterKanjiName = additionalInformation.characterKanjiName,
        characterNickNames = additionalInformation.characterNickNames,
        images = character.characterImage,
        role = character.characterRole,
        favoriteBy = character.characterFavorite,
        characterInformation = additionalInformation.characterInformation
    )
