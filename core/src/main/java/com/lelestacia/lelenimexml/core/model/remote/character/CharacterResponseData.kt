package com.lelestacia.lelenimexml.core.model.remote.character

import com.google.gson.annotations.SerializedName

data class CharacterResponseData(
    @SerializedName("mal_id")
    val characterMalId: Int,
    @SerializedName("images")
    val images: Images,
    @SerializedName("name")
    val name: String
) {
    data class Images(
        @SerializedName("webp")
        val webp: Webp
    ) {
        data class Webp(
            @SerializedName("image_url")
            val imageUrl: String
        )
    }
}