package com.lelestacia.lelenimexml.core.network.test


import com.google.gson.annotations.SerializedName

data class User(
    @SerializedName("url")
    val url: String,
    @SerializedName("username")
    val username: String
)