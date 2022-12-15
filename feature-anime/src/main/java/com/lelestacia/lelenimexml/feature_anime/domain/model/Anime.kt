package com.lelestacia.lelenimexml.feature_anime.domain.model

import android.os.Parcelable
import com.lelestacia.lelenimexml.core.model.local.AnimeEntity
import kotlinx.parcelize.Parcelize
import java.util.*

@Parcelize
data class Anime(
    val malId: Int,
    val images: String,
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
) : Parcelable {
    @Parcelize
    data class Trailer(
        val youtubeId: String?,
        val url: String?,
        val images: String?
    ) : Parcelable

    fun toAnimeEntity() : AnimeEntity {
        return AnimeEntity(
            malId = malId,
            coverImages = images,
            trailer = AnimeEntity.Trailer(
                youtubeId = trailer?.youtubeId,
                url = trailer?.url,
                images = trailer?.images
            ),
            title = title,
            titleEnglish = titleEnglish,
            titleJapanese = titleJapanese,
            type = type,
            episodes = episodes,
            status = status,
            rating = rating,
            score = score,
            scoredBy = scoredBy,
            rank = rank,
            synopsis = synopsis,
            season = season,
            year = year,
            genres = genres,
            lastViewed = Date()
        )
    }
}
