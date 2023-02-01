package com.lelestacia.lelenimexml.core.data.utility

import com.lelestacia.lelenimexml.core.database.entity.character.CharacterEntity
import com.lelestacia.lelenimexml.core.database.entity.character.CharacterInformationEntity
import com.lelestacia.lelenimexml.core.database.entity.character.CharacterProfile
import com.lelestacia.lelenimexml.core.database.entity.voice_actor.VoiceActorEntity
import com.lelestacia.lelenimexml.core.model.character.Character
import com.lelestacia.lelenimexml.core.model.character.CharacterDetail
import com.lelestacia.lelenimexml.core.network.model.character.NetworkCharacter
import com.lelestacia.lelenimexml.core.network.model.character.NetworkCharacterDetail
import java.util.*

fun NetworkCharacterDetail.asNewEntity(): CharacterInformationEntity =
    CharacterInformationEntity(
        characterID = characterMalId,
        characterKanjiName = characterKanjiName ?: "",
        characterNickNames = characterNickNames,
        characterFavorite = characterFavoriteCount,
        characterInformation = characterInformation ?: "",
        createdAt = Date(),
        updatedAt = null
    )

fun NetworkCharacter.asNewEntity(): CharacterEntity =
    CharacterEntity(
        characterID = characterData.malID,
        name = characterData.name,
        image = characterData.images.webp.imageUrl,
        role = role,
        favorite = favorites,
        createdAt = Date(),
        updatedAt = null
    )

fun NetworkCharacter.asCharacterWithVoiceActorEntities(): Pair<Int, List<VoiceActorEntity>> {
    val characterID: Int = characterData.malID
    val voiceActorEntities: List<VoiceActorEntity> = voiceActors.map { networkVoiceActor ->
        VoiceActorEntity(
            voiceActorID = networkVoiceActor.person.malID,
            image = networkVoiceActor.person.image.jpg.imageUrl,
            name = networkVoiceActor.person.name,
            language = networkVoiceActor.language,
            created_at = Date(),
            updated_at = null
        )
    }
    return Pair(characterID, voiceActorEntities)
}

fun NetworkCharacter.asNewVoiceActor(): List<VoiceActorEntity> {
    return voiceActors.map {networkVoiceActor ->
        VoiceActorEntity(
            voiceActorID = networkVoiceActor.person.malID,
            image = networkVoiceActor.person.image.jpg.imageUrl,
            name = networkVoiceActor.person.name,
            language = networkVoiceActor.language,
            created_at = Date(),
            updated_at = null
        )
    }
}

fun CharacterEntity.asCharacter(): Character =
    Character(
        malID = characterID,
        images = image,
        name = name,
        role = name,
        favorites = favorite
    )

fun CharacterProfile.asCharacterDetail(): CharacterDetail =
    CharacterDetail(
        characterID = character.characterID,
        name = character.name,
        characterKanjiName = additionalInformation.characterKanjiName,
        characterNickNames = additionalInformation.characterNickNames,
        images = character.image,
        role = character.role,
        favoriteBy = character.favorite,
        characterInformation = additionalInformation.characterInformation
    )
