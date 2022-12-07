package com.lelestacia.lelenimexml.core.domain.dto

import com.google.gson.annotations.SerializedName

data class AnimeImagesDto(

    @SerializedName("jpg")
    val jpg: JpgDto
)
