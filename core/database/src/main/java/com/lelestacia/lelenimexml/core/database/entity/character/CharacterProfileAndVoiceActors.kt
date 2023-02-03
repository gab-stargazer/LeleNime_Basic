package com.lelestacia.lelenimexml.core.database.entity.character

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation
import com.lelestacia.lelenimexml.core.database.entity.voice_actor.VoiceActorEntity
import com.lelestacia.lelenimexml.core.database.util.DatabaseConstant.CHARACTER_ID
import com.lelestacia.lelenimexml.core.database.util.DatabaseConstant.VOICE_ACTOR_ID

data class CharacterProfileAndVoiceActors(
    @Embedded val characterEntity: CharacterEntity,

    @Relation(
        parentColumn = CHARACTER_ID,
        entityColumn = CHARACTER_ID
    )
    val characterInformationEntity: CharacterInformationEntity,

    @Relation(
        entity = VoiceActorEntity::class,
        parentColumn = CHARACTER_ID,
        entityColumn = VOICE_ACTOR_ID,
        associateBy = Junction(
            value = CharacterVoiceActorCrossRefEntity::class,
            parentColumn = CHARACTER_ID,
            entityColumn = VOICE_ACTOR_ID
        )
    )
    val voiceActorEntities: List<VoiceActorEntity>
)