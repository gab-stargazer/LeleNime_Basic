package com.lelestacia.lelenimexml.core.domain.dto.animebytitle


import com.google.gson.annotations.SerializedName

data class To(
    @SerializedName("day")
    val day: Int = 0,
    @SerializedName("month")
    val month: Int = 0,
    @SerializedName("year")
    val year: Int = 0
)