package com.lelestacia.lelenimexml.core.domain.dto.animebytitle


import com.google.gson.annotations.SerializedName

data class Prop(
    @SerializedName("from")
    val from: From = From(),
    @SerializedName("to")
    val to: To = To()
)