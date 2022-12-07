package com.lelestacia.lelenimexml.core.domain.dto.animebytitle


import com.google.gson.annotations.SerializedName

data class Images(
    @SerializedName("jpg")
    val jpg: Jpg = Jpg(),
    @SerializedName("webp")
    val webp: Webp = Webp()
)