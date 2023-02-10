package com.lelestacia.lelenimexml.core.database.entity.character

import androidx.room.ColumnInfo
import androidx.room.Entity
import com.lelestacia.lelenimexml.core.database.util.DatabaseConstant.CHARACTER_ID
import com.lelestacia.lelenimexml.core.database.util.DatabaseConstant.VOICE_ACTOR_ID

@Entity(
    tableName = "character_voice_actor_reference_table",
    primaryKeys = [CHARACTER_ID, VOICE_ACTOR_ID],
)
data class CharacterVoiceActorCrossRefEntity(
    @ColumnInfo(name = CHARACTER_ID)
    val characterID: Int,

    @ColumnInfo(name = VOICE_ACTOR_ID)
    val voiceActorID: Int
)
