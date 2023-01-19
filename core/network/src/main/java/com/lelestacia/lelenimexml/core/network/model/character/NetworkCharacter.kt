package com.lelestacia.lelenimexml.core.network.model.character

import com.google.gson.annotations.SerializedName

data class NetworkCharacter(
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
        val images: NetworkCharacterImage,

        @SerializedName("name")
        val name: String
    )
}
