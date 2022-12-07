package com.lelestacia.lelenimexml.core.domain.dto.animebytitle


import com.google.gson.annotations.SerializedName

data class Licensor(
    @SerializedName("mal_id")
    val malId: Int = 0,
    @SerializedName("name")
    val name: String = "",
    @SerializedName("type")
    val type: String = "",
    @SerializedName("url")
    val url: String = ""
)