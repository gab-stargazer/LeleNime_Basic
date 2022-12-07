package com.lelestacia.lelenimexml.core.domain.dto.animebyid


import com.google.gson.annotations.SerializedName

data class Theme(
    @SerializedName("endings")
    val endings: List<String> = listOf(),
    @SerializedName("openings")
    val openings: List<String> = listOf()
)