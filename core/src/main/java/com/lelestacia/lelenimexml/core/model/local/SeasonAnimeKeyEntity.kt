package com.lelestacia.lelenimexml.core.model.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "season_anime_key")
data class SeasonAnimeKeyEntity(
    @PrimaryKey(autoGenerate = false)
    val id: Int,
    val prefKey: Int?,
    val nextKey: Int?
)
