package com.lelestacia.lelenimexml.core.database.model.character

import androidx.room.Embedded
import androidx.room.Relation

data class CharacterProfile(
    @Embedded
    val character: CharacterEntity,

    @Relation(
        parentColumn = "id",
        entityColumn = "character_id"
    )
    val additionalInformation: CharacterInformationEntity
)