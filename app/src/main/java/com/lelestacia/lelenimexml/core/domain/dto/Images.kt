package com.lelestacia.lelenimexml.core.domain.dto


import com.google.gson.annotations.SerializedName

data class Images(
    @SerializedName("jpg")
    val jpg: Jpg = Jpg(),

    @SerializedName("webp")
    val webp: Webp = Webp()
) {
    data class Jpg(
        @SerializedName("image_url")
        val imageUrl: String = "",

        @SerializedName("large_image_url")
        val largeImageUrl: String = "",
    )

    data class Webp(
        @SerializedName("image_url")
        val imageUrl: String = "",

        @SerializedName("large_image_url")
        val largeImageUrl: String = "",
    )
}