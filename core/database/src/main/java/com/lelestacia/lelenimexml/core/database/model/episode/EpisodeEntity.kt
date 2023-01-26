package com.lelestacia.lelenimexml.core.database.model.episode

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

@Entity(tableName = "episode")
data class EpisodeEntity(
    @ColumnInfo(name = "id")
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,

    @ColumnInfo(name = "mal_id")
    val malID: Int,

    @ColumnInfo(name = "anime_id")
    val animeID: Int,

    @ColumnInfo(name = "title")
    val title: String,

    @ColumnInfo(name = "title_japanese")
    val titleJapanese: String?,

    @ColumnInfo(name = "aired")
    val aired: String?,

    @ColumnInfo(name = "score")
    val score: Double,

    @ColumnInfo(name = "filler")
    val filler: Boolean,

    @ColumnInfo(name = "recap")
    val recap: Boolean,

    @ColumnInfo(name = "forum_url")
    val forumURL: String,

    @ColumnInfo(name = "created_at")
    val createdAt: Date,

    @ColumnInfo(name = "updated_at")
    val updatedAt: Date?
)