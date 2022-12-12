package com.lelestacia.lelenimexml.core.domain.dto.animefull


import com.google.gson.annotations.SerializedName

data class To(
    @SerializedName("day")
    val day: Any? = null,
    @SerializedName("month")
    val month: Any? = null,
    @SerializedName("year")
    val year: Any? = null
)