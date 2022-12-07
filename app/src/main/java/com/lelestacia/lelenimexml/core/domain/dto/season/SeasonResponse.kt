package com.lelestacia.lelenimexml.core.domain.dto.season


import com.google.gson.annotations.SerializedName

data class SeasonResponse(
    @SerializedName("data")
    val data: List<Data> = listOf(),
    @SerializedName("pagination")
    val pagination: Pagination = Pagination()
)