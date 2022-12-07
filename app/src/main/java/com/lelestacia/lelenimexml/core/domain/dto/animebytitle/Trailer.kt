package com.lelestacia.lelenimexml.core.domain.dto.animebytitle


import com.google.gson.annotations.SerializedName

data class Trailer(
    @SerializedName("embed_url")
    val embedUrl: Any? = Any(),
    @SerializedName("images")
    val images: ImagesX = ImagesX(),
    @SerializedName("url")
    val url: Any? = Any(),
    @SerializedName("youtube_id")
    val youtubeId: Any? = Any()
)