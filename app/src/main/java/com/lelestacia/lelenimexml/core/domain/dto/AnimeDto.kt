package com.lelestacia.lelenimexml.core.domain.dto

import com.google.gson.annotations.SerializedName

data class AnimeDto(

    @SerializedName("mal_id")
    val malId: Int,

    @SerializedName("images")
    val images: AnimeImagesDto,


)
