package com.lelestacia.lelenimexml.core.model.remote.animefull


import com.google.gson.annotations.SerializedName

data class ThemeX(
    @SerializedName("mal_id")
    val malId: Int = 0,
    @SerializedName("name")
    val name: String = "",
    @SerializedName("type")
    val type: String = "",
    @SerializedName("url")
    val url: String = ""
)