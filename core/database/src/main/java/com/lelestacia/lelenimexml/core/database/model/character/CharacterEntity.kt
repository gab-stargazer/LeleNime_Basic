package com.lelestacia.lelenimexml.core.database.model.character

import androidx.room.ColumnInfo
import androidx.room.Entity
import java.util.*

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
    val characterFavorite: Int,

    @ColumnInfo(name = "created_at")
    val createdAt: Date,

    @ColumnInfo(name = "updated_at")
    val updatedAt: Date?,
)
