package com.lelestacia.lelenimexml.core.model.remote.animefull


import com.google.gson.annotations.SerializedName

data class From(
    @SerializedName("day")
    val day: Int = 0,
    @SerializedName("month")
    val month: Int = 0,
    @SerializedName("year")
    val year: Int = 0
)