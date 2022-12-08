package com.lelestacia.lelenimexml.core.domain.dto

import com.google.gson.annotations.SerializedName
import com.lelestacia.lelenimexml.feature_anime.domain.model.AnimeCard

data class AnimeDTO(

    @SerializedName("mal_id")
    val malId: Int,

    @SerializedName("images")
    val images: Images,

    @SerializedName("title")
    val title: String,

    @SerializedName("score")
    val score: Double = 0.0,

    @SerializedName("episodes")
    val episodes: Int = 0,

    @SerializedName("status")
    val status: String,
) {
    fun toAnimeCard(): AnimeCard {
        return AnimeCard(
            malID = malId,
            title = title,
            coverImage = images.webp.largeImageUrl,
            rating = score,
            episode = episodes,
            status = status
        )
    }
}
