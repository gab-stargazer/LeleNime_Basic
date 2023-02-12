package com.lelestacia.lelenimexml.core.network.test


import com.google.gson.annotations.SerializedName

data class test(
    @SerializedName("data")
    val `data`: List<Data>,
    @SerializedName("pagination")
    val pagination: Pagination
)