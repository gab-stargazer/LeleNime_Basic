package com.lelestacia.lelenimexml.core.domain.dto.animebytitle


import com.google.gson.annotations.SerializedName

data class Broadcast(
    @SerializedName("day")
    val day: String? = null,
    @SerializedName("string")
    val string: String? = null,
    @SerializedName("time")
    val time: String? = null,
    @SerializedName("timezone")
    val timezone: String? = null
)