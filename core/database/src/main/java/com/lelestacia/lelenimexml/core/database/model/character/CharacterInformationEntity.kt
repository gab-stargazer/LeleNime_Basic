package com.lelestacia.lelenimexml.core.database.model.character

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(
    tableName = "character_information"
)
data class CharacterInformationEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    val id: Int = 0,

    @ColumnInfo(name = "character_id")
    val characterID: Int,

    @ColumnInfo(name = "kanji_name")
    val characterKanjiName: String,

    @ColumnInfo(name = "nickname")
    val characterNickNames: List<String>,

    @ColumnInfo(name = "favorite")
    val characterFavorite: Int,

    @ColumnInfo(name = "story")
    val characterInformation: String
)