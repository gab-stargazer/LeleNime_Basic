package com.lelestacia.lelenimexml.core.model.remote.character

import com.google.gson.annotations.SerializedName

data class CharacterResponse(
    @SerializedName("character")
    val characterData: CharacterResponseData,

    @SerializedName("role")
    val role: String,

    @SerializedName("favorites")
    val favoriteBy: Int = 0
) {
    data class CharacterResponseData(
        @SerializedName("mal_id")
        val characterMalId: Int,

        @SerializedName("images")
        val images: CharacterImageResponse,

        @SerializedName("name")
        val name: String
    )
}


