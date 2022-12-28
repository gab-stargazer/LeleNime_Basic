package com.lelestacia.lelenimexml.core.model.database

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

@Entity(tableName = "anime_table")
data class AnimeEntity(
    @PrimaryKey(autoGenerate = false)
    val malId: Int,
    val coverImages: String,
    @Embedded
    val trailer: Trailer?,
    val title: String,
    val titleEnglish: String?,
    val titleJapanese: String?,
    val type: String,
    val episodes: Int?,
    val status: String,
    val rating: String,
    val score: Double?,
    val scoredBy: Int?,
    val rank: Int,
    val synopsis: String?,
    val season: String?,
    val year: Int,
    val genres: List<String>,
    val lastViewed: Date,
    @ColumnInfo(defaultValue = "false")
    var isFavorite: Boolean
) {

    data class Trailer(
        val youtubeId: String?,
        val url: String?,
        val images: String?
    )
}
