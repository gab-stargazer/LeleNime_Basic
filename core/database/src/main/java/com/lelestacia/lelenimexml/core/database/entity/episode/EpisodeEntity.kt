package com.lelestacia.lelenimexml.core.database.entity.episode

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.ForeignKey.RESTRICT
import com.lelestacia.lelenimexml.core.database.entity.DatabaseConstant.ANIME_ID
import com.lelestacia.lelenimexml.core.database.entity.DatabaseConstant.CREATED_AT
import com.lelestacia.lelenimexml.core.database.entity.DatabaseConstant.EPISODE_ID
import com.lelestacia.lelenimexml.core.database.entity.DatabaseConstant.UPDATED_AT
import com.lelestacia.lelenimexml.core.database.entity.anime.AnimeEntity
import java.util.*

@Entity(
    tableName = "episode_table",
    primaryKeys = [EPISODE_ID, ANIME_ID],
    foreignKeys = [
        ForeignKey(
            entity = AnimeEntity::class,
            parentColumns = [ANIME_ID],
            childColumns = [ANIME_ID],
            onDelete = RESTRICT,
        )
    ]
)
data class EpisodeEntity(
    @ColumnInfo(name = EPISODE_ID)
    val episodeID: Int,

    @ColumnInfo(name = ANIME_ID)
    val animeID: Int,

    @ColumnInfo(name = "title")
    val title: String,

    @ColumnInfo(name = "title_japanese")
    val titleJapanese: String?,

    @ColumnInfo(name = "title_romanji")
    val titleRomanji: String?,

    @ColumnInfo(name = "aired")
    val aired: String?,

    @ColumnInfo(name = "score")
    val score: Double,

    @ColumnInfo(name = "filler")
    val filler: Boolean,

    @ColumnInfo(name = "recap")
    val recap: Boolean,

    @ColumnInfo(name = "forum_url")
    val forumURL: String?,

    @ColumnInfo(name = CREATED_AT)
    val createdAt: Date,

    @ColumnInfo(name = UPDATED_AT)
    val updatedAt: Date?
)
