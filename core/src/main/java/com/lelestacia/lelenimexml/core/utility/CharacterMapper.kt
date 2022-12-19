package com.lelestacia.lelenimexml.core.utility

import com.lelestacia.lelenimexml.core.model.local.character.CharacterAdditionalInformationEntity
import com.lelestacia.lelenimexml.core.model.local.character.CharacterEntity
import com.lelestacia.lelenimexml.core.model.remote.character.CharacterDetailResponse
import com.lelestacia.lelenimexml.core.model.remote.character.CharacterResponse

object CharacterMapper {

    fun networkToEntity(animeId: Int, response: CharacterResponse): CharacterEntity =
        CharacterEntity(
            animeId = animeId,
            characterId = response.characterData.characterMalId,
            characterName = response.characterData.name,
            characterImage = response.characterData.images.webp.imageUrl,
            role = response.role,
            favoriteBy = response.favoriteBy
        )

    fun characterDetailResponseToCharacterAdditionalInformationEntity(response: CharacterDetailResponse)
            : CharacterAdditionalInformationEntity =
        CharacterAdditionalInformationEntity(
            characterId = response.characterMalId,
            characterKanjiName = response.characterKanjiName ?: "",
            characterNickNames = response.characterNickNames,
            characterFavoriteCount = response.characterFavoriteCount,
            characterInformation = response.characterInformation ?: ""
        )
}