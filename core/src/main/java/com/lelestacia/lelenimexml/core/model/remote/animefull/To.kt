package com.lelestacia.lelenimexml.core.model.remote.animefull


import com.google.gson.annotations.SerializedName

data class To(
    @SerializedName("day")
    val day: Any? = null,
    @SerializedName("month")
    val month: Any? = null,
    @SerializedName("year")
    val year: Any? = null
)