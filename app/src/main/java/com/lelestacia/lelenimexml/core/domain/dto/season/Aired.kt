package com.lelestacia.lelenimexml.core.domain.dto.season


import com.google.gson.annotations.SerializedName

data class Aired(
    @SerializedName("from")
    val from: String = "",
    @SerializedName("prop")
    val prop: Prop = Prop(),
    @SerializedName("string")
    val string: String = "",
    @SerializedName("to")
    val to: String? = null
)