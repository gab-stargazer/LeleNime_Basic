package com.lelestacia.lelenimexml.core.model.remote.character

import com.google.gson.annotations.SerializedName

data class CharacterResponse(
    @SerializedName("character")
    val characterResponseData: CharacterResponseData,
    @SerializedName("role")
    val role: String,
    @SerializedName("favorites")
    val favoriteBy: Int
)
