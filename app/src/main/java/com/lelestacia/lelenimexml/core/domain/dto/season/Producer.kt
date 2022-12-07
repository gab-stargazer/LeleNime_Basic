package com.lelestacia.lelenimexml.core.domain.dto.season


import com.google.gson.annotations.SerializedName

data class Producer(
    @SerializedName("mal_id")
    val malId: Int = 0,
    @SerializedName("name")
    val name: String = "",
    @SerializedName("type")
    val type: String = "",
    @SerializedName("url")
    val url: String = ""
)