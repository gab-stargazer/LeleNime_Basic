package com.lelestacia.lelenimexml.core.domain.dto.animebyid


import com.google.gson.annotations.SerializedName

data class Trailer(
    @SerializedName("embed_url")
    val embedUrl: String = "",
    @SerializedName("images")
    val images: ImagesX = ImagesX(),
    @SerializedName("url")
    val url: String = "",
    @SerializedName("youtube_id")
    val youtubeId: String = ""
)