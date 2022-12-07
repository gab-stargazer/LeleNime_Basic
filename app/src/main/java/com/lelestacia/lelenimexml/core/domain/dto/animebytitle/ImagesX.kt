package com.lelestacia.lelenimexml.core.domain.dto.animebytitle


import com.google.gson.annotations.SerializedName

data class ImagesX(
    @SerializedName("image_url")
    val imageUrl: Any? = null,
    @SerializedName("large_image_url")
    val largeImageUrl: Any? = null,
    @SerializedName("maximum_image_url")
    val maximumImageUrl: Any? = null,
    @SerializedName("medium_image_url")
    val mediumImageUrl: Any? = null,
    @SerializedName("small_image_url")
    val smallImageUrl: Any? = null
)