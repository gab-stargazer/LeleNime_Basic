package com.lelestacia.lelenimexml.core.domain.dto.season


import com.google.gson.annotations.SerializedName

data class Title(
    @SerializedName("title")
    val title: String = "",
    @SerializedName("type")
    val type: String = ""
)