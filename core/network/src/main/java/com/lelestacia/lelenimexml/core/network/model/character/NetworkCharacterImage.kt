package com.lelestacia.lelenimexml.core.network.model.character

import com.google.gson.annotations.SerializedName

data class NetworkCharacterImage(
    @SerializedName("webp")
    val webp: Webp
) {
    data class Webp(
        @SerializedName("image_url")
        val imageUrl: String
    )
}
