package com.lelestacia.lelenimexml.core.network.model.manga

import com.google.gson.annotations.SerializedName

data class MangaResponse(

    @SerializedName("mal_id")
    val malID: Int,

    @SerializedName("images")
    val images: MangaImageResponse,

    @SerializedName("approved")
    val approved: Boolean,

    @SerializedName("title")
    val title: String,

    @SerializedName("title_english")
    val titleEnglish: String?,

    @SerializedName("title_japanese")
    val titleJapanese: String?,

    @SerializedName("chapters")
    val chapters: Int,

    @SerializedName("volume")
    val volume: Int,

    @SerializedName("status")
    val status: String,

    @SerializedName("publishing")
    val publishing: Boolean,

    @SerializedName("score")
    val score: Double?,

    @SerializedName("scored_by")
    val scoredBy: Double?,

    @SerializedName("rank")
    val rank: Int,

    @SerializedName("synopsis")
    val synopsis: String
) {

    data class MangaImageResponse(

        @SerializedName("webp")
        val webp: Webp
    ) {

        data class Webp(

            @SerializedName("large_image_url")
            val largeImageURL: String
        )
    }
}
