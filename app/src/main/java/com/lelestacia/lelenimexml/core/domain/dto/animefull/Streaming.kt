package com.lelestacia.lelenimexml.core.domain.dto.animefull


import com.google.gson.annotations.SerializedName

data class Streaming(
    @SerializedName("name")
    val name: String = "",
    @SerializedName("url")
    val url: String = ""
)