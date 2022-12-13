package com.lelestacia.lelenimexml.core.model.remote.anime

import com.google.gson.annotations.SerializedName

data class AnimeResponse(
    @SerializedName("mal_id")
    val malId: Int,
    @SerializedName("images")
    val images: Images,
    @SerializedName("trailer")
    val trailer: Trailer?,
    @SerializedName("title")
    val title: String,
    @SerializedName("title_english")
    val titleEnglish: String?,
    @SerializedName("title_japanese")
    val titleJapanese: String?,
    @SerializedName("type")
    val type: String,
    @SerializedName("episodes")
    val episodes: Int?,
    @SerializedName("status")
    val status: String,
    @SerializedName("rating")
    val rating: String?,
    @SerializedName("score")
    val score: Double?,
    @SerializedName("scored_by")
    val scoredBy: Int?,
    @SerializedName("rank")
    val rank: Int,
    @SerializedName("synopsis")
    val synopsis: String?,
    @SerializedName("season")
    val season: String?,
    @SerializedName("year")
    val year: Int,
    @SerializedName("genres")
    val genres: List<Genre>,
) {

    data class Images(
        @SerializedName("webp")
        val webp: Webp
    ) {
        data class Webp(
            @SerializedName("image_url")
            val imageUrl: String,
            @SerializedName("large_image_url")
            val largeImageUrl: String
        )
    }

    data class Trailer(
        @SerializedName("youtube_id")
        val youtubeId: String?,
        @SerializedName("url")
        val url: String?,
        @SerializedName("embed_url")
        val embedUrl: String?,
        @SerializedName("images")
        val images: Images?
    ) {
        data class Images(
            @SerializedName("medium_image_url")
            val mediumImageUrl: String,
            @SerializedName("large_image_url")
            val largeImageUrl: String,
            @SerializedName("maximum_image_url")
            val maximumImageUrl: String
        )
    }

    data class Genre(
        @SerializedName("mal_id")
        val malId: Int,
        @SerializedName("type")
        val type: String,
        @SerializedName("name")
        val name: String,
        @SerializedName("url")
        val url: String
    )
}
