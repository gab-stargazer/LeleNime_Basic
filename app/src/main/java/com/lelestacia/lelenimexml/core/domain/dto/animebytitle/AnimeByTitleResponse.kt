package com.lelestacia.lelenimexml.core.domain.dto.animebytitle


import com.google.gson.annotations.SerializedName

data class AnimeByTitleResponse(
    @SerializedName("data")
    val `data`: List<Data> = listOf(),
    @SerializedName("pagination")
    val pagination: Pagination = Pagination()
)