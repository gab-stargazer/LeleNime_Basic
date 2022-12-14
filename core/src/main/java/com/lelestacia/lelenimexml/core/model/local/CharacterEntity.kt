package com.lelestacia.lelenimexml.core.model.local

import androidx.room.Entity

@Entity(tableName = "character_table", primaryKeys = ["animeId", "characterId"])
data class CharacterEntity(
    val animeId: Int,
    val characterId: Int,
    val characterName: String,
    val characterImage: String,
    val role: String,
    val favoriteBy: Int
)
