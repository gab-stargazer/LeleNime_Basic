package com.lelestacia.lelenimexml.core.model.remote.character

import com.google.gson.annotations.SerializedName

data class CharacterImageResponse(
    @SerializedName("webp")
    val webp: Webp
) {
    data class Webp(
        @SerializedName("image_url")
        val imageUrl: String
    )
}
