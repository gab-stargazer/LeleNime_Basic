package com.lelestacia.lelenimexml.core.network.test


import com.google.gson.annotations.SerializedName

data class Entry(
    @SerializedName("images")
    val images: Images,
    @SerializedName("mal_id")
    val malId: Int,
    @SerializedName("title")
    val title: String,
    @SerializedName("url")
    val url: String
)