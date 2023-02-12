package com.lelestacia.lelenimexml.core.network.test


import com.google.gson.annotations.SerializedName

data class Data(
    @SerializedName("content")
    val content: String,
    @SerializedName("date")
    val date: String,
    @SerializedName("entry")
    val entry: List<Entry>,
    @SerializedName("mal_id")
    val malId: String,
    @SerializedName("user")
    val user: User
)