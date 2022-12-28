package com.lelestacia.lelenimexml.core.model.domain.anime

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Anime(
    val malID: Int,
    val coverImages: String,
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
    val isFavorite: Boolean,
) : Parcelable {
    @Parcelize
    data class Trailer(
        val youtubeId: String?,
        val url: String?,
        val images: String?
    ) : Parcelable
}
