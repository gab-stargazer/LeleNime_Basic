package com.lelestacia.lelenimexml.core.utility

import com.lelestacia.lelenimexml.core.model.local.CharacterEntity
import com.lelestacia.lelenimexml.core.model.remote.character.CharacterResponse

object CharacterMapper {

    fun networkToEntity(animeId: Int, networkCharacter: CharacterResponse) : CharacterEntity {
        return CharacterEntity(
            animeId = animeId,
            characterId = networkCharacter.characterResponseData.characterMalId,
            characterName = networkCharacter.characterResponseData.name,
            characterImage = networkCharacter.characterResponseData.images.webp.imageUrl,
            role = networkCharacter.role,
            favoriteBy = networkCharacter.favoriteBy
        )
    }
}