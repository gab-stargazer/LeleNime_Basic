package com.lelestacia.lelenimexml.core.domain.dto

import com.google.gson.annotations.SerializedName

data class JpgDto(

    @SerializedName("image_url")
    val imageUrl: String = "",

    @SerializedName("large_image_url")
    val largeImageUrl: String = "",
)
