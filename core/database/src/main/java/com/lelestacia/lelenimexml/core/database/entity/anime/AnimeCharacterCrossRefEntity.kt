package com.lelestacia.lelenimexml.core.database.entity.anime

import androidx.room.ColumnInfo
import androidx.room.Entity
import com.lelestacia.lelenimexml.core.database.entity.Constant.ANIME_ID
import com.lelestacia.lelenimexml.core.database.entity.Constant.CHARACTER_ID

@Entity(
    tableName = "anime_characters_cross_ref_table",
    primaryKeys = [ANIME_ID, CHARACTER_ID]
)
data class AnimeCharacterCrossRefEntity(
    @ColumnInfo(name = ANIME_ID)
    val animeID: Int,

    @ColumnInfo(name = CHARACTER_ID)
    val characterID: Int
)
