package com.lelestacia.lelenimexml.core.database.model.anime

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

@Entity(tableName = "anime")
data class AnimeEntity(
    @PrimaryKey(autoGenerate = false)
    @ColumnInfo(name = "id")
    val animeID: Int,

    @ColumnInfo(name = "title")
    val title: String,

    @ColumnInfo(name = "title_english")
    val titleEnglish: String?,

    @ColumnInfo(name = "title_japanese")
    val titleJapanese: String?,

    @ColumnInfo(name = "image")
    val coverImages: String,

    @ColumnInfo(name = "rank")
    val rank: Int,

    @ColumnInfo(name = "score")
    val score: Double?,

    @ColumnInfo(name = "scored_by")
    val scoredBy: Int?,

    @ColumnInfo(name = "type")
    val type: String,

    @ColumnInfo(name = "rating")
    val rating: String,

    @ColumnInfo(name = "episodes")
    val episodes: Int?,

    @ColumnInfo(name = "genres")
    val genres: List<String>,

    @ColumnInfo(name = "status")
    val status: String,

    @ColumnInfo(name = "season")
    val season: String?,

    @ColumnInfo(name = "year")
    val year: Int,

    @ColumnInfo(name = "started_date")
    val startedDate: String?,

    @ColumnInfo(name = "finished_date")
    val finishedDate: String?,

    @ColumnInfo(name = "synopsis")
    val synopsis: String?,

    @ColumnInfo(name = "favorite")
    val isFavorite: Boolean,

    @Embedded
    val trailer: Trailer?,

    @ColumnInfo(name = "last_viewed")
    val lastViewed: Date,

    @ColumnInfo(name = "created_at")
    val createdAt: Date,

    @ColumnInfo(name = "updated_at")
    val updatedAt: Date?,
) {

    data class Trailer(
        val youtubeId: String?,
        val url: String?,
        val images: String?
    )
}
