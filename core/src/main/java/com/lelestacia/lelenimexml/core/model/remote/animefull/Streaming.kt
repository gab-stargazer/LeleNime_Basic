package com.lelestacia.lelenimexml.core.model.remote.animefull


import com.google.gson.annotations.SerializedName

data class Streaming(
    @SerializedName("name")
    val name: String = "",
    @SerializedName("url")
    val url: String = ""
)