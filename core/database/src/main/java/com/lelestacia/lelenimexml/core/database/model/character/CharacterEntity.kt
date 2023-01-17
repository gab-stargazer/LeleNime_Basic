package com.lelestacia.lelenimexml.core.database.model.character

import androidx.room.ColumnInfo
import androidx.room.Entity

@Entity(
    tableName = "character",
    primaryKeys = ["id", "anime_id"]
)
data class CharacterEntity(
    @ColumnInfo(name = "id")
    val characterID: Int,

    @ColumnInfo(name = "anime_id")
    val animeID: Int,

    @ColumnInfo(name = "name")
    val characterName: String,

    @ColumnInfo(name = "image")
    val characterImage: String,

    @ColumnInfo(name = "role")
    val characterRole: String,

    @ColumnInfo(name = "favorite")
    val characterFavorite: Int
)