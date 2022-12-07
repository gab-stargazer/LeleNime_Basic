package com.lelestacia.lelenimexml.core.domain.dto.animebyid


import com.google.gson.annotations.SerializedName

data class AnimeByIdResponse(
    @SerializedName("data")
    val `data`: Data = Data()
)