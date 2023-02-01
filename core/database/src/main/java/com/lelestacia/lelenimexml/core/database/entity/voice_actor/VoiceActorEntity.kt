package com.lelestacia.lelenimexml.core.database.entity.voice_actor

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.lelestacia.lelenimexml.core.database.util.DatabaseConstant.CREATED_AT
import com.lelestacia.lelenimexml.core.database.util.DatabaseConstant.IMAGE
import com.lelestacia.lelenimexml.core.database.util.DatabaseConstant.UPDATED_AT
import com.lelestacia.lelenimexml.core.database.util.DatabaseConstant.VOICE_ACTOR_ID
import java.util.*

@Entity(tableName = "voice_actor_table")
data class VoiceActorEntity(
    @PrimaryKey(autoGenerate = false)
    @ColumnInfo(name = VOICE_ACTOR_ID)
    val voiceActorID: Int,

    @ColumnInfo(name = IMAGE)
    val image: String,

    @ColumnInfo(name = "name")
    val name: String,

    @ColumnInfo(name = "language")
    val language: String,

    @ColumnInfo(name = CREATED_AT)
    val created_at: Date,

    @ColumnInfo(name = UPDATED_AT)
    val updated_at: Date?
)
